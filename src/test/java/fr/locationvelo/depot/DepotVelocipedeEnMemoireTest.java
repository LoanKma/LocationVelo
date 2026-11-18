package fr.locationvelo.depot;

import fr.locationvelo.modele.Velocipede;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DepotVelocipedeEnMemoireTest {

    private DepotVelocipede depot;

    @BeforeEach
    void setUp() {
        depot = new DepotVelocipedeEnMemoire();
    }

    @Test
    void unVeloAjouteEstRetrouveParSonIdentifiant() {
        // Arrange
        Velocipede velo = new Velocipede("V1", "VTT", new BigDecimal("10.00"));

        // Act
        depot.ajouter(velo);
        Optional<Velocipede> resultat = depot.trouverParId("V1");

        // Assert
        assertTrue(resultat.isPresent());
        assertSame(velo, resultat.get());
    }

    @Test
    void rechercherUnVeloAbsentRenvoieUnOptionalVide() {
        depot.ajouter(new Velocipede("V1", "VTT", new BigDecimal("10.00")));

        assertTrue(depot.trouverParId("INCONNU").isEmpty());
    }

    @Test
    void ajouterUnVeloNullEstRefuse() {
        assertThrows(IllegalArgumentException.class, () -> depot.ajouter(null));
    }

    @Test
    void ajouterDeuxVelosAvecLeMemeIdentifiantEstRefuse() {
        // Arrange
        Velocipede original = new Velocipede("V1", "VTT", new BigDecimal("10.00"));
        depot.ajouter(original);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> depot.ajouter(new Velocipede("V1", "Ville", new BigDecimal("8.00"))));
        assertSame(original, depot.trouverParId("V1").orElseThrow());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    void rechercherAvecUnIdentifiantAbsentOuVideEstRefuse(String id) {
        assertThrows(IllegalArgumentException.class, () -> depot.trouverParId(id));
    }
}
