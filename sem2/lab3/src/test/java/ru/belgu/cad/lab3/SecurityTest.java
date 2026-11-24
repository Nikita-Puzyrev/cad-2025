package ru.belgu.cad.lab3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Проверки разграничения прав. Запросы на изменение данных передают CSRF-токен,
 * поскольку защита от межсайтовой подделки запроса включена по умолчанию.
 */
@SpringBootTest
@AutoConfigureMockMvc
class SecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Анонимный пользователь перенаправляется на форму входа")
    void anonymousRedirectedToLogin() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @DisplayName("GUEST видит список задач, но не может добавлять")
    @WithMockUser(username = "guest", roles = "GUEST")
    void guestCanOnlyView() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk());
        mockMvc.perform(post("/addTask").param("title", "задача гостя")
                        .param("category", "Работа").with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("USER добавляет задачи, но не удаляет их")
    @WithMockUser(username = "user", roles = "USER")
    void userCannotDelete() throws Exception {
        mockMvc.perform(post("/addTask").param("title", "задача пользователя")
                        .param("description", "описание").param("category", "Учеба")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
        mockMvc.perform(get("/deleteTask/1")).andExpect(status().isForbidden());
        mockMvc.perform(post("/updateTask/1").param("completed", "true").with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("MODERATOR изменяет статус и удаляет задачи")
    @WithMockUser(username = "moderator", roles = "MODERATOR")
    void moderatorManagesTasks() throws Exception {
        mockMvc.perform(post("/addTask").param("title", "задача модератора")
                        .param("description", "").param("category", "Работа").with(csrf()))
                .andExpect(status().is3xxRedirection());
        mockMvc.perform(post("/updateTask/1").param("completed", "true").with(csrf()))
                .andExpect(status().is3xxRedirection());
        mockMvc.perform(get("/deleteTask/1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("ADMIN управляет категориями, USER — нет")
    void onlyAdminManagesCategories() throws Exception {
        mockMvc.perform(post("/addCategory").param("name", "Спорт").with(csrf())
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(post("/addCategory").param("name", "Отдых").with(csrf())
                        .with(user("user").roles("USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Страница входа доступна без аутентификации")
    void loginPageIsPublic() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Sign in")));
    }
}
