package models;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private LocalDateTime endTime;
    private final List<Integer> subtaskList;

    public Epic(String name, String description) {
        super(name, description, Duration.ofMinutes(0), LocalDateTime.now());
        this.subtaskList = new ArrayList<>();
        this.taskType = TaskTypes.EPIC;
    }

    public List<Integer> getSubtaskList() {
        return subtaskList;
    }

    public void addSubtask(int subtaskId) {
        this.subtaskList.add(subtaskId);
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void clearSubtaskList() {
        subtaskList.clear();
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", taskStatus=" + taskStatus +
                ", taskType=" + taskType +
                ", duration=" + getDuration() +
                ", startTime=" + getStartTime() +
                '}';
    }
}
