package fr.locationvelo.depot;

import fr.locationvelo.modele.Client;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Dépôt en mémoire (aucune base de données). Pas d'état statique : chaque instance est indépendante.
 */
public class DepotClientEnMemoire implements DepotClient {

    private final Map<String, Client> clients = new HashMap<>();

    @Override
    public void ajouter(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("Le client ne peut pas être null");
        }
        if (clients.containsKey(client.getId())) {
            throw new IllegalArgumentException("Un client avec l'identifiant " + client.getId() + " existe déjà");
        }
        clients.put(client.getId(), client);
    }

    @Override
    public Optional<Client> trouverParId(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("L'identifiant recherché est obligatoire");
        }
        return Optional.ofNullable(clients.get(id));
    }
}
