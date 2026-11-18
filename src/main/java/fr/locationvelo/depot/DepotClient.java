package fr.locationvelo.depot;

import fr.locationvelo.modele.Client;

import java.util.Optional;

public interface DepotClient {

    void ajouter(Client client);

    Optional<Client> trouverParId(String id);
}
