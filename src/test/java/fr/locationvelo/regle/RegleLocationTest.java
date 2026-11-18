package fr.locationvelo.regle;

import fr.locationvelo.modele.Client;
import fr.locationvelo.modele.Velocipede;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegleLocationTest {

    private RegleLocation regle;
    private Velocipede velo;
    private Client client;

    @BeforeEach
    void setUp() {
        regle = new RegleLocation();
        velo = new Velocipede("V1", "VTT", new BigDecimal("10.00"));
        client = new Client("C1", "Alice");
    }

    @Test
    void leQuotaMaximalEstDeDeuxLocationsSimultanees() {
        assertEquals(2, RegleLocation.QUOTA_MAX_LOCATIONS);
    }

    @Test
    void locationAutoriseePourUnVeloDisponibleEtUnClientSansLocation() {
        assertDoesNotThrow(() -> regle.verifier(velo, client));
    }

    @Test
    void locationAutoriseePourUnClientJusteSousLeQuota() {
        // Arrange : 1 location en cours, quota = 2
        client.incrementerLocations();

        // Act + Assert
        assertDoesNotThrow(() -> regle.verifier(velo, client));
    }

    @Test
    void locationRefuseeSiLeVeloEstIndisponible() {
        // Arrange
        velo.louer();

        // Act
        IllegalStateException exception =
                assertThrows(IllegalStateException.class, () -> regle.verifier(velo, client));

        // Assert
        assertTrue(exception.getMessage().contains("indisponible"));
    }

    @Test
    void locationRefuseeSiLeClientAAtteintSonQuota() {
        // Arrange
        client.incrementerLocations();
        client.incrementerLocations();

        // Act
        IllegalStateException exception =
                assertThrows(IllegalStateException.class, () -> regle.verifier(velo, client));

        // Assert
        assertTrue(exception.getMessage().contains("quota"));
    }

    @Test
    void verifierNeModifiePasLesEtats() {
        regle.verifier(velo, client);

        assertTrue(velo.estDisponible());
        assertEquals(0, client.getLocationsEnCours());
    }

    @Test
    void locationRefuseeSansVelo() {
        assertThrows(IllegalArgumentException.class, () -> regle.verifier(null, client));
    }

    @Test
    void locationRefuseeSansClient() {
        assertThrows(IllegalArgumentException.class, () -> regle.verifier(velo, null));
    }
}
