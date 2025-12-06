package com.musicstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                // Публичные URL (доступны всем)
                .requestMatchers("/", "/web/", "/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/api/equipment", "/api/equipment/**").permitAll()
                .requestMatchers("/api/categories", "/api/stats").permitAll()
                .requestMatchers("/web/equipment", "/web/equipment/search").permitAll()
                
                // URL только для аутентифицированных пользователей
                .requestMatchers("/web/equipment/add", "/web/equipment/edit/**").authenticated()
                .requestMatchers("/web/stats").authenticated()
                
                // API для управления (только для ролей)
                .requestMatchers("/api/equipment/**").hasAnyRole("USER", "MODERATOR", "ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/web/")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/web/")
                .permitAll()
            )
            .exceptionHandling(exception -> exception
                .accessDeniedPage("/access-denied")
            );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("user")
            .password(passwordEncoder().encode("password"))
            .roles("USER")
            .build();

        UserDetails moderator = User.withUsername("moderator")
            .password(passwordEncoder().encode("password"))
            .roles("MODERATOR", "USER")
            .build();

        UserDetails admin = User.withUsername("admin")
            .password(passwordEncoder().encode("admin"))
            .roles("ADMIN", "MODERATOR", "USER")
            .build();

        return new InMemoryUserDetailsManager(user, moderator, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}