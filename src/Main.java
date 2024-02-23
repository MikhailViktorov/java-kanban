import managers.FileBackedTaskManager;
import managers.InMemoryTaskManager;
import managers.Managers;
import managers.TaskManager;
import models.Epic;
import models.Subtask;
import models.Task;


/**
 * Created by Mikhail Viktorov on 24.12.2023 (update 19.02.2024)
 * the-rayn@yandex.ru
 */
public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager taskTracker = Managers.getDefault();

        // 1. Создайте две задачи, эпик с тремя подзадачами и эпик без подзадач.

        Epic epic1 = new Epic("Эпик 1", "Нужно сделать");
        taskTracker.createEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask1 создания ",
                "Написать что то ", epic1.getId());
        taskTracker.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Subtask2 создания ",
                "Написать что то ", epic1.getId());
        taskTracker.createSubtask(subtask2);
        Subtask subtask3 = new Subtask("Subtask3 создания ",
                "Написать что то ", epic1.getId());
        taskTracker.createSubtask(subtask3);

        Epic epic2 = new Epic("Эпик 2", "Нужно сделать");
        taskTracker.createEpic(epic2);

        // 2. Запросите созданные задачи несколько раз в разном порядке.
        // 3. После каждого запроса выведите историю и убедитесь, что в ней нет повторов.

        taskTracker.getEpicById(epic2.getId());
        System.out.println("Размер истории: " + taskTracker.getHistorySize());
        printAllTasks(taskTracker);
        taskTracker.getSubtaskById(subtask3.getId());
        System.out.println("Размер истории: " + taskTracker.getHistorySize());
        printAllTasks(taskTracker);
        taskTracker.getSubtaskById(subtask2.getId());
        System.out.println("Размер истории: " + taskTracker.getHistorySize());
        printAllTasks(taskTracker);
        taskTracker.getSubtaskById(subtask1.getId());
        System.out.println("Размер истории: " + taskTracker.getHistorySize());
        printAllTasks(taskTracker);
        taskTracker.getEpicById(epic1.getId());
        System.out.println("Размер истории: " + taskTracker.getHistorySize());
        printAllTasks(taskTracker);

        // 4. Удалите задачу, которая есть в истории, и проверьте, что при печати она не будет выводиться.
        taskTracker.removeHistoryNode(epic2.getId());

        System.out.println("Размер истории после удаления епика без сабтасков: " + taskTracker.getHistorySize());
        // 5. Удалите эпик с тремя подзадачами и убедитесь, что из истории удалился как сам эпик, так и все его подзадачи.
        taskTracker.deleteEpicById(epic1.getId());
        for (Integer s : epic1.getSubtaskList()) {
            taskTracker.removeHistoryNode(s);
        }
        System.out.println("Размер истории после удаление эпика с 3 сабтасками: " + taskTracker.getHistorySize());
        printAllTasks(taskTracker);

    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }


    }
}
