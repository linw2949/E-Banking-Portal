package com.winnie.demo.service;

import com.winnie.demo.dao.GeneralSimpleJdbcCallDAO;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractBaseService {
    /**
     * General SimpleJdbcCall Data access object service
     */
    @Getter
    @Autowired
    private GeneralSimpleJdbcCallDAO generalSimpleJdbcCallDAO;
}