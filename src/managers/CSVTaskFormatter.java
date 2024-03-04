package managers;

import models.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CSVTaskFormatter {

    public static String toString(Task task) {
        int id = task.getId();
        String type = task.getTaskType().toString();
        String name = task.getName();
        TaskStatus status = task.getTaskStatus();
        String description = task.getDescription();
        Long duration = task.getDuration().toMinutes();
        LocalDateTime startTime = task.getStartTime();
        LocalDateTime endTime = task.getEndTime();


        String taskString = String.format("%s,%s,%s,%s,%s,%s,%s,%s", id, type, name, status, description, startTime, duration, endTime);
        if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            taskString += ",";
            taskString += subtask.getEpicId();

        }
        return taskString;
    }

    public static Task fromString(String value) {
        String[] dataValues = value.split(",");
        int id = Integer.parseInt(dataValues[0]);
        TaskTypes taskType = TaskTypes.valueOf(dataValues[1]);
        String name = dataValues[2];
        TaskStatus status = TaskStatus.valueOf(dataValues[3]);
        String description = dataValues[4];
        LocalDateTime startTime = LocalDateTime.parse(dataValues[5]);
        Duration duration = Duration.ofMinutes(Long.parseLong(dataValues[6]));


        switch (taskType) {
            case TASK:
                Task task = new Task(name, description, duration, startTime);
                task.setId(id);
                task.setTaskStatus(status);
                return task;
            case EPIC:
                Epic epic = new Epic(name, description);
                epic.setId(id);
                return epic;
            case SUBTASK:
                Integer epicId = Integer.parseInt(dataValues[8]);
                Subtask subtask = new Subtask(name, description, epicId, startTime, duration);
                subtask.setId(id);
                subtask.setTaskStatus(status);
                return subtask;

        }
        return null;
    }

    public static String historyToString(HistoryManager historyManager) {
        List<Task> historyData = historyManager.getHistory();
        if (historyData != null) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Task task : historyData) {
                stringBuilder.append(task.getId()).append(",");
            }
            return String.valueOf(stringBuilder);
        } else return "";
    }

    public static List<Integer> historyFromString(String value) {
        List<Integer> historyList = new ArrayList<>();
        if (!value.equals("")) {
            String[] values = value.split(",");
            for (String taskId : values) {
                historyList.add(Integer.parseInt(taskId));
            }
            Collections.reverse(historyList);
            return historyList;
        }
        return null;
    }
}
