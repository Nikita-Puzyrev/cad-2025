package ru.bsuedu.cad.lab4.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

interface StoredTaskRepository extends JpaRepository<StoredTask, Long> {
    @EntityGraph(attributePaths = "owner")
    List<StoredTask> findAllByOrderByIdAsc();

}
