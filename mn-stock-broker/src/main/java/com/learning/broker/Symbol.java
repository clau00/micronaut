package com.learning.broker;

import java.util.Objects;

public class Symbol {

    private String value;

    public Symbol() {}

    public Symbol(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Symbol symbol = (Symbol) o;
        return value.equals(symbol.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
