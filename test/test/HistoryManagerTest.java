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

    private final Task task1 = new Task("Task1 name", "Task1 description", Duration.ofMinutes(60), LocalDateTime.of(2024, 2, 2, 0, 0));
    private final Task task2 = new Task("Task2 name", "Task2 description", Duration.ofMinutes(60), LocalDateTime.of(2024, 2, 2, 0, 0));
    private final Task task3 = new Task("Task3 name", "Task3 description", Duration.ofMinutes(60), LocalDateTime.of(2024, 2, 2, 0, 0));

    @BeforeEach
    public void createHistoryManager() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    public void shouldGetHistory() {
        historyManager.add(task1);

        List<Task> testTasks = List.of(task1);

        assertEquals(testTasks, historyManager.getHistory());
    }

    @Test
    public void shouldReturnEmptyListWhenNoActions() {
        List<Task> history = historyManager.getHistory();

        assertNotNull(history, "History not empty");
        assertEquals("[]", history.toString());
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

        List<Task> testTasks = List.of(task3, task2);
        historyManager.remove(task1.getId());

        assertEquals(testTasks, historyManager.getHistory(), "incorrect history size after delete head");
    }

    @Test
    public void shouldDeleteMiddleInHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        List<Task> testTasks = List.of(task3, task1);
        historyManager.remove(task2.getId());

        assertEquals(testTasks, historyManager.getHistory(), "incorrect history size after delete tail");
    }

    @Test
    public void shouldDeleteTailInHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        List<Task> testTasks = List.of(task2, task1);
        historyManager.remove(task3.getId());

        assertEquals(testTasks, historyManager.getHistory(), "incorrect history size after delete tail");
    }

    @Test
    public void shouldReturnHistorySize() {
        historyManager.add(task1);
        historyManager.add(task2);

        assertEquals(2, historyManager.historySize(), "incorrect history size");
    }


}
