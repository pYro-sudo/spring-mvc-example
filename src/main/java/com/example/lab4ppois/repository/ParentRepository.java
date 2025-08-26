package com.example.lab4ppois.repository;

import com.example.lab4ppois.entity.Parent;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ParentRepository extends ReactiveElasticsearchRepository<Parent, String> {
    default Mono<Long> deleteByNameCaseInsensitive(String name) {
        return getByNameCaseInsensitive(name)
                .flatMap(this::delete)
                .count();
    }

    @Query("{\"query\": {\"term\": {\"name.keyword\": {\"value\": \"?0\", \"case_insensitive\": true}}}}")
    Flux<Parent> getByNameCaseInsensitive(String name);
}
