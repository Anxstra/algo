package com.anxstra.entities;

import java.util.ArrayList;
import java.util.List;

public class DBFile {

    List<User> users;

    List<Credit> credits;

    List<Discount> discounts;

    List<Event> events;

    List<Transaction> transactions;

    public DBFile() {
        users = new ArrayList<>();
        credits = new ArrayList<>();
        discounts = new ArrayList<>();
        events = new ArrayList<>();
        transactions = new ArrayList<>();
    }
}
