package by.losik.lab1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ChildTest {
    @Test
    public void testChild(){
        Child child = new Child();
        child.setSlept(false);
        child.setDad(new Parent());
        child.setMom(new Parent());
        child.setAte(false);
        child.setTalkedToParents(false);
        child.setExercised(false);
        child.setName("Ivan");
        child.setStudyingMaterials(new StudyingMaterials());
        Assertions.assertEquals("Ivan", child.getName());
        Assertions.assertFalse(child.isExercised());
        Assertions.assertFalse(child.isAte());
        Assertions.assertFalse(child.isTalkedToParents());
        Assertions.assertNotNull(child.getMom());
        Assertions.assertNotNull(child.getDad());
        Assertions.assertTrue(child.study());
        Assertions.assertNotNull(child.getStudyingMaterials());
        Assertions.assertTrue(child.isStudied());
    }
}
