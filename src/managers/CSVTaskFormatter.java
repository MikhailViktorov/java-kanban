package managers;

import models.*;

import java.util.ArrayList;
import java.util.List;

public class CSVTaskFormatter {

    public static String toString(Task task) {
        int id = task.getId();
        String type = task.getClass().getSimpleName().toUpperCase();
        String name = task.getName();
        TaskStatus status = task.getTaskStatus();
        String description = task.getDescription();
        String taskString = String.format("%s,%s,%s,%s,%s", id, type, name, status, description);
        if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
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

        switch (taskType) {
            case TASK:
                Task task = new Task(name, description);
                task.setId(id);
                task.setTaskStatus(status);
                return task;
            case SUBTASK:
                Integer epicId = Integer.parseInt(dataValues[8]);
                Subtask subtask = new Subtask(name, description, epicId);
                subtask.setId(id);
                subtask.setTaskStatus(status);
                return subtask;
            case EPIC:
                Epic epic = new Epic(name, description);
                epic.setId(id);
                return epic;
            default:

        }
        return null;
    }
    public static String historyToString(HistoryManager historyManager) {
        List<Task> historyData = historyManager.getHistory();
        StringBuilder stringBuilder = new StringBuilder();
        if (historyData != null) {
            for (Task task : historyData) {
                stringBuilder.append(task.getId()).append(",");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            return String.valueOf(stringBuilder);
        } else return "";
    }

    public static List<Integer> historyFromString(String value) {
        String[] values = value.split(",");
        List<Integer> historyList = new ArrayList<>();
        for (String taskId : values) {
            historyList.add(Integer.parseInt(taskId));
        }
        return historyList;
    }
}
