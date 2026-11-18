# LocationVelo

Cœur métier d'une petite application de location de vélos, développé et testé avec **JUnit 5** et **Mockito**.
Pas d'interface graphique, pas de base de données, pas de véritable service d'envoi de notifications.

L'énoncé de référence est dans [ENONCE.md](ENONCE.md).

## Prérequis

- JDK 17 ou plus récent
- Maven 3.9+

## Commandes

```bash
mvn compile
mvn test
mvn test -DtrimStackTrace=false
```

## Organisation

```
src/main/java/fr/locationvelo
├── modele/        Velocipede, Client
├── depot/         DepotVelocipede, DepotClient (+ implémentations en mémoire)
├── regle/         RegleLocation (quota : 2 locations simultanées)
├── tarif/         CalculateurTarif (tarif x jours + forfait de 1 €)
├── notification/  ServiceNotification, NotificateurLocation, NotificateurRetard
└── service/       GestionnaireLocation, GestionnaireRetour
```

Chaque classe a sa classe de test dans `src/test/java`, au même emplacement de package.

## Choix de test

| Classe testée | Approche |
|---|---|
| `Velocipede`, `Client` | Objets réels : état initial, changements d'état, valeurs invalides, invariants |
| `DepotVelocipedeEnMemoire`, `DepotClientEnMemoire` | Ajout, recherche d'un objet présent ou absent, entrées invalides |
| `RegleLocation` | Objets réels : location autorisée, vélo indisponible, quota atteint, client juste sous le quota |
| `CalculateurTarif` | `@ParameterizedTest` + `@CsvSource` sur les exemples de l'énoncé, entrées négatives refusées |
| `NotificateurRetard`, `NotificateurLocation` | `ServiceNotification` mocké : `verify`, `never()`, `contains(...)`, `ArgumentCaptor` |
| `GestionnaireLocation` | Dépôts et notificateur mockés, `RegleLocation` réelle, horloge fixe (`Clock`) pour une date J + 7 déterministe |
| `GestionnaireRetour` | Dépôts mockés : retour nominal et scénarios d'erreur |

Les gestionnaires dépendent des interfaces `DepotVelocipede` et `DepotClient` (injection par constructeur).
Les erreurs de saisie ou de recherche lèvent `IllegalArgumentException` ; les opérations interdites par l'état
actuel (vélo indisponible, quota atteint, retour impossible) lèvent `IllegalStateException`.
