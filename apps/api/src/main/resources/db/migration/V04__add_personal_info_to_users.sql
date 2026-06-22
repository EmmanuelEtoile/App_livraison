-- =====================================================================
-- V04 — État civil des comptes
-- nom / prénom        : tous les comptes (identité d'affichage).
-- sexe / naissance    : personnes physiques UNIQUEMENT (NULL sinon).
--   La règle « obligatoire pour une personne physique » est appliquée
--   au niveau applicatif (le rôle ne distingue pas personne physique /
--   morale : un client particulier et une entreprise ont tous deux le
--   rôle CLIENT). La base ne garantit ici que le domaine de valeurs.
-- =====================================================================

ALTER TABLE users
    ADD COLUMN last_name     VARCHAR(100) NOT NULL DEFAULT '',   -- nom
    ADD COLUMN first_name    VARCHAR(100) NOT NULL DEFAULT '',   -- prénom
    ADD COLUMN gender        VARCHAR(10) CHECK (gender IN ('M','F')),
    ADD COLUMN date_of_birth DATE;
