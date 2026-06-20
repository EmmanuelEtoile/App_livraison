# LivrApp

Plateforme de livraison à la demande pour Yaoundé (Cameroun) — projet de fin de formation ISI ING3, Institut Universitaire Saint Jean.

## Architecture

Mono-dépôt regroupant le backend, les applications mobiles, le dashboard, le microservice IA et l'infrastructure.

| Dossier | Composant | Stack |
|---|---|---|
| `apps/api` | Backend / API REST | Spring Boot 3, Java 21 |
| `apps/mobile` | Applications client + livreur | Flutter (offline-first) |
| `apps/dashboard` | Dashboard administrateur | Next.js 14 |
| `services/ai` | Microservice d'intelligence artificielle | Python, FastAPI, OR-Tools |
| `infra` | Infrastructure et déploiement | Docker, NGINX, GitHub Actions |
| `docs` | Spécifications de référence | CCT v2.0, Analyse, Conception |

## Fonctionnalités principales

Sept types d'opérations : livraison standard, urgente, programmée, inversée, inter-villes, dépôt point partenaire et ramassage hebdomadaire B2B. Paiements Orange Money, MTN MoMo et espèces. Attribution et tarification pilotées par IA. Confirmation de livraison hors-ligne par jeton signé.

## Démarrage

Les conventions de développement et les commandes par composant sont décrites dans [`CLAUDE.md`](./CLAUDE.md). Les trois cahiers de référence se trouvent dans [`docs/`](./docs).

## Statut

🚧 Projet en cours de développement.
