package com.formation.veloloc.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class CalculateurTarifTest {

    private final CalculateurTarif calculateur = new CalculateurTarif();

    @ParameterizedTest(name = "{0} €/jour pendant {1} jour(s) -> {2} €")
    @CsvSource({
        "10.0, 0, 1.0",
        "10.0, 1, 11.0",
        "12.5, 2, 26.0",
        "20.0, 7, 141.0"
    })
    void calculerLeTarifSelonLeNombreDeJours(double tarifJournalier,
                                              int nombreJours,
                                              double montantAttendu) {
        // Act
        double montant = calculateur.calculer(tarifJournalier, nombreJours);

        // Assert
        assertEquals(montantAttendu, montant, 0.001);
    }

    @Test
    void unTarifNegatifEstRefuse() {
        assertThrows(IllegalArgumentException.class, () -> calculateur.calculer(-1.0, 2));
    }

    @Test
    void unNombreDeJoursNegatifEstRefuse() {
        assertThrows(IllegalArgumentException.class, () -> calculateur.calculer(10.0, -1));
    }
}
