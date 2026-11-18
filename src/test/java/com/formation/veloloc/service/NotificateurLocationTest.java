package com.formation.veloloc.service;

import com.formation.veloloc.modele.Client;
import com.formation.veloloc.modele.Velocipede;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificateurLocationTest {

    @Mock
    private ServiceNotification serviceNotification;

    @Test
    void uneConfirmationDeLocationEnvoieUneNotification() {
        NotificateurLocation notificateur = new NotificateurLocation(serviceNotification);
        Client client = new Client("C001", "Ada");
        Velocipede velo = new Velocipede("V001", "Ville", 12.0, true);

        LocalDate dateRetour = LocalDate.of(2030, 5, 10);

        notificateur.notifierConfirmationLocation(client, velo, dateRetour);

        ArgumentCaptor<String> message = ArgumentCaptor.forClass(String.class);
        verify(serviceNotification).envoyerNotification(eq("C001"), message.capture());
        assertAll(
                () -> assertTrue(message.getValue().contains("V001")),
                () -> assertTrue(message.getValue().contains("2030-05-10"))
        );
    }
}
