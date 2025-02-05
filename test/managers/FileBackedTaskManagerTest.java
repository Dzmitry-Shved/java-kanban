package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

//Под пустым файлом будет пониматься одна строка с шапкой таблицы без данных
class FileBackedTaskManagerTest {

    Path taskManagerPath;
    FileBackedTaskManager taskManager;


    @BeforeEach
    public void beforeEach() throws IOException {
        taskManagerPath = Files.createTempFile("TaskManager", "test");
        taskManager = new FileBackedTaskManager(taskManagerPath);
    }

    @Test
    public void saveEmptyFile() throws IOException {
        Task task = new Task("task", "task desc", Status.NEW);
        taskManager.addTask(task);

        List<String> fileLines = Files.readAllLines(taskManagerPath);
        String expected = "1,TASK,task,NEW,task desc";
        assertEquals(expected, fileLines.get(1));
        taskManager.deleteTask(1);
        List<String> fileLinesAfterDelete = Files.readAllLines(taskManagerPath);

        assertTrue(Files.exists(taskManagerPath));
        assertEquals(1, fileLinesAfterDelete.size());
        String expectedAfterDelete = "id,type,name,status,description,epic";
        assertEquals(expectedAfterDelete, fileLinesAfterDelete.getFirst());
    }

    @Test
    public void add_oneTask_fileExistsAndInMemoryMapFilled() {
        Task task = new Task("task", "task desc", Status.NEW);
        taskManager.addTask(task);

        assertTrue(Files.exists(taskManagerPath));
        assertEquals(1, taskManager.getAllTasks().size());
    }

    @Test
    public void add_oneTask_secondLineIsAddedTask() throws IOException {
        Task task = new Task("task", "task desc", Status.NEW);
        taskManager.addTask(task);

        List<String> fileLines = Files.readAllLines(taskManagerPath);

        String expected = "1,TASK,task,NEW,task desc";
        assertEquals(expected, fileLines.get(1));
    }

    @Test
    public void delete_firstOfTwoTasks_onlySecondTaskPresentInFile() throws IOException {
        Task task1 = new Task("task1", "task desc1", Status.NEW);
        taskManager.addTask(task1);
        Task task2 = new Task("task2", "task desc2", Status.NEW);
        taskManager.addTask(task2);
        assertEquals(3, Files.readAllLines(taskManagerPath).size());

        taskManager.deleteTask(1);

        assertEquals(2, Files.readAllLines(taskManagerPath).size());
        String expected = "2,TASK,task2,NEW,task desc2";
        assertEquals(expected, Files.readAllLines(taskManagerPath).get(1));
    }

    @Test
    public void deleteAllTasks_add3TasksThenDeleteAll_fileIsEmpty() throws IOException {
        Task task1 = new Task("task1", "task desc1", Status.NEW);
        taskManager.addTask(task1);
        Task task2 = new Task("task2", "task desc2", Status.NEW);
        taskManager.addTask(task2);
        Task task3 = new Task("task3", "task desc3", Status.NEW);
        taskManager.addTask(task3);
        assertEquals(4, Files.readAllLines(taskManagerPath).size());

        taskManager.deleteAllTasks();

        assertEquals(1, Files.readAllLines(taskManagerPath).size());
    }

    @Test
    public void updateTask_addTaskThenUpdateStatus_taskWithUpdatedStatusInFile() throws IOException {
        Task task1 = new Task("task1", "task desc1", Status.NEW);
        taskManager.addTask(task1);
        String expectedBeforeUpdate = "1,TASK,task1,NEW,task desc1";
        assertEquals(expectedBeforeUpdate, Files.readAllLines(taskManagerPath).get(1));

        Task updatedTask = new Task("task1", "task desc1", Status.DONE);
        updatedTask.setId(1);
        taskManager.updateTask(updatedTask);

        String expectedAfterUpdate = "1,TASK,task1,DONE,task desc1";
        assertEquals(expectedAfterUpdate, Files.readAllLines(taskManagerPath).get(1));
    }

    @Test
    public void add_oneSubTask_fileExistsAndInMemoryMapFilled() {
        Epic epic = new Epic("epic", "epic desc");
        taskManager.addEpic(epic);
        SubTask subTask1 = new SubTask("subtask1", "subtask desc1", Status.NEW, epic);
        taskManager.addSubTask(subTask1);

        assertTrue(Files.exists(taskManagerPath));
        assertEquals(1, taskManager.getAllSubTasks().size());
    }

    @Test
    public void add_oneSubtask_thirdLineIsAddedSubtask() throws IOException {
        Epic epic = new Epic("epic", "epic desc");
        taskManager.addEpic(epic);
        SubTask subTask1 = new SubTask("subtask1", "subtask desc1", Status.NEW, epic);
        taskManager.addSubTask(subTask1);

        List<String> fileLines = Files.readAllLines(taskManagerPath);

        String expected = "2,SUBTASK,subtask1,NEW,subtask desc1,1";
        assertEquals(expected, fileLines.get(2));
    }

    @Test
    public void deleteSubTask_addEpicWith2SubtasksThenDeleteOneSubtask_epicWithOneSubtaskPresentInFile() throws IOException {
        Epic epic = new Epic("epic", "epic desc");
        SubTask subTask1 = new SubTask("subtask1", "subtask desc1", Status.NEW, epic);
        SubTask subTask2 = new SubTask("subtask2", "subtask desc2", Status.NEW, epic);
        epic.getSubTasks().addAll(List.of(subTask1, subTask2));
        taskManager.addEpic(epic);
        assertEquals(4, Files.readAllLines(taskManagerPath).size());

        taskManager.deleteSubTask(3);
        List<String> linesFromFile = Files.readAllLines(taskManagerPath);

        assertEquals(3, linesFromFile.size());
        String expected = "2,SUBTASK,subtask1,NEW,subtask desc1,1";
        assertEquals(expected, linesFromFile.get(2));
    }

    @Test
    public void deleteAllSubTasks_addEpicWith2SubtasksThenDeleteAllSubtask_onlyEpicRemainsInFile() throws IOException {
        Epic epic = new Epic("epic", "epic desc");
        SubTask subTask1 = new SubTask("subtask1", "subtask desc1", Status.NEW, epic);
        SubTask subTask2 = new SubTask("subtask2", "subtask desc2", Status.NEW, epic);
        epic.getSubTasks().addAll(List.of(subTask1, subTask2));
        taskManager.addEpic(epic);
        assertEquals(4, Files.readAllLines(taskManagerPath).size());

        taskManager.deleteAllSubTasks();
        List<String> linesFromFile = Files.readAllLines(taskManagerPath);
        assertEquals(2, linesFromFile.size());
        String expected = "1,EPIC,epic,NEW,epic desc";
        assertEquals(expected, linesFromFile.get(1));
    }

    @Test
    public void updateSubTask_addEpicWith2SubtasksThenUpdateStatusOfOneSubtask_epicAndOneOfSubtasksUpdatedInFile() throws IOException {
        Epic epic = new Epic("epic", "epic desc");
        SubTask subTask1 = new SubTask("subtask1", "subtask desc1", Status.NEW, epic);
        SubTask subTask2 = new SubTask("subtask2", "subtask desc2", Status.NEW, epic);
        epic.getSubTasks().addAll(List.of(subTask1, subTask2));
        taskManager.addEpic(epic);
        assertEquals(4, Files.readAllLines(taskManagerPath).size());
        String expectedEpic = "1,EPIC,epic,NEW,epic desc";
        assertEquals(expectedEpic, Files.readAllLines(taskManagerPath).get(1));

        SubTask updatedSubtask = new SubTask("subtask2", "subtask desc2", Status.DONE, epic);
        updatedSubtask.setId(3);
        taskManager.updateSubTask(updatedSubtask);
        List<String> linesFromFile = Files.readAllLines(taskManagerPath);

        assertEquals(4, linesFromFile.size());
        String expectedEpicAfterUpdate = "1,EPIC,epic,IN_PROGRESS,epic desc";
        String expectedSubtaskAfterUpdate = "3,SUBTASK,subtask2,DONE,subtask desc2,1";
        assertEquals(expectedEpicAfterUpdate, linesFromFile.get(1));
        assertEquals(expectedSubtaskAfterUpdate, linesFromFile.get(3));
    }

    @Test
    public void add_oneEpic_fileExistsAndInMemoryMapFilled() {
        Epic epic = new Epic("epic", "epic desc");
        SubTask subTask1 = new SubTask("subtask1", "subtask desc1", Status.NEW, epic);
        SubTask subTask2 = new SubTask("subtask2", "subtask desc2", Status.NEW, epic);
        epic.getSubTasks().addAll(List.of(subTask1, subTask2));
        taskManager.addEpic(epic);

        assertTrue(Files.exists(taskManagerPath));
        assertEquals(1, taskManager.getAllEpics().size());
    }

    @Test
    public void add_oneEpic_secondLineIsAddedEpic() throws IOException {
        Epic epic = new Epic("epic", "epic desc");
        SubTask subTask1 = new SubTask("subtask1", "subtask desc1", Status.NEW, epic);
        SubTask subTask2 = new SubTask("subtask2", "subtask desc2", Status.NEW, epic);
        epic.getSubTasks().addAll(List.of(subTask1, subTask2));
        taskManager.addEpic(epic);

        List<String> fileLines = Files.readAllLines(taskManagerPath);

        String expected = "1,EPIC,epic,NEW,epic desc";
        assertEquals(expected, fileLines.get(1));
    }

    @Test
    public void deleteEpic_addEpicWith2SubtasksThenDeleteEpic_fileIsEmpty() throws IOException {
        Epic epic = new Epic("epic", "epic desc");
        SubTask subTask1 = new SubTask("subtask1", "subtask desc1", Status.NEW, epic);
        SubTask subTask2 = new SubTask("subtask2", "subtask desc2", Status.NEW, epic);
        epic.getSubTasks().addAll(List.of(subTask1, subTask2));
        taskManager.addEpic(epic);
        assertEquals(4, Files.readAllLines(taskManagerPath).size());

        taskManager.deleteEpic(1);
        List<String> fileLines = Files.readAllLines(taskManagerPath);

        assertEquals(1, fileLines.size());
        String expectedAfterDelete = "id,type,name,status,description,epic";
        assertEquals(expectedAfterDelete, fileLines.getFirst());
    }

    @Test
    public void deleteAllEpics_add2EpicsWith2SubtasksInEachThenDeleteAllEpics_fileIsEmpty() throws IOException {
        Epic epic = new Epic("epic", "epic desc");
        SubTask subTask1 = new SubTask("subtask1", "subtask desc1", Status.NEW, epic);
        SubTask subTask2 = new SubTask("subtask2", "subtask desc2", Status.NEW, epic);
        epic.getSubTasks().addAll(List.of(subTask1, subTask2));
        taskManager.addEpic(epic);
        Epic epic1 = new Epic("epic1", "epic desc1");
        SubTask subTask3 = new SubTask("subtask3", "subtask desc3", Status.NEW, epic1);
        SubTask subTask4 = new SubTask("subtask4", "subtask desc4", Status.NEW, epic1);
        epic1.getSubTasks().addAll(List.of(subTask3, subTask4));
        taskManager.addEpic(epic1);
        assertEquals(7, Files.readAllLines(taskManagerPath).size());

        taskManager.deleteAllEpics();
        List<String> fileLines = Files.readAllLines(taskManagerPath);

        assertEquals(1, fileLines.size());
        String expectedAfterDelete = "id,type,name,status,description,epic";
        assertEquals(expectedAfterDelete, fileLines.getFirst());
    }

    @Test
    public void updateEpic_add2EpicsWith2SubtasksThenUpdateEpicName_updatedEpicInFile() throws IOException {
        Epic epic = new Epic("epic", "epic desc");
        SubTask subTask1 = new SubTask("subtask1", "subtask desc1", Status.NEW, epic);
        SubTask subTask2 = new SubTask("subtask2", "subtask desc2", Status.NEW, epic);
        epic.getSubTasks().addAll(List.of(subTask1, subTask2));
        taskManager.addEpic(epic);
        assertEquals(4, Files.readAllLines(taskManagerPath).size());

        Epic updatedEpic = new Epic("UpdatedEpic", "epic desc");
        updatedEpic.setId(1);
        updatedEpic.getSubTasks().addAll(List.of(subTask1, subTask2));
        taskManager.updateEpic(updatedEpic);
        List<String> fileLines = Files.readAllLines(taskManagerPath);

        assertEquals(4, fileLines.size());
        String expected = "1,EPIC,UpdatedEpic,NEW,epic desc";
        assertEquals(expected, fileLines.get(1));
    }

    @Test
    public void add_2Tasks2Subtasks1Epic_linesCountIs6AndOrderedById() throws IOException {
        Task task1 = new Task("task1", "task desc1", Status.NEW);
        taskManager.addTask(task1);
        Epic epic = new Epic("epic", "epic desc");
        SubTask subTask1 = new SubTask("subtask1", "subtask desc1", Status.NEW, epic);
        SubTask subTask2 = new SubTask("subtask2", "subtask desc2", Status.NEW, epic);
        epic.getSubTasks().addAll(List.of(subTask1, subTask2));
        taskManager.addEpic(epic);
        Task task2 = new Task("task2", "task desc2", Status.NEW);
        taskManager.addTask(task2);

        List<String> fileLines = Files.readAllLines(taskManagerPath);

        int expectedLinesCount = 6;
        String expectedSixthLine = "5,TASK,task2,NEW,task desc2";
        assertEquals(expectedLinesCount, fileLines.size());
        assertEquals(expectedSixthLine, fileLines.get(5));
        for (int i = 1; i < fileLines.size(); i++) {
            assertEquals(String.valueOf(i), fileLines.get(i).split(",")[0]);
        }
    }

    @Test
    public void loadFromFile_add2TasksThenLoadFromFileAndAddThirdTask_threeTasksPresentInFileAndThirdTaskIdIs3() throws IOException {
        Task task1 = new Task("task1", "task desc1", Status.NEW);
        taskManager.addTask(task1);
        Task task2 = new Task("task2", "task desc2", Status.NEW);
        taskManager.addTask(task2);

        FileBackedTaskManager restoredManager = FileBackedTaskManager.loadFromFile(taskManagerPath);
        Task task3 = new Task("task3", "task desc3", Status.NEW);
        restoredManager.addTask(task3);
        List<String> fileLines = Files.readAllLines(taskManagerPath);

        String expected = "3,TASK,task3,NEW,task desc3";
        assertEquals(4, fileLines.size());
        assertEquals(expected, fileLines.get(3));
    }

    @Test
    public void loadFromFile_addEpicWith2SubtasksNewAndInProgressThenLoadFromFile_threeTasksPresentWithCorrectState() throws IOException {
        Epic epic = new Epic("epic", "epic desc");
        SubTask subTask1 = new SubTask("subtask1", "subtask desc1", Status.NEW, epic);
        SubTask subTask2 = new SubTask("subtask2", "subtask desc2", Status.NEW, epic);
        epic.getSubTasks().addAll(List.of(subTask1, subTask2));
        taskManager.addEpic(epic);
        SubTask subTask1Updated = new SubTask("subtask1", "subtask desc1", Status.DONE, epic);
        subTask1Updated.setId(2);
        taskManager.updateSubTask(subTask1Updated);

        List<String> fileLines = Files.readAllLines(taskManagerPath);
        assertEquals(Status.IN_PROGRESS, Status.valueOf(fileLines.get(1).split(",")[3]));
        FileBackedTaskManager restoredManager = FileBackedTaskManager.loadFromFile(taskManagerPath);

        assertEquals(2, restoredManager.getAllSubTasks().size());
        assertEquals(Status.IN_PROGRESS, restoredManager.getEpic(1).getStatus());
        assertEquals(2, restoredManager.getEpic(1).getSubTasks().size());
        assertEquals("epic", restoredManager.getSubTask(2).getEpic().getName());
    }

    @Test
    public void loadFromFile_add3TasksThenDeleteFirstOneThenAddNewTask_lastAddedTaskIdIs4() throws IOException {
        Task task1 = new Task("task1", "task desc1", Status.NEW);
        taskManager.addTask(task1);
        Task task2 = new Task("task2", "task desc2", Status.NEW);
        taskManager.addTask(task2);
        Task task3 = new Task("task3", "task desc3", Status.NEW);
        taskManager.addTask(task3);

        assertEquals(4, Files.readAllLines(taskManagerPath).size());
        taskManager.deleteTask(1);
        assertEquals(2, Integer.parseInt(Files.readAllLines(taskManagerPath).get(1).split(",")[0]));
        FileBackedTaskManager restoredManager = FileBackedTaskManager.loadFromFile(taskManagerPath);
        Task task4 = new Task("task4", "task desc4", Status.NEW);
        restoredManager.addTask(task4);

        assertEquals(3, restoredManager.getAllTasks().size());
        assertEquals(4, Integer.parseInt(Files.readAllLines(taskManagerPath).get(3).split(",")[0]));
    }

    @Test
    public void loadEmptyFile() throws IOException {
        Task task = new Task("task", "task desc", Status.NEW);
        taskManager.addTask(task);

        List<String> fileLines = Files.readAllLines(taskManagerPath);
        String expected = "1,TASK,task,NEW,task desc";
        assertEquals(expected, fileLines.get(1));
        taskManager.deleteTask(1);
        FileBackedTaskManager restoredManager = FileBackedTaskManager.loadFromFile(taskManagerPath);

        assertEquals(0, restoredManager.getAllTasks().size());
        assertEquals(0, restoredManager.getAllEpics().size());
        assertEquals(0, restoredManager.getAllSubTasks().size());
    }
}