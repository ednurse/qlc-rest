package biz.sigma7.qlcrest.repository;

import org.springframework.web.socket.TextMessage;

public class RequestMessageBuilder {

    private static final String PREFIX = "QLC+API|";

    public static TextMessage getAllFunctions() {
        return new TextMessage(PREFIX + "getFunctionsList");
    }

    public static TextMessage getFunctionType(int id) {
        return new TextMessage(PREFIX + "getFunctionType|" + id);
    }

    public static TextMessage getFunctionStatus(int id) {
        return new TextMessage(PREFIX + "getFunctionStatus|" + id);
    }

    public static TextMessage setFunctionStatus(int id, int status) {
        return new TextMessage(PREFIX + "setFunctionStatus|" + id + "|" + status);
    }

}
