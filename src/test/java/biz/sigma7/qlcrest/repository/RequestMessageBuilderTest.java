package biz.sigma7.qlcrest.repository;

import org.junit.jupiter.api.Test;
import org.springframework.web.socket.TextMessage;

import static biz.sigma7.qlcrest.repository.RequestMessageBuilder.*;
import static org.assertj.core.api.Assertions.assertThat;

class RequestMessageBuilderTest {

    @Test
    public void getAllFunctions_returnsExpectedMessage() {
        TextMessage actual = getAllFunctions();
        TextMessage expected = new TextMessage("QLC+API|getFunctionsList");

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void getFunctionType_returnsExpectedMessage() {
        TextMessage actual = getFunctionType(445545);
        TextMessage expected = new TextMessage("QLC+API|getFunctionType|445545");

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void getFunctionStatus_returnsExpectedMessage() {
        TextMessage actual = getFunctionStatus(445545);
        TextMessage expected = new TextMessage("QLC+API|getFunctionStatus|445545");

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void setFunctionStatus_returnsExpectedMessage() {
        TextMessage actual = setFunctionStatus(445545, 1);
        TextMessage expected = new TextMessage("QLC+API|setFunctionStatus|445545|1");

        assertThat(actual).isEqualTo(expected);
    }

}
