package com.formation.veloloc.modele;

import com.formation.veloloc.exception.VelocipedeIndisponibleException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class VelocipedeTest {

    @Test
    void unVeloDisponibleEstDisponible() {
        // Arrange
        Velocipede velo = new Velocipede("V001", "Ville", 12.0, true);

        // Act
        boolean disponible = velo.estDisponible();

        // Assert
        assertTrue(disponible);
    }

    @Test
    void unVeloIndisponibleNEstPasDisponible() {
        // Arrange
        Velocipede velo = new Velocipede("V002", "VTT", 18.0, false);

        // Act
        boolean disponible = velo.estDisponible();

        // Assert
        assertFalse(disponible);
    }

    @Test
    void unIdentifiantVideEstRefuse() {
        assertThrows(IllegalArgumentException.class,
                () -> new Velocipede("", "Ville", 12.0, true));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t"})
    void unIdentifiantNullOuBlancEstRefuse(String identifiant) {
        assertThrows(IllegalArgumentException.class,
                () -> new Velocipede(identifiant, "Ville", 12.0, true));
    }

    @Test
    void unTarifNegatifEstRefuse() {
        assertThrows(IllegalArgumentException.class,
                () -> new Velocipede("V001", "Ville", -0.01, true));
    }

    @Test
    void unTarifNulEstAccepte() {
        Velocipede velo = new Velocipede("V001", "Ville", 0.0, true);

        assertEquals(0.0, velo.getTarifJournalier());
    }

    @Test
    void unVeloConserveSesAttributs() {
        Velocipede velo = new Velocipede("V001", "Ville", 12.0, true);

        assertAll(
                () -> assertEquals("V001", velo.getIdentifiant()),
                () -> assertEquals("Ville", velo.getModele()),
                () -> assertEquals(12.0, velo.getTarifJournalier())
        );
    }

    @Test
    void retirerDeLaDisponibiliteRendLeVeloIndisponible() {
        // Arrange
        Velocipede velo = new Velocipede("V001", "Ville", 12.0, true);

        // Act
        velo.retirerDeLaDisponibilite();

        // Assert
        assertFalse(velo.estDisponible());
    }

    @Test
    void retirerUnVeloDejaIndisponibleEstRefuse() {
        Velocipede velo = new Velocipede("V001", "Ville", 12.0, false);

        assertThrows(VelocipedeIndisponibleException.class, velo::retirerDeLaDisponibilite);
        assertFalse(velo.estDisponible());
    }

    @Test
    void rendreDisponibleUnVeloLoueLeRendDeNouveauDisponible() {
        // Arrange
        Velocipede velo = new Velocipede("V001", "Ville", 12.0, true);
        velo.retirerDeLaDisponibilite();

        // Act
        velo.rendreDisponible();

        // Assert
        assertTrue(velo.estDisponible());
    }

    @Test
    void deuxVelosDeMemeIdentifiantSontEgaux() {
        Velocipede velo1 = new Velocipede("V001", "Ville", 12.0, true);
        Velocipede velo2 = new Velocipede("V001", "VTT", 18.0, false);

        assertEquals(velo1, velo2);
        assertEquals(velo1.hashCode(), velo2.hashCode());
    }
}
