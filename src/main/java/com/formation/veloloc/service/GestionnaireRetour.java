package com.formation.veloloc.service;

import com.formation.veloloc.depot.DepotClient;
import com.formation.veloloc.depot.DepotVelocipede;
import com.formation.veloloc.exception.ClientInconnuException;
import com.formation.veloloc.exception.VelocipedeIndisponibleException;
import com.formation.veloloc.modele.Client;
import com.formation.veloloc.modele.Velocipede;

public class GestionnaireRetour {
    private final DepotVelocipede depotVelocipede;
    private final DepotClient depotClient;

    public GestionnaireRetour(DepotVelocipede depotVelocipede, DepotClient depotClient) {
        this.depotVelocipede = depotVelocipede;
        this.depotClient = depotClient;
    }

    public void retourner(String identifiantVelo, String identifiantClient) {
        Velocipede velo = depotVelocipede.rechercherParIdentifiant(identifiantVelo)
                .orElseThrow(() -> new VelocipedeIndisponibleException(identifiantVelo));
        Client client = depotClient.rechercherParIdentifiant(identifiantClient)
                .orElseThrow(() -> new ClientInconnuException(identifiantClient));

        velo.rendreDisponible();
        client.decrementerLocations();
    }
}
