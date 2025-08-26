package com.example.lab4ppois.service;

import com.example.lab4ppois.entity.Classroom;
import com.example.lab4ppois.entity.ExercisingGames;
import com.example.lab4ppois.entity.StudyingMaterials;
import com.example.lab4ppois.entity.Teacher;
import com.example.lab4ppois.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final ClassroomService classroomService;

    @Autowired
    public TeacherService(TeacherRepository teacherRepository, ClassroomService classroomService) {
        this.teacherRepository = teacherRepository;
        this.classroomService = classroomService;
    }

    @CachePut(value = "teacher")
    public Mono<Teacher> createTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    @Cacheable(value = "teacher-id", key = "#id")
    public Mono<Teacher> getTeacherById(String id) {
        return teacherRepository.findById(id);
    }

    @Cacheable(value = "teacher")
    public Flux<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    @CachePut(value = "teacher-id", key = "#id")
    public Mono<Teacher> updateTeacher(String id, Teacher teacher) {
        return teacherRepository.findById(id)
                .flatMap(existing -> {
                    teacher.setId(id);
                    return teacherRepository.save(teacher);
                });
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "teacher-id", key = "#id"),
                    @CacheEvict(value = "teacher")
            }
    )
    public Mono<Void> deleteTeacher(String id) {
        return teacherRepository.deleteById(id);
    }

    @CacheEvict(value = "child")
    public Mono<StudyingMaterials> distributeMaterials(String teacherId, String classroomId, StudyingMaterials materials) {
        return teacherRepository.findById(teacherId)
                .zipWith(classroomService.getClassroomById(classroomId))
                .flatMap(tuple -> {
                    Teacher teacher = tuple.getT1();
                    Classroom classroom = tuple.getT2();
                    return Mono.just(teacher.giveStudyingMaterials(classroom, materials));
                });
    }

    @CacheEvict(value = "child")
    public Mono<ExercisingGames> conductExercise(String teacherId, String classroomId, ExercisingGames games) {
        return teacherRepository.findById(teacherId)
                .zipWith(classroomService.getClassroomById(classroomId))
                .flatMap(tuple -> {
                    Teacher teacher = tuple.getT1();
                    Classroom classroom = tuple.getT2();
                    return Mono.just(teacher.exercise(classroom, games));
                });
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "teacher-id"),
                    @CacheEvict(value = "teacher")
            }
    )
    public Mono<Long> deleteTeacherByName(String name) {
        return  teacherRepository.deleteByNameCaseInsensitive(name);
    }
}