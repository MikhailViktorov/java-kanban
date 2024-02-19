package managers;

import exceptions.ManagerSaveException;
import models.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File fileForSaveData;

    public FileBackedTaskManager(File file) {
        this.fileForSaveData = file;
    }


    public void save() {

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileForSaveData))) {
            bufferedWriter.write("id,type,name,status,description,epic\n");

            for (Task task : getAllTasks()) {
                bufferedWriter.write(toString(task) + "\n");
            }
            for (Task task : getAllSubtasks()) {
                bufferedWriter.write(toString(task) + "\n");
            }
            for (Task task : getAllEpics()) {
                bufferedWriter.write(toString(task) + "\n");
            }
            bufferedWriter.write("\n");
            bufferedWriter.write(historyToString(historyManager));


        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }


    private String toString(Task task) {
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

    private static Task fromString(String value) {
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

    private static String historyToString(HistoryManager historyManager) {
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

    private static List<Integer> historyFromString(String value) {
        String[] values = value.split(",");
        List<Integer> historyList = new ArrayList<>();
        for (String taskId : values) {
            historyList.add(Integer.parseInt(taskId));
        }
        return historyList;
    }

    public static FileBackedTaskManager loadFromFile(File file) throws IOException {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        List<String> lines = Files.readAllLines(Path.of(file.toURI()), StandardCharsets.UTF_8);
        lines.remove(lines.get(0));

        if (lines.isEmpty()) {
            return manager;
        }

        int lastElemNum = lines.size() - 1;
        String history = lines.get(lastElemNum);
        lines.remove(lastElemNum);

        for (String line : lines) {
            if (!line.isBlank() && !line.equals("\n")) {
                Task task = fromString(line);
                if (task instanceof Subtask) {
                    manager.createSubtask((Subtask) task);
                } else if (task instanceof Epic) {
                    manager.createEpic((Epic) task);
                } else if (task != null) {
                    manager.createTask(task);
                }
            }
        }
        for (Integer id : historyFromString(history)) {
            manager.historyManager.add(manager.getTaskById(id));
        }
        manager.save();
        return manager;
    }


    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public List<Task> getAllTasks() {
        return super.getAllTasks();
    }

    @Override
    public List<Epic> getAllEpics() {
        return super.getAllEpics();
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return super.getAllSubtasks();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpics(Epic epic) {
        super.updateEpics(epic);
        save();
    }

    @Override
    public void updateSubtasks(Subtask subtask) {
        super.updateSubtasks(subtask);
        save();
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

}
