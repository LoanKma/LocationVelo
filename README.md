# LocationVelo — projet de départ

Projet pédagogique inspiré de la progression du support de cours sur les tests logiciels,
mais transposé dans un autre domaine : la location de vélos.

## Objectif

Compléter progressivement le code et les tests à trous en travaillant notamment :

- JUnit 5 ;
- AAA (Arrange / Act / Assert) ;
- cas nominaux, limites et invalides ;
- `assertThrows` / `assertDoesNotThrow` ;
- tests paramétrés ;
- Mockito (`when`, `verify`, matchers, `ArgumentCaptor`) ;
- injection de dépendances ;
- tests de services orchestrant plusieurs collaborateurs.

## Organisation

```text
src/main/java/com/formation/veloloc/
├── modele/
├── depot/
├── exception/
└── service/

src/test/java/com/formation/veloloc/
├── modele/
├── depot/
└── service/
```

## Consigne générale

Les fichiers Java contiennent volontairement des `TODO`, des valeurs provisoires et/ou
des assertions incomplètes. Ne remplacez pas toutes les réponses d'un coup :
faites évoluer le projet séance par séance.

Le projet est conçu pour rester proche des notions du support tout en évitant le domaine
de la bibliothèque. La pyramide des tests, AAA, FIRST, les cas limites et le mocking
restent les mêmes concepts.
