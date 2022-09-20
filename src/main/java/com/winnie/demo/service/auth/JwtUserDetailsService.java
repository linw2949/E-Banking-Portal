package com.winnie.demo.service.auth;

import com.winnie.demo.service.AbstractBaseService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService extends AbstractBaseService implements UserDetailsService {
    private static Log logger = LogFactory.getLog(JwtUserDetailsService.class);

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        if (logger.isTraceEnabled()) {
            JSONObject logParams = new JSONObject();
            logParams.put("userName", userName);

            logger.debug(logParams);
        }

        if (userName.equals("winnie")) { // encoded the password, which is “password” as “$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6” for our convenience.
            return new User("winnie", "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6", new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("JwtReq not found with usename: " + userName);
        }
    }

}