package fr.locationvelo.service;

import fr.locationvelo.depot.DepotClient;
import fr.locationvelo.depot.DepotVelocipede;
import fr.locationvelo.modele.Client;
import fr.locationvelo.modele.Velocipede;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GestionnaireRetourTest {

    @Mock
    private DepotVelocipede depotVelocipede;
    @Mock
    private DepotClient depotClient;

    private GestionnaireRetour gestionnaire;
    private Velocipede velo;
    private Client client;

    @BeforeEach
    void setUp() {
        gestionnaire = new GestionnaireRetour(depotVelocipede, depotClient);
        velo = new Velocipede("V1", "VTT", new BigDecimal("10.00"));
        client = new Client("C1", "Alice");
    }

    @Test
    void retourNominalRendLeVeloDisponibleEtDecrementeLeClient() {
        // Arrange : le client a loué le vélo
        velo.louer();
        client.incrementerLocations();
        when(depotVelocipede.trouverParId("V1")).thenReturn(Optional.of(velo));
        when(depotClient.trouverParId("C1")).thenReturn(Optional.of(client));

        // Act
        gestionnaire.retourner("V1", "C1");

        // Assert
        assertTrue(velo.estDisponible());
        assertEquals(0, client.getLocationsEnCours());
        verify(depotVelocipede).trouverParId("V1");
        verify(depotClient).trouverParId("C1");
    }

    @Test
    void veloInconnuEstRefuseSansRechercherLeClient() {
        // Arrange
        when(depotVelocipede.trouverParId("INCONNU")).thenReturn(Optional.empty());

        // Act
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> gestionnaire.retourner("INCONNU", "C1"));

        // Assert
        assertTrue(exception.getMessage().contains("Vélo introuvable"));
        verifyNoInteractions(depotClient);
    }

    @Test
    void clientInconnuEstRefuseEtLeVeloResteLoue() {
        // Arrange
        velo.louer();
        when(depotVelocipede.trouverParId("V1")).thenReturn(Optional.of(velo));
        when(depotClient.trouverParId("INCONNU")).thenReturn(Optional.empty());

        // Act
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> gestionnaire.retourner("V1", "INCONNU"));

        // Assert
        assertTrue(exception.getMessage().contains("Client introuvable"));
        assertFalse(velo.estDisponible());
    }

    @Test
    void rendreUnVeloQuiNEstPasEnLocationEstRefuseSansModifierLeClient() {
        // Arrange : vélo disponible, client avec une location sur un autre vélo
        client.incrementerLocations();
        when(depotVelocipede.trouverParId("V1")).thenReturn(Optional.of(velo));
        when(depotClient.trouverParId("C1")).thenReturn(Optional.of(client));

        // Act
        assertThrows(IllegalStateException.class, () -> gestionnaire.retourner("V1", "C1"));

        // Assert
        assertEquals(1, client.getLocationsEnCours());
    }

    @Test
    void unClientSansLocationEnCoursNePeutPasRendreEtLeVeloResteLoue() {
        // Arrange
        velo.louer();
        when(depotVelocipede.trouverParId("V1")).thenReturn(Optional.of(velo));
        when(depotClient.trouverParId("C1")).thenReturn(Optional.of(client));

        // Act
        assertThrows(IllegalStateException.class, () -> gestionnaire.retourner("V1", "C1"));

        // Assert
        assertFalse(velo.estDisponible());
        assertEquals(0, client.getLocationsEnCours());
    }
}
