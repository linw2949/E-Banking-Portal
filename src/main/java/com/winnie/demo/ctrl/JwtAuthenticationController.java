package com.winnie.demo.ctrl;

import com.winnie.demo.model.DAOUser;
import com.winnie.demo.model.JwtRes;
import com.winnie.demo.service.auth.JwtUserDetailsService;
import com.winnie.demo.service.util.JwtTokenUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
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
    private JwtUserDetailsService userDetailsService;

    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody DAOUser authenticationRequest) throws Exception {
        authenticate(authenticationRequest);

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUserId());
        final String token = jwtTokenUtil.generateToken(userDetails);

        logger.debug(new JSONObject().put("userDetails", userDetails));
        logger.debug(new JSONObject().put("token", token));

        return ResponseEntity.ok(new JwtRes(token));
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> saveUser(@RequestBody DAOUser user) {
        logger.debug(new JSONObject().put("user", user));
        return ResponseEntity.ok(userDetailsService.save(user));
    }

    private void authenticate(DAOUser authenticationReques) throws Exception {
        logger.debug(new JSONObject().put("authenticationReques", authenticationReques));

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationReques.getUserId(), authenticationReques.getPassword()));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
