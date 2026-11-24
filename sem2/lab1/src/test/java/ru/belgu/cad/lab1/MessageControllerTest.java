package ru.belgu.cad.lab1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void clean() throws Exception {
        mockMvc.perform(delete("/messages")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Корневой адрес возвращает приветствие")
    void helloWorld() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, World!"));
    }

    @Test
    @DisplayName("Публикация и чтение сообщений")
    void publishAndRead() throws Exception {
        mockMvc.perform(post("/messages").contentType(MediaType.TEXT_PLAIN)
                        .content("первое сообщение"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/messages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].text").value("первое сообщение"));
    }

    @Test
    @DisplayName("Пустое сообщение отклоняется")
    void emptyMessageRejected() throws Exception {
        mockMvc.perform(post("/messages").contentType(MediaType.TEXT_PLAIN).content("   "))
                .andExpect(status().isBadRequest());
        mockMvc.perform(get("/messages/count")).andExpect(content().string("0"));
    }

    @Test
    @DisplayName("Обновление и удаление по индексу")
    void updateAndDelete() throws Exception {
        mockMvc.perform(post("/messages").content("старое"));
        mockMvc.perform(put("/messages/0").contentType(MediaType.TEXT_PLAIN)
                        .content("новое"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/messages/0"))
                .andExpect(jsonPath("$.text").value("новое"));

        mockMvc.perform(delete("/messages/0")).andExpect(status().isOk());
        mockMvc.perform(get("/messages/0")).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Ограничение количества сообщений")
    void limitIsApplied() throws Exception {
        for (int i = 0; i < 25; i++) {
            mockMvc.perform(post("/messages").contentType(MediaType.TEXT_PLAIN)
                    .content("сообщение " + i));
        }
        mockMvc.perform(get("/messages/count")).andExpect(content().string("20"));
        mockMvc.perform(get("/messages"))
                .andExpect(jsonPath("$[0].text").value("сообщение 5"));
    }
}
