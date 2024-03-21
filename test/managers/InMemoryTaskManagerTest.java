package managers;

import models.Epic;
import models.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    public void shouldCalculateEpicDuration() {
        Epic epic = new Epic("epic1", "epicDescription");
        Subtask subtask1 = new Subtask("subtask", "subtaskDescription", epic.getId(), LocalDateTime.of(2024, 3, 5, 0, 0), Duration.ofDays(1));
        Subtask subtask2 = new Subtask("subtask", "subtaskDescription", epic.getId(), LocalDateTime.of(2024, 3, 6, 0, 0), Duration.ofDays(1));
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        assertEquals("PT48H", epic.getDuration().toString(), "Incorrect epic duration");
        assertEquals(2, epic.getDuration().toDays(), "Incorrect epic duration");

    }

    @Test
    public void shouldCalculateEpicStartTime() {
        Epic epic = new Epic("epic1", "epicDescription");
        Subtask subtask1 = new Subtask("subtask", "subtaskDescription", epic.getId(), LocalDateTime.of(2024, 3, 5, 0, 0), Duration.ofDays(1));
        Subtask subtask2 = new Subtask("subtask", "subtaskDescription", epic.getId(), LocalDateTime.of(2024, 3, 6, 0, 0), Duration.ofDays(2));
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        assertEquals("2024-03-05", epic.getStartTime().toLocalDate().toString(), "Incorrect epic start time");

    }

    @Test
    public void shouldCalculateEpicEndTime() {
        Epic epic = new Epic("epic1", "epicDescription");
        Subtask subtask1 = new Subtask("subtask", "subtaskDescription", epic.getId(), LocalDateTime.of(2024, 3, 5, 0, 0), Duration.ofDays(1));
        Subtask subtask2 = new Subtask("subtask", "subtaskDescription", epic.getId(), LocalDateTime.of(2024, 3, 6, 0, 0), Duration.ofDays(2));
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        assertEquals("2024-03-08", epic.getEndTime().toLocalDate().toString(), "Incorrect epic end time");

    }
}