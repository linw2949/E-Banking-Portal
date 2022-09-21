package com.winnie.demo.service.kafka;

import com.winnie.model.Transaction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class SendKafka {

    private static Log logger = LogFactory.getLog(SendKafka.class);

    @Autowired
    private KafkaTemplate<String, Transaction> kafkaTemplate;

    @Value("${app.topic.transaction}")
    private String topic;

    public void send(Transaction transaction){
        if (logger.isDebugEnabled()) {
            JSONObject logParams = new JSONObject();
            logParams.put("transaction", transaction);

            logger.debug(logParams);
        }

        kafkaTemplate.send(topic, transaction);
    }
}