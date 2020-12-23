package biz.sigma7.qlcrest.repository;

import biz.sigma7.qlcrest.domain.Function;
import biz.sigma7.qlcrest.domain.exception.DownstreamException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResponseMapper {

    public static List<Function> mapGetFunctionsResponse(String[] response) {
        checkType(response, "getFunctionsList");

        List<Function> functions = new ArrayList<>();

        for (int i = 2; i < response.length; i += 2) {
            functions.add(new Function(Integer.parseInt(response[i]), response[i + 1]));
        }

        return functions;
    }

    public static String mapGetFunctionTypeResponse(String[] response) {
        checkType(response, "getFunctionType");

        return response[2];
    }

    public static String mapGetFunctionStatusResponse(String[] response) {
        checkType(response, "getFunctionStatus");

        return response[2];
    }

    private static void checkType(String[] response, String type) {
        if (response.length < 3
                || !response[0].equals("QLC+API")
                || !response[1].equals(type)
        ) {
            throw new DownstreamException("Expecting QLC+API response of type \"" + type + "\", " +
                    "but got " + Arrays.toString(response));
        }
    }

}
