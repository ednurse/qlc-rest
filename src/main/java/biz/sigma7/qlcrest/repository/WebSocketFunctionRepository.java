package biz.sigma7.qlcrest.repository;

import biz.sigma7.qlcrest.domain.Function;
import biz.sigma7.qlcrest.domain.exception.DownstreamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.*;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.Objects.isNull;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Repository
public class WebSocketFunctionRepository implements FunctionRepository, WebSocketHandler {

    private static Logger LOG = LoggerFactory.getLogger(WebSocketFunctionRepository.class);

    @Value("${qlc.host}")
    private String host;

    @Value("${qlc.timeout}")
    private long timeout;

    private WebSocketSession session = null;

    private ReentrantLock responseLock = new ReentrantLock();
    private CompletableFuture<List<Function>> getFunctionsListFuture;
    private CompletableFuture<String> getFunctionTypeFuture;
    private CompletableFuture<String> getFunctionStatusFuture;

    @Override
    public List<Function> getAllFunctions() {
        responseLock.lock();
        getFunctionsListFuture = new CompletableFuture<>();
        sendMessage(RequestMessageBuilder.getAllFunctions());

        try {
            return getFunctionsListFuture.get(timeout, MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new DownstreamException("Exception waiting for functions list future", e);
        } finally {
            responseLock.unlock();
        }
    }

    @Override
    public String getFunctionType(int id) {
        responseLock.lock();
        getFunctionTypeFuture = new CompletableFuture<>();
        sendMessage(RequestMessageBuilder.getFunctionType(id));

        try {
            return getFunctionTypeFuture.get(timeout, MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new DownstreamException("Exception waiting for function type future", e);
        } finally {
            responseLock.unlock();
        }
    }

    @Override
    public String getFunctionStatus(int id) {
        responseLock.lock();
        getFunctionStatusFuture = new CompletableFuture<>();
        sendMessage(RequestMessageBuilder.getFunctionStatus(id));

        try {
            return getFunctionStatusFuture.get(timeout, MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new DownstreamException("Exception waiting for functions status future", e);
        } finally {
            responseLock.unlock();
        }
    }

    @Override
    public void setFunctionStatus(int id, int status) {
        sendMessage(RequestMessageBuilder.setFunctionStatus(id, status));
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        LOG.info("Connection established");
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
        String[] elements = message.getPayload().toString().split("\\|");

        if (elements[0].equals("QLC+API")) {

            switch (elements[1]) {

                case "getFunctionsList":
                    handleGetFunctionsListMessage(elements);
                    break;

                case "getFunctionType":
                    handleGetFunctionTypeMessage(elements);
                    break;

                case "getFunctionStatus":
                    handleGetFunctionStatusMessage(elements);
                    break;

                default:
                    LOG.info("Unhandled message received, type: {}", elements[1]);
            }
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

    private void sendMessage(TextMessage message) {
        if (isNull(session) || !session.isOpen()) {
            connect();
        }

        try {
            session.sendMessage(message);
        } catch (IOException e) {
            throw new DownstreamException("Failed to send message \"" + message + "\"", e);
        }
    }

    private void handleGetFunctionsListMessage(String[] elements) {
        List<Function> functions = mapFunctions(elements);
        getFunctionsListFuture.complete(functions);
    }

    private List<Function> mapFunctions(String[] elements) {
        List<Function> functions = new ArrayList<>();

        for (int i = 2; i < elements.length; i += 2) {
            functions.add(new Function(Integer.parseInt(elements[i]), elements[i + 1]));
        }

        return functions;
    }

    private void handleGetFunctionTypeMessage(String[] elements) {
        getFunctionTypeFuture.complete(elements[2]);
    }

    private void handleGetFunctionStatusMessage(String[] elements) {
        getFunctionStatusFuture.complete(elements[2]);
    }

}
