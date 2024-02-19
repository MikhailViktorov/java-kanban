package managers;

import models.Epic;
import models.Subtask;
import models.Task;
import models.TaskStatus;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public void createTask(Task task) {
        tasks.put(task.getId(), task);
    }
    @Override
    public void createEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }
    @Override
    public void createSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtask(subtask.getId());
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(epic);
    }
    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }
    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }
    @Override
    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }
    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.get(id);
        for (Integer taskId : epic.getSubtaskList()) {
            subtasks.remove(taskId);
        }
        epics.remove(id);
        historyManager.remove(id);
    }
    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            for (Integer subtaskId : epic.getSubtaskList()) {
                epic.deleteSubtask(subtaskId);
            }
            updateEpicStatus(epic);
        }
    }
    @Override
    public void deleteSubtaskById(int id) {
        Epic epic = epics.get(subtasks.get(id));
        subtasks.remove(id);
        epic.deleteSubtask(id);
        updateEpicStatus(epic);
        historyManager.remove(id);
    }
    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }
    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }
    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        historyManager.add(subtask);
        return subtask;
    }
    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }
    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }
    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public Map<Integer, Epic> getEpics() {
        return epics;
    }
    @Override
    public List<Subtask> getEpicTasks(int id) {
        List<Subtask> list = new ArrayList<>();
        Epic epic = epics.get(id);
        for (Integer taskid: epic.getSubtaskList()) {
            list.add(subtasks.get(taskid));
        }
        return list;
    }
    @Override
    public void updateTask(Task task) {
        if (tasks.get(task.getId()) != null) {
            tasks.put(task.getId(), task);
        }
    }
    @Override
    public void updateEpics(Epic epic) {
        if (epics.get(epic.getId()) != null) {
            epics.put(epic.getId(),epic);
            updateEpicStatus(epic);
        }
    }
    @Override
    public void updateSubtasks(Subtask subtask) {
        if (subtasks.get(subtask.getId()) != null) {
            subtasks.put(subtask.getId(),subtask);
            Epic epic = epics.get(subtask.getEpicId());
            updateEpicStatus(epic);
        }
    }

    private void updateEpicStatus(Epic epic) {
        boolean allNew = true;
        boolean allDone = true;
        for (Integer taskId: epic.getSubtaskList()) {
            Subtask subtask = subtasks.get(taskId);
            if (subtask.getTaskStatus() != TaskStatus.NEW) {
                allNew = false;
            }
            if (subtask.getTaskStatus() != TaskStatus.DONE) {
                allDone = false;
            }
        }
        if (allNew) {
            epic.setTaskStatus(TaskStatus.NEW);
        }
        else if (allDone) {
            epic.setTaskStatus(TaskStatus.DONE);
        }
        else {
            epic.setTaskStatus(TaskStatus.IN_PROGRESS);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();

    }
    public int getHistorySize() {
        return historyManager.historySize();
    }
    public void removeHistoryNode(int id) {
        historyManager.remove(id);
    }
}


