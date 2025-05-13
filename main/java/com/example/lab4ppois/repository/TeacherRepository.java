package com.example.lab4ppois.repository;

import com.example.lab4ppois.entity.Teacher;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Mono;

public interface TeacherRepository extends ReactiveElasticsearchRepository<Teacher, String> {
    @Query("{\"query\": {\"term\": {\"name.keyword\": {\"value\": \"?0\", \"case_insensitive\": true}}}}")
    Mono<Long> deleteByNameCaseInsensitive(String name);
}
