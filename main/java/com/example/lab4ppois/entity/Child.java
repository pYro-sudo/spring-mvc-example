package com.example.lab4ppois.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "children")
public class Child {
    @Id
    private String id;
    @Field(type = FieldType.Text)
    private String name;
    @Field(type = FieldType.Boolean)
    private boolean slept;
    @Field(type = FieldType.Boolean)
    private boolean ate;
    @Field(type = FieldType.Boolean)
    private boolean talkedToParents;
    @Field(type = FieldType.Boolean)
    private boolean studied;
    @Field(type = FieldType.Boolean)
    private boolean exercised;
    @Field(type = FieldType.Nested)
    private StudyingMaterials studyingMaterials;
}