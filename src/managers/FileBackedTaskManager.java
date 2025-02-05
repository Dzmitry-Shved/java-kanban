package managers;

import tasks.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final Path taskManagerPath;
    private final TaskIdComparator taskIdComparator = new TaskIdComparator();

    private static final String TABLE_HEAD = "id,type,name,status,description,epic";
    private static final String TASK_AND_EPIC_FORMAT = "%s,%s,%s,%s,%s";
    private static final String SUBTASK_FORMAT = "%s,%s,%s,%s,%s,%s";


    public FileBackedTaskManager(Path taskManagerPath) {
        this.taskManagerPath = taskManagerPath;
    }

    public static void main(String[] args) {
        Path pathToTm = Paths.get("task_manager.txt");
        FileBackedTaskManager tm = new FileBackedTaskManager(pathToTm);

        Task task1 = new Task("Задача1", "Описание задачи1", Status.NEW);
        tm.addTask(task1);
        Task task2 = new Task("Задача2", "Описание задачи2", Status.NEW);
        tm.addTask(task2);
        Epic epic1 = new Epic("Эпик1", "Описание эпика1");
        SubTask subTask1 = new SubTask("Субтаска1", "Описание субтаски1", Status.NEW, epic1);
        SubTask subTask2 = new SubTask("Субтаска2", "Описание субтаски2", Status.NEW, epic1);
        SubTask subTask3 = new SubTask("Субтаска3", "Описание субтаски3", Status.NEW, epic1);
        epic1.getSubTasks().add(subTask1);
        epic1.getSubTasks().add(subTask2);
        epic1.getSubTasks().add(subTask3);
        tm.addEpic(epic1);
        Epic epic2 = new Epic("Эпик2", "Описание эпика2");
        tm.addEpic(epic2);
        Task task3 = new Task("Задача3", "Описание задачи3", Status.NEW);
        tm.addTask(task3);

        FileBackedTaskManager restoredTm = FileBackedTaskManager.loadFromFile(pathToTm);

        Set<Task> originalTasks = new HashSet<>(tm.getAllTasks());
        Set<Epic> originalEpics = new HashSet<>(tm.getAllEpics());
        Set<SubTask> originalSubtasks = new HashSet<>(tm.getAllSubTasks());
        Set<Task> restoredTasks = new HashSet<>(restoredTm.getAllTasks());
        Set<Epic> restoredEpics = new HashSet<>(restoredTm.getAllEpics());
        Set<SubTask> restoredSubtasks = new HashSet<>(restoredTm.getAllSubTasks());

        boolean isTaskReposEqual = originalTasks.containsAll(restoredTasks) && restoredTasks.containsAll(originalTasks);
        boolean isEpicReposEqual = originalEpics.containsAll(restoredEpics) && restoredEpics.containsAll(originalEpics);
        boolean isSubtaskReposEqual =
                originalSubtasks.containsAll(restoredSubtasks) && restoredSubtasks.containsAll(originalSubtasks);

        System.out.printf("Репозитории тасок %s\n", isTaskReposEqual ? "совпадают" : "не совпадают");
        System.out.printf("Репозитории эпиков %s\n", isEpicReposEqual ? "совпадают" : "не совпадают");
        System.out.printf("Репозитории субтасок %s\n", isSubtaskReposEqual ? "совпадают" : "не совпадают");
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void addSubTask(SubTask subTask) {
        super.addSubTask(subTask);
        save();
    }

    @Override
    public void deleteSubTask(int id) {
        super.deleteSubTask(id);
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    public static FileBackedTaskManager loadFromFile(Path path) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(path);
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            reader.readLine();
            while (reader.ready()) {
                String line = reader.readLine();
                Type type = Type.valueOf(line.split(",")[1]);
                switch (type) {
                    case TASK:
                        fileBackedTaskManager.restoreTask(fromString(line, type));
                        break;
                    case EPIC:
                        fileBackedTaskManager.restoreEpic((Epic) fromString(line, type));
                        break;
                    default:
                        fileBackedTaskManager.restoreSubtask((SubTask) fromString(line, type));
                }
                if (!reader.ready()) {
                    fileBackedTaskManager.setIdCount(Integer.parseInt(line.split(",")[0]));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        fileBackedTaskManager.bindEpicsAndSubtasks();

        return fileBackedTaskManager;
    }

    private void save() {
        List<Task> taskManagerSortedById = collectSortedTaskManager();
        try (BufferedWriter writer = Files.newBufferedWriter(taskManagerPath)) {
            writer.write(TABLE_HEAD);
            for (Task task : taskManagerSortedById) {
                writer.newLine();
                writer.write(toString(task));
            }
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    private List<Task> collectSortedTaskManager() {
        List<Task> taskManagerList = new ArrayList<>();
        taskManagerList.addAll(super.getAllTasks());
        taskManagerList.addAll(super.getAllEpics());
        taskManagerList.addAll(super.getAllSubTasks());
        taskManagerList.sort(taskIdComparator);

        return taskManagerList;

    }

    private static String toString(Task task) {
        Formatter formatter = new Formatter();

        if (task instanceof SubTask subTask) {
            formatter.format(
                    SUBTASK_FORMAT,
                    subTask.getId(),
                    Type.SUBTASK,
                    subTask.getName(),
                    subTask.getStatus(),
                    subTask.getDescription(),
                    subTask.getEpic().getId()
            );
            return formatter.toString();
        }

        formatter.format(
                TASK_AND_EPIC_FORMAT,
                task.getId(),
                task.getClass().getSimpleName().toUpperCase(),
                task.getName(),
                task.getStatus(),
                task.getDescription()
        );
        return formatter.toString();
    }

    private static Task fromString(String value, Type type) {
        String[] values = value.split(",");
        int id = Integer.parseInt(values[0]);
        String name = values[2];
        String description = values[4];
        Status status = Status.valueOf(values[3]);
        switch (type) {
            case TASK:
                Task task = new Task(name, description, status);
                task.setId(id);
                return task;
            case EPIC:
                Epic epic = new Epic(name, description);
                epic.setId(id);
                epic.setStatus(status);
                return epic;
            default:
                int epicId = Integer.parseInt(values[5]);
                Epic subtasksEpic = new Epic("", "");
                subtasksEpic.setId(epicId);
                SubTask subTask = new SubTask(name, description, status, subtasksEpic);
                subTask.setId(id);
                return subTask;
        }
    }

    private static class TaskIdComparator implements Comparator<Task> {
        @Override
        public int compare(Task task1, Task task2) {
            return Integer.compare(task1.getId(), task2.getId());
        }
    }

    private static class ManagerSaveException extends RuntimeException {
        public ManagerSaveException() {
        }
    }
}
