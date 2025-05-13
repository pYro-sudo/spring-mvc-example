package com.example.lab4ppois.entity;

import com.example.lab4ppois.tasks.TeacherTasks;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@EqualsAndHashCode(callSuper = false)
@Document(indexName = "teacher")
public class Teacher extends TeacherTasks {
    @Id
    private String id;
    @Field(type = FieldType.Text)
    private String name;

    @Override
    public StudyingMaterials giveStudyingMaterials(Classroom classroom, StudyingMaterials materials) {
        if (this == classroom.getTeacher()) {
            for (Child child : classroom.getChildren()) {
                child.setStudyingMaterials(materials);
            }
        }
        return materials;
    }

    @Override
    public ExercisingGames exercise(Classroom classroom, ExercisingGames exercisingGames) {
        if (this == classroom.getTeacher()) {
            for (Child child : classroom.getChildren()) {
                child.setExercised(true);
            }
        }
        return exercisingGames;
    }
}