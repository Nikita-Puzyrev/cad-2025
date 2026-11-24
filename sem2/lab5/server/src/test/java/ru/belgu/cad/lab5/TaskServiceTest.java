package ru.belgu.cad.lab5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.belgu.cad.lab5.domain.Priority;
import ru.belgu.cad.lab5.domain.Task;
import ru.belgu.cad.lab5.repository.TaskRepository;
import ru.belgu.cad.lab5.service.TaskService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Проверки слоя доступа к данным и бизнес-логики. */
@SpringBootTest
@Transactional
class TaskServiceTest {

    @Autowired
    private TaskService service;

    @Autowired
    private TaskRepository tasks;

    @Test
    @DisplayName("Задача сохраняется в базу вместе с записью истории")
    void addTaskSavesHistory() {
        long before = service.count();
        Task task = new Task("Проверка сохранения", "Описание", "Тест", Priority.HIGH);
        service.add(task, "user");

        assertEquals(before + 1, service.count());
        assertNotNull(task.getId());
        assertEquals("user", task.getAuthor().getUsername());
        assertTrue(service.taskHistory().stream()
                .anyMatch(h -> h.getNewState().contains("Проверка сохранения")));
    }

    @Test
    @DisplayName("Изменение статуса фиксирует старое и новое состояние")
    void statusChangeWritesHistory() {
        Task task = service.add(new Task("Сменить статус", "", "Тест", Priority.NORMAL),
                "user");
        service.changeStatus(task.getId(), true);

        Task saved = tasks.findById(task.getId()).orElseThrow();
        assertTrue(saved.isCompleted());
        assertTrue(service.taskHistory().stream()
                .anyMatch(h -> "Сменить статус".equals(saved.getTitle())
                        && h.getOldState() != null
                        && h.getOldState().contains("completed=false")
                        && h.getNewState().contains("completed=true")));
    }

    @Test
    @DisplayName("Подзадача добавляется и переключается")
    void subTasksWork() {
        Task task = service.add(new Task("Задача с подзадачей", "", "Тест", Priority.LOW),
                "moderator");
        service.addSubTask(task.getId(), "первая подзадача");

        Task loaded = tasks.findAllWithSubTasks().stream()
                .filter(t -> t.getId().equals(task.getId()))
                .findFirst().orElseThrow();
        assertEquals(1, loaded.getSubTasks().size());
        assertFalse(loaded.getSubTasks().get(0).isCompleted());

        service.toggleSubTask(loaded.getSubTasks().get(0).getId());
        Task again = tasks.findAllWithSubTasks().stream()
                .filter(t -> t.getId().equals(task.getId()))
                .findFirst().orElseThrow();
        assertTrue(again.getSubTasks().get(0).isCompleted());
    }

    @Test
    @DisplayName("Фильтрация по категории и приоритету")
    void filtersWork() {
        service.add(new Task("Фильтр HIGH", "", "Фильтр", Priority.HIGH), "user");
        service.add(new Task("Фильтр LOW", "", "Фильтр", Priority.LOW), "user");

        List<Task> high = service.find("Фильтр", Priority.HIGH, null);
        assertEquals(1, high.size());
        assertEquals("Фильтр HIGH", high.get(0).getTitle());
        assertEquals(2, service.find("Фильтр", null, null).size());
    }

    @Test
    @DisplayName("Вход пользователя попадает в историю входов")
    void loginIsRecorded() {
        service.logLogin("admin", "127.0.0.1");
        assertTrue(service.loginHistory().stream()
                .anyMatch(h -> "admin".equals(h.getUser().getUsername())
                        && "127.0.0.1".equals(h.getIpAddress())));
    }

    @Test
    @DisplayName("Удаление задачи убирает её записи истории")
    void deleteRemovesTaskAndHistory() {
        Task task = service.add(new Task("Удалить меня", "", "Тест", Priority.NORMAL), "user");
        Long id = task.getId();
        service.delete(id);

        assertTrue(tasks.findById(id).isEmpty());
        assertTrue(service.taskHistory().stream()
                .noneMatch(h -> h.getTask().getId().equals(id)));
    }
}
