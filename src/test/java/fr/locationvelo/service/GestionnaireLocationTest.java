package fr.locationvelo.service;

import fr.locationvelo.depot.DepotClient;
import fr.locationvelo.depot.DepotVelocipede;
import fr.locationvelo.modele.Client;
import fr.locationvelo.modele.Velocipede;
import fr.locationvelo.notification.NotificateurLocation;
import fr.locationvelo.regle.RegleLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Dépôts et notificateur mockés ; RegleLocation reste réelle (logique simple et déterministe).
 * L'horloge est fixée pour rendre la date J + 7 déterministe.
 */
@ExtendWith(MockitoExtension.class)
class GestionnaireLocationTest {

    private static final LocalDate AUJOURDHUI = LocalDate.of(2026, 1, 1);
    private static final Clock HORLOGE_FIXE =
            Clock.fixed(Instant.parse("2026-01-01T10:00:00Z"), ZoneOffset.UTC);

    @Mock
    private DepotVelocipede depotVelocipede;
    @Mock
    private DepotClient depotClient;
    @Mock
    private NotificateurLocation notificateurLocation;

    private GestionnaireLocation gestionnaire;
    private Velocipede velo;
    private Client client;

    @BeforeEach
    void setUp() {
        gestionnaire = new GestionnaireLocation(depotVelocipede, depotClient, new RegleLocation(),
                notificateurLocation, HORLOGE_FIXE);
        velo = new Velocipede("V1", "VTT", new BigDecimal("10.00"));
        client = new Client("C1", "Alice");
    }

    @Test
    void locationNominaleRendLeVeloIndisponibleEtIncrementeLeClient() {
        // Arrange
        when(depotVelocipede.trouverParId("V1")).thenReturn(Optional.of(velo));
        when(depotClient.trouverParId("C1")).thenReturn(Optional.of(client));

        // Act
        gestionnaire.louer("V1", "C1");

        // Assert
        assertFalse(velo.estDisponible());
        assertEquals(1, client.getLocationsEnCours());
    }

    @Test
    void locationNominaleFixeLaDateDeRetourAJPlusSeptEtNotifieLeClient() {
        // Arrange
        when(depotVelocipede.trouverParId("V1")).thenReturn(Optional.of(velo));
        when(depotClient.trouverParId("C1")).thenReturn(Optional.of(client));
        LocalDate dateAttendue = AUJOURDHUI.plusDays(7);

        // Act
        LocalDate dateRetourPrevue = gestionnaire.louer("V1", "C1");

        // Assert
        assertEquals(dateAttendue, dateRetourPrevue);
        verify(notificateurLocation).notifierConfirmation(client, velo, dateAttendue);
    }

    @Test
    void veloInconnuEstRefuseSansRechercherLeClientNiNotifier() {
        // Arrange
        when(depotVelocipede.trouverParId("INCONNU")).thenReturn(Optional.empty());

        // Act
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> gestionnaire.louer("INCONNU", "C1"));

        // Assert
        assertTrue(exception.getMessage().contains("Vélo introuvable"));
        verifyNoInteractions(depotClient, notificateurLocation);
    }

    @Test
    void clientInconnuEstRefuseSansModifierLeVeloNiNotifier() {
        // Arrange
        when(depotVelocipede.trouverParId("V1")).thenReturn(Optional.of(velo));
        when(depotClient.trouverParId("INCONNU")).thenReturn(Optional.empty());

        // Act
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> gestionnaire.louer("V1", "INCONNU"));

        // Assert
        assertTrue(exception.getMessage().contains("Client introuvable"));
        assertTrue(velo.estDisponible());
        verifyNoInteractions(notificateurLocation);
    }

    @Test
    void veloIndisponibleEstRefuseSansModifierLeClientNiNotifier() {
        // Arrange
        velo.louer();
        when(depotVelocipede.trouverParId("V1")).thenReturn(Optional.of(velo));
        when(depotClient.trouverParId("C1")).thenReturn(Optional.of(client));

        // Act
        assertThrows(IllegalStateException.class, () -> gestionnaire.louer("V1", "C1"));

        // Assert
        assertEquals(0, client.getLocationsEnCours());
        verify(notificateurLocation, never()).notifierConfirmation(any(), any(), any());
    }

    @Test
    void quotaAtteintEstRefuseSansModifierLesEtatsNiNotifier() {
        // Arrange
        client.incrementerLocations();
        client.incrementerLocations();
        when(depotVelocipede.trouverParId("V1")).thenReturn(Optional.of(velo));
        when(depotClient.trouverParId("C1")).thenReturn(Optional.of(client));

        // Act
        assertThrows(IllegalStateException.class, () -> gestionnaire.louer("V1", "C1"));

        // Assert
        assertTrue(velo.estDisponible());
        assertEquals(2, client.getLocationsEnCours());
        verify(notificateurLocation, never()).notifierConfirmation(any(), any(), any());
    }

    @Test
    void clientJusteSousLeQuotaPeutEncoreLouer() {
        // Arrange
        client.incrementerLocations();
        when(depotVelocipede.trouverParId("V1")).thenReturn(Optional.of(velo));
        when(depotClient.trouverParId("C1")).thenReturn(Optional.of(client));

        // Act
        gestionnaire.louer("V1", "C1");

        // Assert
        assertEquals(2, client.getLocationsEnCours());
        verify(notificateurLocation).notifierConfirmation(client, velo, AUJOURDHUI.plusDays(7));
    }
}
