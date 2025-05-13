package com.example.lab4ppois.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Document(indexName = "parent")
public class Parent {
    @Id
    private String id;
    @Field(type = FieldType.Nested)
    private List<Child> kids;
    @Field(type = FieldType.Text)
    private String name;
}