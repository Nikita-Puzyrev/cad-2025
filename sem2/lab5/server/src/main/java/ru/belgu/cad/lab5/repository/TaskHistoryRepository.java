package ru.belgu.cad.lab5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.belgu.cad.lab5.domain.TaskHistory;

import java.util.List;

public interface TaskHistoryRepository extends JpaRepository<TaskHistory, Long> {

    List<TaskHistory> findTop30ByOrderByChangedAtDesc();

    void deleteByTaskId(Long taskId);
}
