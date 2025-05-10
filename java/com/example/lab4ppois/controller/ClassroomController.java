package com.example.lab4ppois.controller;

import com.example.lab4ppois.entity.Child;
import com.example.lab4ppois.entity.Classroom;
import com.example.lab4ppois.service.ClassroomService;
import com.example.lab4ppois.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/classroom")
public class ClassroomController extends BaseController {
    private final ClassroomService classroomService;

    @Autowired
    public ClassroomController(ClassroomService classroomService, JwtService jwtService, UserDetailsService userDetailsService) {
        super(jwtService, userDetailsService);
        this.classroomService = classroomService;
    }

    @PostMapping("/create")
    public Mono<String> createChild(@ModelAttribute("classroom") Classroom classroom,
                                    @CookieValue(name = "JWT", required = false) String token,
                                    Model model) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }
        model.addAttribute("child", classroom);
        return classroomService.createClassroom(classroom).thenReturn("redirect:/classroom/dashboard");
    }

    @GetMapping("/dashboard")
    public Mono<String> dashboard(@CookieValue(name = "JWT", required = false) String token,
                                  Model model) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        String username = jwtService.extractUsername(token);
        model.addAttribute("username", username);

        return classroomService.getAllClassrooms()
                .collectList()
                .doOnNext(classrooms -> model.addAttribute("classrooms", classrooms))
                .thenReturn("redirect:/classroom/dashboard");
    }


    @GetMapping("/by-teacher-name")
    public Mono<String> getClassroomByTeacherName(@ModelAttribute("teacher-name") String name,
                                                  @CookieValue(name = "JWT", required = false) String token,
                                                  Model model) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        String username = jwtService.extractUsername(token);
        model.addAttribute("username", username);

        return classroomService.getClassroomsByTeacherName(name)
                .collectList()
                .doOnNext(classrooms -> model.addAttribute("classrooms", classrooms))
                .thenReturn("redirect:/classroom/dashboard");
    }

    @GetMapping("/by-child-name")
    public Mono<String> getClassroomByChildName(@ModelAttribute("child-name") String name,
                                                  @CookieValue(name = "JWT", required = false) String token,
                                                  Model model) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        String username = jwtService.extractUsername(token);
        model.addAttribute("username", username);

        return classroomService.getClassroomsByChildName(name)
                .collectList()
                .doOnNext(classrooms -> model.addAttribute("classrooms", classrooms))
                .thenReturn("redirect:/classroom/dashboard");
    }

    @PatchMapping("/update")
    public Mono<String> updateClassroom(@ModelAttribute("classroom") Classroom classroom,
                                        @CookieValue(name = "JWT", required = false) String token) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        return classroomService.updateClassroom(classroom).thenReturn("redirect:/classroom/dashboard");
    }

    @DeleteMapping("/delete/{id}")
    public Mono<String> deleteChild(@PathVariable("id") String classroomId,
                                    @CookieValue(name = "JWT", required = false) String token) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        return classroomService.deleteClassroom(classroomId).thenReturn("redirect:/classroom/dashboard");
    }

    @PatchMapping("/add-child-to-classroom")
    public Mono<String> addChildToClassroom(@ModelAttribute("classroom-id") String classroomId,
                                            @ModelAttribute("child") Child child,
                                            @CookieValue(name = "JWT", required = false) String token) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        return classroomService.addChildToClassroom(classroomId, child).thenReturn("redirect:/classroom/dashboard");
    }

    @PatchMapping("/remove-child-from-classroom")
    public Mono<String> removeChildFromClassroom(@ModelAttribute("classroom-id") String classroomId,
                                                 @ModelAttribute("child-name") String name,
                                                 @CookieValue(name = "JWT", required = false) String token) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        return classroomService.removeChildFromClassroom(classroomId, name).thenReturn("redirect:/classroom/dashboard");
    }

    @GetMapping("/dashboard")
    public Mono<String> classroomDashboard(@CookieValue(name = "JWT", required = false) String token) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        return Mono.just("redirect:/classroom");
    }
}
