package com.example.lab4ppois.controller;

import com.example.lab4ppois.entity.Teacher;
import com.example.lab4ppois.service.JwtService;
import com.example.lab4ppois.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/teacher")
public class TeacherController {
    private final TeacherService teacherService;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Autowired
    public TeacherController(TeacherService teacherService, JwtService jwtService, UserDetailsService userDetailsService) {
        this.teacherService = teacherService;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/dashboard")
    public Mono<String> dashboard(@CookieValue(name = "JWT", required = false) String token,
                            Model model) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        String username = jwtService.extractUsername(token);
        model.addAttribute("username", username);

        return teacherService.getAllTeachers()
                .collectList()
                .doOnNext(teachers -> model.addAttribute("teachers", teachers))
                .thenReturn("redirect:/teacher/dashboard");
    }

    @PostMapping("/add")
    public Mono<String> addTeacher(@ModelAttribute("teacher") Teacher teacher,
                                   @CookieValue(name = "JWT", required = false) String token) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        return teacherService.createTeacher(teacher).thenReturn("redirect:/teacher/dashboard");
    }

    @PatchMapping("/update-teacher")
    public Mono<String> updateTeacher(@ModelAttribute("teacher") Teacher teacher,
                                      @ModelAttribute("teacher-id") String teacherId,
                                      @CookieValue(name = "JWT", required = false) String token) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        return teacherService.updateTeacher(teacherId, teacher)
                .thenReturn("redirect:/teacher/dashboard");
    }

    @DeleteMapping("/delete-teacher/by-name")
    public Mono<String> deleteTeacherByName(@ModelAttribute("teacher-name") String teacherName,
                                            @CookieValue(name = "JWT", required = false) String token) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        return teacherService.deleteTeacherByName(teacherName)
                .thenReturn("redirect:/teacher/dashboard");
    }

    @DeleteMapping("/delete-teacher/by-name")
    public Mono<String> deleteTeacherById(@ModelAttribute("teacher-id") String teacherId,
                                          @CookieValue(name = "JWT", required = false) String token) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        return teacherService.deleteTeacher(teacherId)
                .thenReturn("redirect:/teacher/dashboard");
    }

    @GetMapping("/dashboard")
    public Mono<String> exercisingGamesDashboard(@CookieValue(name = "JWT", required = false) String token) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        return Mono.just("redirect:/teacher");
    }

    private boolean validateToken(String token) {
        if (token == null) return false;
        String username = jwtService.extractUsername(token);
        if (username == null) return false;
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return jwtService.validateToken(token, userDetails);
    }
}
