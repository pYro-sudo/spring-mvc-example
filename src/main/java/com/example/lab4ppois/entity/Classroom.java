package com.example.lab4ppois.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(indexName = "classroom")
public class Classroom {
    @Id
    private String id;
    @Field(type = FieldType.Nested)
    private Teacher teacher;
    @Field(type = FieldType.Nested)
    private List<Child> children = new ArrayList<>();
}
