package ru.belgu.cad.lab4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.belgu.cad.lab4.domain.LoginHistory;

import java.util.List;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {

    List<LoginHistory> findTop20ByOrderByLoginTimeDesc();
}
