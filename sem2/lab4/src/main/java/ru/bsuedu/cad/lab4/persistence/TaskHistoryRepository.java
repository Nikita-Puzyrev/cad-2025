package ru.bsuedu.cad.lab4.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

interface TaskHistoryRepository extends JpaRepository<TaskHistory, Long> {
    List<TaskHistory> findByTaskIdOrderByIdDesc(long taskId);
}
