import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskManager {
    private int taskId = 1;
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();

    public void createTask(Task task) {
        int id = taskId++;
        task.setId(id);
        tasks.put(id, task);
    }

    public void createEpic(Epic epic) {
        int id = taskId++;
        epic.setId(id);
        epics.put(id, epic);
    }

    public void createSubtask(Subtask subtask) {
        int id = taskId++;
        subtask.setId(id);
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtask(subtask.getId());
        subtasks.put(id, subtask);
        updateStatus(epic);
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void deleteEpicById(int id) {
        Epic epic = epics.get(id);
        for (Integer tasId : epic.getSubtaskList()) {
            subtasks.remove(tasId);
        }
        epics.remove(id);
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            for (Integer subtaskId : epic.getSubtaskList()) {
                epic.deleteSubtask(subtaskId);
            }
            updateStatus(epic);
        }
    }

    public void deleteSubtaskById(int id) {
        Epic epic = epics.get(subtasks.get(id));
        subtasks.remove(id);
        epic.deleteSubtask(id);
        updateStatus(epic);
    }
    public Task getTaskById(int id) {
        return tasks.get(id);
    }
    public Epic getEpicById(int id) {
        return epics.get(id);
    }
    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public List<Subtask> getEpicTasks(int id) {
        List<Subtask> list = new ArrayList<>();
        Epic epic = epics.get(id);
        for (Integer taskid: epic.getSubtaskList()) {
            list.add(subtasks.get(taskid));
        }
        return list;
    }

    public void updateTask(Task task) {
        if (tasks.get(task.getId()) != null) {
            tasks.put(task.getId(), task);
        }
    }

    public void updateEpics(Epic epic) {
        if (epics.get(epic.getId()) != null) {
            epics.put(epic.getId(),epic);
            updateStatus(epic);
        }
    }
    public void updateSubtasks(Subtask subtask) {
        if (subtasks.get(subtask.getId()) != null) {
            subtasks.put(subtask.getId(),subtask);
            Epic epic = epics.get(subtask.getEpicId());
            updateStatus(epic);
        }
    }
    private void updateStatus(Epic epic) {
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
}
