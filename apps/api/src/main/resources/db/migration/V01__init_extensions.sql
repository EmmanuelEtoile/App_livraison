-- =====================================================================
-- V01 — Extensions PostgreSQL
-- Activées une seule fois, avant toute création de table.
-- =====================================================================

-- PostGIS : types et fonctions géospatiales.
-- Indispensable pour la colonne GEOGRAPHY(POINT, 4326) (position des
-- livreurs, points de retrait/livraison) et les calculs de proximité.
CREATE EXTENSION IF NOT EXISTS postgis;

-- pgcrypto : fonctions cryptographiques + garantit gen_random_uuid()
-- quelle que soit la version de PostgreSQL (filet de sécurité ;
-- gen_random_uuid() est natif depuis PostgreSQL 13).
CREATE EXTENSION IF NOT EXISTS pgcrypto;
