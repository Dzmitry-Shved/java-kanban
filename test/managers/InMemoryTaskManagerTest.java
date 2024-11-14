package managers;

import tasks.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    public InMemoryTaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = new InMemoryTaskManager();

    }

    //Тесты работы с tasks.Task
    @Test
    public void add_newTask_returnedTaskByIdNotNull() {
        Task task = new Task("task", "task description");
        taskManager.addTask(task);
        int generatedId = 1;
        Task addedTask = taskManager.getTask(generatedId);

        assertNotNull(addedTask);
    }

    @Test
    public void add_newTask_returnedTaskFieldsShouldBeCorrect() {
        Task task = new Task("task", "task description", Status.NEW);
        taskManager.addTask(task);

        String name = "task";
        String description = "task description";
        Status status = Status.NEW;
        Task addedTask = taskManager.getTask(1);

        assertEquals(name, addedTask.getName());
        assertEquals(description, addedTask.getDescription());
        assertEquals(status, addedTask.getStatus());
    }

    @Test
    public void getAllTasks_add2Tasks_returnedListSizeShouldBe2() {
        Task task1 = new Task("task", "task description");
        Task task2 = new Task("task", "task description");
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        List<Task> tasks = taskManager.getAllTasks();

        assertEquals(2, tasks.size());
    }

    @Test
    public void deleteAllTasks_add2tasksThenDelete_returnedListSizeShouldBe0() {
        Task task1 = new Task("task", "task description");
        Task task2 = new Task("task", "task description");
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        List<Task> tasksAfterAdd = taskManager.getAllTasks();

        assertEquals(2, tasksAfterAdd.size());

        taskManager.deleteAllTasks();
        List<Task> tasksAfterDelete = taskManager.getAllTasks();

        assertEquals(0, tasksAfterDelete.size());
    }

    @Test
    public void deleteTask_addTaskThenDelete_returnedTaskIsNull() {
        Task task1 = new Task("task", "task description");
        taskManager.addTask(task1);
        int generatedId = 1;
        Task addedTask = taskManager.getTask(generatedId);

        assertNotNull(addedTask);

        taskManager.deleteTask(generatedId);
        Task deletedTask = taskManager.getTask(generatedId);

        assertNull(deletedTask);
    }

    @Test
    public void updateTask_addTaskThenUpdateStatus_statusShouldBeDONE() {
        Task task = new Task("Задача1", "Описание задачи1", Status.NEW);
        taskManager.addTask(task);

        Task taskForUpdate = new Task("Обновленная задача1", "Обновленное описание задачи1");
        taskForUpdate.setStatus(Status.DONE);
        taskForUpdate.setId(1);
        taskManager.updateTask(taskForUpdate);

        Task updatedTask = taskManager.getTask(1);

        assertEquals(Status.DONE, updatedTask.getStatus());
    }

    //Тесты работы с tasks.SubTask
    @Test
    public void add_newSubTask_returnedSubTaskByIdNotNull() {
        Epic epic = new Epic("Эпик1", "Описание эпика1");
        taskManager.addEpic(epic);
        SubTask subTask = new SubTask("Субтаска1", "Описание субтаски1", Status.NEW, epic);
        taskManager.addSubTask(subTask);

        int generatedId = 2;
        SubTask addedSubTask = taskManager.getSubTask(generatedId);

        assertNotNull(addedSubTask);
    }

    @Test
    public void getAllSubTasks_add2SubTasks_returnedListSizeShouldBe2() {
        Epic epic = new Epic("Эпик1", "Описание эпика1");
        taskManager.addEpic(epic);
        SubTask subTask1 = new SubTask("Субтаска1", "Описание субтаски1", Status.NEW, epic);
        taskManager.addSubTask(subTask1);
        SubTask subTask2 = new SubTask("Субтаска1", "Описание субтаски1", Status.NEW, epic);
        taskManager.addSubTask(subTask2);

        List<SubTask> subTasks = taskManager.getAllSubTasks();

        assertEquals(2, subTasks.size());
    }

    @Test
    public void getAllSubTasks_addEpicWith2SubTasksAndGetAllSubtasksByEpic_returnedListSizeShouldBe2() {
        Epic epic = new Epic("Эпик1", "Описание эпика1");
        SubTask subTask1 = new SubTask("Субтаска1", "Описание субтаски1", Status.NEW, epic);
        SubTask subTask2 = new SubTask("Субтаска1", "Описание субтаски1", Status.NEW, epic);
        epic.getSubTasks().add(subTask1);
        epic.getSubTasks().add(subTask2);
        taskManager.addEpic(epic);

        int epicId = 1;
        Epic existingEpic = new Epic("Эпик1", "Описание эпика1");
        existingEpic.setId(epicId);

        List<SubTask> subTasks = taskManager.getAllSubTasks(existingEpic);

        assertEquals(2, subTasks.size());
    }

    @Test
    public void deleteAllSubTasks_add2SubTasksThenDelete_returnedListSizeShouldBe0() {
        Epic epic = new Epic("Эпик1", "Описание эпика1");
        taskManager.addEpic(epic);
        SubTask subTask1 = new SubTask("Субтаска1", "Описание субтаски1", Status.NEW, epic);
        taskManager.addSubTask(subTask1);
        SubTask subTask2 = new SubTask("Субтаска1", "Описание субтаски1", Status.NEW, epic);
        taskManager.addSubTask(subTask2);

        List<SubTask> subTasksAfterAdd = taskManager.getAllSubTasks();

        assertEquals(2, subTasksAfterAdd.size());

        taskManager.deleteAllSubTasks();
        List<SubTask> subTasksAfterDelete = taskManager.getAllSubTasks();

        assertEquals(0, subTasksAfterDelete.size());
    }

    @Test
    public void deleteSubTask_addSubTaskThenDelete_returnedTSubTaskIsNull() {
        Epic epic = new Epic("Эпик1", "Описание эпика1");
        taskManager.addEpic(epic);
        SubTask subTask1 = new SubTask("Субтаска1", "Описание субтаски1", Status.NEW, epic);
        taskManager.addSubTask(subTask1);
        int generatedId = 2;
        SubTask addedSubTask = taskManager.getSubTask(generatedId);

        assertNotNull(addedSubTask);

        taskManager.deleteSubTask(generatedId);
        SubTask deletedSubTask = taskManager.getSubTask(generatedId);

        assertNull(deletedSubTask);
    }

    @Test
    public void updateSubTask_addSubTaskThenUpdateStatus_statusShouldBeDONE() {
        Epic epic = new Epic("Эпик1", "Описание эпика1");
        taskManager.addEpic(epic);
        SubTask subTask1 = new SubTask("Субтаска1", "Описание субтаски1", Status.NEW, epic);
        taskManager.addSubTask(subTask1);

        SubTask subTaskForUpdate = new SubTask("Обнов. субтаска1", "Обнов. оп. субтаски1",
                Status.DONE, epic);
        subTaskForUpdate.setId(2);

        taskManager.updateSubTask(subTaskForUpdate);

        SubTask updatedSubTask = taskManager.getSubTask(2);

        assertEquals(Status.DONE, updatedSubTask.getStatus());
    }

    //Тесты работы с tasks.Epic
    @Test
    public void add_newEpic_returnedEpicByIdNotNull() {
        Epic epic = new Epic("Эпик1", "Описание эпика1");
        taskManager.addEpic(epic);

        int generatedId = 1;
        Epic addedEpic = taskManager.getEpic(generatedId);

        assertNotNull(addedEpic);
    }

    @Test
    public void getAllEpics_add2Epics_returnedListSizeShouldBe2() {
        Epic epic1 = new Epic("Эпик1", "Описание эпика1");
        taskManager.addEpic(epic1);
        Epic epic2 = new Epic("Эпик2", "Описание эпика2");
        taskManager.addEpic(epic2);

        List<Epic> epics = taskManager.getAllEpics();

        assertEquals(2, epics.size());
    }

    @Test
    public void deleteAllEpics_add2EpicsThenDelete_returnedListSizeShouldBe0() {
        Epic epic1 = new Epic("Эпик1", "Описание эпика1");
        taskManager.addEpic(epic1);
        Epic epic2 = new Epic("Эпик2", "Описание эпика2");
        taskManager.addEpic(epic2);

        List<Epic> epicsAfterAdd = taskManager.getAllEpics();

        assertEquals(2, epicsAfterAdd.size());

        taskManager.deleteAllEpics();
        List<Epic> epicsAfterDelete = taskManager.getAllEpics();

        assertEquals(0, epicsAfterDelete.size());
    }

    @Test
    public void deleteEpic_addEpicThenDelete_returnedEpicIsNull() {
        Epic epic1 = new Epic("Эпик1", "Описание эпика1");
        taskManager.addEpic(epic1);
        int generatedId = 1;
        Epic addedEpic = taskManager.getEpic(generatedId);

        assertNotNull(addedEpic);

        taskManager.deleteEpic(generatedId);
        Epic deletedEpic = taskManager.getEpic(generatedId);

        assertNull(deletedEpic);
    }

    @Test
    public void updateEpic_addEpicThenUpdateName_NameShouldBeUpdated() {
        Epic epic1 = new Epic("Эпик1", "Описание эпика1");
        taskManager.addEpic(epic1);

        Epic EpicForUpdate = new Epic("Обновленный Эпик1", "Описание эпика1");
        EpicForUpdate.setId(1);
        taskManager.updateEpic(EpicForUpdate);

        Epic updatedEpic = taskManager.getEpic(1);
        String updatedName = "Обновленный Эпик1";

        assertEquals(updatedName, updatedEpic.getName());
    }

    //Прочие тесты
    @Test
    public void calculateEpicStatus_addEpicWithSubtaskThenChangeSubtaskStatus_epicStatusShouldBeDONE() {
        Epic epic = new Epic("Эпик1", "Описание эпика1");
        SubTask subTask1 = new SubTask("Субтаска1", "Описание субтаски1", Status.NEW, epic);
        epic.getSubTasks().add(subTask1);
        taskManager.addEpic(epic);

        Epic addedEpic = taskManager.getEpic(1);

        assertEquals(Status.NEW, addedEpic.getStatus());

        SubTask subTaskForUpdate = new SubTask("Субтаска1", "Описание субтаски1", Status.DONE, epic);
        subTaskForUpdate.setId(2);
        taskManager.updateSubTask(subTaskForUpdate);

        Epic epicAfterSubtaskUpdate = taskManager.getEpic(1);

        assertEquals(Status.DONE, epicAfterSubtaskUpdate.getStatus());
    }

    //Методы работы с историей просмотров
    @Test
    public void getHistory_addAndGet3TasksAndGetHistory_returnedListSizeShouldBe3() {
        Task task1 = new Task("task1", "task1 description");
        Task task2 = new Task("task2", "task2 description");
        Task task3 = new Task("task3", "task3 description");
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);

        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getTask(3);

        List<Task> history = taskManager.getHistory();

        assertEquals(3, history.size());
    }
}