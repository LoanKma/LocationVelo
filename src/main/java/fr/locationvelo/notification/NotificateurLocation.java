package fr.locationvelo.notification;

import fr.locationvelo.modele.Client;
import fr.locationvelo.modele.Velocipede;

import java.time.LocalDate;

/**
 * Envoie la notification de confirmation d'une location.
 */
public class NotificateurLocation {

    private final ServiceNotification serviceNotification;

    public NotificateurLocation(ServiceNotification serviceNotification) {
        if (serviceNotification == null) {
            throw new IllegalArgumentException("Le service de notification est obligatoire");
        }
        this.serviceNotification = serviceNotification;
    }

    public void notifierConfirmation(Client client, Velocipede velocipede, LocalDate dateRetourPrevue) {
        if (client == null || velocipede == null || dateRetourPrevue == null) {
            throw new IllegalArgumentException("Le client, le vélo et la date de retour prévue sont obligatoires");
        }
        serviceNotification.envoyer(client.getId(),
                "Location confirmée du vélo " + velocipede.getId()
                        + " (" + velocipede.getModele() + "). Retour prévu le " + dateRetourPrevue + ".");
    }
}
