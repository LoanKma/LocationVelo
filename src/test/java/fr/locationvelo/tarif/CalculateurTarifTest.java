package fr.locationvelo.tarif;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalculateurTarifTest {

    private CalculateurTarif calculateur;

    @BeforeEach
    void setUp() {
        calculateur = new CalculateurTarif();
    }

    @ParameterizedTest(name = "{0} €/jour x {1} jour(s) = {2} €")
    @CsvSource({
            "10.00, 0, 1.00",
            "10.00, 1, 11.00",
            "12.50, 2, 26.00",
            "20.00, 7, 141.00"
    })
    void calculeLeMontantAvecLeForfaitDeService(String tarifJournalier, int jours, String montantAttendu) {
        // Act
        BigDecimal montant = calculateur.calculer(new BigDecimal(tarifJournalier), jours);

        // Assert
        assertEquals(new BigDecimal(montantAttendu), montant);
    }

    @Test
    void unTarifJournalierNulNeFactureQueLeForfait() {
        assertEquals(new BigDecimal("1.00"), calculateur.calculer(BigDecimal.ZERO, 5));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -7, Integer.MIN_VALUE})
    void unNombreDeJoursNegatifEstRefuse(int jours) {
        BigDecimal tarif = new BigDecimal("10.00");

        assertThrows(IllegalArgumentException.class, () -> calculateur.calculer(tarif, jours));
    }

    @ParameterizedTest
    @ValueSource(strings = {"-0.01", "-10.00"})
    void unTarifJournalierNegatifEstRefuse(String tarif) {
        BigDecimal tarifNegatif = new BigDecimal(tarif);

        assertThrows(IllegalArgumentException.class, () -> calculateur.calculer(tarifNegatif, 1));
    }

    @Test
    void unTarifJournalierAbsentEstRefuse() {
        assertThrows(IllegalArgumentException.class, () -> calculateur.calculer(null, 1));
    }
}
