package com.example.lab4ppois.service;

import com.example.lab4ppois.entity.ExercisingGames;
import com.example.lab4ppois.repository.ExercisingGamesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ExercisingGamesService {
    private final ExercisingGamesRepository exercisingGamesRepository;

    @Autowired
    public ExercisingGamesService(ExercisingGamesRepository exercisingGamesRepository){
        this.exercisingGamesRepository = exercisingGamesRepository;
    }

    @Caching(
            put = {
                    @CachePut(value = "exercising-games-id", key = "#result"),
                    @CachePut(value = "exercising-games")
            }
    )
    public Mono<ExercisingGames> createExercisingGames(ExercisingGames exercisingGames) {
        return exercisingGamesRepository.save(exercisingGames);
    }

    @Cacheable(value = "exercising-games-id", key = "#id")
    public Mono<ExercisingGames> getAllExercisingGames(String id) {
        return exercisingGamesRepository.findById(id);
    }

    @Cacheable(value = "exercising-games")
    public Flux<ExercisingGames> getAllExercisingGames() {
        return exercisingGamesRepository.findAll();
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "exercising-games-id"),
                    @CacheEvict(value = "exercising-games"),
            }
    )
    public Mono<Void> deleteByExercisingGamesId(String id) {
        return exercisingGamesRepository.deleteById(id);
    }

    @Cacheable(value = "exercising-games")
    public Flux<ExercisingGames> getByExercisingGamesTask(String task) {
        return exercisingGamesRepository.findExercisingGamesByTask(task);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "exercising-games-id"),
                    @CacheEvict(value = "exercising-games"),
            }
    )
    public Mono<Long> deleteByExercisingGamesTask(String task) {
        return exercisingGamesRepository.deleteExercisingGamesByTask(task);
    }

}
