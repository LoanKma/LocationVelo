package fr.locationvelo.depot;

import fr.locationvelo.modele.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DepotClientEnMemoireTest {

    private DepotClient depot;

    @BeforeEach
    void setUp() {
        depot = new DepotClientEnMemoire();
    }

    @Test
    void unClientAjouteEstRetrouveParSonIdentifiant() {
        // Arrange
        Client client = new Client("C1", "Alice");

        // Act
        depot.ajouter(client);
        Optional<Client> resultat = depot.trouverParId("C1");

        // Assert
        assertTrue(resultat.isPresent());
        assertSame(client, resultat.get());
    }

    @Test
    void rechercherUnClientAbsentRenvoieUnOptionalVide() {
        depot.ajouter(new Client("C1", "Alice"));

        assertTrue(depot.trouverParId("INCONNU").isEmpty());
    }

    @Test
    void ajouterUnClientNullEstRefuse() {
        assertThrows(IllegalArgumentException.class, () -> depot.ajouter(null));
    }

    @Test
    void ajouterDeuxClientsAvecLeMemeIdentifiantEstRefuse() {
        // Arrange
        Client original = new Client("C1", "Alice");
        depot.ajouter(original);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> depot.ajouter(new Client("C1", "Bob")));
        assertSame(original, depot.trouverParId("C1").orElseThrow());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    void rechercherAvecUnIdentifiantAbsentOuVideEstRefuse(String id) {
        assertThrows(IllegalArgumentException.class, () -> depot.trouverParId(id));
    }
}
