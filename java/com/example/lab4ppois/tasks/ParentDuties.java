package com.example.lab4ppois.tasks;

import com.example.lab4ppois.entity.Child;
import com.example.lab4ppois.entity.Classroom;

import java.util.List;

public abstract class ParentDuties {
    public abstract boolean feedChildren(Child child);
    public abstract boolean getChildrenToSleep(Child child);
    public abstract boolean talkToKids(Child child);
    public abstract boolean playWithKids(Child child);
    public abstract List<Child> getKidsToSchool(Classroom classroom);
    public abstract List<Child> bringKidsBack(Classroom classroom);
}