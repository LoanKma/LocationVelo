package com.formation.veloloc.exception;

public class ClientInconnuException extends RuntimeException {
    public ClientInconnuException(String identifiant) {
        super("Aucun client trouvé pour l'identifiant '" + identifiant + "'");
    }
}
