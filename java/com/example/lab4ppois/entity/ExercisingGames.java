package com.example.lab4ppois.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "exercisingGames")
public class ExercisingGames {
    @Id
    private String id;
    @Field(type = FieldType.Text)
    private String task;
}
