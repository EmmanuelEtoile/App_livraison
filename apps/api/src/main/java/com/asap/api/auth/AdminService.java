package com.asap.api.auth;

import com.asap.api.auth.dto.CreateCoAdminRequest;
import com.asap.api.auth.dto.UserSummary;
import com.asap.api.common.email.EmailService;
import com.asap.api.identity.domain.Role;
import com.asap.api.identity.domain.User;
import com.asap.api.identity.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public AdminService(UserRepository userRepository,
                        PasswordEncoder passwordEncoder,
                        EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    /** Créé par le super-admin uniquement (contrôlé au niveau du controller). */
    @Transactional
    public UserSummary createCoAdmin(CreateCoAdminRequest req) {
        if (userRepository.existsByEmail(req.email())) {
            throw new IllegalStateException("Cet email est déjà utilisé.");
        }
        if (userRepository.existsByPhone(req.phone())) {
            throw new IllegalStateException("Ce numéro de téléphone est déjà utilisé.");
        }

        User admin = new User();
        admin.setEmail(req.email());
        admin.setPhone(req.phone());
        admin.setPasswordHash(passwordEncoder.encode(req.password()));
        admin.setFirstName(req.firstName());
        admin.setLastName(req.lastName());
        admin.setRole(Role.ADMIN);
        admin.setActive(true);
        admin = userRepository.save(admin);

        emailService.send(admin.getEmail(),
                "Votre compte administrateur ASAP",
                "Bonjour " + admin.getFirstName() + ",\n\n"
                        + "Un compte administrateur ASAP a été créé pour vous.\n"
                        + "Vous pouvez vous connecter avec votre adresse e-mail et le mot de passe qui vous a été communiqué.\n\n"
                        + "L'équipe ASAP");

        return new UserSummary(admin.getId(), admin.getEmail(), admin.getRole().name(),
                admin.getFirstName(), admin.getLastName());
    }
}
