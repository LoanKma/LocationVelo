package fr.locationvelo.modele;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ClientTest {

    @Test
    void unNouveauClientConserveSesAttributsEtNaAucuneLocation() {
        // Act
        Client client = new Client("C1", "Alice");

        // Assert
        assertAll(
                () -> assertEquals("C1", client.getId()),
                () -> assertEquals("Alice", client.getNom()),
                () -> assertEquals(0, client.getLocationsEnCours())
        );
    }

    @Test
    void incrementerLocationsAugmenteLeCompteurDeUn() {
        // Arrange
        Client client = new Client("C1", "Alice");

        // Act
        client.incrementerLocations();

        // Assert
        assertEquals(1, client.getLocationsEnCours());
    }

    @Test
    void decrementerLocationsDiminueLeCompteurDeUn() {
        // Arrange
        Client client = new Client("C1", "Alice");
        client.incrementerLocations();
        client.incrementerLocations();

        // Act
        client.decrementerLocations();

        // Assert
        assertEquals(1, client.getLocationsEnCours());
    }

    @Test
    void decrementerSansLocationEnCoursEstRefuseEtLeCompteurResteAZero() {
        Client client = new Client("C1", "Alice");

        assertThrows(IllegalStateException.class, client::decrementerLocations);
        assertEquals(0, client.getLocationsEnCours());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    void unIdentifiantAbsentOuVideEstRefuse(String id) {
        assertThrows(IllegalArgumentException.class, () -> new Client(id, "Alice"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    void unNomAbsentOuVideEstRefuse(String nom) {
        assertThrows(IllegalArgumentException.class, () -> new Client("C1", nom));
    }
}
