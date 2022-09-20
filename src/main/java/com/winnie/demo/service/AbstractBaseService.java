package com.winnie.demo.service;

import com.winnie.demo.dao.MockDAO;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractBaseService {
    /**
     * General Data access object service
     */
    @Getter
    @Autowired
    private MockDAO mockDAO;
}