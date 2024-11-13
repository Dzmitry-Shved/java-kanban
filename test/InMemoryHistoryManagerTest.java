import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    public InMemoryHistoryManager historyManager;

    public static final Task TASK = new Task("task", "task desc.", Status.NEW);
    public static final Epic EPIC = new Epic("epic", "epic desc.");
    public static final SubTask SUB_TASK = new SubTask("subtask", "subtask desc.", Status.NEW, null);

    @BeforeAll
    public static void beforeAll() {
        TASK.setId(1);
        SUB_TASK.setId(2);
        EPIC.setId(3);
        SUB_TASK.setEpic(EPIC);
        EPIC.getSubTasks().add(SUB_TASK);
    }

    @BeforeEach
    public void beforeEach() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    public void add_3TasksEachType_historySizeIs3() {
        historyManager.add(TASK);
        historyManager.add(SUB_TASK);
        historyManager.add(EPIC);

        List<Task> history = historyManager.getHistory();

        assertNotNull(history);
        assertEquals(3, history.size());
    }

    @Test
    public void add_MoreThanTenTasks_historySizeIs10() {
        for (int i = 1; i <= 10; i++ ) {
            historyManager.add(TASK);
        }
        historyManager.add(SUB_TASK);
        historyManager.add(EPIC);

        List<Task> history = historyManager.getHistory();

        assertNotNull(history);
        assertEquals(10, history.size());
    }

    @Test
    public void add_EleventhTask_secondTaskBecomesFirst() {
        historyManager.add(TASK);
        historyManager.add(SUB_TASK);
        for (int i = 2; i <= 10; i++ ) {
            historyManager.add(TASK);
        }

        List<Task> history = historyManager.getHistory();

        assertEquals(SUB_TASK, history.getFirst());
    }
}