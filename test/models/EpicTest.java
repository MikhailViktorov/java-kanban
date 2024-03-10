package models;

import managers.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
    Epic epic = new Epic("Title", "Description");
    Subtask subtask1;
    Subtask subtask2;

    @BeforeEach
    public void setUp() {
        inMemoryTaskManager.createEpic(epic);
        subtask1 = new Subtask("Title", "Description", epic.getId(), LocalDateTime.of(2024, 3, 5, 0, 0), Duration.ofDays(5));
        subtask2 = new Subtask("Title", "Description", epic.getId(), LocalDateTime.of(2024, 4, 5, 0, 0), Duration.ofDays(5));
    }

    @Test
    public void epicStatusShouldBeNew() {
        assertTrue(inMemoryTaskManager.getEpicById(epic.getId()).getSubtaskList().isEmpty());
        assertEquals(inMemoryTaskManager.getEpicById(epic.getId()).getTaskStatus(), TaskStatus.NEW);
    }

    @Test
    public void epicStatusShouldBeNewWhenTwoSubtasksHasStatusNew() {
        subtask1.setTaskStatus(TaskStatus.NEW);
        subtask2.setTaskStatus(TaskStatus.NEW);
        inMemoryTaskManager.createSubtask(subtask1);
        inMemoryTaskManager.createSubtask(subtask2);
        inMemoryTaskManager.updateEpics(epic);
        assertEquals(inMemoryTaskManager.getEpicById(epic.getId()).getTaskStatus(), TaskStatus.NEW);
    }

    @Test
    public void epicStatusShouldBeDoneWhenTwoSubtasksHasStatusDone() {
        subtask1.setTaskStatus(TaskStatus.DONE);
        subtask2.setTaskStatus(TaskStatus.DONE);
        inMemoryTaskManager.createSubtask(subtask1);
        inMemoryTaskManager.createSubtask(subtask2);
        inMemoryTaskManager.updateEpics(epic);
        assertEquals(inMemoryTaskManager.getEpicById(epic.getId()).getTaskStatus(), TaskStatus.DONE);
    }

    @Test
    public void epicStatusShouldBeInProgressWhenTwoSubtasksHasStatusNewAndDone() {
        subtask1.setTaskStatus(TaskStatus.NEW);
        subtask2.setTaskStatus(TaskStatus.DONE);
        inMemoryTaskManager.createSubtask(subtask1);
        inMemoryTaskManager.createSubtask(subtask2);
        inMemoryTaskManager.updateEpics(epic);
        assertEquals(inMemoryTaskManager.getEpicById(epic.getId()).getTaskStatus(), TaskStatus.IN_PROGRESS);
    }

    @Test
    public void epicStatusShouldBeInProgressWhenTwoSubtasksHasStatusInProgress() {
        subtask1.setTaskStatus(TaskStatus.IN_PROGRESS);
        subtask2.setTaskStatus(TaskStatus.IN_PROGRESS);
        inMemoryTaskManager.createSubtask(subtask1);
        inMemoryTaskManager.createSubtask(subtask2);
        inMemoryTaskManager.updateEpics(epic);
        assertEquals(inMemoryTaskManager.getEpicById(epic.getId()).getTaskStatus(), TaskStatus.IN_PROGRESS);
    }
}