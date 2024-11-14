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

    @Test
    public void add_addTaskThenUpdateAddAgain_2TasksSameIdDifferentNames() {
        Task task = new Task("Изначальная", "Описание", Status.NEW);
        task.setId(1);
        historyManager.add(task);

        task.setName("Обновленная");
        historyManager.add(task);
        List<Task> tasks = historyManager.getHistory();
        Task task1 = tasks.get(0);
        Task task2 = tasks.get(1);

        assertEquals(2, tasks.size());
        assertEquals(task1, task2);
        assertEquals("Изначальная", task1.getName());
        assertEquals("Обновленная", task2.getName());
    }

    @Test
    public void add_addSubTaskThenUpdateAddAgain_2SubTasksSameIdDifferentNames() {
        Epic epic = new Epic("Эпик", "Описание");
        epic.setId(1);
        SubTask subTask = new SubTask("Изначальная", "Описание", Status.NEW, epic);
        subTask.setId(2);
        historyManager.add(subTask);

        subTask.setName("Обновленная");
        historyManager.add(subTask);
        List<Task> subTasks = historyManager.getHistory();
        Task subTask1 = subTasks.get(0);
        Task subTask2 = subTasks.get(1);

        assertEquals(2, subTasks.size());
        assertInstanceOf(SubTask.class, subTask1);
        assertInstanceOf(SubTask.class, subTask2);
        assertEquals(subTask1, subTask2);
        assertEquals("Изначальная", subTask1.getName());
        assertEquals("Обновленная", subTask2.getName());
    }

    @Test
    public void add_addEpicThenUpdateAddAgain_2EpicsSameIdDifferentNames() {
        Epic epic = new Epic("Изначальный", "Описание");
        epic.setId(1);
        historyManager.add(epic);

        epic.setName("Обновленный");
        historyManager.add(epic);
        List<Task> epics = historyManager.getHistory();
        Task epic1 = epics.get(0);
        Task epic2 = epics.get(1);

        assertEquals(2, epics.size());
        assertInstanceOf(Epic.class, epic1);
        assertInstanceOf(Epic.class, epic2);
        assertEquals(epic1, epic2);
        assertEquals("Изначальный", epic1.getName());
        assertEquals("Обновленный", epic2.getName());
    }
}