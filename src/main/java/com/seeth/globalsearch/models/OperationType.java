package com.seeth.globalsearch.models;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OperationType {

    GREATER_THAN		(">"),
    LESS_THAN			("<"),
    EQUALS_TO			(":"),
    LIKE				("%"),
    NOT_EQUALS_TO		("<>"), 
    IN					("IN");

    private final String key;

    @Override
    public String toString() {
        return key;
    }
}
