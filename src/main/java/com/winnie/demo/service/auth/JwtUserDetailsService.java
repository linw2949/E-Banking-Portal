package com.winnie.demo.service.auth;

import com.winnie.demo.dao.UserDao;
import com.winnie.demo.model.DAOUser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    private static Log logger = LogFactory.getLog(JwtUserDetailsService.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder bcryptEncoder;


    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        DAOUser user = userDao.findByUserId(userId);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with userId: " + userId);
        }
        return new org.springframework.security.core.userdetails.User(user.getUserId(), user.getPassword(),
                new ArrayList<>());
    }

    public DAOUser save(DAOUser user) {
        DAOUser newUser = new DAOUser();
        newUser.setUserId(user.getUserId());
        newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
        return userDao.save(newUser);
    }
}