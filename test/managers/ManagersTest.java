package managers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class ManagersTest {

    @Test
    public void getDefault_returns_InMemoryTaskManager() {
        TaskManager taskManager = Managers.getDefault();

        assertInstanceOf(InMemoryTaskManager.class, taskManager);
    }

    @Test
    public void getDefaultHistory_returns_InMemoryHistoryManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();

        assertInstanceOf(InMemoryHistoryManager.class, historyManager);
    }
}