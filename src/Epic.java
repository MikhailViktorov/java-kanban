import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    // Каждый эпик знает, какие подзадачи в него входят.
    private List<Integer> subtaskList;

    public Epic(String name, String description, int id, TaskStatus taskStatus) {
        super(name, description, id, taskStatus);
        this.subtaskList = new ArrayList<>();
    }

    public Epic(String name, String description) {
        this(name, description,0,TaskStatus.NEW);
    }

    public List<Integer> getSubtaskList() {
        return subtaskList;
    }

    public void addSubtask(int subtaskId) {
        this.subtaskList.add(subtaskId);
    }
    public void deleteSubtask(int subtaskId) {
        this.subtaskList.remove(subtaskId);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "epicId= " + getId() +
                ", subtaskList=" + subtaskList +
                ", epicStatus=" + getTaskStatus() +
                '}';
    }
}
