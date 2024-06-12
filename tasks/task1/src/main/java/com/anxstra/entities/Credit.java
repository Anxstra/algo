package com.anxstra.entities;

import com.anxstra.entities.enums.Period;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class Credit {

    private Integer id;
    private Integer userId;
    private LocalDate date;
    private Period period;
    private BigDecimal money;
    private BigDecimal rate;
}
