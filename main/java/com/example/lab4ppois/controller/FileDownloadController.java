package com.example.lab4ppois.controller;

import com.example.lab4ppois.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

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
        if (!validateToken(token)) {
            return Mono.just(ResponseEntity.badRequest().body("Not authenticated"));
        }

        return childService.getAllChildren()
                .collectList()
                .map(list -> {
                    String text = convertChildrenToTxt(Collections.singletonList(list));

                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=children_export.txt")
                            .contentType(MediaType.TEXT_PLAIN)
                            .body(text);
                });
    }

    @GetMapping("/classroom")
    public Mono<ResponseEntity<String>> exportClassrooms(@CookieValue(name = "JWT", required = false) String token) {
        if (!validateToken(token)) {
            return Mono.just(ResponseEntity.badRequest().body("Not authenticated"));
        }

        return classroomService.getAllClassrooms()
                .collectList()
                .map(list -> {
                    String text = convertChildrenToTxt(Collections.singletonList(list));

                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=classroom_export.txt")
                            .contentType(MediaType.TEXT_PLAIN)
                            .body(text);
                });
    }

    @GetMapping("/exercising-games")
    public Mono<ResponseEntity<String>> exportExercisingGames(@CookieValue(name = "JWT", required = false) String token) {
        if (!validateToken(token)) {
            return Mono.just(ResponseEntity.badRequest().body("Not authenticated"));
        }

        return exercisingGamesService.getAllExercisingGames()
                .collectList()
                .map(list -> {
                    String text = convertChildrenToTxt(Collections.singletonList(list));

                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=exercising_games_export.txt")
                            .contentType(MediaType.TEXT_PLAIN)
                            .body(text);
                });
    }

    @GetMapping("/parent")
    public Mono<ResponseEntity<String>> exportParents(@CookieValue(name = "JWT", required = false) String token) {
        if (!validateToken(token)) {
            return Mono.just(ResponseEntity.badRequest().body("Not authenticated"));
        }

        return parentService.getAllParents()
                .collectList()
                .map(list -> {
                    String text = convertChildrenToTxt(Collections.singletonList(list));

                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=parent_export.txt")
                            .contentType(MediaType.TEXT_PLAIN)
                            .body(text);
                });
    }

    @GetMapping("/studying-materials")
    public Mono<ResponseEntity<String>> exportStudyingMaterials(@CookieValue(name = "JWT", required = false) String token) {
        if (!validateToken(token)) {
            return Mono.just(ResponseEntity.badRequest().body("Not authenticated"));
        }

        return studyingMaterialsService.getAllMaterials()
                .collectList()
                .map(list -> {
                    String text = convertChildrenToTxt(Collections.singletonList(list));

                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=studying_materials_export.txt")
                            .contentType(MediaType.TEXT_PLAIN)
                            .body(text);
                });
    }

    @GetMapping("/teacher")
    public Mono<ResponseEntity<String>> exportTeacher(@CookieValue(name = "JWT", required = false) String token) {
        if (!validateToken(token)) {
            return Mono.just(ResponseEntity.badRequest().body("Not authenticated"));
        }

        return teacherService.getAllTeachers()
                .collectList()
                .map(list -> {
                    String text = convertChildrenToTxt(Collections.singletonList(list));

                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=teacher_export.txt")
                            .contentType(MediaType.TEXT_PLAIN)
                            .body(text);
                });
    }

    private String convertChildrenToTxt(List<Object> result) {
        StringBuilder stringBuilder = new StringBuilder();
        result.forEach(child -> stringBuilder.append(child).append("\n"));

        return stringBuilder.toString();
    }
}
