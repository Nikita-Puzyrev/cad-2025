package ru.belgu.cad.lab5.security;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.belgu.cad.lab5.service.TaskService;

/**
 * Записывает успешный вход пользователя в таблицу login_history
 * вместе с IP-адресом клиента.
 */
@Component
public class LoginSuccessListener
        implements ApplicationListener<AuthenticationSuccessEvent> {

    private final TaskService taskService;

    public LoginSuccessListener(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        String username = event.getAuthentication().getName();
        String ip = "unknown";
        if (RequestContextHolder.getRequestAttributes()
                instanceof ServletRequestAttributes attributes) {
            ip = attributes.getRequest().getRemoteAddr();
        }
        taskService.logLogin(username, ip);
    }
}
