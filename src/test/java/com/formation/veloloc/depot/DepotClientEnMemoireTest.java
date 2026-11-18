package com.formation.veloloc.depot;

import com.formation.veloloc.modele.Client;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DepotClientEnMemoireTest {

    private final DepotClient depot = new DepotClientEnMemoire();

    @Test
    void unClientAjouteEstRetrouveParSonIdentifiant() {
        // Arrange
        Client client = new Client("C001", "Ada");

        // Act
        depot.ajouter(client);
        Optional<Client> resultat = depot.rechercherParIdentifiant("C001");

        // Assert
        assertTrue(resultat.isPresent());
        assertSame(client, resultat.get());
    }

    @Test
    void unIdentifiantAbsentRenvoieUnOptionalVide() {
        // Arrange
        depot.ajouter(new Client("C001", "Ada"));

        // Act
        Optional<Client> resultat = depot.rechercherParIdentifiant("inconnu");

        // Assert
        assertTrue(resultat.isEmpty());
    }

    @Test
    void ajouterUnClientNullLeveUneIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> depot.ajouter(null));
        assertTrue(depot.listerTous().isEmpty());
    }

    @Test
    void listerTousRenvoieTousLesClientsAjoutes() {
        depot.ajouter(new Client("C001", "Ada"));
        depot.ajouter(new Client("C002", "Alan"));

        assertEquals(2, depot.listerTous().size());
    }
}
