package com.example.lab4ppois.repository;

import com.example.lab4ppois.entity.ExercisingGames;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ExercisingGamesRepository extends ReactiveElasticsearchRepository<ExercisingGames, String> {
    @Query("{\"match\": {\"task\": \"?0\"}}")
    Mono<Long> deleteExercisingGamesByTask(String name);

    @Query("{\"match\": {\"task\": \"?0\"}}")
    Flux<ExercisingGames> findExercisingGamesByTask(String name);
}
