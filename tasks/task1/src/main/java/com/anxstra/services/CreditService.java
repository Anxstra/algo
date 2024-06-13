package com.anxstra.services;

import com.anxstra.dao.repositories.CreditRepository;
import com.anxstra.dao.repositories.DiscountRepository;
import com.anxstra.dao.repositories.EventRepository;
import com.anxstra.dao.repositories.TransactionRepository;
import com.anxstra.entities.Credit;
import com.anxstra.entities.Discount;
import com.anxstra.entities.Event;
import com.anxstra.entities.Setting;
import com.anxstra.entities.Transaction;
import com.anxstra.entities.User;
import com.anxstra.gson.config.AppConfigurer;
import com.anxstra.utils.DateUtils;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class CreditService {

    private final CreditRepository creditRepository;

    private final TransactionRepository transactionRepository;

    private final EventRepository eventRepository;

    private final DiscountRepository discountRepository;

    public List<Credit> processUserCredits(User user) {
        Setting setting = AppConfigurer.getSetting();
        List<Credit> credits = creditRepository.findAllByUserIdAndPeriod(user.getId(),
                setting.getDateFrom(), setting.getDateTo() == null ? LocalDate.MAX : setting.getDateTo());
        for (Credit credit : credits) {
            LocalDate periodStart = credit.getDate();
            LocalDate settingDate = setting.getDateTo() == null ?
                    transactionRepository.getLastTransactionDateByCreditId(credit.getId()) : setting.getDateTo();
            for (int i = 0; i <= DateUtils.getPeriodCount(credit, settingDate); i++) {
                LocalDate periodEnd = periodStart.plus(1, credit.getPeriod()
                                                                 .unit())
                                                  .minusDays(1);
                if (i != 0 && !periodStart.isAfter(settingDate)) {
                    Optional<Discount> discount = discountRepository.findMaxByDate(periodStart);
                    credit.increaseDebt(discount.orElse(null));
                }
                if (processTransactionsDuringPeriod(credit, periodStart, periodEnd)) {
                    break;
                }
                periodStart = periodEnd.plusDays(1);
            }
        }
        return credits;
    }

    private boolean processTransactionsDuringPeriod(Credit credit, LocalDate periodStart, LocalDate periodEnd) {
        List<Transaction> transactions = transactionRepository.findAllByCreditIdAndPeriod(credit.getId(),
                periodStart, periodEnd);
        LocalDate lastTransactionDate = periodStart;
        boolean isRepaid = false;
        for (Transaction transaction : transactions) {
            Optional<Event> event = eventRepository.findLatestByPeriodAndCurrency(lastTransactionDate,
                    transaction.getDate(), transaction.getCurrency());
            transaction.convert(event.orElse(null));
            credit.applyTransaction(transaction);
            isRepaid = credit.getRepaymentDate() != null;
            lastTransactionDate = transaction.getDate();
        }
        return isRepaid;
    }

}
