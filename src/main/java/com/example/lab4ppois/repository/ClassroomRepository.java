package com.example.lab4ppois.repository;

import com.example.lab4ppois.entity.Classroom;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClassroomRepository extends ReactiveElasticsearchRepository<Classroom, String> {
    @Query("{\"nested\": {\"path\": \"teacher\", \"query\": {\"term\": {\"teacher.name\": \"?0\"}}}}")
    Flux<Classroom> findByTeacherName(String teacherName);
    @Query("{\"nested\": {\"path\": \"children\", \"query\": {\"term\" : {\"child.name\": \"?0\"}}}}")
    Flux<Classroom> findByChildrenName(String childName);

}
