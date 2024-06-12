package com.anxstra.entities;

import com.anxstra.entities.enums.Currency;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class Transaction {

    private Integer id;
    private LocalDate date;
    private Integer userId;
    private Integer creditId;
    private Currency currency;
    private BigDecimal money;
}
