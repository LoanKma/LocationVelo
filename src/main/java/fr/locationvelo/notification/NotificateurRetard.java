package fr.locationvelo.notification;

import fr.locationvelo.modele.Client;

/**
 * Prévient un client en retard. Un retard strictement positif déclenche une notification,
 * zéro jour de retard n'en déclenche aucune, un retard négatif est refusé.
 */
public class NotificateurRetard {

    private final ServiceNotification serviceNotification;

    public NotificateurRetard(ServiceNotification serviceNotification) {
        if (serviceNotification == null) {
            throw new IllegalArgumentException("Le service de notification est obligatoire");
        }
        this.serviceNotification = serviceNotification;
    }

    public void notifierRetard(Client client, int joursDeRetard) {
        if (client == null) {
            throw new IllegalArgumentException("Le client est obligatoire");
        }
        if (joursDeRetard < 0) {
            throw new IllegalArgumentException("Le nombre de jours de retard ne peut pas être négatif");
        }
        if (joursDeRetard == 0) {
            return;
        }
        serviceNotification.envoyer(client.getId(),
                "Retard de " + joursDeRetard + " jour(s) sur votre location. Merci de rendre le vélo.");
    }
}
