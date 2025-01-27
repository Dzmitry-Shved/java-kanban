import managers.*;
import tasks.*;


public class Main {

    public static void main(String[] args) {

        sprint6Test();

    }

    public static void practicumTest() {
        TaskManager tm = new InMemoryTaskManager();

        Task task1 = new Task("Задача1", "Описание задачи1", Status.NEW);
        Task task2 = new Task("Задача2", "Описание задачи2", Status.NEW);

        tm.addTask(task1);
        tm.addTask(task2);

        Epic epic1 = new Epic("Эпик1", "Описание эпика1");
        SubTask subTask1 = new SubTask("Субтаска1", "Описание субтаски1", Status.NEW, epic1);
        SubTask subTask2 = new SubTask("Субтаска2", "Описание субтаски2", Status.NEW, epic1);
        epic1.getSubTasks().add(subTask1);
        epic1.getSubTasks().add(subTask2);

        tm.addEpic(epic1);

        Epic epic2 = new Epic("Эпик2", "Описание эпика2");
        SubTask subTask3 = new SubTask("Субтаска3", "Описание субтаски3", epic2);
        subTask3.setStatus(Status.NEW);
        epic2.getSubTasks().add(subTask3);

        tm.addEpic(epic2);

        System.out.println(tm.getAllTasks());
        System.out.println(tm.getAllSubTasks());
        System.out.println(tm.getAllEpics());

        System.out.println("-".repeat(100));

        Task updatedTask1 = new Task("Обновленная задача1", "Обновленное описание задачи1");
        updatedTask1.setStatus(Status.DONE);
        updatedTask1.setId(1);
        Task updatedTask2 = new Task("Обновленная задача2", "Обновленное описание задачи2");
        updatedTask2.setStatus(Status.DONE);
        updatedTask2.setId(2);

        tm.updateTask(updatedTask1);
        tm.updateTask(updatedTask2);

        Epic existingEpic1 = new Epic("Эпик1", "Описание эпика1");
        existingEpic1.setId(3);
        SubTask updatedSubTask1 = new SubTask("Субтаска1", "Описание субтаски1", existingEpic1);
        updatedSubTask1.setStatus(Status.DONE);
        updatedSubTask1.setId(4);
        SubTask updatedSubTask2 = new SubTask("Субтаска2", "Описание субтаски2", existingEpic1);
        updatedSubTask2.setStatus(Status.NEW);
        updatedSubTask2.setId(5);

        tm.updateSubTask(updatedSubTask1);
        tm.updateSubTask(updatedSubTask2);

        Epic existingEpic2 = new Epic("Эпик2", "Описание эпика2");
        existingEpic2.setId(6);
        SubTask updatedSubTask3 = new SubTask("Субтаска3", "Описание субтаски3", existingEpic2);
        updatedSubTask3.setStatus(Status.DONE);
        updatedSubTask3.setId(7);

        tm.updateSubTask(updatedSubTask3);

        System.out.println("Task1 после обновления:" + tm.getTask(1));
        System.out.println("Task2 после обновления:" + tm.getTask(2));
        System.out.println("Epic1 после обновления субтасков:" + tm.getEpic(3));
        System.out.println("SubTask1 после обновления:" + tm.getSubTask(4));
        System.out.println("SubTask2 после обновления:" + tm.getSubTask(5));
        System.out.println("Epic2 после обновления субтасков::" + tm.getEpic(6));
        System.out.println("SubTask3 после обновления:" + tm.getSubTask(7));

        System.out.println("-".repeat(100));

        tm.deleteTask(1);
        tm.deleteEpic(3);

        System.out.println(tm.getAllTasks());
        System.out.println(tm.getAllSubTasks());
        System.out.println(tm.getAllEpics());
    }

    public static void sprint5Test() {
        TaskManager tm = Managers.getDefault();

        Task task1 = new Task("Задача1", "Описание задачи1", Status.NEW);
        tm.addTask(task1);
        Task task2 = new Task("Задача2", "Описание задачи2", Status.NEW);
        tm.addTask(task2);
        Task task3 = new Task("Задача3", "Описание задачи3", Status.NEW);
        tm.addTask(task3);
        Task task4 = new Task("Задача4", "Описание задачи4", Status.NEW);
        tm.addTask(task4);
        Task task5 = new Task("Задача5", "Описание задачи5", Status.NEW);
        tm.addTask(task5);

        Epic epic1 = new Epic("Эпик1", "Описание эпика1");
        SubTask subTask1 = new SubTask("Субтаска1", "Описание субтаски1", Status.NEW, epic1);
        SubTask subTask2 = new SubTask("Субтаска2", "Описание субтаски2", Status.NEW, epic1);
        epic1.getSubTasks().add(subTask1);
        epic1.getSubTasks().add(subTask2);
        tm.addEpic(epic1);

        Epic epic2 = new Epic("Эпик2", "Описание эпика2");
        SubTask subTask3 = new SubTask("Субтаска3", "Описание субтаски3", Status.NEW, epic2);
        epic2.getSubTasks().add(subTask3);
        tm.addEpic(epic2);

        Task task6 = new Task("Задача6", "Описание задачи6", Status.NEW);
        tm.addTask(task6);

        int id = 1;
        System.out.printf("Вызван tasks.Task с id=%d:\n%s\n", id, tm.getTask(id));
        System.out.printf("История просмотров:\n%s\n", tm.getHistory());
        id = 11;
        System.out.printf("Вызван tasks.Task с id=%d:\n%s\n", id, tm.getTask(id));
        System.out.printf("История просмотров:\n%s\n", tm.getHistory());
        id = 6;
        System.out.printf("Вызван tasks.Epic с id=%d:\n%s\n", id, tm.getEpic(id));
        System.out.printf("История просмотров:\n%s\n", tm.getHistory());
        id = 8;
        System.out.printf("Вызван tasks.SubTask с id=%d:\n%s\n", id, tm.getSubTask(id));
        System.out.printf("История просмотров:\n%s\n", tm.getHistory());

        System.out.println("-".repeat(100));

        id = 3;
        for (int i = 1; i <= 6; i++) {
            System.out.printf("Вызван tasks.Task с id=%d:\n%s\n", id, tm.getTask(id));
        }
        System.out.printf("История просмотров:\n%s\n", tm.getHistory());

        id = 5;
        System.out.printf("Вызван tasks.Task с id=%d:\n%s\n", id, tm.getTask(id));
        System.out.printf("История просмотров:\n%s\n", tm.getHistory());

    }

    public static void sprint6Test() {
        TaskManager tm = Managers.getDefault();

        Task task1 = new Task("Задача1", "Описание задачи1", Status.NEW);
        tm.addTask(task1);
        Task task2 = new Task("Задача2", "Описание задачи2", Status.NEW);
        tm.addTask(task2);
        Epic epic1 = new Epic("Эпик1", "Описание эпика1");
        SubTask subTask1 = new SubTask("Субтаска1", "Описание субтаски1", Status.NEW, epic1);
        SubTask subTask2 = new SubTask("Субтаска2", "Описание субтаски2", Status.NEW, epic1);
        SubTask subTask3 = new SubTask("Субтаска3", "Описание субтаски3", Status.NEW, epic1);
        epic1.getSubTasks().add(subTask1);
        epic1.getSubTasks().add(subTask2);
        epic1.getSubTasks().add(subTask3);
        tm.addEpic(epic1);
        Epic epic2 = new Epic("Эпик2", "Описание эпика2");
        tm.addEpic(epic2);

        System.out.printf("Вызван tasks.Task с id=%d:\n%s\n", 1, tm.getTask(1));
        System.out.printf("Вызван tasks.Task с id=%d:\n%s\n", 2, tm.getTask(2));
        System.out.printf("История просмотров (%d):\n%s\n", tm.getHistory().size(), tm.getHistory());
        System.out.printf("Вызван tasks.Epic с id=%d:\n%s\n", 6, tm.getEpic(3));
        System.out.printf("История просмотров (%d):\n%s\n", tm.getHistory().size(), tm.getHistory());
        System.out.printf("Вызван tasks.Task с id=%d:\n%s\n", 1, tm.getTask(1));
        System.out.printf("История просмотров (%d):\n%s\n", tm.getHistory().size(), tm.getHistory());
        System.out.printf("Вызван tasks.Subtask с id=%d:\n%s\n", 4, tm.getSubTask(4));
        System.out.printf("Вызван tasks.Subtask с id=%d:\n%s\n", 5, tm.getSubTask(5));
        System.out.printf("История просмотров (%d):\n%s\n", tm.getHistory().size(), tm.getHistory());
        System.out.printf("Вызван tasks.Task с id=%d:\n%s\n", 1, tm.getTask(1));
        System.out.printf("История просмотров (%d):\n%s\n", tm.getHistory().size(), tm.getHistory());
        tm.deleteTask(2);
        System.out.printf("История просмотров (%d):\n%s\n", tm.getHistory().size(), tm.getHistory());
        tm.deleteEpic(3);
        System.out.printf("История просмотров (%d):\n%s\n", tm.getHistory().size(), tm.getHistory());

    }

}
