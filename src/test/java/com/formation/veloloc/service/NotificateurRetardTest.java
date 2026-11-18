package com.formation.veloloc.service;

import com.formation.veloloc.modele.Client;
import com.formation.veloloc.modele.Velocipede;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificateurRetardTest {

    @Mock
    private ServiceNotification serviceNotification;

    @Test
    void unRetardDeclencheUneNotification() {
        NotificateurRetard notificateur = new NotificateurRetard(serviceNotification);
        Velocipede velo = new Velocipede("V001", "Ville", 12.0, true);
        Client client = new Client("C001", "Ada");

        notificateur.notifierRetard(client, velo, 3);

        verify(serviceNotification).envoyerNotification(eq("C001"), contains("3 jour(s) de retard"));
    }

    @Test
    void unRetardDUnJourDeclencheUneNotificationMentionnantLeVelo() {
        NotificateurRetard notificateur = new NotificateurRetard(serviceNotification);
        Velocipede velo = new Velocipede("V001", "Ville", 12.0, true);
        Client client = new Client("C001", "Ada");

        notificateur.notifierRetard(client, velo, 1);

        verify(serviceNotification, times(1)).envoyerNotification(eq("C001"), contains("V001"));
    }

    @Test
    void aucunRetardNeDeclencheAucuneNotification() {
        NotificateurRetard notificateur = new NotificateurRetard(serviceNotification);
        Velocipede velo = new Velocipede("V002", "VTT", 18.0, true);
        Client client = new Client("C002", "Alan");

        notificateur.notifierRetard(client, velo, 0);

        verify(serviceNotification, never()).envoyerNotification(anyString(), anyString());
    }

    @Test
    void unRetardNegatifNeDeclencheAucuneNotification() {
        NotificateurRetard notificateur = new NotificateurRetard(serviceNotification);
        Velocipede velo = new Velocipede("V002", "VTT", 18.0, true);
        Client client = new Client("C002", "Alan");

        notificateur.notifierRetard(client, velo, -2);

        verify(serviceNotification, never()).envoyerNotification(anyString(), anyString());
    }
}
