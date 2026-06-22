-- =====================================================================
-- V02 — Socle identité : users + livreurs + clients_entreprise
-- Source : Cahier de Conception §3.2.
-- Modèle : une table de base "users" + des tables d'extension qui
--          partagent sa clé primaire (héritage par tables jointes).
-- =====================================================================

-- ---------------------------------------------------------------------
-- users : tout compte de la plateforme (client, livreur, admin, etc.)
-- ---------------------------------------------------------------------
CREATE TABLE users (
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email         VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,          -- bcrypt (haché côté Java)
    phone         VARCHAR(20)  UNIQUE NOT NULL,
    role          VARCHAR(20)  NOT NULL
                  CHECK (role IN ('CLIENT','LIVREUR','ADMIN','SUPER_ADMIN','PARTENAIRE')),
    is_active     BOOLEAN      NOT NULL DEFAULT false,
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ  NOT NULL DEFAULT now(),
    deleted_at    TIMESTAMPTZ                       -- soft delete (traçabilité légale)
);

-- ---------------------------------------------------------------------
-- livreurs : extension de "users" pour les coursiers.
-- NB : coursiers (NON salariés). wallet_balance = solde des gains
--      (80 % par course) en attente de versement.
-- ---------------------------------------------------------------------
CREATE TABLE livreurs (
    user_id            UUID PRIMARY KEY REFERENCES users(id),
    mode_transport     VARCHAR(50),                 -- libre : déterminé par l'IA
    kyc_status         VARCHAR(20) NOT NULL DEFAULT 'PENDING'
                       CHECK (kyc_status IN ('PENDING','VERIFIED','REJECTED')),
    current_location   GEOGRAPHY(POINT, 4326),
    score              DECIMAL(5,2)  NOT NULL DEFAULT 100.00,
    completed_missions INTEGER       NOT NULL DEFAULT 0,
    is_available       BOOLEAN       NOT NULL DEFAULT false,
    wallet_balance     DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    contract_signed_at TIMESTAMPTZ                  -- contrat de prestation
);

-- ---------------------------------------------------------------------
-- clients_entreprise : extension de "users" pour les comptes B2B.
-- ---------------------------------------------------------------------
CREATE TABLE clients_entreprise (
    user_id            UUID PRIMARY KEY REFERENCES users(id),
    company_name       VARCHAR(255) NOT NULL,
    rccm               VARCHAR(100) UNIQUE NOT NULL, -- registre du commerce
    billing_email      VARCHAR(255) NOT NULL,
    tier               VARCHAR(20) NOT NULL DEFAULT 'STANDARD'
                       CHECK (tier IN ('STANDARD','BUSINESS','PREMIUM')),
    monthly_volume     INTEGER NOT NULL DEFAULT 0,   -- recalculé en début de mois
    contract_url       TEXT,
    contract_signed_at TIMESTAMPTZ
);
