package com.anxstra.dao.implementations;

import com.anxstra.dao.repositories.CreditRepository;
import com.anxstra.entities.Credit;
import com.anxstra.gson.config.DBReader;
import com.anxstra.gson.config.GsonConfigurer;
import com.anxstra.utils.DateUtils;

import java.time.LocalDate;
import java.util.List;

public class CreditRepositoryImpl implements CreditRepository {

    @Override
    public List<Credit> findAllByUserIdAndPeriod(Integer userId, LocalDate from, LocalDate to) {
        return DBReader.getJsonArray("credits")
                       .asList()
                       .stream()
                       .map(el -> GsonConfigurer.getGson()
                                                .fromJson(el, Credit.class))
                       .filter(credit -> credit.getUserId()
                                               .equals(userId) && DateUtils.isBetween(credit.getDate(), from, to))
                       .toList();
    }
}
