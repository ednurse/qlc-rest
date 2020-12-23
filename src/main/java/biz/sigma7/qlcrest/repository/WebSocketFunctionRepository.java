package biz.sigma7.qlcrest.repository;

import biz.sigma7.qlcrest.domain.Function;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WebSocketFunctionRepository implements FunctionRepository {

    private final QlcWebSocketClient client;

    public WebSocketFunctionRepository(QlcWebSocketClient client) {
        this.client = client;
    }

    @Override
    public List<Function> getAllFunctions() {
        String[] response = client.sendMessageAndWaitForResponse(RequestMessageBuilder.getAllFunctions());
        return ResponseMapper.mapGetFunctionsResponse(response);
    }

    @Override
    public String getFunctionType(int id) {
        String[] response = client.sendMessageAndWaitForResponse(RequestMessageBuilder.getFunctionType(id));
        return ResponseMapper.mapGetFunctionTypeResponse(response);
    }

    @Override
    public String getFunctionStatus(int id) {
        String[] response = client.sendMessageAndWaitForResponse(RequestMessageBuilder.getFunctionStatus(id));
        return ResponseMapper.mapGetFunctionStatusResponse(response);
    }

    @Override
    public void setFunctionStatus(int id, int status) {
        client.sendMessage(RequestMessageBuilder.setFunctionStatus(id, status));
    }

}
