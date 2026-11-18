package fr.locationvelo.notification;

import fr.locationvelo.modele.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class NotificateurRetardTest {

    @Mock
    private ServiceNotification serviceNotification;

    private NotificateurRetard notificateur;
    private Client client;

    @BeforeEach
    void setUp() {
        notificateur = new NotificateurRetard(serviceNotification);
        client = new Client("C1", "Alice");
    }

    @Test
    void unRetardStrictementPositifDeclencheUneNotificationAuClient() {
        // Act
        notificateur.notifierRetard(client, 3);

        // Assert
        verify(serviceNotification).envoyer(eq("C1"), contains("3 jour"));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 30})
    void toutRetardPositifDeclencheExactementUneNotification(int jours) {
        notificateur.notifierRetard(client, jours);

        verify(serviceNotification).envoyer(eq("C1"), anyString());
    }

    @Test
    void zeroJourDeRetardNeDeclencheAucuneNotification() {
        // Act
        notificateur.notifierRetard(client, 0);

        // Assert
        verify(serviceNotification, never()).envoyer(anyString(), anyString());
    }

    @Test
    void leMessageDeRetardMentionneLeNombreDeJours() {
        // Arrange
        ArgumentCaptor<String> message = ArgumentCaptor.forClass(String.class);

        // Act
        notificateur.notifierRetard(client, 1);

        // Assert
        verify(serviceNotification).envoyer(eq("C1"), message.capture());
        assertTrue(message.getValue().contains("Retard de 1 jour"));
    }

    @Test
    void unRetardNegatifEstRefuseSansNotification() {
        assertThrows(IllegalArgumentException.class, () -> notificateur.notifierRetard(client, -1));

        verifyNoInteractions(serviceNotification);
    }

    @Test
    void unClientAbsentEstRefuseSansNotification() {
        assertThrows(IllegalArgumentException.class, () -> notificateur.notifierRetard(null, 2));

        verifyNoInteractions(serviceNotification);
    }

    @Test
    void unServiceDeNotificationAbsentEstRefuse() {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> new NotificateurRetard(null));

        assertEquals("Le service de notification est obligatoire", exception.getMessage());
    }
}
