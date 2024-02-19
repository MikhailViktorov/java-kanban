package test;

import managers.FileBackedTaskManager;
import managers.TaskManager;
import models.Epic;
import models.Subtask;
import models.Task;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {
    TaskManager fileBackedTaskManager;
    @BeforeEach
    public void createFileBackTaskManager() {
        fileBackedTaskManager = new FileBackedTaskManager(new File("dataTest.csv"));
    }

    public Task createTaskForTests() {
        Task task = new Task("Task name", "Task description");
        fileBackedTaskManager.createTask(task);
        return task;
    }

    public Epic createEpicForTests() {
        Epic epic = new Epic("Epic name", "Epic description");
        fileBackedTaskManager.createEpic(epic);
        return epic;
    }

    public Subtask createSubtaskForTests(Integer epicId) {
        Subtask subtask = new Subtask("Subtask name", "Subtask description", epicId);
        fileBackedTaskManager.createSubtask(subtask);
        return subtask;
    }

}
