package com.example.lab4ppois.controller;

import com.example.lab4ppois.entity.Child;
import com.example.lab4ppois.entity.Parent;
import com.example.lab4ppois.service.JwtService;
import com.example.lab4ppois.service.ParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/parent")
public class ParentController extends BaseController {
    private final ParentService parentService;

    @Autowired
    public ParentController(ParentService parentService, JwtService jwtService, UserDetailsService userDetailsService) {
        super(jwtService, userDetailsService);
        this.parentService = parentService;
    }

    @PostMapping("/create")
    public Mono<String> createParent(@ModelAttribute("parent") Parent parent,
                                     @CookieValue(name = "JWT", required = false) String token,
                                     Model model) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        model.addAttribute("parent", parent);
        return parentService.createParent(parent).thenReturn("redirect:/parent/dashboard");
    }

    @GetMapping("/dashboard")
    public Mono<String> dashboard(@CookieValue(name = "JWT", required = false) String token,
                                  Model model) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        String username = jwtService.extractUsername(token);
        model.addAttribute("username", username);

        return parentService.getAllParents()
                .collectList()
                .doOnNext(parents -> model.addAttribute("parents", parents))
                .thenReturn("redirect:/parent/dashboard");
    }

    @PatchMapping("/update")
    public Mono<String> updateParent(@ModelAttribute("parent-id") String parentId,
                                     @ModelAttribute("parent") Parent parent,
                                     @CookieValue(name = "JWT", required = false) String token) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        return parentService.updateParent(parentId, parent)
                .thenReturn("redirect:/parent/dashboard");
    }

    @PatchMapping("/add-child")
    public Mono<String> updateParentByAddingChild(@ModelAttribute("parent-id") String parentId,
                                                  @ModelAttribute("child") Child child,
                                                  @CookieValue(name = "JWT", required = false) String token) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        return parentService.addChildToParent(parentId, child)
                .thenReturn("redirect:/parent/dashboard");
    }

    @GetMapping("/by-task")
    public Mono<String> getParentByName(@ModelAttribute("name") String name,
                                        @CookieValue(name = "JWT", required = false) String token,
                                        Model model) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        String username = jwtService.extractUsername(token);
        model.addAttribute("username", username);

        return parentService.getByParentName(name)
                .collectList()
                .doOnNext(parents -> model.addAttribute("parents", parents))
                .thenReturn("redirect:/parent/dashboard");
    }

    @DeleteMapping("/by-task")
    public Mono<String> deleteParentByName(@ModelAttribute("name") String name,
                                           @CookieValue(name = "JWT", required = false) String token) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        return parentService.deleteByParentName(name)
                .thenReturn("redirect:/parent/dashboard");
    }

    @PostMapping("/get-kids-to-school")
    public Mono<String> deleteParentByName(@ModelAttribute("parent-id") String parentId,
                                           @ModelAttribute("classroom-id") String classroomId,
                                           @CookieValue(name = "JWT", required = false) String token) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        return parentService.getKidsToSchool(parentId, classroomId)
                .thenReturn("redirect:/parent/dashboard");
    }

    @DeleteMapping("/delete/{id}")
    public Mono<String> deleteExercisingGames(@PathVariable("id") String parentId,
                                              @CookieValue(name = "JWT", required = false) String token) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        return parentService.deleteParent(parentId).thenReturn("redirect:/parent/dashboard");
    }

    @GetMapping("/dashboard")
    public Mono<String> exercisingGamesDashboard(@CookieValue(name = "JWT", required = false) String token) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        return Mono.just("redirect:/parent");
    }
}
