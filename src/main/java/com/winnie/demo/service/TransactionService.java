package com.winnie.demo.service;

import com.winnie.demo.dao.TransactionDao;
import com.winnie.demo.misc.UnprocessableEntityException;
import com.winnie.demo.model.DAOTransaction;
import com.winnie.demo.model.TransactionRes;
import com.winnie.demo.service.util.Format;
import com.winnie.demo.service.util.JwtTokenUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class TransactionService{
    private static Log logger = LogFactory.getLog(TransactionService.class);
    @Autowired
    private TransactionDao transactionDao;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private AccountService accountService;
    private List<DAOTransaction> findTransactionByIban(String iban, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo-1, pageSize, Sort.by("date").descending());
        return transactionDao.findAllByIban(iban,pageable);
    }

    private DAOTransaction getTransactionById(String id) {
        return transactionDao.findById(id);
    }

    @Transactional
    public DAOTransaction insertTransaction(DAOTransaction trans) {
        logger.debug(new JSONObject().put("trans", trans));

        // 1. check if the primary key is already existed. If so, return HTTP status 422 -------------------------------
        DAOTransaction daoTransaction = getTransactionById(trans.getId());
        if (daoTransaction!=null) {
            throw new UnprocessableEntityException("The id of the product is duplicated.");
        }

        // 2. insert into the DB with the transaction, and return the transaction which has been inserted --------------
        return transactionDao.save(trans);
    }


    public TransactionRes findOwnTransactionByIban(String iban, int pageNo, int pageSize) {
        logger.debug(new JSONObject().put("iban", iban).put("pageNo", pageNo).put("pageSize", pageSize));

        // 1. Query the DB and get the paginated list of the given iban money account transactions ---------------------
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!accountService.findAccountByIban(iban).getUserId().equals(userId)){
            throw new UnprocessableEntityException("The Account is not yours.");
        }

        List<DAOTransaction> transactionList = findTransactionByIban(iban, pageNo, pageSize);
        int totalCount = transactionList.size();
        if (totalCount == 0) return new TransactionRes();
        String endDate = Format.dateFormat(transactionList.get(0).getDate(), "01"); // 01: yyyyMMdd-> yyyy-MM-dd
        String startDate = Format.dateFormat(transactionList.get(totalCount - 1).getDate(), "01"); // 01: yyyyMMdd-> yyyy-MM-dd

        // 2. Get exchange rate on any given date is provided by an external API ---------------------------------------
        JSONObject dateExchange = getExchangeOfDateRange(startDate, endDate);

        BigDecimal totalCredit = BigDecimal.ZERO;
        BigDecimal totalDebit = BigDecimal.ZERO;

        // 3. Sum the total credit and debit values --------------------------------------------------------------------
        for (DAOTransaction p : transactionList) {
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

        return transactionRes;
    }

    public JSONObject getExchangeOfDateRange(String startDate, String endDate) {
        logger.debug(new JSONObject().put("startDate", startDate).put("endDate",endDate));

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
