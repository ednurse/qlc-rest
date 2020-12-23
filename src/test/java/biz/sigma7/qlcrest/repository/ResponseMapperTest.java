package biz.sigma7.qlcrest.repository;

import biz.sigma7.qlcrest.domain.Function;
import biz.sigma7.qlcrest.domain.exception.DownstreamException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static biz.sigma7.qlcrest.repository.ResponseMapper.mapGetFunctionsResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ResponseMapperTest {

    @Test
    public void mapGetFunctionsResponse_returnsFunctionList() {
        String[] response = {
                "QLC+API",
                "getFunctionsList",
                "1",
                "funky function",
                "2",
                "funked up function"
        };
        List<Function> actual = mapGetFunctionsResponse(response);

        List<Function> expected = Arrays.asList(
                new Function(1, "funky function"),
                new Function(2, "funked up function")
        );

        assertThat(actual).containsExactlyElementsOf(expected);
    }

    @Test
    public void mapGetFunctionsResponse_throwsExceptionIfResponseIsTooSmall() {
        String[] response = {
                "QLC+API",
                "getFunctionsList"
        };

        assertThatThrownBy(() -> mapGetFunctionsResponse(response))
                .isInstanceOf(DownstreamException.class);
    }

    @Test
    public void mapGetFunctionsResponse_throwsExceptionIfHeaderIsIncorrect() {
        String[] response = {
                "SHASH",
                "getFunctionsList"
        };

        assertThatThrownBy(() -> mapGetFunctionsResponse(response))
                .isInstanceOf(DownstreamException.class);
    }

    @Test
    public void mapGetFunctionsResponse_throwsExceptionIfTypeIsIncorrect() {
        String[] response = {
                "QLC+API",
                "SHASH"
        };

        assertThatThrownBy(() -> mapGetFunctionsResponse(response))
                .isInstanceOf(DownstreamException.class);
    }

}
