package test;

import managers.*;
import models.Epic;
import models.Subtask;
import models.Task;
import models.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    TaskManager taskManager;


    @BeforeEach
    public void resetAllIdTo0AndCreateTaskManager() {
        taskManager = Managers.getDefault();
        Task.setCount(0);
    }

    public Task createTaskForTests() {
        Task task = new Task("Task name", "Task description");
        taskManager.createTask(task);
        return task;
    }

    public Epic createEpicForTests() {
        Epic epic = new Epic("Epic name", "Epic description");
        taskManager.createEpic(epic);
        return epic;
    }

    public Subtask createSubtaskForTests(Integer epicId) {
        Subtask subtask = new Subtask("Subtask name", "Subtask description", epicId);
        taskManager.createSubtask(subtask);
        return subtask;
    }


    @Test
    public void tasksShouldCreatedAndAddToList() {
        Task task = createTaskForTests();

        final int taskID = task.getId();
        final Task savedTask = taskManager.getTaskById(taskID);

        assertNotNull(savedTask, "Task not found after create");
        assertEquals(task, savedTask, "Tasks with same id not are equals");
        assertEquals(1, task.getId(), "task has an invalid id");
        assertEquals(1, taskManager.getAllTasks().size(), "task is not added to TasksList");

    }

    @Test
    void subtasksShouldCreatedAndAddToList() {
        Epic epic = createEpicForTests();

        Subtask subtask = createSubtaskForTests(epic.getId());

        final int subtaskId = subtask.getId();
        final Subtask savedSubtask = taskManager.getSubtaskById(subtaskId);

        assertNotNull(savedSubtask, "Subtask not found after create");
        assertEquals(subtask, savedSubtask, "Subtasks with same id not are equals");
        assertEquals(2, subtask.getId(), "Subtask has an invalid id");
        assertEquals(1, taskManager.getAllSubtasks().size(), "Subtask is not added to SubtaskList");

    }

    @Test
    void epicsShouldCreatedAndAddToList() {
        Epic epic = createEpicForTests();

        final int epicId = epic.getId();
        final Epic savedEpic = taskManager.getEpicById(epicId);

        assertNotNull(savedEpic, "Epic not found after create");
        assertEquals(epic, savedEpic, "Epics with same id not are equals");
        assertEquals(1, epic.getId(), "Epic has an invalid id");
        assertEquals(1, taskManager.getAllEpics().size(), "Epic is not added to EpicList");
    }

    @Test
    void deleteTaskById() {
        Task task = createTaskForTests();

        taskManager.deleteTaskById(task.getId());

        assertEquals(0, taskManager.getAllTasks().size(), "Task has not been removed from the TaskMap");

    }

    @Test
    void getAllTasks() {
        assertEquals(0, taskManager.getAllTasks().size(), "TaskList not empty");
        Task task = createTaskForTests();
        assertEquals(1, taskManager.getAllTasks().size(), "task is not added to TasksList");
    }

    @Test
    void getAllSubtasks() {
        assertEquals(0, taskManager.getAllSubtasks().size(), "SubtaskList not empty");
        Epic epic = createEpicForTests();
        Subtask subtask = createSubtaskForTests(epic.getId());
        assertEquals(1, taskManager.getAllSubtasks().size(), "Subtask is not added to SubtaskList");
    }

    @Test
    void getAllEpics() {
        assertEquals(0, taskManager.getAllEpics().size(), "EpicList not empty");
        Epic epic = createEpicForTests();
        assertEquals(1, taskManager.getAllEpics().size(), "Epic is not added to EpicList");
    }

    @Test
    void EpicShouldChangeStatus() {
        Epic epic = createEpicForTests();
        assertEquals(TaskStatus.NEW, epic.getTaskStatus(), "Epic status after init is invalid");
        epic.setTaskStatus(TaskStatus.IN_PROGRESS);
        assertEquals(TaskStatus.IN_PROGRESS, epic.getTaskStatus(), "Epic status after change is invalid");
    }

    @Test
    void EpicShouldChangeStatusAfterChangeStatusSubtasks() {
        Epic epic = createEpicForTests();
        Subtask subtask = createSubtaskForTests(epic.getId());
        subtask.setTaskStatus(TaskStatus.DONE);
        taskManager.updateEpics(epic);
        assertEquals(TaskStatus.DONE, epic.getTaskStatus(), "Epic status after change subtask status is invalid");
    }

}