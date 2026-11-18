package com.formation.veloloc.depot;

import com.formation.veloloc.modele.Velocipede;
import java.util.List;
import java.util.Optional;

public interface DepotVelocipede {
    void ajouter(Velocipede velocipede);
    Optional<Velocipede> rechercherParIdentifiant(String identifiant);
    List<Velocipede> listerTous();
}
