package com.example.lab4ppois.service;

import com.example.lab4ppois.entity.Child;
import com.example.lab4ppois.entity.Classroom;
import com.example.lab4ppois.repository.ClassroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ClassroomService {
    private final ClassroomRepository classroomRepository;

    @Autowired
    public ClassroomService(ClassroomRepository classroomRepository) {
        this.classroomRepository = classroomRepository;
    }

    @Caching(
            put = {
                    @CachePut(value = "classroom", key = "#result"),
                    @CachePut(value = "classroom-teacher", key = "#result")
            }
    )
    public Mono<Classroom> createClassroom(Classroom classroom) {
        return classroomRepository.save(classroom);
    }

    @Cacheable(value = "classroom", key = "#id")
    public Mono<Classroom> getClassroomById(String id) {
        return classroomRepository.findById(id);
    }

    @Cacheable(value = "classroom-teacher", key = "#teacherName")
    public Flux<Classroom> getClassroomsByTeacherName(String teacherName) {
        return classroomRepository.findByTeacherName(teacherName);
    }

    @Cacheable(value = "classroom-child", key = "#childName")
    public Flux<Classroom> getClassroomsByChildName(String childName) {
        return classroomRepository.findByChildrenName(childName);
    }

    @Cacheable(value = "classroom")
    public Flux<Classroom> getAllClassrooms() {
        return classroomRepository.findAll();
    }

    @Caching(
            put = {
                    @CachePut(value = "classroom", key = "#classroom.id"),
                    @CachePut(value = "classroom-teacher", key = "#classroom.teacher.name")
            },
            evict = {
                    @CacheEvict(value = "classroom-child", allEntries = true)
            }
    )
    public Mono<Classroom> updateClassroom(Classroom classroom) {
        return classroomRepository.save(classroom);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "classroom", key = "#id"),
                    @CacheEvict(value = "classroom-teacher", allEntries = true),
                    @CacheEvict(value = "classroom-child", allEntries = true)
            }
    )
    public Mono<Void> deleteClassroom(String id) {
        return classroomRepository.deleteById(id);
    }

    @Caching(
            put = {
                    @CachePut(value = "classroom", key = "#classroomId"),
                    @CachePut(value = "child", key = "#child.id"),
                    @CachePut(value = "classroom-child", key = "#child.id")
            }
    )
    public Mono<Classroom> addChildToClassroom(String classroomId, Child child) {
        return classroomRepository.findById(classroomId)
                .flatMap(classroom -> {
                    classroom.getChildren().add(child);
                    return classroomRepository.save(classroom);
                });
    }

    @Caching(
            put = {
                    @CachePut(value = "classroom", key = "#classroomId")
            },
            evict = {
                    @CacheEvict(value = "child"),
                    @CacheEvict(value = "classroom-child", key = "#childName")
            }
    )
    public Mono<Classroom> removeChildFromClassroom(String classroomId, String childName) {
        return classroomRepository.findById(classroomId)
                .flatMap(classroom -> {
                    classroom.getChildren().removeIf(child -> child.getName().equals(childName));
                    return classroomRepository.save(classroom);
                });
    }
}