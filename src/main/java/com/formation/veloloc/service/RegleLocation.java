package com.formation.veloloc.service;

import com.formation.veloloc.exception.QuotaLocationAtteintException;
import com.formation.veloloc.exception.VelocipedeIndisponibleException;
import com.formation.veloloc.modele.Client;
import com.formation.veloloc.modele.Velocipede;

public class RegleLocation {
    public static final int QUOTA_MAXIMUM = 2;

    public void verifierLocationAutorisee(Velocipede velocipede, Client client) {
        if (velocipede == null || client == null) {
            throw new IllegalArgumentException("Le vélo et le client sont obligatoires");
        }

        if (!velocipede.estDisponible()) {
            throw new VelocipedeIndisponibleException(velocipede.getIdentifiant());
        }
        if (client.getLocationsEnCours() >= QUOTA_MAXIMUM) {
            throw new QuotaLocationAtteintException(
                    client.getIdentifiant(), QUOTA_MAXIMUM);
        }
    }
}
