package com.example.lab4ppois.controller;

import com.example.lab4ppois.entity.Child;
import com.example.lab4ppois.service.ChildService;
import com.example.lab4ppois.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/child")
public class ChildController extends BaseController {
    private final ChildService childService;

    @Autowired
    public ChildController(JwtService jwtService,
                           UserDetailsService userDetailsService,
                           ChildService childService) {
        super(jwtService, userDetailsService);
        this.childService = childService;
    }

    @PostMapping("/create")
    public Mono<String> createChild(@ModelAttribute("child") Child child,
                                    @CookieValue(name = "JWT", required = false) String token,
                                    Model model) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }
        model.addAttribute("child", child);
        return childService.createChild(child).thenReturn("redirect:/child/dashboard");
    }

    @GetMapping("/dashboard")
    public Mono<String> dashboard(@CookieValue(name = "JWT", required = false) String token,
                                  Model model) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        String username = jwtService.extractUsername(token);
        model.addAttribute("username", username);

        return childService.getAllChildren()
                .collectList()
                .doOnNext(children -> model.addAttribute("children", children))
                .thenReturn("redirect:/child/dashboard");
    }

    @GetMapping("/by-id/{id}")
    public Mono<String> dashboard(@PathVariable("id") String childId,
                                  @CookieValue(name = "JWT", required = false) String token,
                                  Model model) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        String username = jwtService.extractUsername(token);
        model.addAttribute("username", username);

        return childService.getChildById(childId)
                .doOnNext(child -> model.addAttribute("children", child))
                .thenReturn("redirect:/child/dashboard");
    }

    @PatchMapping("/update")
    public Mono<String> updateChild(@ModelAttribute("child") Child child,
                                    @CookieValue(name = "JWT", required = false) String token) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        return childService.updateChild(child).thenReturn("redirect:/child/dashboard");
    }

    @DeleteMapping("/delete/{id}")
    public Mono<String> deleteChild(@PathVariable("id") String childId,
                                    @CookieValue(name = "JWT", required = false) String token) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        return childService.deleteChild(childId).thenReturn("redirect:/child/dashboard");
    }


    @PatchMapping("/mark-ate")
    public Mono<String> markChildAte(@ModelAttribute("id") String childId,
                                     @ModelAttribute("ate") boolean ate,
                                     @CookieValue(name = "JWT", required = false) String token) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        return childService.markChildAte(childId, ate).thenReturn("redirect:/child/dashboard");
    }

    @PatchMapping("/mark-studied")
    public Mono<String> markChildStudied(@ModelAttribute("id") String childId,
                                         @ModelAttribute("studied") boolean studied,
                                         @CookieValue(name = "JWT", required = false) String token) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        return childService.markChildStudied(childId, studied).thenReturn("redirect:/child/dashboard");
    }

    @DeleteMapping("/delete")
    public Mono<String> deleteByName(@ModelAttribute("name") String name,
                                     @CookieValue(name = "JWT", required = false) String token) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        return childService.deleteByName(name).thenReturn("redirect:/child/dashboard");
    }

    @GetMapping("/dashboard")
    public Mono<String> childDashboard(@CookieValue(name = "JWT", required = false) String token) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }
        return Mono.just("redirect:/child");
    }
}