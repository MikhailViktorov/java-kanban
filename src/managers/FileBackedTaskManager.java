package managers;

import exceptions.ManagerSaveException;
import models.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


public class FileBackedTaskManager extends InMemoryTaskManager {
    private File fileForSaveData;


    public FileBackedTaskManager(File file) {
        this.fileForSaveData = file;
    }


    private void save() {

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileForSaveData))) {
            bufferedWriter.write("id,type,name,status,description,startTime,duration,endTime,epic\n");

            for (Task task : getAllTasks()) {
                bufferedWriter.write(CSVTaskFormatter.toString(task) + "\n");
            }
            for (Task task : getAllSubtasks()) {
                bufferedWriter.write(CSVTaskFormatter.toString(task) + "\n");
            }
            for (Task task : getAllEpics()) {
                bufferedWriter.write(CSVTaskFormatter.toString(task) + "\n");
            }
            bufferedWriter.write("\n");
            bufferedWriter.write(CSVTaskFormatter.historyToString(historyManager));


        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
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
                Task task = CSVTaskFormatter.fromString(line);
                if (task instanceof Subtask) {
                    manager.subtasks.put(task.getId(), (Subtask) task);
                    manager.prioritizedTasks.add(task);
                } else if (task instanceof Epic) {
                    manager.epics.put(task.getId(), (Epic) task);
                } else if (task != null) {
                    manager.tasks.put(task.getId(), task);
                    manager.prioritizedTasks.add(task);

                }
            }
        }
        if (CSVTaskFormatter.historyFromString(history) != null) {
            for (Integer id : CSVTaskFormatter.historyFromString(history)) {
                if (manager.tasks.containsKey(id)) {
                    manager.historyManager.add(manager.tasks.get(id));
                } else if (manager.epics.containsKey(id)) {
                    manager.historyManager.add(manager.epics.get(id));
                } else {
                    manager.historyManager.add(manager.subtasks.get(id));
                }
            }
        }
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
