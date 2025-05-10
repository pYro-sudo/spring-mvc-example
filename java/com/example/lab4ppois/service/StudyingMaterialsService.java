package com.example.lab4ppois.service;

import com.example.lab4ppois.entity.StudyingMaterials;
import com.example.lab4ppois.repository.StudyingMaterialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class StudyingMaterialsService {
    private final StudyingMaterialsRepository materialsRepository;

    @Autowired
    public StudyingMaterialsService(StudyingMaterialsRepository materialsRepository) {
        this.materialsRepository = materialsRepository;
    }

    @CachePut(value = "studying-materials")
    public Mono<StudyingMaterials> createMaterials(StudyingMaterials materials) {
        return materialsRepository.save(materials);
    }

    @Cacheable(value = "studying-materials-id")
    public Mono<StudyingMaterials> getMaterialsById(String id) {
        return materialsRepository.findById(id);
    }

    @Cacheable(value = "studying-materials")
    public Flux<StudyingMaterials> getAllMaterials() {
        return materialsRepository.findAll();
    }

    @Caching(
            put = {
                    @CachePut(value = "studying-materials-id", key = "#id"),
                    @CachePut(value = "studying-materials")
            }
    )
    public Mono<StudyingMaterials> updateMaterials(String id, StudyingMaterials materials) {
        return materialsRepository.findById(id)
                .flatMap(existing -> {
                    materials.setId(id);
                    return materialsRepository.save(materials);
                });
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "studying-materials-id", key = "#id"),
                    @CacheEvict(value = "studying-materials")
            }
    )
    public Mono<Void> deleteMaterials(String id) {
        return materialsRepository.deleteById(id);
    }

    @Caching(
            put = {
                    @CachePut(value = "studying-materials-id", key = "#id"),
                    @CachePut(value = "studying-materials")
            }
    )
    public Mono<StudyingMaterials> updateTopic(String id, String newTopic) {
        return materialsRepository.findById(id)
                .flatMap(materials -> {
                    materials.setTopic(newTopic);
                    return materialsRepository.save(materials);
                });
    }

    @Caching(
            put = {
                    @CachePut(value = "studying-materials-id", key = "#id"),
                    @CachePut(value = "studying-materials")
            }
    )
    public Mono<StudyingMaterials> addMaterial(String id, String key, String value) {
        return materialsRepository.findById(id)
                .flatMap(materials -> {
                    materials.getMaterials().putIfAbsent(key, value);
                    return materialsRepository.save(materials);
                });
    }

    @Cacheable(value = "studying-materials")
    public Flux<StudyingMaterials> getByNameOfSubtopicName(String subtopicName) {
        return materialsRepository.findByMaterialKeyExists(subtopicName);
    }

    @Cacheable(value = "studying-materials")
    public Flux<StudyingMaterials> getByNameOfSubtopicValue(String subtopicValue) {
        return materialsRepository.findByMaterialValue(subtopicValue);
    }
}