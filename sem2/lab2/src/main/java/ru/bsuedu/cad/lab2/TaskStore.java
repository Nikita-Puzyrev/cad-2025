package ru.bsuedu.cad.lab2;

import java.util.List;

public interface TaskStore {
    List<Task> find(String query);
    void add(String title, String description);
    void update(long id, boolean completed);
    void delete(long id);
}
