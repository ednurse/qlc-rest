package biz.sigma7.qlcrest.domain;

import java.util.Objects;

public class Function {

    private final int id;
    private final String name;

    public Function(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Function{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Function function = (Function) o;
        return id == function.id && Objects.equals(name, function.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
