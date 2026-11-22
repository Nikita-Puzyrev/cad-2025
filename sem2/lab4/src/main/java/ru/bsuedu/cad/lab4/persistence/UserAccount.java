package ru.bsuedu.cad.lab4.persistence;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true)
    String username;

    @Column(nullable = false, unique = true)
    String email;

    @Column(nullable = false)
    String passwordHash;

    @Column(nullable = false)
    String role;

    protected UserAccount() {
    }

    UserAccount(String username, String passwordHash, String role) {
        this.username = username;
        this.email = username + "@example.test";
        this.passwordHash = passwordHash;
        this.role = role;
    }

    String username() {
        return username;
    }
}
