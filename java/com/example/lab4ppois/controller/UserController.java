package com.example.lab4ppois.controller;


import com.example.lab4ppois.entity.AuthRequest;
import com.example.lab4ppois.entity.UserInfo;
import com.example.lab4ppois.service.JwtService;
import com.example.lab4ppois.service.UserInfoService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class UserController {
    private final UserInfoService service;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserController(UserInfoService service, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.service = service;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @PostMapping("/add-new-user")
    public String addNewUser(@RequestBody UserInfo userInfo) {
        return service.addUser(userInfo);
    }

    @PostMapping("/generate-token")
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody AuthRequest authRequest,
                                                     HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(),
                            authRequest.getPassword())
            );

            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(authRequest.getUsername());

                Cookie cookie = new Cookie("JWT", token);
                cookie.setHttpOnly(true);
                cookie.setSecure(false);
                cookie.setPath("/");
                cookie.setMaxAge(24 * 60 * 60);
                response.addCookie(cookie);

                return ResponseEntity.ok().body(Map.of(
                        "message", "Login successful",
                        "token", token
                ));
            } else {
                throw new UsernameNotFoundException("Invalid user request!");
            }
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    Map.of("message", "Invalid credentials")
            );
        }
    }
}