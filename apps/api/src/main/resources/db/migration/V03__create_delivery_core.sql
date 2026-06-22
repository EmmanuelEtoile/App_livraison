-- =====================================================================
-- V03 — Cœur métier : pickup_partners, promotions, deliveries, receivers
-- Source : Cahier de Conception §3.2.
-- Statuts de livraison alignés sur le CCT §10 (machine à états v2.0).
-- Gère la référence circulaire deliveries <-> receivers.
-- =====================================================================

-- ---------------------------------------------------------------------
-- pickup_partners : points de retrait / dépôt partenaires
-- ---------------------------------------------------------------------
CREATE TABLE pickup_partners (
    id                   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    company_name         VARCHAR(255) NOT NULL,
    address              TEXT NOT NULL,
    location             GEOGRAPHY(POINT, 4326) NOT NULL,
    opening_hours        JSONB,
    commission_per_colis DECIMAL(10,2) NOT NULL DEFAULT 150.00,  -- 150 XAF / colis
    is_active            BOOLEAN NOT NULL DEFAULT true,
    total_packages       INTEGER NOT NULL DEFAULT 0,
    created_at           TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- ---------------------------------------------------------------------
-- promotions : codes de réduction
-- ---------------------------------------------------------------------
CREATE TABLE promotions (
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code             VARCHAR(50) UNIQUE NOT NULL,
    discount_type    VARCHAR(20) NOT NULL CHECK (discount_type IN ('PERCENT','FIXED')),
    discount_value   DECIMAL(12,2) NOT NULL,
    min_order_amount DECIMAL(12,2) NOT NULL DEFAULT 0,
    usage_limit      INTEGER,
    usage_count      INTEGER NOT NULL DEFAULT 0,
    valid_from       TIMESTAMPTZ,
    valid_until      TIMESTAMPTZ,
    is_active        BOOLEAN NOT NULL DEFAULT true,
    created_by       UUID REFERENCES users(id),
    created_at       TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- ---------------------------------------------------------------------
-- deliveries : la livraison (table centrale du domaine).
--   receiver_id  : FK ajoutée en bas de ce fichier (référence circulaire).
--   batch_item_id: colonne créée ici ; FK ajoutée en V04 (domaine ramassage).
-- ---------------------------------------------------------------------
CREATE TABLE deliveries (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    client_id           UUID NOT NULL REFERENCES users(id),
    livreur_id          UUID REFERENCES users(id),              -- nul tant que non attribuée
    receiver_id         UUID,                                   -- FK ajoutée plus bas
    partner_pickup_id   UUID REFERENCES pickup_partners(id),
    batch_item_id       UUID,                                   -- FK ajoutée en V04
    delivery_type       VARCHAR(20) NOT NULL DEFAULT 'STANDARD'
                        CHECK (delivery_type IN ('STANDARD','URGENT','SCHEDULED','INVERSE',
                                                 'INTER_VILLES','DEPOT_PARTENAIRE','BATCH')),
    pickup_location     GEOGRAPHY(POINT, 4326) NOT NULL,
    pickup_address      TEXT NOT NULL,
    pickup_landmarks    TEXT NOT NULL,                          -- repères obligatoires
    delivery_location   GEOGRAPHY(POINT, 4326) NOT NULL,
    delivery_address    TEXT NOT NULL,
    scheduled_date      DATE,
    scheduled_slot      VARCHAR(20) CHECK (scheduled_slot IN ('MATIN','APRES_MIDI','SOIR')),
    status              VARCHAR(30) NOT NULL DEFAULT 'DRAFT'
                        CHECK (status IN (
                            'DRAFT','PENDING_PAYMENT','PENDING','SCHEDULED','ASSIGNED','ACCEPTED',
                            'EN_ROUTE_PICKUP','AT_PICKUP','PICKED_UP','EN_ROUTE_DROPOFF','AT_DROPOFF',
                            'DELIVERED','DELIVERED_PENDING_SYNC','TENTATIVE_ECHOUEE','REASSIGNMENT_PENDING',
                            'AT_PARTNER','AT_AGENCY','RETURNED','ABANDONED','CANCELLED','EXPIRED','REFUNDED')),
    urgency_level       VARCHAR(20) NOT NULL DEFAULT 'STANDARD'
                        CHECK (urgency_level IN ('STANDARD','URGENT','PLANIFIE')),
    weight_kg           DECIMAL(8,2),
    zone_supplement     DECIMAL(8,2) NOT NULL DEFAULT 0,
    estimated_price     DECIMAL(12,2),
    final_price         DECIMAL(12,2),
    platform_commission DECIMAL(12,2),                          -- 20 % du final_price
    livreur_share       DECIMAL(12,2),                          -- 80 % du final_price
    promotion_id        UUID REFERENCES promotions(id),
    photos              TEXT[],                                 -- URLs des photos
    otp_used            BOOLEAN NOT NULL DEFAULT false,
    distance_km         DECIMAL(8,2),
    eta_minutes         INTEGER,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT now(),
    deleted_at          TIMESTAMPTZ
);

-- ---------------------------------------------------------------------
-- receivers : destinataire (peut être sans application).
-- ---------------------------------------------------------------------
CREATE TABLE receivers (
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name           VARCHAR(255) NOT NULL,
    phone          VARCHAR(20) NOT NULL,                        -- chiffré AES-256 (applicatif)
    delivery_id    UUID NOT NULL REFERENCES deliveries(id),
    has_app        BOOLEAN NOT NULL DEFAULT false,
    otp_code       VARCHAR(10),
    otp_expires_at TIMESTAMPTZ,
    otp_verified   BOOLEAN NOT NULL DEFAULT false,
    otp_attempts   INTEGER NOT NULL DEFAULT 0,
    created_at     TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- ---------------------------------------------------------------------
-- Résolution de la référence circulaire deliveries <-> receivers :
-- maintenant que receivers existe, on ajoute la FK manquante sur deliveries.
-- ---------------------------------------------------------------------
ALTER TABLE deliveries
    ADD CONSTRAINT fk_deliveries_receiver
    FOREIGN KEY (receiver_id) REFERENCES receivers(id);
