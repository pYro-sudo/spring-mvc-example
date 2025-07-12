package by.losik.lab1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GamesTest {
    @Test
    public void testGames(){
        ExercisingGames exercisingGames = new ExercisingGames();
        exercisingGames.setTask("Mop the floor");
        Assertions.assertEquals("Mop the floor", exercisingGames.getTask());
    }
}
