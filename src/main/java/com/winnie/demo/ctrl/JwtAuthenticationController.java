package com.winnie.demo.ctrl;

import com.winnie.demo.service.util.JwtTokenUtil;
import com.winnie.model.JwtReq;
import com.winnie.model.JwtRes;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Expose a POST API /authenticate
 */
@RestController
@CrossOrigin
public class JwtAuthenticationController {
    private static Log logger = LogFactory.getLog(JwtAuthenticationController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService jwtInMemoryUserDetailsService;

    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody JwtReq authenticationRequest) throws Exception {
        if (logger.isDebugEnabled()) {
            JSONObject logParams = new JSONObject();
            logParams.put("authenticationRequest", authenticationRequest);

            logger.debug(logParams);
        }

        // 1. Authenticate the username and password is valid from the database(provided by the JwtUserDetailsService) -
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUserName(),authenticationRequest.getPassword());
        authenticationManager.authenticate(authenticationToken);

        // 2. The credentials are valid, a JWT token is created using the JWTTokenUtil and provided to the client. -----
        final UserDetails userDetails = jwtInMemoryUserDetailsService.loadUserByUsername(authenticationRequest.getUserName());
        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtRes(token));
    }
}
