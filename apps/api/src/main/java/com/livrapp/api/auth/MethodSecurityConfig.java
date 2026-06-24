package com.livrapp.api.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

/**
 * Active les annotations de sécurité au niveau méthode (@PreAuthorize).
 */
@Configuration
@EnableMethodSecurity
public class MethodSecurityConfig {
}
