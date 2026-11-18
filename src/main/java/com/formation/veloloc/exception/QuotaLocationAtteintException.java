package com.formation.veloloc.exception;

public class QuotaLocationAtteintException extends RuntimeException {
    public QuotaLocationAtteintException(String identifiantClient, int quota) {
        super("Le client '" + identifiantClient + "' a atteint son quota de "
                + quota + " location(s)");
    }
}
