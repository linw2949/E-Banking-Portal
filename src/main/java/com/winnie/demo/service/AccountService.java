package com.winnie.demo.service;

import com.winnie.demo.dao.AccountDao;
import com.winnie.demo.model.DAOAccount;
import com.winnie.demo.service.util.JwtTokenUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    private static Log logger = LogFactory.getLog(AccountService.class);
    @Autowired
    private AccountDao AccountDao;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public DAOAccount findAccountByIban(String iban) {
        return AccountDao.findByIban(iban);
    }

    public List<DAOAccount> findAccountByUserId(String jwtToken) {
        return AccountDao.findAllByUserId(jwtTokenUtil.getUserIdFromToken(jwtToken));
    }

    public DAOAccount save(DAOAccount daoAccount) {
        return AccountDao.save(daoAccount);
    }
}
