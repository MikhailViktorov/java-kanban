import Managers.InMemoryTaskManager;
import Managers.TaskManager;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import Tasks.TaskStatus;

/**
 * Created by Mikhail Viktorov on 24.12.2023 (update 15.01.2024)
 * the-rayn@yandex.ru
 */
public class Main {
    public static void main(String[] args) {

        TaskManager taskTracker = new InMemoryTaskManager();
        Epic epic1 = new Epic("Эпик 1", "Нужно сделать");
        taskTracker.createEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask1 создания ",
                "Написать что то ", epic1.getId());
        taskTracker.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Subtask2 создания ",
                "Написать что то ", epic1.getId());
        taskTracker.createSubtask(subtask2);
        //проверка истории
        taskTracker.getEpicById(1);
        taskTracker.getSubtaskById(2);
        taskTracker.getSubtaskById(3);
        printAllTasks(taskTracker);



        System.out.println(epic1);
        subtask1.setTaskStatus(TaskStatus.IN_PROGRESS);
        taskTracker.updateSubtasks(subtask1);
        System.out.println(epic1);
        subtask2.setTaskStatus(TaskStatus.DONE);
        taskTracker.updateSubtasks(subtask2);
        System.out.println(epic1);
        subtask1.setTaskStatus(TaskStatus.DONE);
        taskTracker.updateSubtasks(subtask1);
        System.out.println(epic1);


    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getAllEpics()) {
            System.out.println(epic);

            for (Task task : manager.getEpicTasks(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
