package com.example.lab4ppois.controller;

import com.example.lab4ppois.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/download")
public class FileDownloadController extends BaseController {
    private final ChildService childService;
    private final ClassroomService classroomService;
    private final ExercisingGamesService exercisingGamesService;
    private final ParentService parentService;
    private final StudyingMaterialsService studyingMaterialsService;
    private final TeacherService teacherService;

    @Autowired
    public FileDownloadController(
            ChildService childService,
            ClassroomService classroomService,
            ExercisingGamesService exercisingGamesService,
            ParentService parentService,
            StudyingMaterialsService studyingMaterialsService,
            TeacherService teacherService,
            JwtService jwtService,
            UserDetailsService userDetailsService) {
        super(jwtService, userDetailsService);
        this.childService = childService;
        this.classroomService = classroomService;
        this.exercisingGamesService = exercisingGamesService;
        this.parentService = parentService;
        this.studyingMaterialsService = studyingMaterialsService;
        this.teacherService = teacherService;
    }

    @GetMapping("/child")
    public Mono<ResponseEntity<String>> exportChildren(@CookieValue(name = "JWT", required = false) String token) {
        return exportData(
                childService.getAllChildren(),
                "children_export.txt",
                token
        );
    }

    @GetMapping("/classroom")
    public Mono<ResponseEntity<String>> exportClassrooms(@CookieValue(name = "JWT", required = false) String token) {
        return exportData(
                classroomService.getAllClassrooms(),
                "classroom_export.txt",
                token
        );
    }

    @GetMapping("/exercising-games")
    public Mono<ResponseEntity<String>> exportExercisingGames(@CookieValue(name = "JWT", required = false) String token) {
        return exportData(
                exercisingGamesService.getAllExercisingGames(),
                "exercising_games_export.txt",
                token
        );
    }

    @GetMapping("/parent")
    public Mono<ResponseEntity<String>> exportParents(@CookieValue(name = "JWT", required = false) String token) {
        return exportData(
                parentService.getAllParents(),
                "parent_export.txt",
                token
        );
    }

    @GetMapping("/studying-materials")
    public Mono<ResponseEntity<String>> exportStudyingMaterials(@CookieValue(name = "JWT", required = false) String token) {
        return exportData(
                studyingMaterialsService.getAllMaterials(),
                "studying_materials_export.txt",
                token
        );
    }

    @GetMapping("/teacher")
    public Mono<ResponseEntity<String>> exportTeachers(@CookieValue(name = "JWT", required = false) String token) {
        return exportData(
                teacherService.getAllTeachers(),
                "teacher_export.txt",
                token
        );
    }

    private <T> Mono<ResponseEntity<String>> exportData(
            Flux<T> dataFlux,
            String filename,
            String token
    ) {
        log.info("Starting export to {}", filename);

        if (!validateToken(token)) {
            return Mono.just(ResponseEntity.badRequest().body("Not authenticated"));
        }

        return dataFlux
                .collectList()
                .map(this::convertToTxt)
                .map(text -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                        .contentType(MediaType.TEXT_PLAIN)
                        .body(text))
                .onErrorResume(e -> {
                    log.error("Export failed for {}", filename, e);
                    return Mono.just(ResponseEntity
                            .internalServerError()
                            .body("Failed to export: " + e.getMessage()));
                });
    }

    private <T> String convertToTxt(List<T> data) {
        return data.stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n"));
    }
}