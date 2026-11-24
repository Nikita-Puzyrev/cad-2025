package ru.belgu.cad.lab4.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.belgu.cad.lab4.domain.AppUser;
import ru.belgu.cad.lab4.domain.Priority;
import ru.belgu.cad.lab4.domain.SubTask;
import ru.belgu.cad.lab4.domain.Task;
import ru.belgu.cad.lab4.domain.TaskHistory;
import ru.belgu.cad.lab4.repository.AppUserRepository;
import ru.belgu.cad.lab4.repository.TaskHistoryRepository;
import ru.belgu.cad.lab4.repository.TaskRepository;

import java.util.List;

/**
 * Начальное заполнение базы: учётные записи четырёх ролей и демонстрационные
 * задачи с подзадачами. Выполняется только при пустой таблице пользователей.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final AppUserRepository users;
    private final TaskRepository tasks;
    private final TaskHistoryRepository taskHistory;
    private final PasswordEncoder encoder;

    public DataInitializer(AppUserRepository users, TaskRepository tasks,
                           TaskHistoryRepository taskHistory, PasswordEncoder encoder) {
        this.users = users;
        this.tasks = tasks;
        this.taskHistory = taskHistory;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        if (users.count() > 0) {
            return;
        }
        AppUser admin = user("Администратор", "admin@todo.local", "admin", "ADMIN");
        user("Модератор", "moderator@todo.local", "moderator", "MODERATOR");
        AppUser user = user("Пользователь", "user@todo.local", "user", "USER");
        user("Гость", "guest@todo.local", "guest", "GUEST");

        Task first = new Task("Сдать лабораторную работу №4",
                "Подключить PostgreSQL, вынести задачи в базу данных",
                "Учеба", Priority.HIGH);
        first.setAuthor(user);
        first.addSubTask(new SubTask("Создать базу данных"));
        first.addSubTask(new SubTask("Настроить application.properties"));
        first.addSubTask(new SubTask("Проверить сохранение задач"));
        tasks.save(first);
        taskHistory.save(new TaskHistory(first, null, first.state()));

        Task second = new Task("Проверить работу подзадач",
                "Добавить подзадачу и отметить её выполненной",
                "Работа", Priority.NORMAL);
        second.setAuthor(admin);
        tasks.save(second);
        taskHistory.save(new TaskHistory(second, null, second.state()));

        Task third = new Task("Подготовить отчёт",
                "Описать структуру таблиц и результаты работы",
                "Учеба", Priority.LOW);
        third.setAuthor(user);
        third.setCompleted(true);
        tasks.save(third);
        taskHistory.save(new TaskHistory(third, null, third.state()));

        System.out.printf("База заполнена: пользователей %d, задач %d%n",
                users.count(), tasks.count());
        List<Task> all = tasks.findAll();
        System.out.println("Задачи: " + all.stream().map(Task::getTitle).toList());
    }

    private AppUser user(String name, String email, String username, String role) {
        AppUser user = new AppUser(name, email, username,
                encoder.encode("password"), role);
        return users.save(user);
    }
}
