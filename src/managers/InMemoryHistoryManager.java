package managers;

import tasks.*;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node> history = new HashMap<>();

    private Node historyHead;
    private Node historyTail;

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }

        boolean isTaskPresent = history.containsKey(task.getId());
        if (isTaskPresent) {
            removeNode(history.get(task.getId()));
        }

        Node node = new Node(task);
        linkLast(node);
        history.put(task.getId(), node);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        Node nodeForRemove = history.remove(id);
        removeNode(nodeForRemove);
    }

    private void linkLast(Node nodeForLink) {
        if (historyHead == null) {
            historyHead = nodeForLink;
            historyTail = historyHead;
        } else {
            nodeForLink.prev = historyTail;
            historyTail.next = nodeForLink;
            historyTail = nodeForLink;
        }
    }

    private void removeNode(Node node) {
        if (node == null) {
            return;
        }

        Node next = node.next;
        Node prev = node.prev;

        if (prev == null) {
            historyHead = next;
        } else {
            prev.next = next;
            node.prev = null;
        }

        if (next == null) {
            historyTail = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }
    }

    private List<Task> getTasks() {
        if (history.isEmpty()) {
            return List.of();
        }
        ArrayList<Task> taskHistory = new ArrayList<>(history.size());
        Node currentNode = historyHead;
        taskHistory.add(currentNode.task);
        currentNode = historyHead.next;
        while (currentNode != null) {
            taskHistory.add(currentNode.task);
            currentNode = currentNode.next;
        }
        return taskHistory;
    }

    static class Node {
        private final Task task;
        private Node next;
        private Node prev;

        public Node(Task task) {
            this.task = task;
            this.next = null;
            this.prev = null;
        }
    }
}
