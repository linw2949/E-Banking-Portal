package com.winnie.demo.service.kafka;

import com.winnie.demo.service.TransactionService;
import com.winnie.model.Transaction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ReceiveKafka {
    @Autowired
    private TransactionService transactionService;
    private static Log logger = LogFactory.getLog(ReceiveKafka.class);

    @KafkaListener(topics = "${app.topic.transaction}", containerFactory = "transactionListener")
    public void receive(Transaction transaction) {
        if (logger.isDebugEnabled()) {
            JSONObject logParams = new JSONObject();
            logParams.put("transaction", transaction);

            logger.debug(logParams);
        }
        transactionService.insertTransaction(transaction);
    }
}
