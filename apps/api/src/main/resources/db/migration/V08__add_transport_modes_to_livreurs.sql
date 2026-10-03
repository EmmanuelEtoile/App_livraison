-- =====================================================================
-- V08 — Modes de transport du livreur [Décision : modèle hybride]
-- Remplace l'ancien champ unique `mode_transport` (obsolète : il supposait
-- que « l'IA détermine le mode ») par la CAPACITÉ DÉCLARÉE au KYC :
--   - has_own_vehicle : le livreur possède son propre véhicule ou non ;
--   - transport_modes : l'ensemble des modes qu'il peut utiliser.
-- Un livreur sans véhicule utilise des modes payants (benskin, taxi) à sa
-- charge ; l'affectation en tient compte (cf. CCT §11.1).
-- =====================================================================

-- Champ mono-mode devenu obsolète.
ALTER TABLE livreurs DROP COLUMN IF EXISTS mode_transport;

-- Possession d'un véhicule propre (sinon transport payant à la charge du livreur).
ALTER TABLE livreurs
    ADD COLUMN has_own_vehicle BOOLEAN NOT NULL DEFAULT false;

-- Ensemble des modes déclarés. Valeurs attendues (validées côté Java via l'enum
-- TransportMode) : A_PIED, BENSKIN, MOTO_PROPRE, VOITURE.
ALTER TABLE livreurs
    ADD COLUMN transport_modes TEXT[] NOT NULL DEFAULT '{}';

COMMENT ON COLUMN livreurs.has_own_vehicle IS
    'Le livreur possede son propre vehicule (sinon transport paye : benskin/taxi, a sa charge)';
COMMENT ON COLUMN livreurs.transport_modes IS
    'Modes declares au KYC : A_PIED, BENSKIN, MOTO_PROPRE, VOITURE';
