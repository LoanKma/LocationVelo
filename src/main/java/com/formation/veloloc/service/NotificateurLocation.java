package com.formation.veloloc.service;

import com.formation.veloloc.modele.Client;
import com.formation.veloloc.modele.Velocipede;

import java.time.LocalDate;

public class NotificateurLocation {
    private final ServiceNotification serviceNotification;

    public NotificateurLocation(ServiceNotification serviceNotification) {
        this.serviceNotification = serviceNotification;
    }

    public void notifierConfirmationLocation(Client client, Velocipede velocipede,
                                              LocalDate dateRetourPrevue) {
        String message = "Votre location du vélo '" + velocipede.getIdentifiant()
                + "' (" + velocipede.getModele() + ") est confirmée. Retour prévu le "
                + dateRetourPrevue;

        serviceNotification.envoyerNotification(client.getIdentifiant(), message);
    }
}
