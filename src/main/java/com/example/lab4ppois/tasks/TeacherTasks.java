package com.example.lab4ppois.tasks;

import com.example.lab4ppois.entity.Classroom;
import com.example.lab4ppois.entity.ExercisingGames;
import com.example.lab4ppois.entity.StudyingMaterials;

public abstract class TeacherTasks {
    public abstract StudyingMaterials giveStudyingMaterials(Classroom classroom, StudyingMaterials materials);

    public abstract ExercisingGames exercise(Classroom classroom, ExercisingGames exercisingGames);
}