package ru.belgu.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class TaskApiClient {
    private final HttpClient http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
    private final ObjectMapper json = new ObjectMapper();
    private final String baseUrl;
    private final String authorization;

    public TaskApiClient(String baseUrl, String username, String password) {
        this.baseUrl = baseUrl.replaceAll("/+$", "");
        this.authorization = "Basic " + Base64.getEncoder().encodeToString(
                (username + ":" + password).getBytes(StandardCharsets.UTF_8));
    }

    public List<TaskItem> list() throws IOException, InterruptedException {
        String body = send("GET", "/api", null, 200);
        return json.readValue(body, new TypeReference<List<TaskItem>>() { });
    }

    public void add(String description, int priority) throws IOException, InterruptedException {
        String body = json.writeValueAsString(Map.of("description", description, "priority", priority));
        send("POST", "/api", body, 201);
    }

    public void delete(long id) throws IOException, InterruptedException {
        send("DELETE", "/api/" + id, null, 204);
    }

    private String send(String method, String path, String body, int expected)
            throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl + path))
                .timeout(Duration.ofSeconds(10))
                .header("Authorization", authorization)
                .header("Content-Type", "application/json")
                .method(method, body == null ? HttpRequest.BodyPublishers.noBody()
                        : HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
                .build();
        HttpResponse<String> response = http.send(request,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        if (response.statusCode() != expected) {
            String message = switch (response.statusCode()) {
                case 401 -> "Неверный логин или пароль";
                case 403 -> "Недостаточно прав: удалять задачи может только модератор";
                case 400 -> "Проверьте данные задачи";
                case 404 -> "Задача уже удалена";
                default -> "Ошибка сервера: HTTP " + response.statusCode();
            };
            throw new IOException(message);
        }
        return response.body();
    }
}
