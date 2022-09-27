package com.winnie.demo.ctrl;

import com.winnie.demo.service.TransactionService;
import com.winnie.demo.model.DAOTransaction;
import com.winnie.demo.model.TransactionRes;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/transaction")
public class TransactionController {
    private static Log logger = LogFactory.getLog(TransactionController.class);
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private HttpServletRequest request;

    @PostMapping("/create")
    public ResponseEntity<DAOTransaction> createTransaction(@Valid @RequestBody DAOTransaction request) {
        if (logger.isDebugEnabled()) {
            logger.debug(new JSONObject());
        }

        DAOTransaction data = transactionService.insertTransaction(request);

        return ResponseEntity.ok().body(data);
    }


    @GetMapping("/{iban}/page={pageNo}&pageSize={pageSize}")
    public ResponseEntity<TransactionRes> queryTransaction(@PathVariable("iban") String iban, @PathVariable("pageNo") int pageNo, @PathVariable("pageSize") int pageSize) {
        if (logger.isDebugEnabled()) {
            logger.debug(new JSONObject());
        }

        final String requestTokenHeader = request.getHeader("Authorization");
        String jwtToken = requestTokenHeader.substring(7);
        return ResponseEntity.ok().body(transactionService.findTransactionByIban(jwtToken, iban, pageNo, pageSize));
    }
}