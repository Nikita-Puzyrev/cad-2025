package ru.belgu.cad.lab5.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.belgu.cad.lab5.client.model.TaskItem;
import ru.belgu.cad.lab5.client.model.UserInfo;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Клиент REST-интерфейса сервера. Аутентификация — HTTP Basic:
 * заголовок Authorization формируется после успешного входа.
 */
public class ApiClient {

    private final HttpClient http = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();
    private final ObjectMapper mapper = new ObjectMapper();
    private final String baseUrl;

    private String authorization;

    public ApiClient(String baseUrl) {
        this.baseUrl = baseUrl.endsWith("/")
                ? baseUrl.substring(0, baseUrl.length() - 1)
                : baseUrl;
    }

    /** Проверка логина и пароля; при успехе заголовок сохраняется для запросов. */
    public UserInfo login(String username, String password)
            throws IOException, InterruptedException {
        String token = Base64.getEncoder().encodeToString(
                (username + ":" + password).getBytes(StandardCharsets.UTF_8));
        authorization = "Basic " + token;

        HttpResponse<String> response = send(request("/api/me").GET().build(), "вход");
        if (response.statusCode() == 401) {
            authorization = null;
            throw new ApiException("Неверный логин или пароль");
        }
        return mapper.readValue(response.body(), UserInfo.class);
    }

    public List<TaskItem> list(String sort, String direction)
            throws IOException, InterruptedException {
        String uri = "/api/tasks?sort=" + sort + "&direction=" + direction;
        HttpResponse<String> response = send(request(uri).GET().build(), "получение задач");
        return mapper.readValue(response.body(), new TypeReference<List<TaskItem>>() {
        });
    }

    public TaskItem create(String title, String description,
                           String category, String priority)
            throws IOException, InterruptedException {
        Map<String, String> body = new LinkedHashMap<>();
        body.put("title", title);
        body.put("description", description);
        body.put("category", category);
        body.put("priority", priority);

        HttpRequest request = request("/api/tasks")
                .header("Content-Type", "application/json; charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(body)))
                .build();
        HttpResponse<String> response = send(request, "добавление задачи");
        if (response.statusCode() == 403) {
            throw new ApiException("Недостаточно прав для добавления задачи");
        }
        return mapper.readValue(response.body(), TaskItem.class);
    }

    public void delete(long id) throws IOException, InterruptedException {
        HttpResponse<String> response = send(
                request("/api/tasks/" + id).DELETE().build(), "удаление задачи");
        if (response.statusCode() == 403) {
            throw new ApiException("Удаление разрешено только модератору или администратору");
        }
        if (response.statusCode() == 404) {
            throw new ApiException("Задача не найдена");
        }
    }

    private HttpRequest.Builder request(String path) {
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(baseUrl + path))
                .timeout(Duration.ofSeconds(10))
                .header("Accept", "application/json");
        if (authorization != null) {
            builder.header("Authorization", authorization);
        }
        return builder;
    }

    private HttpResponse<String> send(HttpRequest request, String action)
            throws IOException, InterruptedException {
        HttpResponse<String> response = http.send(request,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        if (response.statusCode() >= 500) {
            throw new ApiException("Ошибка сервера при операции «" + action + "»: "
                    + response.statusCode());
        }
        return response;
    }

    /** Ошибка обмена с сервером, текст пригоден для показа пользователю. */
    public static class ApiException extends IOException {
        public ApiException(String message) {
            super(message);
        }
    }
}
