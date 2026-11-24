package ru.belgu.cad.lab5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.belgu.cad.lab5.domain.Priority;
import ru.belgu.cad.lab5.domain.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    /** Список задач с подзадачами, новые сверху. */
    @Query("select distinct t from Task t left join fetch t.subTasks "
            + "order by t.createdAt desc")
    List<Task> findAllWithSubTasks();

    List<Task> findByCompleted(boolean completed);

    List<Task> findByCategory(String category);

    List<Task> findByPriority(Priority priority);

    long countByCompleted(boolean completed);
}
