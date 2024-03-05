package managers;

import models.Epic;
import models.Subtask;
import models.Task;

import java.util.List;
import java.util.TreeSet;

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

    List<Task> getHistory();

    void calculateEpicStartTime(int epicId);

    void calculateEpicDuration(int epicId);

    void calculateEpicEndTime(int epicId);

    TreeSet<Task> getPrioritizedTasks();

}
