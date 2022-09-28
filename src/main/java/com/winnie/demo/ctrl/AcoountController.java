package com.winnie.demo.ctrl;

import com.winnie.demo.model.DAOAccount;
import com.winnie.demo.service.AccountService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/account")
public class AcoountController {
    private static Log logger = LogFactory.getLog(AcoountController.class);
    @Autowired
    private AccountService accountService;

    @Autowired
    private HttpServletRequest request;

    @PostMapping("/create")
    public ResponseEntity<DAOAccount> createTransaction(@Valid @RequestBody DAOAccount request) {
        return ResponseEntity.ok().body(accountService.save(request));
    }

    @GetMapping("/myAccount")
    public ResponseEntity<List<DAOAccount>> queryTransaction() {
        return ResponseEntity.ok().body(accountService.findAccountByUserId());
    }
}