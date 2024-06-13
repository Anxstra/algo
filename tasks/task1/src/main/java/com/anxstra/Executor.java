package com.anxstra;

import com.anxstra.dao.implementations.CreditRepositoryImpl;
import com.anxstra.dao.implementations.DiscountRepositoryImpl;
import com.anxstra.dao.implementations.EventRepositoryImpl;
import com.anxstra.dao.implementations.TransactionRepositoryImpl;
import com.anxstra.dao.implementations.UserRepositoryImpl;
import com.anxstra.services.AppService;
import com.anxstra.services.CreditService;
import com.anxstra.services.UserService;

public class Executor {

    private final AppService appService;

    private Executor(AppService appService) {
        this.appService = appService;
    }

    public static void main(String[] args) {
        UserService userService = new UserService(new UserRepositoryImpl());
        CreditService creditService = new CreditService(new CreditRepositoryImpl(), new TransactionRepositoryImpl(),
                new EventRepositoryImpl(), new DiscountRepositoryImpl());
        Executor executor = new Executor(new AppService(userService, creditService));
        executor.execute();
    }

    private void execute() {
        appService.calculateUsersCredits();
    }
}