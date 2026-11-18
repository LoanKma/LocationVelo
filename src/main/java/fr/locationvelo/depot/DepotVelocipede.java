package fr.locationvelo.depot;

import fr.locationvelo.modele.Velocipede;

import java.util.Optional;

public interface DepotVelocipede {

    void ajouter(Velocipede velocipede);

    Optional<Velocipede> trouverParId(String id);
}
