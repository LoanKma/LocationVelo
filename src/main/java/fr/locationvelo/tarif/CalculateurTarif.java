package fr.locationvelo.tarif;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Montant = tarif journalier x nombre de jours + forfait de service de 1 €.
 */
public class CalculateurTarif {

    public static final BigDecimal FORFAIT_SERVICE = new BigDecimal("1.00");

    public BigDecimal calculer(BigDecimal tarifJournalier, int nombreJours) {
        if (tarifJournalier == null || tarifJournalier.signum() < 0) {
            throw new IllegalArgumentException("Le tarif journalier doit être positif ou nul");
        }
        if (nombreJours < 0) {
            throw new IllegalArgumentException("Le nombre de jours doit être positif ou nul");
        }
        return tarifJournalier
                .multiply(BigDecimal.valueOf(nombreJours))
                .add(FORFAIT_SERVICE)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
