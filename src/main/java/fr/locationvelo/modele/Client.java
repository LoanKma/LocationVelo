package fr.locationvelo.modele;

/**
 * Un client : identifiant, nom et nombre de locations en cours.
 * Le nombre de locations en cours ne peut jamais devenir négatif.
 */
public class Client {

    private final String id;
    private final String nom;
    private int locationsEnCours;

    public Client(String id, String nom) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("L'identifiant du client est obligatoire");
        }
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Le nom du client est obligatoire");
        }
        this.id = id;
        this.nom = nom;
        this.locationsEnCours = 0;
    }

    public void incrementerLocations() {
        locationsEnCours++;
    }

    public void decrementerLocations() {
        if (locationsEnCours == 0) {
            throw new IllegalStateException("Le client " + id + " n'a aucune location en cours");
        }
        locationsEnCours--;
    }

    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public int getLocationsEnCours() {
        return locationsEnCours;
    }
}
