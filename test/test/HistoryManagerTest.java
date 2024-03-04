package test;

import managers.*;
import models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryManagerTest {
    HistoryManager historyManager;

    private final Task task1 = new Task("Task1 name", "Task1 description", Duration.ofMinutes(60), LocalDateTime.of(2024,2,2,0,0));
    private final Task task2 = new Task("Task2 name", "Task2 description",Duration.ofMinutes(60), LocalDateTime.of(2024,2,2,0,0));
    private final Task task3 = new Task("Task3 name", "Task3 description",Duration.ofMinutes(60), LocalDateTime.of(2024,2,2,0,0));

    @BeforeEach
    public void createHistoryManager() {
        historyManager = new InMemoryHistoryManager();
    }


    @Test
    public void shouldReturnEmptyListWhenNoActions() {
        List<Task> history = historyManager.getHistory();

        assertNotNull(history, "History not empty");
        assertEquals(0, history.size(), "History not empty");
    }

    @Test
    public void shouldFillHistoryAfterAdding() {
        historyManager.add(task1);

        List<Task> historyWith1Elem = historyManager.getHistory();

        assertEquals(1, historyWith1Elem.size(), "History size after add 1 task is incorrect");
        assertEquals(task1, historyWith1Elem.get(0), "incorrect adding task");
    }

    @Test
    public void shouldDeleteSameTaskInHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task1);

        List<Task> history = historyManager.getHistory();

        assertEquals(2, history.size(), "Incorrect history size after adding task with same id");
    }

    @Test
    public void shouldDeleteHeadInHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task1.getId());
        List<Task> historyAfterDelete = historyManager.getHistory();

        assertEquals(2, historyAfterDelete.size(), "incorrect history size after delete head");
    }

    @Test
    public void shouldDeleteTailInHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);


        historyManager.remove(task3.getId());
        List<Task> history = historyManager.getHistory();

        assertEquals(2, history.size(), "incorrect history size after delete tail");
    }


}
