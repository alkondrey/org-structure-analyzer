package com.swissre.dto;

import java.math.BigDecimal;
import java.util.Objects;

public record Employee(
        int id,
        String firstName,
        String lastName,
        BigDecimal salary
) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee employee)) return false;
        return id == employee.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
