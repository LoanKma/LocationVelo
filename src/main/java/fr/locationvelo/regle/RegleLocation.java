package fr.locationvelo.regle;

import fr.locationvelo.modele.Client;
import fr.locationvelo.modele.Velocipede;

/**
 * Règles métier d'autorisation d'une location.
 * L'existence du vélo et du client est vérifiée par le gestionnaire (via les dépôts).
 */
public class RegleLocation {

    public static final int QUOTA_MAX_LOCATIONS = 2;

    /**
     * Lève une exception si la location n'est pas autorisée.
     */
    public void verifier(Velocipede velocipede, Client client) {
        if (velocipede == null) {
            throw new IllegalArgumentException("Le vélo est obligatoire");
        }
        if (client == null) {
            throw new IllegalArgumentException("Le client est obligatoire");
        }
        if (!velocipede.estDisponible()) {
            throw new IllegalStateException("Le vélo " + velocipede.getId() + " est indisponible");
        }
        if (client.getLocationsEnCours() >= QUOTA_MAX_LOCATIONS) {
            throw new IllegalStateException("Le client " + client.getId() + " a atteint son quota de "
                    + QUOTA_MAX_LOCATIONS + " locations");
        }
    }
}
