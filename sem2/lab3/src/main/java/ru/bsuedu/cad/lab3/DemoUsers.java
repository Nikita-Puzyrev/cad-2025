package ru.bsuedu.cad.lab3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration(proxyBeanMethods = false)
class DemoUsers {
    @Bean
    InMemoryUserDetailsManager userDetailsService(PasswordEncoder encoder, @Value("${TASKS_PASSWORD:password}") String password) {
        var users = User.builder().password(encoder.encode(password));
        return new InMemoryUserDetailsManager(
                users.username("user").roles("USER").build(),
                users.username("moderator").roles("MODERATOR").build(),
                users.username("searcher").roles("SEARCHER").build());
    }
}
