package com.example.lab4ppois.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.concurrent.ConcurrentHashMap;

@Data
@Document(indexName = "studying-materials")
public class StudyingMaterials {
    @Id
    private String id;
    @Field(type = FieldType.Text)
    private String topic;
    @Field(type = FieldType.Nested)
    private ConcurrentHashMap<String, String> materials;
}
