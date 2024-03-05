package test;


import managers.InMemoryTaskManager;
import managers.TaskManager;
import models.Epic;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;


public abstract class TaskManagerTest<T extends TaskManager> {

    InMemoryTaskManager taskManager = new InMemoryTaskManager();

    @Test
    public void shouldReturnNewStatusEpicWithoutSubtask() {
        Epic epic = new Epic("Test Epic", "Test Epic disc");
        taskManager.createEpic(epic);
        assertEquals("NEW", epic.getTaskStatus().toString(), "Неверный статус Epic");
    }
}