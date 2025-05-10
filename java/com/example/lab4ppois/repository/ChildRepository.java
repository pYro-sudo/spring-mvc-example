package com.example.lab4ppois.repository;

import com.example.lab4ppois.entity.Child;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Mono;


public interface ChildRepository extends ReactiveElasticsearchRepository<Child, String> {
    @Query("{\"match\": {\"name\": \"?0\"}}")
    Mono<Long> deleteChildrenByName(String name);
}
