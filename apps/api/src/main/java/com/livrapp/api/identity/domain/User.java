package com.livrapp.api.identity.domain;

import com.livrapp.api.common.domain.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(nullable = false, unique = true, length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @Column(name = "is_active", nullable = false)
    private boolean active = false;

    // --- État civil (V04) ---
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName = "";

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName = "";

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Gender gender;            // nullable : personnes physiques uniquement

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;    // nullable : personnes physiques uniquement

    // --- Soft delete ---
    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;
}
