package test;


import managers.FileBackedTaskManager;
import managers.TaskManager;
import models.Epic;
import models.Subtask;
import models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    File tmpFile;

    @BeforeEach
    public void setUp() {
        {
            try {
                tmpFile = File.createTempFile("data", ".csv");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        taskManager = new FileBackedTaskManager(tmpFile);
    }

    @Test
    public void shouldReturnHistoryAndDataAfterCreating() {
        Task task = new Task("task", "taskDescription", Duration.ofMinutes(5), LocalDateTime.of(2024, 3, 5, 0, 0));
        Epic epic = new Epic("epic", "epicDescription");
        taskManager.createTask(task);
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("subtask", "subtaskDescription", epic.getId(), LocalDateTime.of(2024, 3, 5, 20, 0), Duration.ofMinutes(10));
        Subtask subtask2 = new Subtask("subtask2", "subtaskDescription2", epic.getId(), LocalDateTime.of(2024, 3, 6, 20, 0), Duration.ofMinutes(10));
        taskManager.createSubtask(subtask);
        taskManager.createSubtask(subtask2);

        taskManager.getTaskById(task.getId());
        taskManager.getEpicById(epic.getId());
        taskManager.getSubtaskById(subtask.getId());
        taskManager.getSubtaskById(subtask2.getId());

        TaskManager newFBTM;
        try {
            newFBTM = FileBackedTaskManager.loadFromFile(tmpFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertEquals(1, newFBTM.getAllTasks().size());
        assertEquals(2, newFBTM.getAllSubtasks().size());
        assertEquals(1, newFBTM.getAllEpics().size());
        assertEquals(4, newFBTM.getHistory().size());

    }


    @Test
    public void shouldReturnEmptyHistoryAndDataAfterDeleting() {
        taskManager.deleteAllTasks();
        taskManager.deleteAllEpics();
        TaskManager newFBTM;
        try {
            newFBTM = FileBackedTaskManager.loadFromFile(tmpFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertEquals(0, newFBTM.getAllTasks().size());
        assertEquals(0, newFBTM.getAllSubtasks().size());
        assertEquals(0, newFBTM.getAllEpics().size());
        assertEquals(0, newFBTM.getHistory().size());
    }

    @Test
    public void getEpicTimeAfterCreatingSubtasks() {
        LocalDateTime startTimeSubtask1 = LocalDateTime.of(2024, 3, 5, 12, 0);
        LocalDateTime startTimeSubtask2 = LocalDateTime.of(2024, 3, 5, 15, 0);
        Duration durationTimeSubtask1 = Duration.ofHours(2);
        Duration durationTimeSubtask2 = Duration.ofHours(3);
        Epic epic = new Epic("Title", "Description");
        Subtask subtask1 = new Subtask("Title", "Description", epic.getId(), startTimeSubtask1, durationTimeSubtask1);
        Subtask subtask2 = new Subtask("Title", "Description", epic.getId(), startTimeSubtask2, durationTimeSubtask2);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        assertEquals(startTimeSubtask1, epic.getStartTime(), "Epic startTime is incorrect");
        assertEquals(durationTimeSubtask1.plus(durationTimeSubtask2), epic.getDuration(), "Epic duration is incorrect");

    }


}


