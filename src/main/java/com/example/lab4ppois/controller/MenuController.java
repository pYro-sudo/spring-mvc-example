package com.example.lab4ppois.controller;

import com.example.lab4ppois.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/menu")
public class MenuController extends BaseController {
    @Autowired
    public MenuController(JwtService jwtService, UserDetailsService userDetailsService) {
        super(jwtService, userDetailsService);
    }

    @GetMapping
    public Mono<String> showMenu(@CookieValue(name = "JWT", required = false) String token) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        return Mono.just("menu");
    }

    @GetMapping("/child")
    public Mono<String> showChildMenu(@CookieValue(name = "JWT", required = false) String token) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        return Mono.just("child");
    }

    @GetMapping("/classroom")
    public Mono<String> showClassroomMenu(@CookieValue(name = "JWT", required = false) String token) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        return Mono.just("classroom");
    }

    @GetMapping("/exercising-games")
    public Mono<String> showExercisingGamesMenu(@CookieValue(name = "JWT", required = false) String token) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        return Mono.just("exercising-games");
    }

    @GetMapping("/parent")
    public Mono<String> showParentMenu(@CookieValue(name = "JWT", required = false) String token) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        return Mono.just("parent");
    }

    @GetMapping("/studying-materials")
    public Mono<String> showStudyingMaterialsMenu(@CookieValue(name = "JWT", required = false) String token) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        return Mono.just("studying-materials");
    }

    @GetMapping("/teacher")
    public Mono<String> showTeacherMenu(@CookieValue(name = "JWT", required = false) String token) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        return Mono.just("teacher");
    }
}
