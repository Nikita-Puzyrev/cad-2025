package ru.belgu.cad.lab5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.belgu.cad.lab5.domain.Priority;
import ru.belgu.cad.lab5.domain.Task;
import ru.belgu.cad.lab5.repository.TaskRepository;
import ru.belgu.cad.lab5.service.TaskService;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Проверки веб-слоя и разграничения прав. */
@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskService service;

    @Autowired
    private TaskRepository tasks;

    @Test
    @DisplayName("Список задач отображается для аутентифицированного пользователя")
    @WithMockUser(username = "user", roles = "USER")
    void listIsRendered() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Список задач")))
                .andExpect(content().string(containsString("Новая задача")));
    }

    @Test
    @DisplayName("Анонимный пользователь перенаправляется на форму входа")
    void anonymousRedirected() throws Exception {
        mockMvc.perform(get("/").accept(org.springframework.http.MediaType.TEXT_HTML))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("USER добавляет задачу через форму")
    @WithMockUser(username = "user", roles = "USER")
    void userAddsTask() throws Exception {
        long before = tasks.count();
        mockMvc.perform(post("/addTask")
                        .param("title", "Задача из теста")
                        .param("description", "описание")
                        .param("category", "Тест")
                        .param("priority", "HIGH")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
        assertTrue(tasks.count() > before);
    }

    @Test
    @DisplayName("GUEST не может добавлять задачи")
    @WithMockUser(username = "guest", roles = "GUEST")
    void guestCannotAdd() throws Exception {
        mockMvc.perform(post("/addTask").param("title", "нельзя").with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Удаление задачи запрещено пользователю и разрешено модератору")
    void deleteRights() throws Exception {
        Task task = service.add(new Task("Задача для удаления", "", "Тест", Priority.LOW),
                "user");

        mockMvc.perform(get("/deleteTask/" + task.getId()).with(user("user").roles("USER")))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/deleteTask/" + task.getId())
                        .with(user("moderator").roles("MODERATOR")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
        assertTrue(tasks.findById(task.getId()).isEmpty());
    }

    @Test
    @DisplayName("Страница истории доступна только модератору и администратору")
    void historyRights() throws Exception {
        mockMvc.perform(get("/history").with(user("moderator").roles("MODERATOR")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("История входов")));

        mockMvc.perform(get("/history").with(user("user").roles("USER")))
                .andExpect(status().isForbidden());
    }
}
