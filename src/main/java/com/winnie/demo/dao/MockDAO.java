package com.winnie.demo.dao;

import com.winnie.demo.misc.Tuple;
import com.winnie.demo.service.util.Format;
import com.winnie.model.Account;
import com.winnie.model.Transaction;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * For simplicity reasons, simulate the statement operation of the database in MockDAO
 *
 * @author Winnie Lin
 * @date 19/09/2022
 */
@Repository
public class MockDAO {
    private static Log logger = LogFactory.getLog(MockDAO.class);
    /**
     * Mock Account Table
     */
    private final List<Account> accountTable = new ArrayList<>();
    /**
     * Mock Transaction Table
     */
    private final List<Transaction> transactionTable = new ArrayList<>();

    /**
     * Mock Account and Transaction View
     */
    private List<Tuple<Account, Transaction>> AccountTransactionView = new ArrayList<>();

    @PostConstruct
    private void initDB() {
        Transaction transaction = Transaction.builder().id("89d3o179-abcd-465b-o9ee-e2d5f6ofEld46").currency("CHF")
                .iban("CH93-0000-0000-0000-0000-0").date("20220921").description("Online payment CHF")
                .amount(new BigDecimal(75)).build();


        accountTable.add(new Account("CH93-0000-0000-0000-0000-0", "winnie"));
        accountTable.add(new Account("CH93-0000-0000-0000-0000-1", "winnie"));
        accountTable.add(new Account("CH93-0000-0000-0000-0000-2", "winnie"));
        accountTable.add(new Account("CH93-0000-0000-0000-0000-3", "lily"));
        accountTable.add(new Account("CH93-0000-0000-0000-0000-4", "lily"));
        transactionTable.add(transaction);
        view();
    }
    private void view() {
        AccountTransactionView = joinOn(
                accountTable,
                transactionTable,
                t -> t.a.getIban().equals(t.b.getIban())
        );
    }
    /**
     * this method mock Oracle DB join
     *
     * @param l1        List1 to be joined
     * @param l2        List2 to be joined
     * @param predicate join condition
     * @param <A>       List1 data type
     * @param <B>       List2 data type
     * @return
     */
    public static <A, B> List<Tuple<A, B>> joinOn(List<A> l1, List<B> l2, Predicate<Tuple<A, B>> predicate) {
        return l1.stream()
                .flatMap(a -> l2.stream().map(b -> new Tuple<>(a, b)))
                .filter(predicate)
                .collect(Collectors.toList());
    }

    /**
     * Mock Oracle Sql Function : insert into transactionTable
     *
     * @param transaction the transaction to be inserted into transactionTable
     * @return Transaction the data has been inserted successfully
     */
    public Transaction callFunctionInsertTransaction(Transaction transaction) {
        if (logger.isDebugEnabled()) {
            JSONObject logParams = new JSONObject();
            logParams.put("transaction", transaction);

            logger.debug(logParams);
        }

        transactionTable.add(transaction);
        view();
        return transaction;
    }

    public List<Transaction> callFunctionQueryTransaction(String username, String iban, int pageNo, int pageSize) {
        if (logger.isDebugEnabled()) {
            JSONObject logParams = new JSONObject();
            logParams.put("username", username);
            logParams.put("iban", iban);
            logParams.put("pageNo", pageNo);
            logParams.put("pageSize", pageSize);

            logger.debug(logParams);
        }

        int fromIndex = (pageNo - 1) * pageSize;
        String aYearAgo = Format.dateFormat(DateUtils.addYears(new Date(), -1), "03"); // 03: yyyyMMdd
        List<Transaction> transaction = AccountTransactionView.stream()
                .filter(p -> p.a.getIban().equals(iban) && p.a.getUserName().equals(username) && p.b.getDate().compareTo(aYearAgo)>0)
                .map(p -> p.b).sorted(Comparator.comparing(Transaction::getDate).reversed()).collect(Collectors.toList());

        return transaction.subList(fromIndex, Math.min(fromIndex + pageSize, transaction.size()));
    }

    public Optional<Transaction> callFunctionGetTransactionById(String id) {
        if (logger.isDebugEnabled()) {
            JSONObject logParams = new JSONObject();
            logParams.put("id", id);

            logger.debug(logParams);
        }

        return transactionTable.stream()
                .filter(p -> p.getId().equals(id)).findFirst();
    }
}
