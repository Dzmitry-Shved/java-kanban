package tasks;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {

    public static final SubTask SUB_TASK_1 = new SubTask("subTask1", "subTask1 desc.", Status.NEW, null);
    public static final SubTask SUB_TASK_2 = new SubTask("subTask1", "subTask1 desc.", Status.NEW, null);
    public static final SubTask SUB_TASK_3 = new SubTask("subTask2", "subTask2 desc.", Status.NEW, null);


    @BeforeAll
    public static void beforeAll() {
        SUB_TASK_1.setId(1);
        SUB_TASK_2.setId(1);
        SUB_TASK_3.setId(2);
    }

    @Test
    public void equals_twoSubTasksWithSameId_true() {
        assertEquals(SUB_TASK_1, SUB_TASK_2);
    }

    @Test
    public void equals_twoSubTasksWithDifferentId_false() {
        assertNotEquals(SUB_TASK_1, SUB_TASK_3);
    }
}