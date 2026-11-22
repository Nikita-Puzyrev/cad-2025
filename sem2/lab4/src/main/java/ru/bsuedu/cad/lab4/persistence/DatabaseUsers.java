package ru.bsuedu.cad.lab4.persistence;

import java.time.Instant;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class DatabaseUsers implements UserDetailsService, ApplicationRunner {
    private final UserAccountRepository accounts;
    private final LoginHistoryRepository history;
    private final PasswordEncoder encoder;
    private final String initialPassword;

    DatabaseUsers(UserAccountRepository accounts, LoginHistoryRepository history, PasswordEncoder encoder,
                  @Value("${TASKS_PASSWORD:password}") String initialPassword) {
        this.accounts = accounts;
        this.history = history;
        this.encoder = encoder;
        this.initialPassword = initialPassword;
    }

    @Override
    public void run(ApplicationArguments args) {
        seed("user", "USER");
        seed("moderator", "MODERATOR");
        seed("searcher", "SEARCHER");
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserAccount account = account(username);
        return User.withUsername(account.username)
                .password(account.passwordHash)
                .roles(account.role)
                .build();
    }

    @EventListener
    public void recordLogin(AuthenticationSuccessEvent event) {
        var authentication = event.getAuthentication();
        if (authentication.getDetails() instanceof WebAuthenticationDetails details) {
            history.save(new LoginHistory(account(authentication.getName()), details.getRemoteAddress()));
        }
    }

    public List<Login> logins(String username) {
        return history.findTop20ByAccountUsernameOrderByIdDesc(username).stream()
                .map(entry -> new Login(entry.loggedAt, entry.ipAddress))
                .toList();
    }

    UserAccount account(String username) {
        return accounts.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    private void seed(String username, String role) {
        if (accounts.findByUsername(username).isEmpty()) {
            accounts.save(new UserAccount(username, encoder.encode(initialPassword), role));
        }
    }

    public record Login(Instant loggedAt, String ipAddress) {
    }
}
