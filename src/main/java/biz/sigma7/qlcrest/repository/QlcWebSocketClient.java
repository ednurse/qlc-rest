package biz.sigma7.qlcrest.repository;

import biz.sigma7.qlcrest.domain.exception.DownstreamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.*;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.Objects.isNull;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Component
public class QlcWebSocketClient implements WebSocketHandler {

    private static final Logger LOG = LoggerFactory.getLogger(QlcWebSocketClient.class);

    @Value("${qlc.host}")
    private String host;

    @Value("${qlc.timeout}")
    private long timeout;

    private final ReentrantLock responseLock = new ReentrantLock();

    private WebSocketSession session = null;

    private CompletableFuture<String[]> responseFuture;

    public String[] sendMessageAndWaitForResponse(TextMessage message) {
        responseLock.lock();

        sendMessage(message);

        responseFuture = new CompletableFuture<>();
        try {
            return responseFuture.get(timeout, MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new DownstreamException("Failed to receive response", e);
        } finally {
            responseLock.unlock();
        }
    }

    public void sendMessage(TextMessage message) {
        if (isNull(session) || !session.isOpen()) {
            connect();
        }

        try {
            session.sendMessage(message);
        } catch (IOException e) {
            throw new DownstreamException("Failed to send message \"" + message + "\"", e);
        }
    }

    private void connect() {
        WebSocketClient webSocketClient = new StandardWebSocketClient();
        String uri = host + "/qlcplusWS";
        ListenableFuture<WebSocketSession> future = webSocketClient.doHandshake(this, uri);

        try {
            session = future.get();
            LOG.info("Successfully connected to QLC at {}", uri);
        } catch (InterruptedException | ExecutionException e) {
            throw new DownstreamException("Could not connect to QLC at " + uri, e);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        LOG.info("Connection established");
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
        if (!responseFuture.isDone() || !responseFuture.isCancelled()) {
            String[] elements = message.getPayload().toString().split("\\|");
            responseFuture.complete(elements);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        LOG.error("Transport error", exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        LOG.info("Connection closed, status: {}", closeStatus);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

}
