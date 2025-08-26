package com.example.lab4ppois.repository;

import com.example.lab4ppois.entity.ExercisingGames;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ExercisingGamesRepository extends ReactiveElasticsearchRepository<ExercisingGames, String> {
    default Mono<Long> deleteExercisingGamesByTask(String name) {
        return findExercisingGamesByTask(name)
                .flatMap(this::delete)
                .count();
    }
    @Query("{\"match\": {\"task\": \"?0\"}}")
    Flux<ExercisingGames> findExercisingGamesByTask(String name);
}
