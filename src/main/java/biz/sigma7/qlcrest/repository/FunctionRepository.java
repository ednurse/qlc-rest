package biz.sigma7.qlcrest.repository;

import biz.sigma7.qlcrest.domain.Function;

import java.util.List;

public interface FunctionRepository {

    List<Function> getAllFunctions();
    String getFunctionType(int id);
    String getFunctionStatus(int id);
    void setFunctionStatus(int id, int status);
}
