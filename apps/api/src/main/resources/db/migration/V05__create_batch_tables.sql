-- =====================================================================
-- V05 — Ramassage B2B : batch_orders, batch_delivery_items
-- Source : Cahier de Conception §3.2.
-- Recoud la FK deliveries.batch_item_id laissée en attente en V03.
-- =====================================================================

-- ---------------------------------------------------------------------
-- batch_orders : lot de ramassage hebdomadaire d'une structure B2B.
-- ---------------------------------------------------------------------
CREATE TABLE batch_orders (
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    structure_id   UUID NOT NULL REFERENCES users(id),         -- le compte B2B
    scheduled_day  VARCHAR(10) NOT NULL
                   CHECK (scheduled_day IN ('LUNDI','MARDI','MERCREDI','JEUDI','VENDREDI','SAMEDI')),
    scheduled_hour TIME NOT NULL,
    file_url       TEXT,                                       -- CSV/XLSX uploadé
    total_orders   INTEGER NOT NULL DEFAULT 0,
    urgent_orders  INTEGER NOT NULL DEFAULT 0,
    status         VARCHAR(20) NOT NULL DEFAULT 'PENDING'
                   CHECK (status IN ('PENDING','VALIDATED','LOCKED','PLANNED','IN_PROGRESS','COMPLETED','REPORTED')),
    total_amount   DECIMAL(12,2),
    processed_at   TIMESTAMPTZ,
    created_at     TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- ---------------------------------------------------------------------
-- batch_delivery_items : une ligne de commande à l'intérieur d'un lot.
--   delivery_id : la livraison générée à partir de cette ligne
--                 (nullable tant qu'elle n'a pas été convertie).
-- ---------------------------------------------------------------------
CREATE TABLE batch_delivery_items (
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    batch_order_id UUID NOT NULL REFERENCES batch_orders(id),
    delivery_id    UUID REFERENCES deliveries(id),
    is_urgent      BOOLEAN NOT NULL DEFAULT false,
    receiver_name  VARCHAR(255) NOT NULL,
    receiver_phone VARCHAR(20) NOT NULL,
    address        TEXT NOT NULL,
    landmarks      TEXT NOT NULL,                              -- repères obligatoires
    weight_kg      DECIMAL(8,2),
    payment_mode   VARCHAR(20),                                -- domaine à figer ultérieurement
    pickup_order   INTEGER,                                    -- ordre de tournée optimisé par l'IA
    status         VARCHAR(20) NOT NULL DEFAULT 'PENDING'      -- domaine à figer ultérieurement
);

-- ---------------------------------------------------------------------
-- Recoud la référence laissée en attente en V03 :
-- deliveries.batch_item_id -> batch_delivery_items(id).
-- ---------------------------------------------------------------------
ALTER TABLE deliveries
    ADD CONSTRAINT fk_deliveries_batch_item
    FOREIGN KEY (batch_item_id) REFERENCES batch_delivery_items(id);
