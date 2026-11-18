package com.formation.veloloc.exception;

public class VelocipedeIndisponibleException extends RuntimeException {
    public VelocipedeIndisponibleException(String identifiant) {
        super("Le vélo '" + identifiant + "' n'est pas disponible actuellement");
    }
}
