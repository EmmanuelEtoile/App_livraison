package com.asap.api.identity.domain;

/**
 * Modes de transport qu'un livreur déclare au KYC [Décision : modèle hybride].
 *
 * Un livreur qui possède un véhicule l'utilise (coût marginal : carburant).
 * Un livreur sans véhicule recourt à des modes payants (benskin, taxi) dont
 * le coût reste à sa charge, compensé par la composante distance du prix et,
 * le cas échéant, le supplément transport facturé au client (cf. CCT §9.1 / §11.1).
 */
public enum TransportMode {
    A_PIED,        // à pied (courses courtes en zone dense)
    BENSKIN,       // moto-taxi, en tant que passager
    MOTO_PROPRE,   // moto personnelle
    VOITURE        // voiture personnelle
}
