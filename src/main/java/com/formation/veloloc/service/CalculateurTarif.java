package com.formation.veloloc.service;

public class CalculateurTarif {
    private static final double TARIF_SERVICE = 1.0;

    public double calculer(double tarifJournalier, int nombreJours) {
        if (tarifJournalier < 0 || nombreJours < 0) {
            throw new IllegalArgumentException("Les paramètres ne peuvent pas être négatifs");
        }

        return tarifJournalier * nombreJours + TARIF_SERVICE;
    }
}
