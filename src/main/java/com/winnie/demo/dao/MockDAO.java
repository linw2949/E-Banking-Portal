package com.winnie.demo.dao;

import com.winnie.demo.misc.Tuple;
import com.winnie.model.Account;
import com.winnie.model.Transaction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.NotAcceptableStatusException;

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
                .iban("CH93-0000-0000-0000-0000-0").date("01-10-2020").description("Online payment CHF")
                .amount(new BigDecimal(75)).build();


        accountTable.add(new Account("winnie", "CH93-0000-0000-0000-0000-0"));
        transactionTable.add(transaction);
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
        return transaction;
    }

    public List<Transaction> callFunctionQueryTransaction(String username, String iban) {
        if (logger.isDebugEnabled()) {
            JSONObject logParams = new JSONObject();
            logParams.put("username", username);
            logParams.put("iban", iban);

            logger.debug(logParams);
        }

        Optional<Account> optional = accountTable.stream().filter(p -> p.getUserName().equals(username) && p.getIban().equals(iban)).findFirst();
        if (!optional.isPresent()) {
            throw new NotAcceptableStatusException("The iban cannot meet the conditions in the request header.");
        }

        return transactionTable.stream()
                .filter(p -> p.getIban().equals(iban))
                .sorted(Comparator.comparing(Transaction::getDate))
                .collect(Collectors.toList());
    }

    public List<Transaction> getPage(List<Transaction> sourceList, int page, int pageSize) {
        if (logger.isDebugEnabled()) {
            JSONObject logParams = new JSONObject();
            logParams.put("sourceList.size", sourceList.size());
            logParams.put("page", page);
            logParams.put("pageSize", pageSize);

            logger.debug(logParams);
        }

        if (pageSize <= 0 || page <= 0) {
            throw new IllegalArgumentException("invalid page size: " + pageSize);
        }

        int fromIndex = (page - 1) * pageSize;
        if (sourceList == null || sourceList.size() <= fromIndex) {
            return Collections.emptyList();
        }

        // toIndex exclusive
        return sourceList.subList(fromIndex, Math.min(fromIndex + pageSize, sourceList.size()));
    }

    public Optional<Transaction> findById(String id) {
        if (logger.isDebugEnabled()) {
            JSONObject logParams = new JSONObject();
            logParams.put("id", id);

            logger.debug(logParams);
        }

        return transactionTable.stream()
                .filter(p -> p.getId().equals(id)).findFirst();
    }

    public Optional<Account> findUser(String userName) {
        if (logger.isDebugEnabled()) {
            JSONObject logParams = new JSONObject();
            logParams.put("userName", userName);

            logger.debug(logParams);
        }

        return accountTable.stream().filter(p -> p.getUserName().equals(userName)).findFirst();
    }
}
