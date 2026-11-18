package fr.locationvelo.modele;

import java.math.BigDecimal;

/**
 * Un vélo : identifiant unique, modèle, tarif journalier et état de disponibilité.
 * Un vélo loué devient indisponible ; un vélo rendu redevient disponible.
 */
public class Velocipede {

    private final String id;
    private final String modele;
    private final BigDecimal tarifJournalier;
    private boolean disponible;

    public Velocipede(String id, String modele, BigDecimal tarifJournalier) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("L'identifiant du vélo est obligatoire");
        }
        if (modele == null || modele.isBlank()) {
            throw new IllegalArgumentException("Le modèle du vélo est obligatoire");
        }
        if (tarifJournalier == null || tarifJournalier.signum() < 0) {
            throw new IllegalArgumentException("Le tarif journalier doit être positif ou nul");
        }
        this.id = id;
        this.modele = modele;
        this.tarifJournalier = tarifJournalier;
        this.disponible = true;
    }

    public void louer() {
        if (!disponible) {
            throw new IllegalStateException("Le vélo " + id + " est déjà loué");
        }
        disponible = false;
    }

    public void rendre() {
        if (disponible) {
            throw new IllegalStateException("Le vélo " + id + " n'est pas en location");
        }
        disponible = true;
    }

    public String getId() {
        return id;
    }

    public String getModele() {
        return modele;
    }

    public BigDecimal getTarifJournalier() {
        return tarifJournalier;
    }

    public boolean estDisponible() {
        return disponible;
    }
}
