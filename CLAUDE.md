# CLAUDE.md — LivrApp

Ce fichier est lu automatiquement par Claude Code à chaque session. Il définit le contexte, l'architecture et les conventions du projet. Le garder **concis et à jour**.

## Projet
LivrApp — plateforme de livraison à la demande pour Yaoundé (Cameroun). Projet de fin de formation ISI ING3, Institut Universitaire Saint Jean. Sept types d'opérations : livraison standard, urgente, programmée, inversée, inter-villes, dépôt point partenaire, ramassage hebdomadaire B2B.

Les spécifications détaillées sont dans `docs/` (Cahier des Charges Technique v2.0, Cahier d'Analyse, Cahier de Conception). **Ces cahiers font autorité.** En cas de doute, s'y référer et citer la section.

## Pile technique
- **Backend** : Spring Boot 3 / Java 21 (`apps/api`) — architecture hexagonale, Spring Security (JWT + OTP), Spring Data JPA/Hibernate, Flyway, Spring StateMachine.
- **Base de données** : PostgreSQL 16 + PostGIS 3 ; cache et temps réel : Redis 7.
- **Mobile** : Flutter (`apps/mobile`) — apps client + livreur, offline-first (SQLite/SQLCipher), tracking temps réel par WebSocket.
- **Dashboard** : Next.js 14 (`apps/dashboard`) ; interface partenaire : PWA.
- **IA** : Python / FastAPI (`services/ai`) — OR-Tools (attribution VRP, scoring, ETA, clustering DBSCAN, détection d'anomalie GPS).
- **Infra** : Docker + docker-compose, NGINX (reverse proxy + TLS), VPS Ubuntu, CI/CD GitHub Actions.

## Structure du dépôt
```
livrapp/
  apps/api/           Backend Spring Boot
  apps/mobile/        Flutter (client + livreur)
  apps/dashboard/     Next.js (dashboard admin)
  services/ai/        Microservice IA Python/FastAPI
  infra/              Docker, compose, NGINX, scripts
  docs/               Cahiers de référence + doc technique
  .github/workflows/  CI/CD
```

## Commandes (à compléter au fil du développement)
- Backend : `cd apps/api && ./mvnw spring-boot:run` — tests : `./mvnw test`
- IA : `cd services/ai && uvicorn app.main:app --reload`
- Dashboard : `cd apps/dashboard && npm run dev`
- Mobile : `cd apps/mobile && flutter run`
- Infra locale : `docker compose -f infra/docker/docker-compose.dev.yml up -d` (PostgreSQL + Redis)

## Règles métier critiques (cahier des charges v2.0 — NE JAMAIS contredire)
- **Livreurs = coursiers, PAS salariés.** Rémunération : 80 % du prix de chaque livraison + pourboires + prime hebdomadaire de performance (versée par Mobile Money). La plateforme encaisse 100 % du prix et conserve 20 % de commission. Entité `RemunerationLivreur` / table `livreur_remunerations` (jamais de notion de « salaire »).
- **Confirmation de livraison** : jeton signé (PIN + QR) vérifiable hors-ligne (état `DELIVERED_PENDING_SYNC`), avec repli OTP-SMS pour les receveurs sans application.
- **Score composite d'attribution** : 35 % proximité + 25 % performance + 20 % disponibilité + 10 % acceptation + 10 % note.
- **Tarification** : formule canonique du §9.1 (5 étapes ordonnées) + suppléments de zone (+0 / +200 / +500 XAF) ; plancher 1000 XAF ; arrondi au multiple de 50 XAF supérieur.
- **Machine à états** : cycle de vie complet du §10 (incl. `DELIVERED_PENDING_SYNC`, `AT_AGENCY`, `TENTATIVE_ECHOUEE`, `REASSIGNMENT_PENDING`, `RETURNED`, `EXPIRED`, `CANCELLED`, `REFUNDED`). Implémenter avec Spring StateMachine.
- **COD (MVP)** : frais de livraison en espèces uniquement ; encaissement de la valeur marchande = module dédié de Phase 2.
- **Paiements** : Orange Money + MTN MoMo + espèces. Transactions strictes (idempotence, cohérence).
- **B2B** : paliers Standard / Business (−10 %) / Premium (−20 %) sur le volume du mois précédent ; sous-comptes Admin entreprise / Gestionnaire / Demandeur (plafond).

## Conventions de code
- Architecture hexagonale ; un module par domaine (§5 conception) : auth, livraisons, paiements, scoring, partenaires, B2B, notifications…
- **Sécurité d'abord** : secrets via variables d'environnement (jamais commités) ; validation systématique des entrées ; `@Transactional` strict sur les paiements.
- **Base de données** : tout changement de schéma passe par une migration Flyway versionnée. Timestamps en UTC. Géolocalisation en `GEOGRAPHY(POINT, 4326)`. Soft delete via `deleted_at`.
- **Tests** : unitaires (JUnit 5) + intégration (Testcontainers PostgreSQL/Redis). Tout module est livré avec ses tests.
- Code et commentaires en français. Commits conventionnels (`feat`, `fix`, `docs`, `chore`…).
- **Contexte local** : faible bande passante et coupures réseau fréquentes (offline-first) ; interfaces en français ; adresses par repères.

## Style de travail attendu
Avancer par étapes et valider chaque étape avant la suivante. Fournir du code complet et prêt à l'emploi. Expliquer le « pourquoi » des choix d'architecture. Signaler explicitement tout écart aux cahiers.
