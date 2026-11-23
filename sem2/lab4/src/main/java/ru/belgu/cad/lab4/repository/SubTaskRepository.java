package ru.belgu.cad.lab4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.belgu.cad.lab4.domain.SubTask;

import java.util.List;

public interface SubTaskRepository extends JpaRepository<SubTask, Long> {

    List<SubTask> findByTaskId(Long taskId);
}
