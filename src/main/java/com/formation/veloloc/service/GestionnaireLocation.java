package com.formation.veloloc.service;

import com.formation.veloloc.depot.DepotClient;
import com.formation.veloloc.depot.DepotVelocipede;
import com.formation.veloloc.exception.ClientInconnuException;
import com.formation.veloloc.exception.VelocipedeIndisponibleException;
import com.formation.veloloc.modele.Client;
import com.formation.veloloc.modele.Velocipede;

import java.time.LocalDate;

public class GestionnaireLocation {
    private final DepotVelocipede depotVelocipede;
    private final DepotClient depotClient;
    private final RegleLocation regleLocation;
    private final NotificateurLocation notificateurLocation;

    public GestionnaireLocation(DepotVelocipede depotVelocipede,
                                 DepotClient depotClient,
                                 RegleLocation regleLocation,
                                 NotificateurLocation notificateurLocation) {
        this.depotVelocipede = depotVelocipede;
        this.depotClient = depotClient;
        this.regleLocation = regleLocation;
        this.notificateurLocation = notificateurLocation;
    }

    public void louer(String identifiantVelo, String identifiantClient) {
        // 1. rechercher le vélo
        Velocipede velo = depotVelocipede.rechercherParIdentifiant(identifiantVelo)
                .orElseThrow(() -> new VelocipedeIndisponibleException(identifiantVelo));

        // 2. rechercher le client
        Client client = depotClient.rechercherParIdentifiant(identifiantClient)
                .orElseThrow(() -> new ClientInconnuException(identifiantClient));

        // 3. vérifier les règles
        regleLocation.verifierLocationAutorisee(velo, client);

        // 4. modifier les états
        velo.retirerDeLaDisponibilite();
        client.incrementerLocations();

        // 5. notifier avec une date de retour prévue à J+7
        LocalDate dateRetourPrevue = LocalDate.now().plusDays(7);
        notificateurLocation.notifierConfirmationLocation(client, velo, dateRetourPrevue);
    }
}
