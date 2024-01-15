package test;

import Managers.*;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    public static InMemoryTaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
    }

    @Test
    public void tasksShouldBeEqualsWithSameId() {
        taskManager.createTask(new Task("task1","description1"));
        assertSame(taskManager.getTaskById(1),taskManager.getTaskById(1),"Tasks with same id not are equals");
    }
    @Test
    public void epicsShouldBeEqualsWithSameId() {
        taskManager.createEpic(new Epic("epic1","description1"));
        assertSame(taskManager.getEpicById(1),taskManager.getEpicById(1),"Epics with same id not are equals");
    }
    @Test
    public void TaskShouldBeCreatedAndCanFindIt() {
        Task task1 = new Task("task1", "description1");
        Task task2 = new Task("task2", "description2");
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        List<Task> expected = new ArrayList<>();
        expected.add(task1);
        expected.add(task2);
        List<Task> actual = taskManager.getAllTasks();
        assertEquals(task1,taskManager.getTaskById(1));
        assertNotNull(actual);
        assertEquals(expected,actual);
    }

    @Test
    public void EpicAndSubTaskShouldBeCreatedAndCanFindIt() {
        taskManager.createEpic( new Epic("epic1","description1"));
        taskManager.createSubtask(new Subtask("subtask","description",1));
        assertNotNull(taskManager.getEpicById(1));
        assertNotNull(taskManager.getSubtaskById(2));
    }
    //Не пойму с тестами, если запускать по отдельности, то проходят, запускаю проверку всего класса то выдаёт ошибки.



}