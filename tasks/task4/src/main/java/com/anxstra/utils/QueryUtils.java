package com.anxstra.utils;

import com.anxstra.exceptions.ParameterNullException;

import java.util.Objects;

public class QueryUtils {

    private QueryUtils() {
    }

    public static <T> T validateProperty(T value) {
        if (Objects.isNull(value)) {
            throw new ParameterNullException("Null value for non null field");
        }
        return value;
    }
}
