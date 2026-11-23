package ru.belgu.cad.lab5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.belgu.cad.lab5.domain.AppUser;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);

    boolean existsByUsername(String username);
}
