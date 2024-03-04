package models;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private Integer epicId;

    public Subtask(String name, String description, Integer epicId, LocalDateTime startTime, Duration duration) {
        super(name, description, duration, startTime);
        this.epicId = epicId;
        this.taskType = TaskTypes.SUBTASK;
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    @Override
    public Duration getDuration() {
        return super.getDuration();
    }

    @Override
    public void setDuration(Duration duration) {
        super.setDuration(duration);
    }

    @Override
    public LocalDateTime getStartTime() {
        return super.getStartTime();
    }

    @Override
    public void setStartTime(LocalDateTime startTime) {
        super.setStartTime(startTime);
    }

    @Override
    public LocalDateTime getEndTime() {
        return super.getEndTime();
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", epicId=" + getEpicId() +
                ", taskStatus=" + taskStatus +
                ", taskType=" + taskType +
                ", duration=" + getDuration() +
                ", startTime=" + getStartTime() +
                '}';
    }
}
