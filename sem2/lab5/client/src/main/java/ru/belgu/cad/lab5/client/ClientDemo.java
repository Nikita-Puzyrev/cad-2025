package ru.belgu.cad.lab5.client;

import ru.belgu.cad.lab5.client.model.TaskItem;
import ru.belgu.cad.lab5.client.model.UserInfo;

import java.util.List;

/**
 * Консольная демонстрация работы клиента (без графического интерфейса):
 * вход, вывод задач с данными из базы, добавление, сортировка и попытка
 * удаления. Используется для проверки обмена с сервером.
 *
 * Запуск: java -cp "target/classes;target/lib/*" ru.belgu.cad.lab5.client.ClientDemo [сервер] [логин] [пароль]
 */
public class ClientDemo {

    public static void main(String[] args) throws Exception {
        String server = args.length > 0 ? args[0] : "http://localhost:8080";
        String login = args.length > 1 ? args[1] : "user";
        String password = args.length > 2 ? args[2] : "password";

        ApiClient api = new ApiClient(server);
        UserInfo user = api.login(login, password);
        System.out.printf("Вход выполнен: %s, роли %s%n", user.username(), user.roles());
        System.out.printf("Права: добавление — %s, удаление — %s%n",
                user.canCreate(), user.canDelete());

        printList("Список задач (по времени создания)", api.list("created", "desc"));
        printList("Сортировка по приоритету (по убыванию)", api.list("priority", "desc"));

        TaskItem created = api.create("Задача из Swing-клиента",
                "создана через REST из клиентского приложения", "Клиент", "HIGH");
        System.out.printf("Добавлена задача №%d: %s (приоритет %s, автор %s)%n",
                created.id(), created.title(), created.priority(), created.author());

        printList("Список после добавления", api.list("created", "desc"));

        try {
            api.delete(created.id());
            System.out.println("Удаление выполнено");
        } catch (ApiClient.ApiException ex) {
            System.out.println("Удаление запрещено: " + ex.getMessage());
        }
    }

    private static void printList(String caption, List<TaskItem> tasks) {
        System.out.println("\n=== " + caption + " (" + tasks.size() + " шт.)");
        System.out.printf("%-4s %-38s %-10s %-10s %-17s %-9s %s%n",
                "№", "Название", "Категория", "Приоритет", "Создана", "Автор", "Подзадачи");
        for (TaskItem task : tasks) {
            System.out.printf("%-4d %-38s %-10s %-10s %-17s %-9s %s%n",
                    task.id(), cut(task.title(), 38), cut(task.category(), 10),
                    task.priority(), task.createdAt(), task.author(),
                    cut(task.subTasksAsText(), 40));
        }
    }

    private static String cut(String value, int length) {
        if (value == null) {
            return "—";
        }
        return value.length() <= length ? value : value.substring(0, length - 1) + "…";
    }
}
