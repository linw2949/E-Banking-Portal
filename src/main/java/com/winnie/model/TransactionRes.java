package com.winnie.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRes extends PagingRes implements Serializable {
    private static final long serialVersionUID = 1L;
    private BigDecimal totalCredit;
    private BigDecimal totalDebit ;
    private List<Transaction> transactionList = new ArrayList<>();
}
