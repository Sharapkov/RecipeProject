package com.pasta.security;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final TokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;
    private final String jwtHeaderString;
    private final String jwtTokenPrefix;

    public JwtConfigurer(TokenProvider tokenProvider, UserDetailsService userDetailsService, String jwtHeaderString, String jwtTokenPrefix) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
        this.jwtHeaderString = jwtHeaderString;
        this.jwtTokenPrefix = jwtTokenPrefix;
    }

    @Override
    public void configure(HttpSecurity http) {
        JwtFilter customFilter = new JwtFilter(jwtHeaderString, jwtTokenPrefix, tokenProvider, userDetailsService);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
