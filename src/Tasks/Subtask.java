package Tasks;

public class Subtask extends Task {
    // Для каждой подзадачи известно, в рамках какого эпика она выполняется
    private Integer epicId;

    public Subtask(String name, String description, Integer epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Tasks.Subtask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", Id=" + getId() +
                ", Tasks.TaskStatus=" + getTaskStatus() +
                ", epicId=" + epicId;
    }
}
