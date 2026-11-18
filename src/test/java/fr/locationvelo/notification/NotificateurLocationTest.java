package fr.locationvelo.notification;

import fr.locationvelo.modele.Client;
import fr.locationvelo.modele.Velocipede;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class NotificateurLocationTest {

    private static final LocalDate DATE_RETOUR = LocalDate.of(2026, 1, 8);

    @Mock
    private ServiceNotification serviceNotification;

    @Captor
    private ArgumentCaptor<String> message;

    private NotificateurLocation notificateur;
    private Client client;
    private Velocipede velo;

    @BeforeEach
    void setUp() {
        notificateur = new NotificateurLocation(serviceNotification);
        client = new Client("C1", "Alice");
        velo = new Velocipede("V1", "VTT", new BigDecimal("10.00"));
    }

    @Test
    void laConfirmationEstEnvoyeeUneSeuleFoisAuClient() {
        // Act
        notificateur.notifierConfirmation(client, velo, DATE_RETOUR);

        // Assert
        verify(serviceNotification, times(1)).envoyer(eq("C1"), anyString());
    }

    @Test
    void leMessageDeConfirmationContientLeVeloEtLaDateDeRetourPrevue() {
        // Act
        notificateur.notifierConfirmation(client, velo, DATE_RETOUR);

        // Assert
        verify(serviceNotification).envoyer(eq("C1"), message.capture());
        String contenu = message.getValue();
        assertAll(
                () -> assertTrue(contenu.contains("V1")),
                () -> assertTrue(contenu.contains("VTT")),
                () -> assertTrue(contenu.contains("2026-01-08"))
        );
    }

    @Test
    void uneConfirmationIncompleteEstRefuseeSansNotification() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> notificateur.notifierConfirmation(null, velo, DATE_RETOUR)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> notificateur.notifierConfirmation(client, null, DATE_RETOUR)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> notificateur.notifierConfirmation(client, velo, null))
        );

        verifyNoInteractions(serviceNotification);
    }

    @Test
    void unServiceDeNotificationAbsentEstRefuse() {
        assertThrows(IllegalArgumentException.class, () -> new NotificateurLocation(null));
    }
}
