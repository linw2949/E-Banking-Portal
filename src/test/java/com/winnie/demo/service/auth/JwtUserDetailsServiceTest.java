package com.winnie.demo.service.auth;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtUserDetailsServiceTest {

    @Test
    void loadUserByUsername() {
        // given - setup or precondition
        JwtUserDetailsService jwtUserDetailsService = new JwtUserDetailsService();

        // when - action
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername("winnie");

        // then - verify the output
        assertThat(userDetails.getUsername().equals("winnie"));
        assertThat(userDetails.getPassword().equals("$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6"));
        UsernameNotFoundException thrown = assertThrows(
                UsernameNotFoundException.class,
                () -> jwtUserDetailsService.loadUserByUsername("lily"), "Expected UsernameNotFoundException to throw, but it didn't"
        );

        assertThat(thrown.getMessage().contains("JwtReq not found with usename: lily"));
    }
}