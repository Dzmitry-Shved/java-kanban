package tasks;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private List<SubTask> subTasks;

    public Epic(String name, String description) {
        super(name, description);
        this.subTasks = new ArrayList<>();
    }

    public List<SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(List<SubTask> subTasks) {
        this.subTasks = subTasks;
    }

    @Override
    public Type getType() {
        return Type.EPIC;
    }

    @Override
    public String toString() {
        return "Epic" + super.toString() +
                ", subTasksSize=" + subTasks.size() +
                '}';
    }
}
