import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<SubTask> subTasks;

    public Epic(String name, String description) {
        super(name, description);
        this.subTasks = new ArrayList<>();
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    @Override
    public String toString() {
        return "Epic" + super.toString() +
                ", subTasksSize=" + subTasks.size() +
                '}';
    }
}
