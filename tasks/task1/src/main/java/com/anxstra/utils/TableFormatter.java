package com.anxstra.utils;

public class TableFormatter {


    public static final String SEPARATOR_ROW = "_".repeat(128) + "\n";

    public static final String ROW_FORMAT = "| %9d | %7d | %31s | %18d | %11.2f | %13s | %18s |\n";

    public static final String HEADER = "| Credit id | User id |" + formatHeader("FIO", 15)
            + "| Transaction amount | Debt amount | Credit Period |" + formatHeader("Status", 7) + "|\n";

    private TableFormatter() {
    }

    private static String formatHeader(String head, int spaceCount) {
        return " ".repeat(spaceCount) + head + " ".repeat(spaceCount);
    }


}
