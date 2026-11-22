package ru.bsuedu.cad.lab1;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class MessageController {
    private final List<String> messages = new ArrayList<>();

    @GetMapping("/")
    public String helloWorld() {
        return "Hello, World!";
    }

    @GetMapping("/messages")
    public synchronized List<String> getAllMessages() {
        return List.copyOf(messages);
    }

    @PostMapping("/messages")
    public synchronized String publishMessage(@RequestBody(required = false) String message) {
        validateMessage(message);
        messages.add(message);
        return "Message published successfully!";
    }

    @PutMapping("/messages/{index}")
    public synchronized String updateMessage(@PathVariable int index, @RequestBody(required = false) String message) {
        checkIndex(index);
        validateMessage(message);
        messages.set(index, message);
        return "Message updated successfully!";
    }

    @DeleteMapping("/messages/{index}")
    public synchronized String deleteMessage(@PathVariable int index) {
        checkIndex(index);
        messages.remove(index);
        return "Message deleted successfully!";
    }

    @GetMapping("/messages/count")
    public synchronized int countMessages() {
        return messages.size();
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= messages.size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Нет заметки с таким индексом");
        }
    }

    private void validateMessage(String message) {
        if (message == null || message.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Заметка не может быть пустой");
        }
    }
}
