package com.livrapp.api.auth;

import com.livrapp.api.identity.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Charge un utilisateur pour Spring Security :
 *  - par email OU téléphone (connexion),
 *  - par id (filtre JWT).
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String identifier) {
        return userRepository.findByEmail(identifier)
                .or(() -> userRepository.findByPhone(identifier))
                .map(SecurityUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable : " + identifier));
    }

    public UserDetails loadUserById(UUID id) {
        return userRepository.findById(id)
                .map(SecurityUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable : " + id));
    }
}
