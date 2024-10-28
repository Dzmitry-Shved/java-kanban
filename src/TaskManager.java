import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {

    private static int idCount = 0;

    private final HashMap<Integer, Task> tasksById = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasksById = new HashMap<>();
    private final HashMap<Integer, Epic> epicsById = new HashMap<>();

    //Методы работы с Task
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasksById.values());
    }

    public void deleteAllTasks() {
        tasksById.clear();
    }

    public Task getTask(int id) {
        return tasksById.get(id);
    }

    public void addTask(Task task) {
        task.setId(generateId());
        tasksById.put(task.getId(), task);
    }

    public void updateTask(Task task) {
        tasksById.put(task.getId(), task);
    }

    public void deleteTask(int id) {
        tasksById.remove(id);
    }

    //Методы работы с SubTask
    public List<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasksById.values());
    }

    public List<SubTask> getAllSubTasks(Epic epic) {
        return epicsById.get(epic.getId()).getSubTasks();
    }

    public void deleteAllSubTasks() {

        //очищаю списки Субтасков всех Эпиков и рассчитываю их статус
        for (Epic epic : epicsById.values()) {
            epic.getSubTasks().clear();
            calculateEpicStatus(epic);
        }

        //очищаю репозиторий
        subTasksById.clear();
    }

    public SubTask getSubTask(int id) {
        return subTasksById.get(id);
    }

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

    public void deleteSubTask(int id) {
        //получаю существующую в репозитории Субтаску
        SubTask subTask = subTasksById.get(id);

        //удаляю Субтаску из списка ее Эпика
        subTask.getEpic().getSubTasks().remove(subTask);

        //рассчитываю статус Эпика после удаления
        calculateEpicStatus(subTask.getEpic());

        //удаляю Субтаску из репозитория
        subTasksById.remove(id);
    }

    //Методы работы с Epic
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epicsById.values());
    }

    public void deleteAllEpics() {
        //очищаю репозиторий Субтасков
        subTasksById.clear();

        //очищаю репозиторий Эпиков
        epicsById.clear();
    }

    public Epic getEpic(int id) {
        return epicsById.get(id);
    }

    public void addEpic(Epic epic) {
        //генерирую id Эпика
        epic.setId(generateId());

        //назначаю id Субтаскам Эпика и добавляю в репозиторий
        ArrayList<SubTask> subTasks = epic.getSubTasks();
        for (SubTask subTask : subTasks) {
            subTask.setId(generateId());
            subTasksById.put(subTask.getId(), subTask);
        }

        //рассчитываю статус Эпика
        calculateEpicStatus(epic);

        //добавляю Эпик в репозиторий
        epicsById.put(epic.getId(), epic);
    }

    public void updateEpic(Epic epic) {
        //заменяю Субтаски в репозитории (назначаю id если Субтаска новая)
        ArrayList<SubTask> subTasks = epic.getSubTasks();
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

    public void deleteEpic(int id) {
        //удаляю Субтаски удаляемого Эпика из репозитория
        ArrayList<SubTask> epicSubTasks = epicsById.get(id).getSubTasks();
        for (SubTask subTask : epicSubTasks) {
            subTasksById.remove(subTask.getId());
        }

        //удаляю Эпик из репозитория
        epicsById.remove(id);
    }

    //Генерация id и расчет статуса Эпика
    private static int generateId() {
        return ++idCount;
    }

    private static void calculateEpicStatus(Epic epic) {
        if (epic.getSubTasks().isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        ArrayList<SubTask> subTasks = epic.getSubTasks();
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
