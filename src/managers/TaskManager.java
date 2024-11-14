package managers;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.List;

public interface TaskManager {
    //Методы работы с tasks.Task
    List<Task> getAllTasks();

    void deleteAllTasks();

    Task getTask(int id);

    void addTask(Task task);

    void updateTask(Task task);

    void deleteTask(int id);

    //Методы работы с tasks.SubTask
    List<SubTask> getAllSubTasks();

    List<SubTask> getAllSubTasks(Epic epic);

    void deleteAllSubTasks();

    SubTask getSubTask(int id);

    void addSubTask(SubTask subTask);

    void updateSubTask(SubTask subTask);

    void deleteSubTask(int id);

    //Методы работы с tasks.Epic
    List<Epic> getAllEpics();

    void deleteAllEpics();

    Epic getEpic(int id);

    void addEpic(Epic epic);

    void updateEpic(Epic epic);

    void deleteEpic(int id);

    //Методы работы с историей просмотров
    List<Task> getHistory();
}
