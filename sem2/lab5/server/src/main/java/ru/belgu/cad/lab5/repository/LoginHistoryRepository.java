package ru.belgu.cad.lab5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.belgu.cad.lab5.domain.LoginHistory;

import java.util.List;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {

    List<LoginHistory> findTop20ByOrderByLoginTimeDesc();
}
