package com.winnie.demo.service.kafka;

import com.winnie.demo.model.DAOTransaction;
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
    private KafkaTemplate<String, DAOTransaction> kafkaTemplate;

    @Value("${app.topic.transaction}")
    private String topic;

    public void send(DAOTransaction transaction){
        logger.debug(new JSONObject().put("transaction", transaction));

        kafkaTemplate.send(topic, transaction);
    }
}