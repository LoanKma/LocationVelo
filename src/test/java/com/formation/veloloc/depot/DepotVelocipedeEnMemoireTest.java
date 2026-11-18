package com.formation.veloloc.depot;

import com.formation.veloloc.modele.Velocipede;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DepotVelocipedeEnMemoireTest {

    private final DepotVelocipede depot = new DepotVelocipedeEnMemoire();

    @Test
    void unVeloAjouteEstRetrouveParSonIdentifiant() {
        // Arrange
        Velocipede velo = new Velocipede("V001", "Ville", 12.0, true);

        // Act
        depot.ajouter(velo);
        Optional<Velocipede> resultat = depot.rechercherParIdentifiant("V001");

        // Assert
        assertTrue(resultat.isPresent());
        assertSame(velo, resultat.get());
    }

    @Test
    void unIdentifiantAbsentNeRenvoieRien() {
        // Arrange
        depot.ajouter(new Velocipede("V001", "Ville", 12.0, true));

        // Act
        Optional<Velocipede> resultat = depot.rechercherParIdentifiant("inconnu");

        // Assert
        assertTrue(resultat.isEmpty());
    }

    @Test
    void ajouterUnVeloNullLeveUneIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> depot.ajouter(null));
        assertTrue(depot.listerTous().isEmpty());
    }

    @Test
    void listerTousRenvoieTousLesVelosAjoutes() {
        // Arrange
        Velocipede velo1 = new Velocipede("V001", "Ville", 12.0, true);
        Velocipede velo2 = new Velocipede("V002", "VTT", 18.0, true);
        depot.ajouter(velo1);
        depot.ajouter(velo2);

        // Act + Assert
        assertEquals(2, depot.listerTous().size());
        assertTrue(depot.listerTous().containsAll(java.util.List.of(velo1, velo2)));
    }
}
