package com.winnie.demo.service;

import com.winnie.demo.misc.UnprocessableEntityException;
import com.winnie.demo.service.util.Format;
import com.winnie.demo.service.util.JwtTokenUtil;
import com.winnie.demo.service.util.SimpleJdbcCallUtils;
import com.winnie.model.Transaction;
import com.winnie.model.TransactionRes;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TransactionService extends AbstractBaseService {
    private static Log logger = LogFactory.getLog(TransactionService.class);
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private List<Transaction> queryTransaction(String userId, String iban, int pageNo, int pageSize) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("iban", iban)
                .addValue("pageNo", pageNo)
                .addValue("pageSize", pageSize);

        Map<String, Object> map = getGeneralSimpleJdbcCallDAO().doCallProcedure("QueryTransaction", parameterSource);
        return SimpleJdbcCallUtils.convertMapList2BeanList((List<?>) map.get("#result-set-1"), Transaction.class);
    }

    private Optional<Transaction> getTransactionById(String id) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", id);

        Map<String, Object> map = getGeneralSimpleJdbcCallDAO().doCallProcedure("GetTransactionById", parameterSource);
        return SimpleJdbcCallUtils.convertMapList2BeanList((List<?>) map.get("#result-set-1"), Transaction.class).stream().findFirst();
    }

    @Transactional
    public Transaction insertTransaction(Transaction trans) {
        if (logger.isDebugEnabled()) {
            JSONObject logParams = new JSONObject();
            logParams.put("trans", trans);

            logger.debug(logParams);
        }

        // 1. check if the primary key is already existed. If so, return HTTP status 422 -------------------------------
        Optional<Transaction> optional = getTransactionById(trans.getId());
        if (optional.isPresent()) {
            throw new UnprocessableEntityException("The id of the product is duplicated.");
        }

        // 2. insert into the DB with the transaction, and return the transaction which has been inserted --------------
        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", trans.getId())
                .addValue("currency", trans.getCurrency())
                .addValue("iban", trans.getIban())
                .addValue("date", trans.getDate())
                .addValue("description", trans.getDescription())
                .addValue("amount", trans.getAmount());

        getGeneralSimpleJdbcCallDAO().doCallProcedure("InsertTransaction", parameterSource);
        return trans;
    }


    public TransactionRes findTransactionByIban(String jwtToken, String iban, int pageNo, int pageSize) {
        if (logger.isDebugEnabled()) {
            JSONObject logParams = new JSONObject();
            logParams.put("jwtToken", jwtToken);
            logParams.put("iban", iban);
            logParams.put("pageNo", pageNo);
            logParams.put("pageSize", pageSize);

            logger.debug(logParams);
        }

        // 1. Query the DB and get the paginated list of the given iban money account transactions ---------------------
        String username = jwtTokenUtil.getUserIdFromToken(jwtToken);
        List<Transaction> transactionList = queryTransaction(username, iban, pageNo, pageSize);
        int totalCount = transactionList.size();
        if (totalCount == 0) return new TransactionRes();
        String endDate = Format.dateFormat(transactionList.get(0).getDate(), "01"); // 01: yyyyMMdd-> yyyy-MM-dd
        String startDate = Format.dateFormat(transactionList.get(totalCount - 1).getDate(), "01"); // 01: yyyyMMdd-> yyyy-MM-dd

        // 2. Get exchange rate on any given date is provided by an external API ---------------------------------------
        JSONObject dateExchange = getExchangeOfDateRange(startDate, endDate);

        BigDecimal totalCredit = BigDecimal.ZERO;
        BigDecimal totalDebit = BigDecimal.ZERO;

        // 3. Sum the total credit and debit values --------------------------------------------------------------------
        for (Transaction p : transactionList) {
            String dateKey = Format.dateFormat(p.getDate(), "01"); // 01: yyyyMMdd-> yyyy-MM-dd
            BigDecimal exchangeRate = (BigDecimal) dateExchange.getJSONObject(dateKey).get(p.getCurrency());
            BigDecimal baseAmount = p.getAmount().divide(exchangeRate, 2, RoundingMode.HALF_UP);
            // 3.1. If the amount is greater than zero, then add to totalCredit; otherwise, totalDebit -----------------
            if (baseAmount.compareTo(BigDecimal.ZERO) > 0) {
                totalCredit = totalCredit.add(new BigDecimal(baseAmount.toString()));
            } else {
                totalDebit = totalDebit.add(baseAmount);
            }
        }

        // 4. Assemble the resulting parameters to TransactionRes object and return ------------------------------------
        TransactionRes transactionRes = TransactionRes.builder().totalCredit(totalCredit).totalDebit(totalDebit)
                .transactionList(transactionList).build();
        transactionRes.setPageNo(pageNo);
        transactionRes.setTotalCount(totalCount);

        return transactionRes;
    }

    public JSONObject getExchangeOfDateRange(String startDate, String endDate) {
        if (logger.isDebugEnabled()) {
            JSONObject logParams = new JSONObject();
            logParams.put("startDate", startDate);
            logParams.put("endDate", endDate);

            logger.debug(logParams);
        }

        // 1. Build the details of http request ------------------------------------------------------------------------
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder().url("https://api.apilayer.com/exchangerates_data/timeseries?start_date=" + startDate + "&end_date=" + endDate).addHeader("apikey", "d3WeawwnaMwCsvfSitJmzp8oEgrVfRiO").method("GET", null).build();

        // 2. Execute the request immediately and blocks until the response can be processed or is in error ------------
        String body = "";
        try {
            Response response = client.newCall(request).execute();
            body = response.body().string();
            response.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new JSONObject(body).getJSONObject("rates");
    }
}
