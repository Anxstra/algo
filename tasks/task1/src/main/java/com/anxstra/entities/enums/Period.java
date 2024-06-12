package com.anxstra.entities.enums;

public enum Period {
    DAY("Day"),
    WEEK("Week"),
    MONTH("Month"),
    YEAR("Year");

    private final String periodName;

    Period(String periodName) {
        this.periodName = periodName;
    }

    @Override
    public String toString() {
        return periodName;
    }
}
