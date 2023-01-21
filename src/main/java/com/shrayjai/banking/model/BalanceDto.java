package com.shrayjai.banking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class BalanceDto {

    private String accountNumber;

    private BigDecimal balance;
}
