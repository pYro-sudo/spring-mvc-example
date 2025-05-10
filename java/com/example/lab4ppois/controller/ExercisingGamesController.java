package com.example.lab4ppois.controller;

import com.example.lab4ppois.entity.ExercisingGames;
import com.example.lab4ppois.service.ExercisingGamesService;
import com.example.lab4ppois.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/exercising-games")
public class ExercisingGamesController extends BaseController{
    private final ExercisingGamesService exercisingGamesService;

    @Autowired
    public ExercisingGamesController(ExercisingGamesService exercisingGamesService, JwtService jwtService, UserDetailsService userDetailsService) {
        super(jwtService, userDetailsService);
        this.exercisingGamesService = exercisingGamesService;
    }

    @PostMapping("/create")
    public Mono<String> createExercisingGames(@ModelAttribute("classroom") ExercisingGames exercisingGames,
                                              @CookieValue(name = "JWT", required = false) String token,
                                              Model model) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }
        model.addAttribute("exercising-game", exercisingGames);
        return exercisingGamesService.createExercisingGames(exercisingGames).thenReturn("redirect:/exercising-games/dashboard");
    }

    @GetMapping("/dashboard")
    public Mono<String> dashboard(@CookieValue(name = "JWT", required = false) String token,
                                  Model model) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        String username = jwtService.extractUsername(token);
        model.addAttribute("username", username);

        return exercisingGamesService.getAllExercisingGames()
                .collectList()
                .doOnNext(exercisingGamesList -> model.addAttribute("exercising-games", exercisingGamesList))
                .thenReturn("redirect:/exercising-games/dashboard");
    }

    @DeleteMapping("/delete/{id}")
    public Mono<String> deleteExercisingGames(@PathVariable("id") String exercisingGamesId,
                                    @CookieValue(name = "JWT", required = false) String token) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        return exercisingGamesService.deleteByExercisingGamesId(exercisingGamesId).thenReturn("redirect:/exercising-games/dashboard");
    }

    @GetMapping("/by-task")
    public Mono<String> getExercisingGamesByTask(@ModelAttribute("task") String task,
                                                @CookieValue(name = "JWT", required = false) String token,
                                                Model model) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        String username = jwtService.extractUsername(token);
        model.addAttribute("username", username);

        return exercisingGamesService.getByExercisingGamesTask(task)
                .collectList()
                .doOnNext(exercisingGamesList -> model.addAttribute("exercising-games", exercisingGamesList))
                .thenReturn("redirect:/exercising-games/dashboard");
    }

    @DeleteMapping("/by-task")
    public Mono<String> deleteExercisingGamesByTask(@ModelAttribute("task") String task,
                                                    @CookieValue(name = "JWT", required = false) String token) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        return exercisingGamesService.deleteByExercisingGamesTask(task)
                .thenReturn("redirect:/exercising-games/dashboard");
    }

    @GetMapping("/dashboard")
    public Mono<String> exercisingGamesDashboard(@CookieValue(name = "JWT", required = false) String token) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        return Mono.just("redirect:/exercising-games");
    }
}
