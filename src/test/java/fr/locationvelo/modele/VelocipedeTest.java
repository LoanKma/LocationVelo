package fr.locationvelo.modele;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VelocipedeTest {

    private static final BigDecimal TARIF = new BigDecimal("10.00");

    @Test
    void unNouveauVeloConserveSesAttributsEtEstDisponible() {
        // Act
        Velocipede velo = new Velocipede("V1", "VTT", TARIF);

        // Assert
        assertAll(
                () -> assertEquals("V1", velo.getId()),
                () -> assertEquals("VTT", velo.getModele()),
                () -> assertEquals(TARIF, velo.getTarifJournalier()),
                () -> assertTrue(velo.estDisponible())
        );
    }

    @Test
    void unTarifJournalierNulEstAccepte() {
        Velocipede velo = new Velocipede("V1", "VTT", BigDecimal.ZERO);

        assertEquals(BigDecimal.ZERO, velo.getTarifJournalier());
    }

    @Test
    void louerRendLeVeloIndisponible() {
        // Arrange
        Velocipede velo = new Velocipede("V1", "VTT", TARIF);

        // Act
        velo.louer();

        // Assert
        assertFalse(velo.estDisponible());
    }

    @Test
    void rendreUnVeloLoueLeRendDeNouveauDisponible() {
        // Arrange
        Velocipede velo = new Velocipede("V1", "VTT", TARIF);
        velo.louer();

        // Act
        velo.rendre();

        // Assert
        assertTrue(velo.estDisponible());
    }

    @Test
    void louerUnVeloDejaLoueEstRefuse() {
        Velocipede velo = new Velocipede("V1", "VTT", TARIF);
        velo.louer();

        assertThrows(IllegalStateException.class, velo::louer);
        assertFalse(velo.estDisponible());
    }

    @Test
    void rendreUnVeloDisponibleEstRefuse() {
        Velocipede velo = new Velocipede("V1", "VTT", TARIF);

        assertThrows(IllegalStateException.class, velo::rendre);
        assertTrue(velo.estDisponible());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t"})
    void unIdentifiantAbsentOuVideEstRefuse(String id) {
        assertThrows(IllegalArgumentException.class, () -> new Velocipede(id, "VTT", TARIF));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    void unModeleAbsentOuVideEstRefuse(String modele) {
        assertThrows(IllegalArgumentException.class, () -> new Velocipede("V1", modele, TARIF));
    }

    @Test
    void unTarifJournalierNegatifEstRefuse() {
        BigDecimal tarifNegatif = new BigDecimal("-0.01");

        assertThrows(IllegalArgumentException.class, () -> new Velocipede("V1", "VTT", tarifNegatif));
    }

    @Test
    void unTarifJournalierAbsentEstRefuse() {
        assertThrows(IllegalArgumentException.class, () -> new Velocipede("V1", "VTT", null));
    }
}
