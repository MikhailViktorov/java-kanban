package Managers;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import Tasks.TaskStatus;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    void createTask(Task task);

    void createEpic(Epic epic);

    void createSubtask(Subtask subtask);

    void deleteAllTasks();

    void deleteTaskById(int id);

    void deleteAllEpics();

    void deleteEpicById(int id);

    void deleteAllSubtasks();

    void deleteSubtaskById(int id);

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<Subtask> getAllSubtasks();

    List<Subtask> getEpicTasks(int id);

    void updateTask(Task task);

    void updateEpics(Epic epic);

    void updateSubtasks(Subtask subtask);

    void updateEpicStatus(Epic epic);

    List<Task> getHistory();
}
