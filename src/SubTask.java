public class SubTask extends Task {

    private Epic epic;

    public SubTask(String name, String description, Epic epic) {
        super(name, description);
        this.epic = epic;
    }

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }

    @Override
    public String toString() {
        return "Sub" + super.toString() +
                ", epic=" + epic.toString() +
                '}';
    }
}
