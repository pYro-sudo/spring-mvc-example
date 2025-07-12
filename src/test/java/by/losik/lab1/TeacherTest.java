package by.losik.lab1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TeacherTest {
    @Test
    public void testTeachers(){
        Teacher teacher = new Teacher();
        teacher.setName("Nambi");
        Assertions.assertNotNull(teacher.exercise(new Classroom(), new ExercisingGames()));
        Assertions.assertNotNull(teacher.
                giveStudyingMaterials(new Classroom(), new StudyingMaterials()));
        Assertions.assertEquals("Nambi", teacher.getName());
    }
}
