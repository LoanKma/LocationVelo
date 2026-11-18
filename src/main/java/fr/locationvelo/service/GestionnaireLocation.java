package fr.locationvelo.service;

import fr.locationvelo.depot.DepotClient;
import fr.locationvelo.depot.DepotVelocipede;
import fr.locationvelo.modele.Client;
import fr.locationvelo.modele.Velocipede;
import fr.locationvelo.notification.NotificateurLocation;
import fr.locationvelo.regle.RegleLocation;

import java.time.Clock;
import java.time.LocalDate;

/**
 * Service de location : recherche le vélo, recherche le client, applique les règles métier,
 * modifie les états et déclenche la notification.
 * Dépend des interfaces de dépôts (injection par constructeur).
 */
public class GestionnaireLocation {

    public static final int DUREE_LOCATION_JOURS = 7;

    private final DepotVelocipede depotVelocipede;
    private final DepotClient depotClient;
    private final RegleLocation regleLocation;
    private final NotificateurLocation notificateurLocation;
    private final Clock horloge;

    public GestionnaireLocation(DepotVelocipede depotVelocipede,
                                DepotClient depotClient,
                                RegleLocation regleLocation,
                                NotificateurLocation notificateurLocation,
                                Clock horloge) {
        this.depotVelocipede = depotVelocipede;
        this.depotClient = depotClient;
        this.regleLocation = regleLocation;
        this.notificateurLocation = notificateurLocation;
        this.horloge = horloge;
    }

    /**
     * @return la date de retour prévue (J + 7)
     */
    public LocalDate louer(String idVelocipede, String idClient) {
        Velocipede velocipede = depotVelocipede.trouverParId(idVelocipede)
                .orElseThrow(() -> new IllegalArgumentException("Vélo introuvable : " + idVelocipede));
        Client client = depotClient.trouverParId(idClient)
                .orElseThrow(() -> new IllegalArgumentException("Client introuvable : " + idClient));

        regleLocation.verifier(velocipede, client);

        velocipede.louer();
        client.incrementerLocations();

        LocalDate dateRetourPrevue = LocalDate.now(horloge).plusDays(DUREE_LOCATION_JOURS);
        notificateurLocation.notifierConfirmation(client, velocipede, dateRetourPrevue);
        return dateRetourPrevue;
    }
}
