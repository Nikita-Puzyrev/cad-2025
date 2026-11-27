package ru.bsuedu.cad.lab5.client;

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

final class TaskApi {
    private static final URI SERVER = URI.create("http://127.0.0.1:8080");
    private static final HttpClient HTTP = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(4)).build();
    private static final ObjectMapper JSON = new ObjectMapper();

    private final String authorization;

    TaskApi(String username, char[] password) {
        byte[] credentials = (username + ":" + new String(password)).getBytes(StandardCharsets.UTF_8);
        authorization = "Basic " + Base64.getEncoder().encodeToString(credentials);
        java.util.Arrays.fill(credentials, (byte) 0);
    }

    List<Task> list() throws IOException, InterruptedException {
        return JSON.readValue(send("GET", "/api", null), new TypeReference<>() {});
    }

    Details details(long id) throws IOException, InterruptedException {
        return JSON.readValue(send("GET", "/api/" + id, null), Details.class);
    }

    List<Login> loginHistory() throws IOException, InterruptedException {
        return JSON.readValue(send("GET", "/api/login-history", null), new TypeReference<>() {});
    }

    void create(NewTask task) throws IOException, InterruptedException {
        send("POST", "/api", JSON.writeValueAsString(task));
    }

    void delete(long id) throws IOException, InterruptedException {
        send("DELETE", "/api/" + id, null);
    }

    private String send(String method, String path, String body) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder(SERVER.resolve(path))
                .timeout(Duration.ofSeconds(8))
                .header("Authorization", authorization)
                .header("Accept", "application/json");
        var publisher = HttpRequest.BodyPublishers.noBody();
        if (body != null) {
            request.header("Content-Type", "application/json");
            publisher = HttpRequest.BodyPublishers.ofString(body);
        }
        var response = HTTP.send(request.method(method, publisher).build(), HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return response.body();
        }
        String message = switch (response.statusCode()) {
            case 400 -> "Проверьте данные задачи";
            case 401 -> "Неверный логин или пароль";
            case 403 -> "Недостаточно прав для этой операции";
            case 404 -> "Задача уже удалена";
            default -> "Сервер вернул HTTP " + response.statusCode();
        };
        throw new IOException(message);
    }

    record Task(long id, String title, String description, boolean completed, String createdAt,
                String priority, String category, String owner) {
    }

    record NewTask(String title, String description, String priority, String category) {
    }

    record Details(Task task, List<Subtask> subtasks, List<Change> changes) {
    }

    record Subtask(long id, String description, boolean completed) {
    }

    record Change(String changedAt, String oldState, String newState) {
    }

    record Login(String loggedAt, String ipAddress) {
    }
}
