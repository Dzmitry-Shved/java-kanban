import java.util.List;

public class Main {

    public static void main(String[] args) {

        practicumTest();

    }

    public static void practicumTest() {
        TaskManager tm = new TaskManager();

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

    public static void testTasks() {
        TaskManager tm = new TaskManager();

        Task task1 = new Task("Задача1", "Описание задачи1");
        task1.setStatus(Status.NEW);
        Task task2 = new Task("Задача2", "Описание задачи2");
        task2.setStatus(Status.NEW);
        Task task3 = new Task("Задача3", "Описание задачи3");
        task3.setStatus(Status.NEW);
        tm.addTask(task1);
        tm.addTask(task2);
        tm.addTask(task3);

        System.out.println(tm.getTask(1));
        System.out.println(tm.getTask(2));
        System.out.println(tm.getTask(3));
        System.out.println(tm.getAllTasks());

        tm.deleteAllTasks();

        Task task4 = new Task("Задача4", "Описание задачи4");
        task4.setStatus(Status.NEW);
        Task task5 = new Task("Задача5", "Описание задачи5");
        task5.setStatus(Status.NEW);
        Task task6 = new Task("Задача6", "Описание задачи6");
        task6.setStatus(Status.NEW);
        Task task7 = new Task("Задача7", "Описание задачи7");
        task7.setStatus(Status.NEW);
        tm.addTask(task4);
        tm.addTask(task5);
        tm.addTask(task6);
        tm.addTask(task7);

        System.out.println(tm.getTask(4));
        System.out.println(tm.getTask(5));
        System.out.println(tm.getTask(6));
        System.out.println(tm.getTask(7));
        System.out.println(tm.getAllTasks());

        Task updatedTask4 = new Task("Задача4 Обновленная", "Описание задачи4 Обновлено");
        updatedTask4.setId(4);
        updatedTask4.setStatus(Status.DONE);
        Task updatedTask5 = new Task("Задача5 Обновленная", "Описание задачи5 Обновлено");
        updatedTask5.setId(5);
        updatedTask5.setStatus(Status.DONE);

        tm.updateTask(updatedTask4);
        tm.updateTask(updatedTask5);

        System.out.println(tm.getTask(4));
        System.out.println(tm.getTask(5));
        System.out.println(tm.getAllTasks());

        tm.deleteAllTasks();
        System.out.println(tm.getAllTasks());
    }

    public static void testSubTasks() {
        TaskManager tm = new TaskManager();

        Epic epic1 = new Epic("Эпик1", "Описание эпика1");
        Epic epic2 = new Epic("Эпик2", "Описание эпика2");
        Epic epic3 = new Epic("Эпик3", "Описание эпика2");
        tm.addEpic(epic1);
        tm.addEpic(epic2);
        tm.addEpic(epic3);

        System.out.println(tm.getEpic(1));
        System.out.println(tm.getEpic(2));
        System.out.println(tm.getEpic(3));
        System.out.println(tm.getAllEpics());

        //понимаю что субтаска создается исключительно в рамках существующего в репозитории эпика и знает о нем
        Epic existingEpic1 = new Epic("Эпик1", "Описание эпика1");
        existingEpic1.setId(1);

        SubTask subTask1 = new SubTask("Субтаска1", "Описание субтаски1", existingEpic1);
        subTask1.setStatus(Status.NEW);
        SubTask subTask2 = new SubTask("Субтаска2", "Описание субтаски2", existingEpic1);
        subTask2.setStatus(Status.NEW);
        SubTask subTask3 = new SubTask("Субтаска3", "Описание субтаски3", existingEpic1);
        subTask3.setStatus(Status.NEW);
        tm.addSubTask(subTask1);
        tm.addSubTask(subTask2);
        tm.addSubTask(subTask3);

        List<SubTask> subTasks = tm.getAllSubTasks();
        for (SubTask subTask : subTasks) {
            System.out.println(subTask);
        }

        System.out.println(tm.getEpic(1));
        System.out.println(tm.getSubTask(5));
        System.out.println(tm.getAllSubTasks(existingEpic1));

        //понимаю что субтаска обновляется исключительно в рамках существующего в репозитории эпика и знает о нем
        Epic anotherExistingEpic1 = new Epic("Эпик1", "Описание эпика1");
        anotherExistingEpic1.setId(1);
        SubTask updatedSubTask2 = new SubTask("Обновленная Субтаска2", "Обновленное Описание субтаски2",
                anotherExistingEpic1);
        updatedSubTask2.setId(5);
        updatedSubTask2.setStatus(Status.DONE);
        tm.updateSubTask(updatedSubTask2);

        System.out.println(tm.getSubTask(5));
        System.out.println(tm.getEpic(1));

        tm.deleteSubTask(4);
        tm.deleteSubTask(6);

        System.out.println(tm.getEpic(1));
        for (SubTask subTask : tm.getAllSubTasks()) {
            System.out.println(subTask);
        }


        Epic existingEpic3 = new Epic("Эпик1", "Описание эпика1");
        existingEpic3.setId(3);
        SubTask subTask4 = new SubTask("Субтаска4", "Описание субтаски4", existingEpic3);
        subTask4.setStatus(Status.NEW);
        SubTask subTask5 = new SubTask("Субтаска5", "Описание субтаски5", existingEpic3);
        subTask5.setStatus(Status.DONE);
        tm.addSubTask(subTask4);
        tm.addSubTask(subTask5);

        System.out.println(tm.getAllSubTasks());
        System.out.println(tm.getEpic(3));


        tm.deleteAllSubTasks();
        System.out.println(tm.getAllSubTasks());
        System.out.println(tm.getEpic(1));
        System.out.println(tm.getEpic(3));

    }
}
