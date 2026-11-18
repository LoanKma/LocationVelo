package fr.locationvelo.depot;

import fr.locationvelo.modele.Velocipede;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Dépôt en mémoire (aucune base de données). Pas d'état statique : chaque instance est indépendante.
 */
public class DepotVelocipedeEnMemoire implements DepotVelocipede {

    private final Map<String, Velocipede> velocipedes = new HashMap<>();

    @Override
    public void ajouter(Velocipede velocipede) {
        if (velocipede == null) {
            throw new IllegalArgumentException("Le vélo ne peut pas être null");
        }
        if (velocipedes.containsKey(velocipede.getId())) {
            throw new IllegalArgumentException("Un vélo avec l'identifiant " + velocipede.getId() + " existe déjà");
        }
        velocipedes.put(velocipede.getId(), velocipede);
    }

    @Override
    public Optional<Velocipede> trouverParId(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("L'identifiant recherché est obligatoire");
        }
        return Optional.ofNullable(velocipedes.get(id));
    }
}
