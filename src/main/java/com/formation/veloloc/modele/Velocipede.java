package com.formation.veloloc.modele;

import com.formation.veloloc.exception.VelocipedeIndisponibleException;

import java.util.Objects;

public class Velocipede {
    private final String identifiant;
    private final String modele;
    private final double tarifJournalier;
    private boolean disponible;

    public Velocipede(String identifiant, String modele, double tarifJournalier, boolean disponible) {
        if (identifiant == null || identifiant.isBlank()) {
            throw new IllegalArgumentException("L'identifiant du vélo ne peut pas être vide");
        }
        if (tarifJournalier < 0) {
            throw new IllegalArgumentException("Le tarif journalier ne peut pas être négatif");
        }
        this.identifiant = identifiant;
        this.modele = modele;
        this.tarifJournalier = tarifJournalier;
        this.disponible = disponible;
    }

    public boolean estDisponible() {
        return disponible;
    }

    public void retirerDeLaDisponibilite() {
        if (!disponible) {
            throw new VelocipedeIndisponibleException(identifiant);
        }
        disponible = false;
    }

    public void rendreDisponible() {
        disponible = true;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public String getModele() {
        return modele;
    }

    public double getTarifJournalier() {
        return tarifJournalier;
    }

    public boolean getDisponible() {
        return disponible;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Velocipede)) return false;
        Velocipede that = (Velocipede) o;
        return identifiant.equals(that.identifiant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifiant);
    }
}
