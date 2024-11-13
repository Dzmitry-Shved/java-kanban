import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    public static final Task TASK_1 = new Task("task1", "task1 description", Status.NEW);
    public static final Task TASK_2 = new Task("task1", "task1 description", Status.NEW);
    public static final Task TASK_3 = new Task("task2", "task2 description", Status.NEW);


    @BeforeAll
    public static void beforeAll() {
        TASK_1.setId(1);
        TASK_2.setId(1);
        TASK_3.setId(2);
    }

    @Test
    public void equals_twoTasksWithSameId_true() {
        assertEquals(TASK_1, TASK_2);
    }

    @Test
    public void equals_twoTasksWithDifferentId_false() {
        assertNotEquals(TASK_1, TASK_3);
    }
}