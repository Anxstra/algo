package com.anxstra.services;

import com.anxstra.entities.Credit;
import com.anxstra.entities.User;
import com.anxstra.gson.config.AppConfigurer;
import com.anxstra.gson.config.DBReader;
import com.anxstra.utils.TableFormatter;
import lombok.AllArgsConstructor;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
public class AppService {

    private UserService userService;

    private CreditService creditService;

    public void calculateUsersCredits() {
        DBReader.initDB();
        List<User> users = userService.findAllByShowType();
        List<Credit> credits = new LinkedList<>();
        for (User user : users) {
            credits.addAll(creditService.processUserCredits(user));
        }
        printToConsole(credits);
    }

    private void printToConsole(List<Credit> credits) {
        StringBuilder table = new StringBuilder();
        table.append(TableFormatter.SEPARATOR_ROW);
        table.append(TableFormatter.HEADER);
        table.append(TableFormatter.SEPARATOR_ROW);
        switch (AppConfigurer.getSetting()
                             .getSortBy()) {
            case NAME -> credits.sort((cr1, cr2) -> userService.findById(cr1.getUserId())
                                                               .getFullName()
                                                               .compareTo(userService.findById(cr2.getUserId())
                                                                                     .getFullName()));
            case DEBT -> credits.sort(Comparator.comparing(Credit::getMoney));
            case AGE -> credits.sort(Comparator.comparing(Credit::getDate));
        }
        for (Credit credit : credits) {
            String name = userService.findById(credit.getUserId())
                                     .getFullName();
            table.append(String.format(TableFormatter.ROW_FORMAT, credit.getId(), credit.getUserId(),
                    name, credit.getProcessedTransactions(), credit.getMoney(), credit.getPeriod(),
                    credit.getRepaymentDate() == null ? "IN PROGRESS" : "DONE(" + credit.getRepaymentDate() + ")"));
            table.append(TableFormatter.SEPARATOR_ROW);
        }
        System.out.println(table);
    }


}
