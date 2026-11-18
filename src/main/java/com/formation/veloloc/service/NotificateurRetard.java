package com.formation.veloloc.service;

import com.formation.veloloc.modele.Client;
import com.formation.veloloc.modele.Velocipede;

public class NotificateurRetard {
    private final ServiceNotification serviceNotification;

    public NotificateurRetard(ServiceNotification serviceNotification) {
        this.serviceNotification = serviceNotification;
    }

    public void notifierRetard(Client client, Velocipede velocipede, int joursDeRetard) {
        if (joursDeRetard <= 0) {
            return;
        }

        String message = "Votre location du vélo '" + velocipede.getIdentifiant()
                + "' a " + joursDeRetard + " jour(s) de retard";

        serviceNotification.envoyerNotification(client.getIdentifiant(), message);
    }
}
