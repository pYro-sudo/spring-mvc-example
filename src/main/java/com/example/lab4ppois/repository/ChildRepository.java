package com.example.lab4ppois.repository;

import com.example.lab4ppois.entity.Child;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface ChildRepository extends ReactiveElasticsearchRepository<Child, String> {
    @Query("{\"query\": {\"match\": {\"name\": \"?0\"}}}")
    Flux<Child> findChildrenByName(String name);

    default Mono<Long> deleteChildrenByName(String name) {
        return findChildrenByName(name)
                .flatMap(this::delete)
                .count();
    }
}
