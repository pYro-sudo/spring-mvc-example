package com.example.lab4ppois.controller;

import com.example.lab4ppois.entity.StudyingMaterials;
import com.example.lab4ppois.service.JwtService;
import com.example.lab4ppois.service.StudyingMaterialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/studying-materials")
public class StudyingMaterialsController extends BaseController{
    private final StudyingMaterialsService studyingMaterialsService;

    @Autowired
    public StudyingMaterialsController(StudyingMaterialsService studyingMaterialsService, JwtService jwtService, UserDetailsService userDetailsService) {
        super(jwtService, userDetailsService);
        this.studyingMaterialsService = studyingMaterialsService;
    }

    @PostMapping("/create")
    public Mono<String> createStudyingMaterials(@ModelAttribute("studying-material") StudyingMaterials studyingMaterials,
                                                @CookieValue(name = "JWT", required = false) String token,
                                                Model model) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        model.addAttribute("studying-material", studyingMaterials);
        return studyingMaterialsService.createMaterials(studyingMaterials).thenReturn("redirect:/studying-materials/dashboard");
    }

    @GetMapping("/dashboard")
    public Mono<String> dashboard(@CookieValue(name = "JWT", required = false) String token,
                                  Model model) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        String username = jwtService.extractUsername(token);
        model.addAttribute("username", username);

        return studyingMaterialsService.getAllMaterials()
                .collectList()
                .doOnNext(materials -> model.addAttribute("studying-materials", materials))
                .thenReturn("redirect:/studying-materials/dashboard");
    }

    @GetMapping("/get-by-id/{id}")
    public Mono<String> getStudyingMaterialsById(@PathVariable("id") String studyingMaterialsId,
                                                 @CookieValue(name = "JWT", required = false) String token,
                                                 Model model) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        String username = jwtService.extractUsername(token);
        model.addAttribute("username", username);

        return studyingMaterialsService.getMaterialsById(studyingMaterialsId)
                .doOnNext(materials -> model.addAttribute("studying-materials", materials))
                .thenReturn("redirect:/studying-materials/dashboard");
    }

    @PatchMapping("/update")
    public Mono<String> updateStudyingMaterials(@ModelAttribute("studying-material-id") String studyingMaterialsId,
                                                @ModelAttribute("studying-material") StudyingMaterials studyingMaterials,
                                                @CookieValue(name = "JWT", required = false) String token) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        return studyingMaterialsService.updateMaterials(studyingMaterialsId, studyingMaterials)
                .thenReturn("redirect:/studying-materials/dashboard");
    }

    @DeleteMapping("/delete/{id}")
    public Mono<String> deleteStudyingMaterials(@PathVariable("id") String studyingMaterialsId,
                                                @CookieValue(name = "JWT", required = false) String token) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        return studyingMaterialsService.deleteMaterials(studyingMaterialsId).thenReturn("redirect:/studying-materials/dashboard");
    }

    @PatchMapping("/update-topic-by-id-topic/{id}")
    public Mono<String> updateStudyingMaterialsTopic(@PathVariable("id") String studyingMaterialsId,
                                                     @ModelAttribute("topic") String topic,
                                                     @CookieValue(name = "JWT", required = false) String token) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        return studyingMaterialsService.updateTopic(studyingMaterialsId, topic).thenReturn("redirect:/studying-materials/dashboard");
    }

    @PatchMapping("/update-topic-by-id-content/{id}")
    public Mono<String> updateStudyingMaterialsContent(@PathVariable("id") String studyingMaterialsId,
                                                       @ModelAttribute("subtopic") String subtopic,
                                                       @ModelAttribute("content") String content,
                                                       @CookieValue(name = "JWT", required = false) String token) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        return studyingMaterialsService.addMaterial(studyingMaterialsId, subtopic, content).thenReturn("redirect:/studying-materials/dashboard");
    }

    @GetMapping("/get-by-subtopic-name")
    public Mono<String> getByStudyingMaterialsSubtopicName(@ModelAttribute("subtopic") String subtopic,
                                                       @CookieValue(name = "JWT", required = false) String token,
                                                       Model model) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        return studyingMaterialsService.getByNameOfSubtopicName(subtopic)
                .collectList()
                .doOnNext(materials -> model.addAttribute("studying-materials", materials))
                .thenReturn("redirect:/studying-materials/dashboard");
    }

    @GetMapping("/get-by-subtopic-content")
    public Mono<String> getByStudyingMaterialsSubtopicContent(@ModelAttribute("content") String content,
                                                              @CookieValue(name = "JWT", required = false) String token,
                                                              Model model) {
        if (!validateToken(token)) {
            return Mono.just("redirect:/login");
        }

        return studyingMaterialsService.getByNameOfSubtopicValue(content)
                .collectList()
                .doOnNext(materials -> model.addAttribute("studying-materials", materials))
                .thenReturn("redirect:/studying-materials/dashboard");
    }
}
