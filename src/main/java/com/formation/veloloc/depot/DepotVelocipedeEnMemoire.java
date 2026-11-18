package com.formation.veloloc.depot;

import com.formation.veloloc.modele.Velocipede;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DepotVelocipedeEnMemoire implements DepotVelocipede {
    private final Map<String, Velocipede> velosParIdentifiant = new HashMap<>();

    @Override
    public void ajouter(Velocipede velocipede) {
        if (velocipede == null) {
            throw new IllegalArgumentException("Le vélo ne peut pas être null");
        }
        velosParIdentifiant.put(velocipede.getIdentifiant(), velocipede);
    }

    @Override
    public Optional<Velocipede> rechercherParIdentifiant(String identifiant) {
        return Optional.ofNullable(velosParIdentifiant.get(identifiant));
    }

    @Override
    public List<Velocipede> listerTous() {
        return new ArrayList<>(velosParIdentifiant.values());
    }
}
