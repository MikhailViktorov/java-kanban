import Managers.TaskManager;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.TaskStatus;

/**
 * Created by Mikhail Viktorov on 24.12.2023
 * the-rayn@yandex.ru
 */
public class Main {
    public static void main(String[] args) {

        TaskManager taskTracker = new TaskManager();
        Epic epic1 = new Epic("Эпик 1","Нужно сделать");
        taskTracker.createEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask1 создания ",
                "Написать что то ", epic1.getId());
        taskTracker.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Subtask2 создания ",
                "Написать что то ", epic1.getId());
        taskTracker.createSubtask(subtask2);
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
}
