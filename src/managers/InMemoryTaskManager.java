package managers;

import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    private int idCount = 0;

    private final Map<Integer, Task> tasksById = new HashMap<>();
    private final Map<Integer, SubTask> subTasksById = new HashMap<>();
    private final Map<Integer, Epic> epicsById = new HashMap<>();

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    //Методы работы с Task
    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasksById.values());
    }

    @Override
    public void deleteAllTasks() {
        for (Task taskForDelete : tasksById.values()) {
            historyManager.remove(taskForDelete.getId());
        }
        tasksById.clear();
    }

    @Override
    public Task getTask(int id) {
        Task task = tasksById.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public void addTask(Task task) {
        task.setId(generateId());
        tasksById.put(task.getId(), task);
    }

    @Override
    public void updateTask(Task task) {
        tasksById.put(task.getId(), task);
    }

    @Override
    public void deleteTask(int id) {
        historyManager.remove(id);
        tasksById.remove(id);
    }

    //Методы работы с SubTask
    @Override
    public List<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasksById.values());
    }

    @Override
    public List<SubTask> getAllSubTasks(Epic epic) {
        return epicsById.get(epic.getId()).getSubTasks();
    }

    @Override
    public void deleteAllSubTasks() {

        //очищаю списки Субтасков всех Эпиков и рассчитываю их статус
        for (Epic epic : epicsById.values()) {
            epic.getSubTasks().clear();
            calculateEpicStatus(epic);
        }

        //очищаю историю
        for (SubTask subTaskForDelete : subTasksById.values()) {
            historyManager.remove(subTaskForDelete.getId());
        }
        //очищаю репозиторий
        subTasksById.clear();
    }

    @Override
    public SubTask getSubTask(int id) {
        SubTask subTask = subTasksById.get(id);
        historyManager.add(subTask);
        return subTask;
    }

    @Override
    public void addSubTask(SubTask subTask) {
        //генерирую id
        subTask.setId(generateId());

        //связываю новую Субтаску с существующим в репозитории Эпиком и добавляю Субтаску в список существующего Эпика
        Epic presentSubTasksEpic = epicsById.get(subTask.getEpic().getId());
        subTask.setEpic(presentSubTasksEpic);
        presentSubTasksEpic.getSubTasks().add(subTask);

        //рассчитывают статус Эпика с добавленной Субтаской
        calculateEpicStatus(presentSubTasksEpic);

        //добавляю Субтаску в репозиторий
        subTasksById.put(subTask.getId(), subTask);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        //связываю новую Субтаску с существующим в репозитории Эпиком
        Epic presentSubTasksEpic = epicsById.get(subTask.getEpic().getId());
        subTask.setEpic(presentSubTasksEpic);

        //заменяю Субтаску в списке существующего Эпика
        int indexToReplace = presentSubTasksEpic.getSubTasks().indexOf(subTask);
        presentSubTasksEpic.getSubTasks().set(indexToReplace, subTask);

        //рассчитывают статус Эпика с добавленной Субтаской
        calculateEpicStatus(presentSubTasksEpic);

        //заменяю Субтаску в репозитории
        subTasksById.put(subTask.getId(), subTask);
    }

    @Override
    public void deleteSubTask(int id) {
        //получаю существующую в репозитории Субтаску
        SubTask subTask = subTasksById.get(id);

        //удаляю Субтаску из списка ее Эпика
        subTask.getEpic().getSubTasks().remove(subTask);

        //рассчитываю статус Эпика после удаления
        calculateEpicStatus(subTask.getEpic());

        //удаляю субтаску из истории
        historyManager.remove(id);
        //удаляю Субтаску из репозитория
        subTasksById.remove(id);
    }

    //Методы работы с Epic
    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epicsById.values());
    }

    @Override
    public void deleteAllEpics() {
        //очищаю историю субтасков
        deleteAllSubTasks();
        //очищаю репозиторий Субтасков
        subTasksById.clear();

        //очищаю историю эпиков
        for (Epic epicForDelete : epicsById.values()) {
            historyManager.remove(epicForDelete.getId());
        }
        //очищаю репозиторий Эпиков
        epicsById.clear();
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epicsById.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public void addEpic(Epic epic) {
        //генерирую id Эпика
        epic.setId(generateId());

        //назначаю id Субтаскам Эпика и добавляю в репозиторий
        List<SubTask> subTasks = epic.getSubTasks();
        for (SubTask subTask : subTasks) {
            subTask.setId(generateId());
            subTasksById.put(subTask.getId(), subTask);
        }

        //рассчитываю статус Эпика
        calculateEpicStatus(epic);

        //добавляю Эпик в репозиторий
        epicsById.put(epic.getId(), epic);
    }

    @Override
    public void updateEpic(Epic epic) {
        //заменяю Субтаски в репозитории (назначаю id если Субтаска новая)
        List<SubTask> subTasks = epic.getSubTasks();
        for (SubTask subTask : subTasks) {
            if (subTask.getId() == 0) {
                subTask.setId(generateId());
            }
            subTasksById.put(subTask.getId(), subTask);
        }

        //рассчитываю статус Эпика
        calculateEpicStatus(epic);

        //заменяю Эпик в репозитории
        epicsById.put(epic.getId(), epic);
    }

    @Override
    public void deleteEpic(int id) {
        //удаляю Субтаски удаляемого Эпика из репозитория и истории
        List<SubTask> epicSubTasks = epicsById.get(id).getSubTasks();
        for (SubTask subTask : epicSubTasks) {
            historyManager.remove(subTask.getId());
            subTasksById.remove(subTask.getId());
        }

        //удаляю эпик из истории
        historyManager.remove(id);
        //удаляю Эпик из репозитория
        epicsById.remove(id);
    }

    //Методы работы с историей просмотров
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    //Генерация id, расчет статуса Эпика и заполнение истории просмотров
    private int generateId() {
        return ++idCount;
    }

    private static void calculateEpicStatus(Epic epic) {
        if (epic.getSubTasks().isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        List<SubTask> subTasks = epic.getSubTasks();
        int doneSubTasks = 0;
        for (SubTask subTask : epic.getSubTasks()) {
            if (subTask.getStatus() == Status.DONE) {
                doneSubTasks++;
            }
        }
        if (doneSubTasks == 0) {
            epic.setStatus(Status.NEW);
        } else if (doneSubTasks == subTasks.size()) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
}
