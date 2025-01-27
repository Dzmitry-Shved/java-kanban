package managers;

import tasks.*;

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
    public void add_15TasksWithDifferentId_historySizeIs15() {
        for (int i = 1; i <= 15; i++) {
            Task task = new Task("task" + i, "task desc." + i, Status.NEW);
            task.setId(i);
            historyManager.add(task);
        }

        List<Task> history = historyManager.getHistory();
        assertNotNull(history);
        assertEquals(15, history.size());
    }

    @Test
    public void add_taskWithSameIdAgain_historySizeIs1() {
        historyManager.add(TASK);
        historyManager.add(TASK);

        List<Task> history = historyManager.getHistory();

        assertNotNull(history);
        assertEquals(1, history.size());

    }

    @Test
    public void add_5Tasks2OfThemWithSameId_historySizeIs3TasksShiftsAndLastTaskRewriteItsPreviousVersion() {
        historyManager.add(EPIC);
        historyManager.add(SUB_TASK);
        historyManager.add(TASK);

        assertEquals(EPIC, historyManager.getHistory().getFirst());
        assertEquals(SUB_TASK, historyManager.getHistory().get(1));
        assertEquals(TASK, historyManager.getHistory().getLast());

        historyManager.add(EPIC);
        historyManager.add(SUB_TASK);

        assertEquals(3, historyManager.getHistory().size());
        assertEquals(TASK, historyManager.getHistory().getFirst());
        assertEquals(EPIC, historyManager.getHistory().get(1));
        assertEquals(SUB_TASK, historyManager.getHistory().getLast());
    }

    @Test
    public void remove_Task_PreviousAndNextTasksBecomesLinked() {
        historyManager.add(TASK);
        historyManager.add(SUB_TASK);
        historyManager.add(EPIC);

        assertEquals(3, historyManager.getHistory().size());
        assertEquals(SUB_TASK, historyManager.getHistory().get(1));

        historyManager.remove(2);

        assertEquals(2, historyManager.getHistory().size());
        assertEquals(EPIC, historyManager.getHistory().get(1));
    }
}