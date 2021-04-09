package com.epam.esm.model;

import java.util.Objects;

public class Tag extends Entity{
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return super.equals(o);

    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
