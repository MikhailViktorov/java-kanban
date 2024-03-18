import managers.FileBackedTaskManager;
import managers.InMemoryTaskManager;
import managers.TaskManager;
import models.Epic;
import models.Subtask;
import models.Task;
import server.HttpTaskServer;


import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;


/**
 * Created by Mikhail Viktorov on 24.12.2023 (update 05.03.2024)
 * the-rayn@yandex.ru
 */
public class Main {

    public static void main(String[] args) {
        HttpTaskServer taskServer;
        TaskManager taskManager;
        taskManager = new FileBackedTaskManager(new File("saveHTTT.csv"));
        try {
            taskServer = new HttpTaskServer(taskManager);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        taskServer.start();

        Task task = new Task("nameE", "description0", Duration.ofMinutes(5), LocalDateTime.now());
        Epic epic = new Epic("epic", "epicDescription");
        Subtask subtask = new Subtask("subtask", "subtaskDescription", epic.getId(), LocalDateTime.of(2024, 3, 5, 20, 0), Duration.ofMinutes(10));
        taskManager.createTask(task);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);

    }
}
