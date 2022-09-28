package com.winnie.demo.ctrl;

import com.winnie.demo.model.DAOTransaction;
import com.winnie.demo.model.TransactionRes;
import com.winnie.demo.service.TransactionService;
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
        logger.debug(new JSONObject().put("request", request));

        DAOTransaction data = transactionService.insertTransaction(request);

        return ResponseEntity.ok().body(data);
    }


    @GetMapping("/{iban}/page={pageNo}&pageSize={pageSize}")
    public ResponseEntity<TransactionRes> queryTransaction(@PathVariable("iban") String iban, @PathVariable("pageNo") int pageNo, @PathVariable("pageSize") int pageSize) {
        logger.debug(new JSONObject().put("iban", iban).put("pageNo", pageNo).put("pageSize", pageSize));
        return ResponseEntity.ok().body(transactionService.findOwnTransactionByIban(iban, pageNo, pageSize));
    }
}