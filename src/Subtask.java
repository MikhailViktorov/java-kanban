
public class Subtask extends Task {
    // Для каждой подзадачи известно, в рамках какого эпика она выполняется
    private Integer epicId;

    public Subtask(String name, String description, int id, TaskStatus taskStatus, Integer epicId) {
        super(name, description, id, taskStatus);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, Integer epicId) {
        this(name,description,0,TaskStatus.NEW,epicId);
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", Id=" + getId() +
                ", TaskStatus=" + getTaskStatus() +
                ", epicId=" + epicId;
    }
}
