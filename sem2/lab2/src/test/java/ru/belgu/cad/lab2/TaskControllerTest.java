package ru.belgu.cad.lab2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Главная страница отображается")
    void indexRenders() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("ToDo List")))
                .andExpect(content().string(containsString("Учеба")));
    }

    @Test
    @DisplayName("Добавление задачи и её отображение в списке")
    void addTask() throws Exception {
        mockMvc.perform(post("/addTask")
                        .param("title", "Подготовить отчёт по лабораторной")
                        .param("description", "Раздел 3")
                        .param("category", "Учеба"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        mockMvc.perform(get("/"))
                .andExpect(content().string(
                        containsString("Подготовить отчёт по лабораторной")));
    }

    @Test
    @DisplayName("Фильтрация задач по категории")
    void filterByCategory() throws Exception {
        mockMvc.perform(post("/addTask").param("title", "Купить корм")
                .param("description", "").param("category", "Личные дела"));
        mockMvc.perform(post("/addTask").param("title", "Сдать отчёт")
                .param("description", "").param("category", "Работа"));

        mockMvc.perform(get("/").param("category", "Личные дела"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Купить корм")))
                .andExpect(content().string(not(containsString("Сдать отчёт"))));
    }

    @Test
    @DisplayName("Добавление новой категории")
    void addCategory() throws Exception {
        mockMvc.perform(post("/addCategory").param("name", "Спорт"))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(get("/"))
                .andExpect(content().string(containsString("Спорт")));
    }

    @Test
    @DisplayName("Изменение статуса и удаление задачи")
    void updateAndDelete() throws Exception {
        mockMvc.perform(post("/addTask").param("title", "Временная задача")
                .param("description", "").param("category", "Работа"));

        Task created = task("Временная задача");
        mockMvc.perform(post("/updateTask/" + created.getId()).param("completed", "true"))
                .andExpect(status().is3xxRedirection());
        mockMvc.perform(get("/"))
                .andExpect(content().string(containsString("выполнена")));

        mockMvc.perform(get("/deleteTask/" + created.getId()))
                .andExpect(status().is3xxRedirection());
        mockMvc.perform(get("/"))
                .andExpect(content().string(not(containsString("Временная задача"))));
    }

    @Autowired
    private TaskService taskService;

    private Task task(String title) {
        return taskService.getAllTasks().stream()
                .filter(t -> title.equals(t.getTitle()))
                .reduce((first, second) -> second)
                .orElseThrow();
    }
}
