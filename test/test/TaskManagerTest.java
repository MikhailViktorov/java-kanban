package test;


import managers.TaskManager;
import models.Epic;
import models.Subtask;
import models.Task;
import models.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    @BeforeEach
    public void resetAllIdTo0() {
        Task.setCount(0);
    }

    @Test
    public void tasksShouldCreatedAndAddToList() {
        Task task = new Task("task", "taskDescription", Duration.ofMinutes(5), LocalDateTime.of(2024, 3, 5, 0, 0));
        taskManager.createTask(task);
        final int taskId = task.getId();
        final Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Task not found after create");
        assertEquals(task, savedTask, "Tasks with same id not are equals");
        assertEquals(1, task.getId(), "task has an invalid id");
        assertEquals(1, taskManager.getAllTasks().size(), "task is not added to TasksList");

    }

    @Test
    void subtasksShouldCreatedAndAddToList() {
        Epic epic = new Epic("epic", "epicDescription");
        Subtask subtask = new Subtask("subtask", "subtaskDescription", epic.getId(), LocalDateTime.of(2024, 3, 5, 20, 0), Duration.ofMinutes(10));
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);

        final int subtaskId = subtask.getId();
        final Subtask savedSubtask = taskManager.getSubtaskById(subtaskId);

        assertNotNull(savedSubtask, "Subtask not found after create");
        assertEquals(subtask, savedSubtask, "Subtasks with same id not are equals");
        assertEquals(2, subtask.getId(), "Subtask has an invalid id");
        assertEquals(1, taskManager.getAllSubtasks().size(), "Subtask is not added to SubtaskList");
        assertEquals(1, taskManager.getEpicTasks(epic.getId()).size(), "Subtask not found in EpicSubtaskList");

    }

    @Test
    void epicsShouldCreatedAndAddToList() {
        Epic epic = new Epic("epic", "epicDescription");
        taskManager.createEpic(epic);

        final int epicId = epic.getId();
        final Epic savedEpic = taskManager.getEpicById(epicId);

        assertNotNull(savedEpic, "Epic not found after create");
        assertEquals(epic, savedEpic, "Epics with same id not are equals");
        assertEquals(1, epic.getId(), "Epic has an invalid id");
        assertEquals(1, taskManager.getAllEpics().size(), "Epic is not added to EpicList");
    }


    @Test
    void shouldDeleteTaskById() {
        Task task = new Task("task", "taskDescription", Duration.ofMinutes(5), LocalDateTime.of(2024, 3, 5, 0, 0));
        taskManager.createTask(task);

        taskManager.deleteTaskById(task.getId());

        assertEquals(0, taskManager.getAllTasks().size(), "Task has not been removed from the TaskMap");

    }

    @Test
    void shouldGetAllTasks() {
        assertEquals(0, taskManager.getAllTasks().size(), "TaskList not empty");
        Task task = new Task("task", "taskDescription", Duration.ofMinutes(5), LocalDateTime.of(2024, 3, 5, 0, 0));
        taskManager.createTask(task);

        assertEquals(1, taskManager.getAllTasks().size(), "task is not added to TasksList");
    }


    @Test
    void shouldGetAllSubtasks() {
        assertEquals(0, taskManager.getAllSubtasks().size(), "SubtaskList not empty");
        Epic epic = new Epic("epic", "epicDescription");
        Subtask subtask = new Subtask("subtask", "subtaskDescription", epic.getId(), LocalDateTime.of(2024, 3, 5, 20, 0), Duration.ofMinutes(10));
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);

        assertEquals(1, taskManager.getAllSubtasks().size(), "Subtask is not added to SubtaskList");
    }


    @Test
    void shouldGetAllEpics() {
        assertEquals(0, taskManager.getAllEpics().size(), "EpicList not empty");

        Epic epic = new Epic("epic", "epicDescription");
        taskManager.createEpic(epic);

        assertEquals(1, taskManager.getAllEpics().size(), "Epic is not added to EpicList");
    }


    @Test
    void EpicShouldChangeStatus() {
        Epic epic = new Epic("epic", "epicDescription");
        taskManager.createEpic(epic);

        assertEquals(TaskStatus.NEW, epic.getTaskStatus(), "Epic status after init is invalid");

        epic.setTaskStatus(TaskStatus.IN_PROGRESS);

        assertEquals(TaskStatus.IN_PROGRESS, epic.getTaskStatus(), "Epic status after change is invalid");
    }


    @Test
    void EpicShouldChangeStatusAfterChangeStatusSubtasks() {
        Epic epic = new Epic("epic", "epicDescription");
        Subtask subtask = new Subtask("subtask", "subtaskDescription", epic.getId(), LocalDateTime.of(2024, 3, 5, 20, 0), Duration.ofMinutes(10));
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);

        subtask.setTaskStatus(TaskStatus.DONE);
        taskManager.updateEpics(epic);

        assertEquals(TaskStatus.DONE, epic.getTaskStatus(), "Epic status after change subtask status is invalid");
    }


    @Test
    void shouldUpdateTask() {
        Task task = new Task("task", "taskDescription", Duration.ofMinutes(5), LocalDateTime.of(2024, 3, 5, 0, 0));
        taskManager.createTask(task);

        task.setName("newName");
        task.setDuration(Duration.ofMinutes(50));
        taskManager.updateTask(task);

        assertEquals("newName", taskManager.getTaskById(task.getId()).getName(), "Name task is not updated");
        assertEquals("PT50M", taskManager.getTaskById(task.getId()).getDuration().toString(), "Duration task is not updated");

    }


    @Test
    void shouldUpdateSubtask() {
        Epic epic = new Epic("epic", "epicDescription");
        Subtask subtask = new Subtask("subtask", "subtaskDescription", epic.getId(), LocalDateTime.of(2024, 3, 5, 20, 0), Duration.ofMinutes(10));
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);


        subtask.setId(10);
        subtask.setTaskStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtasks(subtask);


        assertEquals(10, subtask.getId(),
                "Subtask id is not updated");
        assertEquals(TaskStatus.IN_PROGRESS, subtask.getTaskStatus(), "Subtask Status is not updated");

    }


    @Test
    void shouldUpdateEpic() {
        Epic epic = new Epic("epic", "epicDescription");
        taskManager.createEpic(epic);

        epic.setDescription("newDescription");
        taskManager.updateEpics(epic);

        assertEquals("newDescription", epic.getDescription(), "Epic description is not updated");


    }

    @Test
    void shouldGetHistory() {
        assertEquals(taskManager.getHistory().size(), 0, "History is fill");

        Task task = new Task("task", "taskDescription", Duration.ofMinutes(5), LocalDateTime.of(2024, 3, 5, 0, 0));
        Epic epic = new Epic("epic", "epicDescription");
        taskManager.createTask(task);
        taskManager.createEpic(epic);

        taskManager.getTaskById(task.getId());
        taskManager.getEpicById(epic.getId());

        assertEquals(taskManager.getHistory().size(), 2, "History has incorrect fill");

    }

    @Test
    public void shouldGetEndTime() {
        Task task = new Task("Title", "Description", Duration.ofDays(10), LocalDateTime.of(2024, 3, 5, 0, 0));
        taskManager.createTask(task);
        assertEquals(LocalDateTime.of(2024, 3, 15, 0, 0), task.getEndTime());
    }

}