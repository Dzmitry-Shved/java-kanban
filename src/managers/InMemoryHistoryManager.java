package managers;

import tasks.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int HISTORY_CAPACITY = 10;
    private final List<Task> history = new LinkedList<>();

    //В тестах проверяю только по полю name, так как Epic и SubTask в данном случае не глубокие копии (я не осилил их сделать)
    @Override
    public void add(Task task) {
        if (task == null) {return;}

        if (history.size() + 1 > HISTORY_CAPACITY) {
            history.removeFirst();
        }

        if (task instanceof Epic epic) {
            Epic epicCopy = new Epic(epic.getName(), epic.getDescription());
            epicCopy.setStatus(epic.getStatus());
            epicCopy.setSubTasks(new ArrayList<>(epic.getSubTasks()));
            epicCopy.setId(epic.getId());
            history.add(epicCopy);
        } else if (task instanceof SubTask subTask) {
            SubTask subTaskCopy = new SubTask(subTask.getName(), subTask.getDescription(), subTask.getStatus(),
                    subTask.getEpic());
            subTaskCopy.setId(subTask.getId());
            history.add(subTaskCopy);
        } else {
            Task taskCopy = new Task(task.getName(), task.getDescription(), task.getStatus());
            taskCopy.setId(task.getId());
            history.add(taskCopy);
        }
    }

    //Так как история отдается для просмотра и в приоритете операции получения по индексу - отдаю ArrayList
    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }
}
