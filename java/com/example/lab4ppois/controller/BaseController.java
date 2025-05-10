package com.example.lab4ppois.controller;

import com.example.lab4ppois.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;

@Controller
public abstract class BaseController {
    protected final JwtService jwtService;
    protected final UserDetailsService userDetailsService;
    @Autowired
    public BaseController(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    protected boolean validateToken(String token) {
        if (token == null) return false;
        String username = jwtService.extractUsername(token);
        if (username == null) return false;
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return jwtService.validateToken(token, userDetails);
    }
}
