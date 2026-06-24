package com.livrapp.api.auth;

import com.livrapp.api.identity.domain.Role;
import com.livrapp.api.identity.domain.User;
import com.livrapp.api.identity.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Crée le super-admin au démarrage s'il n'existe pas encore.
 * Identifiants fournis par la configuration (variables d'environnement).
 * Idempotent : ne fait rien si un super-admin est déjà présent.
 */
@Slf4j
@Component
public class SuperAdminInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.superadmin.email}")
    private String email;

    @Value("${app.superadmin.phone}")
    private String phone;

    @Value("${app.superadmin.password}")
    private String password;

    @Value("${app.superadmin.first-name:Super}")
    private String firstName;

    @Value("${app.superadmin.last-name:Admin}")
    private String lastName;

    public SuperAdminInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.existsByRole(Role.SUPER_ADMIN)) {
            log.info("Super-admin déjà présent — aucune création.");
            return;
        }
        User admin = new User();
        admin.setEmail(email);
        admin.setPhone(phone);
        admin.setPasswordHash(passwordEncoder.encode(password));
        admin.setFirstName(firstName);
        admin.setLastName(lastName);
        admin.setRole(Role.SUPER_ADMIN);
        admin.setActive(true);
        userRepository.save(admin);
        log.info("Super-admin créé : {}", email);
    }
}
