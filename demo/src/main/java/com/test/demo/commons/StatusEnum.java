package com.test.demo.commons;

public enum StatusEnum {
    STARTED("STARTED"),
    FINISHED("FINISHED");

    String value;

    StatusEnum(String value)
    {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
