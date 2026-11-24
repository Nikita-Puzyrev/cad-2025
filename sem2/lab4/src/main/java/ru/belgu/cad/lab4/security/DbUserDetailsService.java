package ru.belgu.cad.lab4.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.belgu.cad.lab4.domain.AppUser;
import ru.belgu.cad.lab4.repository.AppUserRepository;

/**
 * Аутентификация по данным таблицы app_users (в отличие от лабораторной работы
 * №3, где пользователи хранились в памяти приложения).
 */
@Service
public class DbUserDetailsService implements UserDetailsService {

    private final AppUserRepository users;

    public DbUserDetailsService(AppUserRepository users) {
        this.users = users;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        AppUser user = users.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Пользователь " + username + " не найден"));
        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }
}
