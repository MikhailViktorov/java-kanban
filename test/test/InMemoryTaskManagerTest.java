package test;

import managers.*;
import models.Epic;
import models.Subtask;
import models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    TaskManager taskManager;


    @BeforeEach
    public void createTaskManager() {
        taskManager = Managers.getDefault();;
    }


    @Test
    public void tasksShouldBeEqualsWithSameId() {
        taskManager.createTask(new Task("task1", "description1"));
        assertSame(taskManager.getTaskById(1), taskManager.getTaskById(1), "Tasks with same id not are equals");
    }

    @Test
    void createTask() {
        Task task = new Task("Task name", "Task description");
        taskManager.createTask(task);
        assertEquals(1, task.getId(), "task has an invalid ID");
        assertEquals(1, taskManager.getAllTasks().size(), "task is not added to TasksMap");
    }
    @Test
    void createSubtask() {
        Epic epic = new Epic("Epic name", "Epic description");
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Subtask name","Subtask description",epic.getId());
        taskManager.createSubtask(subtask);
        assertEquals(2, subtask.getId(), "Subtask has an invalid ID");
        assertEquals(1, epic.getSubtaskList().size(), "Subtask is not added to EpicSubtasksList");
        assertEquals(1, taskManager.getAllSubtasks().size(), "Subtask is not added to SubtasksMap");
    }









}