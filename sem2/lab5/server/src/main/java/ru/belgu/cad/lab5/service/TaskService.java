package ru.belgu.cad.lab5.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.belgu.cad.lab5.domain.AppUser;
import ru.belgu.cad.lab5.domain.LoginHistory;
import ru.belgu.cad.lab5.domain.Priority;
import ru.belgu.cad.lab5.domain.SubTask;
import ru.belgu.cad.lab5.domain.Task;
import ru.belgu.cad.lab5.domain.TaskHistory;
import ru.belgu.cad.lab5.repository.AppUserRepository;
import ru.belgu.cad.lab5.repository.LoginHistoryRepository;
import ru.belgu.cad.lab5.repository.SubTaskRepository;
import ru.belgu.cad.lab5.repository.TaskHistoryRepository;
import ru.belgu.cad.lab5.repository.TaskRepository;

import java.util.Comparator;
import java.util.List;

/**
 * Бизнес-логика работы с задачами: хранение в базе данных через Spring Data JPA,
 * ведение истории изменений, подзадачи и история входов.
 */
@Service
@Transactional(readOnly = true)
public class TaskService {

    private final TaskRepository tasks;
    private final SubTaskRepository subTasks;
    private final TaskHistoryRepository taskHistory;
    private final LoginHistoryRepository loginHistory;
    private final AppUserRepository users;

    public TaskService(TaskRepository tasks, SubTaskRepository subTasks,
                       TaskHistoryRepository taskHistory,
                       LoginHistoryRepository loginHistory,
                       AppUserRepository users) {
        this.tasks = tasks;
        this.subTasks = subTasks;
        this.taskHistory = taskHistory;
        this.loginHistory = loginHistory;
        this.users = users;
    }

    public List<Task> find(String category, Priority priority, Boolean completed) {
        return tasks.findAllWithSubTasks().stream()
                .filter(t -> category == null || category.isBlank()
                        || category.equals(t.getCategory()))
                .filter(t -> priority == null || priority == t.getPriority())
                .filter(t -> completed == null || completed == t.isCompleted())
                .sorted(Comparator.comparing(Task::getCreatedAt).reversed())
                .toList();
    }

    public List<String> categories() {
        return tasks.findAll().stream()
                .map(Task::getCategory)
                .filter(c -> c != null && !c.isBlank())
                .distinct()
                .sorted()
                .toList();
    }

    public long count() {
        return tasks.count();
    }

    public long countCompleted() {
        return tasks.countByCompleted(true);
    }

    @Transactional
    public Task add(Task task, String username) {
        users.findByUsername(username).ifPresent(task::setAuthor);
        Task saved = tasks.save(task);
        taskHistory.save(new TaskHistory(saved, null, saved.state()));
        return saved;
    }

    @Transactional
    public void changeStatus(Long id, boolean completed) {
        Task task = tasks.findById(id).orElseThrow();
        String oldState = task.state();
        task.setCompleted(completed);
        Task saved = tasks.save(task);
        taskHistory.save(new TaskHistory(saved, oldState, saved.state()));
    }

    @Transactional
    public void delete(Long id) {
        taskHistory.deleteByTaskId(id);
        tasks.deleteById(id);
    }

    @Transactional
    public void addSubTask(Long taskId, String description) {
        Task task = tasks.findById(taskId).orElseThrow();
        String oldState = task.state();
        task.addSubTask(new SubTask(description));
        Task saved = tasks.save(task);
        taskHistory.save(new TaskHistory(saved, oldState, oldState + " + подзадача"));
    }

    @Transactional
    public void toggleSubTask(Long subTaskId) {
        SubTask subTask = subTasks.findById(subTaskId).orElseThrow();
        subTask.setCompleted(!subTask.isCompleted());
        subTasks.save(subTask);
    }

    public List<TaskHistory> taskHistory() {
        return taskHistory.findTop30ByOrderByChangedAtDesc();
    }

    public List<LoginHistory> loginHistory() {
        return loginHistory.findTop20ByOrderByLoginTimeDesc();
    }

    /** Регистрация входа пользователя в историю. */
    @Transactional
    public void logLogin(String username, String ip) {
        users.findByUsername(username)
                .ifPresent(user -> loginHistory.save(new LoginHistory(user, ip)));
    }

    public List<AppUser> allUsers() {
        return users.findAll();
    }
}
