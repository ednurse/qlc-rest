package biz.sigma7.qlcrest.controller.representation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FunctionStatusRepresentation {

    private final int id;
    private final int value;

    @JsonCreator
    public FunctionStatusRepresentation(@JsonProperty("id") int id,
                                        @JsonProperty("value") int value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public int getValue() {
        return value;
    }

}
