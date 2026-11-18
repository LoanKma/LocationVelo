package fr.locationvelo.service;

import fr.locationvelo.depot.DepotClient;
import fr.locationvelo.depot.DepotVelocipede;
import fr.locationvelo.modele.Client;
import fr.locationvelo.modele.Velocipede;

/**
 * Service de retour : retrouve le vélo et le client, rend le vélo disponible
 * et diminue le nombre de locations en cours du client.
 */
public class GestionnaireRetour {

    private final DepotVelocipede depotVelocipede;
    private final DepotClient depotClient;

    public GestionnaireRetour(DepotVelocipede depotVelocipede, DepotClient depotClient) {
        this.depotVelocipede = depotVelocipede;
        this.depotClient = depotClient;
    }

    public void retourner(String idVelocipede, String idClient) {
        Velocipede velocipede = depotVelocipede.trouverParId(idVelocipede)
                .orElseThrow(() -> new IllegalArgumentException("Vélo introuvable : " + idVelocipede));
        Client client = depotClient.trouverParId(idClient)
                .orElseThrow(() -> new IllegalArgumentException("Client introuvable : " + idClient));

        // Vérifications avant toute modification pour ne jamais laisser un état incohérent
        if (velocipede.estDisponible()) {
            throw new IllegalStateException("Le vélo " + idVelocipede + " n'est pas en location");
        }
        if (client.getLocationsEnCours() == 0) {
            throw new IllegalStateException("Le client " + idClient + " n'a aucune location en cours");
        }

        velocipede.rendre();
        client.decrementerLocations();
    }
}
