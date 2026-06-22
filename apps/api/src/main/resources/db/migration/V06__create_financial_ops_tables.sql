-- =====================================================================
-- V06 — Domaine financier & opérationnel
-- payments, penalites, avertissements, livreur_remunerations,
-- partner_deposits, inter_villes_expeditions, litiges.
-- Source : Cahier de Conception §3.2. (Index regroupés à part en V07.)
-- =====================================================================

-- ---------------------------------------------------------------------
-- payments : paiement rattaché à une livraison.
-- ---------------------------------------------------------------------
CREATE TABLE payments (
    id                   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    delivery_id          UUID NOT NULL REFERENCES deliveries(id),
    amount               DECIMAL(12,2) NOT NULL,
    currency             VARCHAR(5) NOT NULL DEFAULT 'XAF',
    method               VARCHAR(20) NOT NULL
                         CHECK (method IN ('ORANGE_MONEY','MTN_MOMO','CASH_PREPAID','CASH_COD')),
    status               VARCHAR(20) NOT NULL DEFAULT 'PENDING'
                         CHECK (status IN ('PENDING','SUCCESS','FAILED','REFUNDED')),
    transaction_ref      VARCHAR(255),
    cash_photo_url       TEXT,
    cash_amount_declared DECIMAL(12,2),
    cash_client_otp      VARCHAR(10),
    cash_otp_verified    BOOLEAN NOT NULL DEFAULT false,
    created_at           TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- ---------------------------------------------------------------------
-- penalites : pénalités imputées à un utilisateur (livreur ou client).
-- ---------------------------------------------------------------------
CREATE TABLE penalites (
    id                       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    target_user_id           UUID NOT NULL REFERENCES users(id),
    delivery_id              UUID REFERENCES deliveries(id),
    type                     VARCHAR(30)
                             CHECK (type IN ('ANNULATION_TARDIVE','ATTENTE_EXCESSIVE',
                                             'RECEVEUR_ABSENT','ANNULATION_PROGRAMMEE')),
    amount                   DECIMAL(12,2) NOT NULL,
    livreur_share            DECIMAL(12,2),
    platform_share           DECIMAL(12,2),
    status                   VARCHAR(20) NOT NULL DEFAULT 'PENDING'
                             CHECK (status IN ('PENDING','COLLECTED','DISPUTED')),
    collected_on_delivery_id UUID REFERENCES deliveries(id),
    created_at               TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- ---------------------------------------------------------------------
-- avertissements : avertissements (comptage des suspensions à J+30).
-- ---------------------------------------------------------------------
CREATE TABLE avertissements (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id     UUID NOT NULL REFERENCES users(id),
    delivery_id UUID REFERENCES deliveries(id),
    reason      TEXT NOT NULL,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    expires_at  TIMESTAMPTZ
);

-- ---------------------------------------------------------------------
-- livreur_remunerations : récap hebdomadaire de rémunération du coursier
-- (cumul des parts 80 % par course + prime − déductions).
-- ---------------------------------------------------------------------
CREATE TABLE livreur_remunerations (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    livreur_id      UUID NOT NULL REFERENCES users(id),
    period_start    DATE NOT NULL,
    period_end      DATE NOT NULL,
    total_missions  INTEGER NOT NULL DEFAULT 0,
    gross_amount    DECIMAL(12,2) NOT NULL DEFAULT 0,   -- 80 % cumulés + prime
    deductions      DECIMAL(12,2) NOT NULL DEFAULT 0,
    net_amount      DECIMAL(12,2) NOT NULL DEFAULT 0,
    payment_method  VARCHAR(20),                        -- domaine à figer (versé par MoMo)
    status          VARCHAR(20) NOT NULL DEFAULT 'PENDING',  -- domaine à figer
    transaction_ref VARCHAR(255),
    processed_at    TIMESTAMPTZ,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- ---------------------------------------------------------------------
-- partner_deposits : dépôt d'un colis en point partenaire (expire à +72 h).
-- ---------------------------------------------------------------------
CREATE TABLE partner_deposits (
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    delivery_id      UUID NOT NULL REFERENCES deliveries(id),
    partner_id       UUID NOT NULL REFERENCES pickup_partners(id),
    deposited_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
    expires_at       TIMESTAMPTZ NOT NULL,              -- dépôt + 72 h
    retrieved_at     TIMESTAMPTZ,
    status           VARCHAR(20) NOT NULL DEFAULT 'DEPOSITED'
                     CHECK (status IN ('DEPOSITED','RETRIEVED','EXPIRED','RETURNED')),
    extension_days   INTEGER NOT NULL DEFAULT 0,
    extension_fees   DECIMAL(12,2) NOT NULL DEFAULT 0,  -- 200 XAF / jour
    expiry_photo_url TEXT
);

-- ---------------------------------------------------------------------
-- inter_villes_expeditions : remise en agence inter-villes.
-- ---------------------------------------------------------------------
CREATE TABLE inter_villes_expeditions (
    id                     UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    delivery_id            UUID NOT NULL REFERENCES deliveries(id),
    agency_name            VARCHAR(255),
    agency_city            VARCHAR(100),
    tracking_number        VARCHAR(100),
    receipt_photo_url      TEXT,
    package_photo_url      TEXT,
    estimated_arrival      TIMESTAMPTZ,
    destination_livreur_id UUID REFERENCES users(id),
    status                 VARCHAR(20),                 -- domaine à figer
    created_at             TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- ---------------------------------------------------------------------
-- litiges : réclamations / différends sur une livraison.
-- ---------------------------------------------------------------------
CREATE TABLE litiges (
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    delivery_id      UUID REFERENCES deliveries(id),
    reported_by      UUID NOT NULL REFERENCES users(id),
    type             VARCHAR(30),                       -- domaine à figer
    status           VARCHAR(20) NOT NULL DEFAULT 'OPEN',  -- domaine à figer
    description      TEXT NOT NULL,
    evidence_photos  TEXT[],
    resolution       TEXT,
    deduction_amount DECIMAL(12,2),
    sla_hours        INTEGER,
    closed_at        TIMESTAMPTZ,
    created_at       TIMESTAMPTZ NOT NULL DEFAULT now()
);
