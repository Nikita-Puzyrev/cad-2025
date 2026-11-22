package ru.bsuedu.cad.lab4.persistence;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "login_history")
class LoginHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    Instant loggedAt = Instant.now();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    UserAccount account;

    @Column(nullable = false, length = 45)
    String ipAddress;

    protected LoginHistory() {
    }

    LoginHistory(UserAccount account, String ipAddress) {
        this.account = account;
        this.ipAddress = ipAddress;
    }
}
