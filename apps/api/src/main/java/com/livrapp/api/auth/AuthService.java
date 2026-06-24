package com.livrapp.api.auth;

import com.livrapp.api.auth.dto.AuthResponse;
import com.livrapp.api.auth.dto.LoginRequest;
import com.livrapp.api.auth.dto.RegisterRequest;
import com.livrapp.api.common.email.EmailService;
import com.livrapp.api.identity.domain.Livreur;
import com.livrapp.api.identity.domain.Role;
import com.livrapp.api.identity.domain.User;
import com.livrapp.api.identity.repository.LivreurRepository;
import com.livrapp.api.identity.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final LivreurRepository livreurRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmailService emailService;

    public AuthService(UserRepository userRepository,
                       LivreurRepository livreurRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService,
                       EmailService emailService) {
        this.userRepository = userRepository;
        this.livreurRepository = livreurRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest req) {
        if (req.role() != Role.CLIENT && req.role() != Role.LIVREUR) {
            throw new IllegalArgumentException("Rôle non autorisé à l'inscription : " + req.role());
        }
        if (userRepository.existsByEmail(req.email())) {
            throw new IllegalStateException("Cet email est déjà utilisé.");
        }
        if (userRepository.existsByPhone(req.phone())) {
            throw new IllegalStateException("Ce numéro de téléphone est déjà utilisé.");
        }

        User user = new User();
        user.setEmail(req.email());
        user.setPhone(req.phone());
        user.setPasswordHash(passwordEncoder.encode(req.password()));
        user.setFirstName(req.firstName());
        user.setLastName(req.lastName());
        user.setRole(req.role());
        user.setActive(true); // notification simple : compte actif immédiatement
        user = userRepository.save(user);

        // Un livreur reçoit en plus un profil avec KYC en attente.
        if (req.role() == Role.LIVREUR) {
            Livreur livreur = new Livreur();
            livreur.setUser(user);              // @MapsId : partage la clé primaire du User
            livreurRepository.save(livreur);    // kyc_status = PENDING par défaut
        }

        sendWelcomeEmail(user);

        String token = jwtService.generateToken(user.getId(), user.getRole().name());
        return new AuthResponse(token, "Bearer", user.getId(), user.getRole().name());
    }

    public AuthResponse login(LoginRequest req) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.identifier(), req.password()));

        User user = userRepository.findByEmail(req.identifier())
                .or(() -> userRepository.findByPhone(req.identifier()))
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));

        String token = jwtService.generateToken(user.getId(), user.getRole().name());
        return new AuthResponse(token, "Bearer", user.getId(), user.getRole().name());
    }

    private void sendWelcomeEmail(User user) {
        if (user.getRole() == Role.LIVREUR) {
            emailService.send(user.getEmail(),
                    "Votre inscription livreur LivrApp",
                    "Bonjour " + user.getFirstName() + ",\n\n"
                            + "Votre inscription comme livreur a bien été reçue.\n"
                            + "Votre dossier est en cours de vérification. Vous recevrez un second e-mail "
                            + "dès que votre demande sera acceptée ou rejetée.\n\n"
                            + "L'équipe LivrApp");
        } else {
            emailService.send(user.getEmail(),
                    "Bienvenue sur LivrApp",
                    "Bonjour " + user.getFirstName() + ",\n\n"
                            + "Votre compte LivrApp a été créé avec succès. Vous pouvez dès maintenant "
                            + "vous connecter et commander vos livraisons.\n\n"
                            + "L'équipe LivrApp");
        }
    }
}
