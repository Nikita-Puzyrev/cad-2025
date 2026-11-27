package ru.bsuedu.cad.lab4.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {
    List<LoginHistory> findTop20ByAccountUsernameOrderByIdDesc(String username);
}
