package com.formation.veloloc.depot;

import com.formation.veloloc.modele.Client;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DepotClientEnMemoire implements DepotClient {
    private final Map<String, Client> clientsParIdentifiant = new HashMap<>();

    @Override
    public void ajouter(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("Le client ne peut pas être null");
        }
        clientsParIdentifiant.put(client.getIdentifiant(), client);
    }

    @Override
    public Optional<Client> rechercherParIdentifiant(String identifiant) {
        return Optional.ofNullable(clientsParIdentifiant.get(identifiant));
    }

    @Override
    public List<Client> listerTous() {
        return new ArrayList<>(clientsParIdentifiant.values());
    }
}
