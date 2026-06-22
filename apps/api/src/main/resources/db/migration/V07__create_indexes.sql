-- =====================================================================
-- V07 — Index : optimisation des requêtes.
-- Source : Cahier de Conception §3.3 (+ index avertissements de §3.2).
-- =====================================================================

-- ---------------------------------------------------------------------
-- Index spatiaux GIST : indispensables pour les requêtes de proximité
-- PostGIS (« quels livreurs à moins de X km de ce point »).
-- ---------------------------------------------------------------------
CREATE INDEX idx_livreurs_location ON livreurs USING GIST (current_location);
CREATE INDEX idx_deliveries_pickup ON deliveries USING GIST (pickup_location);
CREATE INDEX idx_partners_location ON pickup_partners USING GIST (location);

-- ---------------------------------------------------------------------
-- Index métier (B-tree).
-- ---------------------------------------------------------------------
CREATE INDEX idx_deliveries_status   ON deliveries (status);
CREATE INDEX idx_deliveries_client   ON deliveries (client_id);
CREATE INDEX idx_deliveries_livreur  ON deliveries (livreur_id);
CREATE INDEX idx_deliveries_created  ON deliveries (created_at DESC);

-- Index partiels : ne couvrent que les lignes utiles (plus petits, plus rapides).
CREATE INDEX idx_deliveries_scheduled ON deliveries (scheduled_date, scheduled_slot)
    WHERE status = 'SCHEDULED';
CREATE INDEX idx_receivers_otp ON receivers (otp_code)
    WHERE otp_verified = false;
CREATE INDEX idx_deposits_expiry ON partner_deposits (expires_at)
    WHERE status = 'DEPOSITED';
CREATE INDEX idx_batch_orders_day ON batch_orders (scheduled_day, status);

-- ---------------------------------------------------------------------
-- avertissements : la conception proposait
--   ... ON avertissements(user_id) WHERE expires_at > NOW()
-- mais PostgreSQL REFUSE NOW() dans un index partiel (fonction non
-- immuable). Correction : on indexe (user_id, expires_at) et le filtre
-- « actif » (expires_at > now()) est appliqué dans la requête, qui
-- utilisera quand même cet index.
-- ---------------------------------------------------------------------
CREATE INDEX idx_avert_user_active ON avertissements (user_id, expires_at);
