package com.winnie.demo.service.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Component
public class JwtTokenUtil implements Serializable {
    private static Log logger = LogFactory.getLog(JwtTokenUtil.class);

    private static final long serialVersionUID = 1L;
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
    @Value("${jwt.secret}")
    private String secret;

    public String getUserIdFromToken(String token) {
        if (logger.isDebugEnabled()) {
            JSONObject logParams = new JSONObject();
            logParams.put("token", token);

            logger.debug(logParams);
        }

        return getClaimFromToken(token, Claims::getSubject);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        if (logger.isDebugEnabled()) {
            JSONObject logParams = new JSONObject();
            logParams.put("token", token);
            logParams.put("claimsResolver", claimsResolver);

            logger.debug(logParams);
        }
        final Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        if (logger.isDebugEnabled()) {
            JSONObject logParams = new JSONObject();
            logParams.put("userDetails", userDetails);

            logger.debug(logParams);
        }

        String token = Jwts.builder().setClaims(new HashMap<String, Object>()).setSubject(userDetails.getUsername()).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000)).signWith(SignatureAlgorithm.HS512, secret).compact();

        return token;
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        if (logger.isDebugEnabled()) {
            JSONObject logParams = new JSONObject();
            logParams.put("token", token);
            logParams.put("userDetails", userDetails);

            logger.debug(logParams);
        }

        final Date expiration = getClaimFromToken(token, Claims::getExpiration);
        final String username = getUserIdFromToken(token);
        return (username.equals(userDetails.getUsername()) && !expiration.before(new Date()));
    }
}
