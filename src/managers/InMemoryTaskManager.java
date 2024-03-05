package managers;

import exceptions.IntersectionException;
import models.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();

    protected final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.nullsLast((o1, o2) -> {
        if (o1.getStartTime() != null && o2.getStartTime() != null) {
            return o1.getStartTime().compareTo(o2.getStartTime());
        } else if (o1 == o2) {
            return 0;
        } else {
            return 1;
        }
    }));

    @Override
    public void createTask(Task task) {
        checkIntersectionOfTasks(task);
        tasks.put(task.getId(), task);
        prioritizedTasks.add(task);

    }

    @Override
    public void createEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        checkIntersectionOfTasks(subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtask(subtask.getId());
        subtasks.put(subtask.getId(), subtask);

        prioritizedTasks.add(subtask);
        updateEpicStatus(epic);
        calculateEpicTimesAndDuration(subtask.getEpicId());

    }

    @Override
    public void deleteAllTasks() {
        for (Integer id : tasks.keySet()) {
            historyManager.remove(id);
        }
        tasks.clear();
        prioritizedTasks.removeIf(task -> task.getTaskType() == TaskTypes.TASK);
    }

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteAllEpics() {
        for (Integer id : epics.keySet()) {
            historyManager.remove(id);
        }
        for (Integer id : subtasks.keySet()) {
            historyManager.remove(id);
        }
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
            calculateEpicTimesAndDuration(epic.getId());
            for (Integer subtaskId : epic.getSubtaskList()) {
                epic.deleteSubtask(subtaskId);
            }
            updateEpicStatus(epic);
        }
        for (Integer id : subtasks.keySet()) {
            historyManager.remove(id);
        }
        prioritizedTasks.removeIf(subtasks -> subtasks.getTaskType() == TaskTypes.SUBTASK);
    }

    @Override
    public void deleteSubtaskById(int id) {
        Epic epic = epics.get(subtasks.get(id));
        calculateEpicTimesAndDuration(subtasks.get(id).getEpicId());
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
        for (Integer taskid : epic.getSubtaskList()) {
            list.add(subtasks.get(taskid));
        }
        return list;
    }

    @Override
    public void updateTask(Task task) {
        try {
            checkIntersectionOfTasks(task);
            Task saveTask = tasks.get(task.getId());
            if (saveTask == null) {
                return;
            }
            saveTask.setName(task.getName());
            saveTask.setDescription(task.getDescription());
            saveTask.setTaskStatus(task.getTaskStatus());
            saveTask.setStartTime(task.getStartTime());
            saveTask.setId(task.getId());
            saveTask.setDuration(task.getDuration());
            prioritizedTasks.remove(task);
            prioritizedTasks.add(saveTask);
        } catch (IntersectionException e) {
            System.out.println("Time overlap when updating a task with ID:" + task.getId());
        }
    }


    @Override
    public void updateEpics(Epic epic) {
        if (epics.get(epic.getId()) != null) {
            epics.put(epic.getId(), epic);
            updateEpicStatus(epic);
        }
    }

    @Override
    public void updateSubtasks(Subtask subtask) {
        try {
            checkIntersectionOfTasks(subtask);
            Subtask saveSubtask = subtasks.get(subtask.getId());
            if (subtasks.get(subtask.getId()) != null) {
                subtasks.put(subtask.getId(), subtask);
                Epic epic = epics.get(subtask.getEpicId());
                calculateEpicTimesAndDuration(subtask.getEpicId());
                updateEpicStatus(epic);
                prioritizedTasks.remove(subtask);
                prioritizedTasks.add(saveSubtask);
            }
        } catch (IntersectionException e) {
            System.out.println("Time overlap when updating a subtask with ID:" + subtask.getId());
        }
    }

    private void updateEpicStatus(Epic epic) {
        boolean allNew = true;
        boolean allDone = true;
        for (Integer taskId : epic.getSubtaskList()) {
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
        } else if (allDone) {
            epic.setTaskStatus(TaskStatus.DONE);
        } else {
            epic.setTaskStatus(TaskStatus.IN_PROGRESS);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();

    }

    public void calculateEpicTimesAndDuration(int epicId) {
        calculateEpicStartTime(epicId);
        calculateEpicDuration(epicId);
        calculateEpicEndTime(epicId);
    }

    @Override
    public void calculateEpicStartTime(int epicId) {
        List<LocalDateTime> subtasksLocalDateTime = new ArrayList<>();
        if (epics.isEmpty()) {
            return;
        }
        for (Epic epic : epics.values()) {
            if (epic.getId() == epicId) {
                List<Subtask> subtasksOfEpic = getEpicTasks(epicId);
                if (subtasksOfEpic.isEmpty()) {
                    return;
                }
                for (Subtask subtask : subtasksOfEpic) {
                    if (subtask.getStartTime() == null) {
                        continue;
                    }
                    subtasksLocalDateTime.add(subtask.getStartTime());
                    LocalDateTime minTime = Collections.min(subtasksLocalDateTime);
                    epic.setStartTime(minTime);
                }
            }
        }
    }

    @Override
    public void calculateEpicDuration(int epicId) {
        long duration = 0;
        if (epics.isEmpty()) {
            return;
        }
        for (Epic epic : epics.values()) {
            if (epic.getId() == epicId) {
                List<Subtask> subtasksOfEpic = getEpicTasks(epicId);
                if (subtasksOfEpic.isEmpty()) {
                    return;
                }
                for (Subtask subtask : subtasksOfEpic) {
                    if (subtask.getDuration() == null) {
                        epic.setDuration(Duration.ofMinutes(duration));
                        return;
                    }
                    duration = duration + subtask.getDuration().toMinutes();
                    epic.setDuration(Duration.of(duration, ChronoUnit.MINUTES));
                }
            }
        }
    }

    @Override
    public void calculateEpicEndTime(int epicId) {
        for (Epic epic : epics.values()) {
            if (epic.getId() == epicId) {
                calculateEpicStartTime(epic.getId());
                if (epic.getStartTime() == null || epic.getDuration() == null) {
                    return;
                }
                epic.setEndTime(epic.getStartTime().plusMinutes(epic.getDuration().toMinutes()));
            }
        }
    }


    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    public int getHistorySize() {
        return historyManager.historySize();
    }

    public void removeHistoryNode(int id) {
        historyManager.remove(id);
    }

    private void checkIntersectionOfTasks(Task task) {
        LocalDateTime startTime = task.getStartTime();
        LocalDateTime endTime = task.getEndTime();

        Integer result = prioritizedTasks.stream()
                .map(tsk -> {
                    if (startTime.isBefore(tsk.getEndTime()) && endTime.isAfter(tsk.getStartTime())) {
                        return 1;
                    }
                    if (startTime.isBefore(tsk.getEndTime()) && endTime.isAfter(tsk.getEndTime())) {
                        return 1;
                    }
                    return 0;
                })
                .reduce(Integer::sum)
                .orElse(0);
        if (result > 0) {
            throw new IntersectionException("tasks overlap");
        }
    }

}


