package com.formation.veloloc.modele;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    @Test
    void unClientEstInitialementSansLocation() {
        // Act
        Client client = new Client("C001", "Ada");

        // Assert
        assertAll(
                () -> assertEquals("C001", client.getIdentifiant()),
                () -> assertEquals("Ada", client.getNom()),
                () -> assertEquals(0, client.getLocationsEnCours())
        );
    }

    @Test
    void incrementerLocationsAugmenteLeCompteur() {
        // Arrange
        Client client = new Client("C001", "Ada");

        // Act
        client.incrementerLocations();

        // Assert
        assertEquals(1, client.getLocationsEnCours());
    }

    @Test
    void decrementerLocationsDiminueLeCompteur() {
        // Arrange
        Client client = new Client("C001", "Ada");
        client.incrementerLocations();
        client.incrementerLocations();

        // Act
        client.decrementerLocations();

        // Assert
        assertEquals(1, client.getLocationsEnCours());
    }

    @Test
    void decrementerLocationsNePassePasSousZero() {
        // Arrange
        Client client = new Client("C001", "Ada");

        // Act
        client.decrementerLocations();

        // Assert
        assertEquals(0, client.getLocationsEnCours());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    void unIdentifiantNullOuVideEstRefuse(String identifiant) {
        assertThrows(IllegalArgumentException.class, () -> new Client(identifiant, "Ada"));
    }
}
