package ru.belgu.cad.lab1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * REST-контроллер заметок. Сообщения хранятся в памяти процесса.
 *
 * Базовая часть работы — приветствие и CRUD по индексу.
 * Дополнительные методы: очистка списка, проверка пустого сообщения,
 * получение по индексу, ограничение количества, выборка по дате, подсчёт.
 */
@RestController
public class MessageController {

    /** Предел количества хранимых сообщений: при переполнении удаляется самое старое. */
    private static final int LIMIT = 20;

    private final List<Message> userMessages = new ArrayList<>();
    private final AtomicLong sequence = new AtomicLong(1);

    @GetMapping("/")
    public String helloWorld() {
        return "Hello, World!";
    }

    @GetMapping("/messages")
    public List<Message> getAllMessages(
            @RequestParam(required = false) String after) {
        if (after == null || after.isBlank()) {
            return List.copyOf(userMessages);
        }
        LocalDateTime moment = LocalDateTime.parse(after);
        return userMessages.stream()
                .filter(m -> m.createdAt().isAfter(moment))
                .toList();
    }

    @PostMapping("/messages")
    public ResponseEntity<?> publishMessage(@RequestBody String message) {
        String text = message == null ? "" : message.trim();
        if (text.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Сообщение не может быть пустым");
        }
        synchronized (userMessages) {
            if (userMessages.size() >= LIMIT) {
                userMessages.remove(0);
            }
            userMessages.add(new Message(sequence.getAndIncrement(), text,
                    LocalDateTime.now()));
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Message published successfully!");
    }

    @GetMapping("/messages/count")
    public int countMessages() {
        return userMessages.size();
    }

    @GetMapping("/messages/{index}")
    public ResponseEntity<?> getMessageByIndex(@PathVariable int index) {
        synchronized (userMessages) {
            if (index < 0 || index >= userMessages.size()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Message not found at index " + index);
            }
            return ResponseEntity.ok(userMessages.get(index));
        }
    }

    @PutMapping("/messages/{index}")
    public ResponseEntity<String> updateMessage(@PathVariable int index,
                                                @RequestBody String message) {
        String text = message == null ? "" : message.trim();
        if (text.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Сообщение не может быть пустым");
        }
        synchronized (userMessages) {
            if (index < 0 || index >= userMessages.size()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Message not found at index " + index);
            }
            Message old = userMessages.get(index);
            userMessages.set(index, new Message(old.id(), text, old.createdAt()));
            return ResponseEntity.ok("Message updated successfully!");
        }
    }

    @DeleteMapping("/messages/{index}")
    public ResponseEntity<String> deleteMessage(@PathVariable int index) {
        synchronized (userMessages) {
            if (index < 0 || index >= userMessages.size()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Message not found at index " + index);
            }
            userMessages.remove(index);
            return ResponseEntity.ok("Message deleted successfully!");
        }
    }

    @DeleteMapping("/messages")
    public ResponseEntity<String> clearAllMessages() {
        synchronized (userMessages) {
            userMessages.clear();
        }
        return ResponseEntity.ok("All messages cleared");
    }
}
