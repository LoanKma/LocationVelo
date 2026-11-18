package com.formation.veloloc.depot;

import com.formation.veloloc.modele.Client;
import java.util.List;
import java.util.Optional;

public interface DepotClient {
    void ajouter(Client client);
    Optional<Client> rechercherParIdentifiant(String identifiant);
    List<Client> listerTous();
}
