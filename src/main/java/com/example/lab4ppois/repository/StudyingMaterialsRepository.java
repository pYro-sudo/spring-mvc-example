package com.example.lab4ppois.repository;

import com.example.lab4ppois.entity.StudyingMaterials;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

public interface StudyingMaterialsRepository extends ReactiveElasticsearchRepository<StudyingMaterials, String> {
    @Query("{\"nested\": {\"path\": \"materials\", \"query\": {\"exists\": {\"field\": \"materials.?0\"}}}}")
    Flux<StudyingMaterials> findByMaterialKeyExists(String key);
    @Query("{\"nested\": {\"path\": \"materials\", \"query\": {\"match\": {\"materials.value\": \"?0\"}}}}")
    Flux<StudyingMaterials> findByMaterialValue(String value);
}
