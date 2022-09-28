package com.winnie.demo.ctrl;

import com.winnie.demo.service.kafka.SendKafka;
import com.winnie.demo.model.DAOTransaction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/SendKafka", produces = MediaType.APPLICATION_JSON_VALUE)
public class SendKafkaController {
    private static Log logger = LogFactory.getLog(SendKafkaController.class);
    @Autowired
    private SendKafka sendKafka;

    @PostMapping
    public ResponseEntity<DAOTransaction> sendKafka(@Valid @RequestBody DAOTransaction request) {
        logger.debug(new JSONObject().put("request", request));

        sendKafka.send(request);
        return ResponseEntity.ok().body(request);
    }
}