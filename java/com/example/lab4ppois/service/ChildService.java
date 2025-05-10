package com.example.lab4ppois.service;

import com.example.lab4ppois.entity.Child;
import com.example.lab4ppois.repository.ChildRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ChildService {
    ChildRepository childRepository;

    @Autowired
    public ChildService(ChildRepository childRepository){
        this.childRepository = childRepository;
    }

    @CachePut(value = "child", key = "#result")
    public Mono<Child> createChild(Child child) {
        return childRepository.save(child);
    }

    @Cacheable(value = "child", key = "#id")
    public Mono<Child> getChildById(String id) {
        return childRepository.findById(id);
    }

    @Cacheable(value = "child")
    public Flux<Child> getAllChildren() {
        return childRepository.findAll();
    }

    @Caching(
            put = {
                    @CachePut(value = "child", key = "#child.id")
            },
            evict = {
                    @CacheEvict(value = "classroom-child", key = "#child.id")
            }
    )
    public Mono<Child> updateChild(Child child) {
        return childRepository.save(child);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "child", key = "#id"),
                    @CacheEvict(value = "classroom-child", key = "#id")
            }
    )
    public Mono<Void> deleteChild(String id) {
        return childRepository.deleteById(id);
    }

    @CachePut(value = "child", key = "#childId")
    public Mono<Child> markChildAte(String childId, boolean ate) {
        return childRepository.findById(childId)
                .flatMap(child -> {
                    child.setAte(ate);
                    return childRepository.save(child);
                });
    }

    @CachePut(value = "child", key = "#childId")
    public Mono<Child> markChildStudied(String childId, boolean studied) {
        return childRepository.findById(childId)
                .flatMap(child -> {
                    child.setStudied(studied);
                    return childRepository.save(child);
                });
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "child"),
                    @CacheEvict(value = "classroom-child")
            }
    )
    public Mono<Long> deleteByName(String name) {
        return childRepository.deleteChildrenByName(name);
    }
}
