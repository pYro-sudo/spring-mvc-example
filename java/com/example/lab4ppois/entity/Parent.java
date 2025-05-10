package com.example.lab4ppois.entity;

import com.example.lab4ppois.tasks.ParentDuties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Filter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
@EqualsAndHashCode(callSuper = false)
public class Parent extends ParentDuties {
    @Id
    private String id;
    @Field(type = FieldType.Nested)
    private List<Child> kids;
    @Field(type = FieldType.Text)
    private String name;

    @Override
    public boolean feedChildren(Child child) {
        child.setAte(true);
        return child.isAte();
    }

    @Override
    public boolean getChildrenToSleep(Child child) {
        child.setSlept(true);
        return child.isSlept();
    }

    @Override
    public boolean talkToKids(Child child) {
        child.setTalkedToParents(this == child.getDad() || this == child.getMom());
        return child.isTalkedToParents();
    }

    @Override
    public boolean playWithKids(Child child) {
        child.setExercised(this == child.getDad() || this == child.getMom());
        return child.isExercised();
    }

    @Override
    public List<Child> getKidsToSchool(Classroom classroom) {
        for (Child child : kids) {
            if (!classroom.getChildren().contains(child)) {
                classroom.getChildren().add(child);
            }
        }
        return this.kids;
    }

    @Override
    public List<Child> bringKidsBack(Classroom classroom) {
        classroom.getChildren().removeIf(child -> this == child.getMom() || this == child.getDad());
        return this.kids;
    }
}