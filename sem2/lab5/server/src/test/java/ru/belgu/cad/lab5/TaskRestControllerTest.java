package ru.belgu.cad.lab5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.belgu.cad.lab5.domain.Priority;
import ru.belgu.cad.lab5.domain.Task;
import ru.belgu.cad.lab5.repository.TaskRepository;
import ru.belgu.cad.lab5.service.TaskService;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Проверки REST-интерфейса, используемого клиентским приложением. */
@SpringBootTest
@AutoConfigureMockMvc
class TaskRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskService service;

    @Autowired
    private TaskRepository tasks;

    @Test
    @DisplayName("Список задач возвращается с дополнительными полями")
    void listReturnsExtraFields() throws Exception {
        mockMvc.perform(get("/api/tasks").with(httpBasic("user", "password")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").exists())
                .andExpect(jsonPath("$[0].priority").exists())
                .andExpect(jsonPath("$[0].category").exists())
                .andExpect(jsonPath("$[0].createdAt").exists())
                .andExpect(jsonPath("$[0].author").exists())
                .andExpect(jsonPath("$[0].subTasks").isArray());
    }

    @Test
    @DisplayName("Без аутентификации REST вернёт 401")
    void unauthorized() throws Exception {
        mockMvc.perform(get("/api/tasks").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Сведения о текущем пользователе содержат роли")
    void meReturnsRoles() throws Exception {
        mockMvc.perform(get("/api/me").with(httpBasic("moderator", "password")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("moderator"))
                .andExpect(jsonPath("$.roles[0]").value("ROLE_MODERATOR"));
    }

    @Test
    @DisplayName("Сортировка по приоритету выполняется на сервере")
    void sortByPriority() throws Exception {
        service.add(new Task("Сортировка LOW", "", "Сорт", Priority.LOW), "user");
        service.add(new Task("Сортировка HIGH", "", "Сорт", Priority.HIGH), "user");

        mockMvc.perform(get("/api/tasks")
                        .param("sort", "priority")
                        .param("direction", "desc")
                        .with(httpBasic("user", "password")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].priority").value("HIGH"));
    }

    @Test
    @DisplayName("Задача создаётся клиентом и сохраняется в базе")
    void createFromClient() throws Exception {
        long before = tasks.count();
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Задача из клиента\","
                                + "\"description\":\"создана по REST\","
                                + "\"category\":\"Клиент\",\"priority\":\"HIGH\"}")
                        .with(httpBasic("user", "password")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.author").value("user"));
        assertEquals(before + 1, tasks.count());
        assertEquals(1, tasks.findAll().stream()
                .filter(t -> "Задача из клиента".equals(t.getTitle()))
                .count());
    }

    @Test
    @DisplayName("GUEST не может создавать задачи")
    void guestCannotCreate() throws Exception {
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"нельзя\"}")
                        .with(httpBasic("guest", "password")))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Удаление через REST разрешено только модератору и администратору")
    void deleteRights() throws Exception {
        Task task = service.add(new Task("Удаление по REST", "", "Клиент",
                Priority.NORMAL), "user");

        mockMvc.perform(delete("/api/tasks/" + task.getId())
                        .with(httpBasic("user", "password")))
                .andExpect(status().isForbidden());

        mockMvc.perform(delete("/api/tasks/" + task.getId())
                        .with(httpBasic("moderator", "password")))
                .andExpect(status().isNoContent());
        assertTrue(tasks.findById(task.getId()).isEmpty());
    }

    @Test
    @DisplayName("Несуществующая задача возвращает 404")
    @WithMockUser(username = "user", roles = "USER")
    void notFound() throws Exception {
        mockMvc.perform(get("/api/tasks/999999"))
                .andExpect(status().isNotFound());
    }
}
