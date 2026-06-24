package com.livrapp.api.identity;

import com.livrapp.api.common.email.EmailService;
import com.livrapp.api.identity.domain.KycStatus;
import com.livrapp.api.identity.domain.Livreur;
import com.livrapp.api.identity.dto.LivreurSummary;
import com.livrapp.api.identity.repository.LivreurRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class KycService {

    private final LivreurRepository livreurRepository;
    private final EmailService emailService;

    public KycService(LivreurRepository livreurRepository, EmailService emailService) {
        this.livreurRepository = livreurRepository;
        this.emailService = emailService;
    }

    /** Liste les livreurs dont le dossier est en attente de validation. */
    @Transactional(readOnly = true)
    public List<LivreurSummary> listPending() {
        return livreurRepository.findByKycStatus(KycStatus.PENDING).stream()
                .map(this::toSummary)
                .toList();
    }

    /** Décision d'un admin : validation ou rejet (avec motif) d'un dossier en attente. */
    @Transactional
    public LivreurSummary decide(UUID livreurId, boolean approved, String reason) {
        Livreur livreur = livreurRepository.findById(livreurId)
                .orElseThrow(() -> new IllegalArgumentException("Livreur introuvable : " + livreurId));

        if (livreur.getKycStatus() != KycStatus.PENDING) {
            throw new IllegalStateException("Ce dossier n'est pas en attente (statut actuel : "
                    + livreur.getKycStatus() + ").");
        }

        if (approved) {
            livreur.setKycStatus(KycStatus.VERIFIED);
            sendApprovedEmail(livreur);
        } else {
            if (reason == null || reason.isBlank()) {
                throw new IllegalArgumentException("Un motif est requis pour rejeter un dossier.");
            }
            livreur.setKycStatus(KycStatus.REJECTED);
            sendRejectedEmail(livreur, reason);
        }
        // Entité gérée dans la transaction : pas de save() explicite nécessaire.
        return toSummary(livreur);
    }

    /** Le livreur re-soumet son dossier rejeté : retour au statut PENDING. */
    @Transactional
    public LivreurSummary resubmit(UUID currentUserId) {
        Livreur livreur = livreurRepository.findById(currentUserId)
                .orElseThrow(() -> new IllegalArgumentException("Profil livreur introuvable."));

        if (livreur.getKycStatus() != KycStatus.REJECTED) {
            throw new IllegalStateException("Seul un dossier rejeté peut être re-soumis (statut actuel : "
                    + livreur.getKycStatus() + ").");
        }
        livreur.setKycStatus(KycStatus.PENDING);
        return toSummary(livreur);
    }

    private LivreurSummary toSummary(Livreur l) {
        return new LivreurSummary(
                l.getUserId(),
                l.getUser().getFirstName(),
                l.getUser().getLastName(),
                l.getUser().getEmail(),
                l.getUser().getPhone(),
                l.getKycStatus());
    }

    private void sendApprovedEmail(Livreur l) {
        emailService.send(l.getUser().getEmail(),
                "Votre demande livreur est acceptée",
                "Bonjour " + l.getUser().getFirstName() + ",\n\n"
                        + "Bonne nouvelle : votre dossier a été validé. Vous pouvez désormais recevoir "
                        + "et effectuer des livraisons sur LivrApp.\n\n"
                        + "L'équipe LivrApp");
    }

    private void sendRejectedEmail(Livreur l, String reason) {
        emailService.send(l.getUser().getEmail(),
                "Votre demande livreur n'a pas été retenue",
                "Bonjour " + l.getUser().getFirstName() + ",\n\n"
                        + "Votre dossier n'a pas pu être validé pour le motif suivant :\n"
                        + reason + "\n\n"
                        + "Vous pouvez corriger les éléments concernés et soumettre à nouveau votre demande.\n\n"
                        + "L'équipe LivrApp");
    }
}
