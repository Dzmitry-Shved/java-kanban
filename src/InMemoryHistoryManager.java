import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int HISTORY_CAPACITY = 10;
    private final List<Task> history = new ArrayList<>(HISTORY_CAPACITY);

    @Override
    public void add(Task task) {
        if (history.size() + 1 > HISTORY_CAPACITY) {
            history.removeFirst();
        }
        history.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
