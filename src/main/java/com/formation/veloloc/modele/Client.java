package com.formation.veloloc.modele;

public class Client {
    private final String identifiant;
    private final String nom;
    private int locationsEnCours;

    public Client(String identifiant, String nom) {
        if (identifiant == null || identifiant.isBlank()) {
            throw new IllegalArgumentException("L'identifiant du client ne peut pas être vide");
        }
        this.identifiant = identifiant;
        this.nom = nom;
        this.locationsEnCours = 0;
    }

    public void incrementerLocations() {
        locationsEnCours++;
    }

    public void decrementerLocations() {
        if (locationsEnCours > 0) {
            locationsEnCours--;
        }
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public String getNom() {
        return nom;
    }

    public int getLocationsEnCours() {
        return locationsEnCours;
    }
}
