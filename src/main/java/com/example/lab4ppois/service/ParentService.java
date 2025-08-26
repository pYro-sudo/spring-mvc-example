package com.example.lab4ppois.service;

import com.example.lab4ppois.entity.Child;
import com.example.lab4ppois.entity.Parent;
import com.example.lab4ppois.repository.ParentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ParentService {
    private final ParentRepository parentRepository;
    private final ClassroomService classroomService;

    @Autowired
    public ParentService(ParentRepository parentRepository, ClassroomService classroomService) {
        this.parentRepository = parentRepository;
        this.classroomService = classroomService;
    }

    @CachePut(value = "parent")
    public Mono<Parent> createParent(Parent parent) {
        return parentRepository.save(parent);
    }

    @Cacheable(value = "parent-id", key = "#id")
    public Mono<Parent> getParentById(String id) {
        return parentRepository.findById(id);
    }

    @Cacheable(value = "parent")
    public Flux<Parent> getAllParents() {
        return parentRepository.findAll();
    }

    @Caching(
            put = {
                    @CachePut(value = "parent-id", key = "#id"),
                    @CachePut(value = "parent")
            }
    )
    public Mono<Parent> updateParent(String id, Parent parent) {
        return parentRepository.findById(id)
                .flatMap(existing -> {
                    parent.setId(id);
                    return parentRepository.save(parent);
                });
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "parent-id", key = "#id"),
                    @CacheEvict(value = "parent")
            }
    )
    public Mono<Void> deleteParent(String id) {
        return parentRepository.deleteById(id);
    }

    @Caching(
            put = {
                    @CachePut(value = "parent-id", key = "#parentId"),
                    @CachePut(value = "parent")
            }
    )
    public Mono<Parent> addChildToParent(String parentId, Child child) {
        return parentRepository.findById(parentId)
                .flatMap(parent -> {
                    parent.getKids().add(child);
                    return parentRepository.save(parent);
                });
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "parent-id"),
                    @CacheEvict(value = "parent")
            }
    )
    public Mono<Long> deleteByParentName(String parentName) {
        return parentRepository.deleteByNameCaseInsensitive(parentName);
    }

    @Cacheable(value = "parent")
    public Flux<Parent> getByParentName(String name) {
        return parentRepository.getByNameCaseInsensitive(name);
    }
}
