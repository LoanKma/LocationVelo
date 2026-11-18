# Projet - LocationVelo

## 1. Contexte

Vous devez développer et tester le cœur métier d'une petite application de location de vélos.

L'application permet à des clients de louer des vélos, de les restituer et de recevoir des notifications liées à leurs locations.

Le projet est volontairement limité au métier : aucune interface graphique, aucune base de données et aucun véritable service d'envoi de notifications ne sont demandés.

L'objectif principal est de construire une suite de tests fiable autour d'un code Java progressivement complété.

## 2. Objectifs pédagogiques

Au terme du projet, vous devez être capable de :

- Écrire des tests unitaires lisibles avec JUnit 5 ;
- Structurer les tests selon AAA (Arrange / Act / Assert) ;
- Identifier les cas nominaux, limites et invalides ;
- Tester les exceptions avec `assertThrows` ;
- Utiliser les tests paramétrés lorsque plusieurs valeurs vérifient la même règle ;
- Distinguer les objets métier des dépendances à isoler ;
- Utiliser Mockito pour créer des mocks et vérifier des interactions ;
- Utiliser `when(...)`, `verify(...)`, `never()`, les matchers et, lorsque pertinent, `ArgumentCaptor` ;
- Comprendre l'intérêt de l'injection de dépendances par constructeur ;
- Tester un service qui orchestre plusieurs collaborateurs ;
- Conserver des tests indépendants, rapides et déterministes.

## 3. Fonctionnalités attendues

### 3.1 Gestion des vélos

Chaque vélo possède un identifiant unique, un modèle, un tarif journalier et un état de disponibilité.

Un vélo peut être disponible ou indisponible. Un vélo loué devient indisponible. Un vélo rendu redevient disponible.

### 3.2 Gestion des clients

Chaque client possède un identifiant, un nom et le nombre de locations en cours.

Le nombre de locations doit rester cohérent avec les opérations de location et de retour.

### 3.3 Règles de location

Une location n'est autorisée que si :

- Le vélo existe ;
- Le client existe ;
- Le vélo est disponible ;
- Le client n'a pas atteint son quota maximal de locations.

Le quota défini dans le squelette est de 2 locations simultanées par client.

Une location autorisée rend le vélo indisponible, augmente le nombre de locations en cours du client et déclenche une notification de confirmation.

### 3.4 Retour

Lors d'un retour :

- Le vélo doit être retrouvé ;
- Le client doit être retrouvé ;
- Le vélo redevient disponible ;
- Le nombre de locations en cours du client diminue.

### 3.5 Notifications

Les notifications passent par l'interface `ServiceNotification`.

Aucun véritable envoi d'e-mail, SMS ou notification push n'est demandé. Dans les tests, cette dépendance doit être remplacée par un mock Mockito.

Pour `NotificateurRetard`, tester notamment qu'un retard strictement positif déclenche une notification et que zéro jour de retard ne déclenche aucune notification.

## 4. Calcul du tarif

`CalculateurTarif` calcule le montant à partir du tarif journalier et du nombre de jours. Le squelette prévoit un forfait de service de 1 €.

| Tarif/jour | Jours | Montant |
|---|---|---|
| 10,00 € | 0 | 1,00 € |
| 10,00 € | 1 | 11,00 € |
| 12,50 € | 2 | 26,00 € |
| 20,00 € | 7 | 141,00 € |

Les valeurs négatives doivent être refusées. Ce service constitue un bon candidat pour `@ParameterizedTest` avec `@CsvSource`.

## 5. Architecture imposée

Les gestionnaires doivent dépendre des interfaces `DepotVelocipede` et `DepotClient`, et non de leurs implémentations concrètes.

## 6. Travail demandé

### Étape 1 - Modèle métier

Compléter `Velocipede` et `Client`, puis leurs tests : état initial, changements d'état, valeurs valides et invalides, invariants simples.

### Étape 2 - Dépôts en mémoire

Compléter les deux dépôts en mémoire et leurs tests : ajout, recherche d'un objet existant, recherche d'un objet absent et entrées invalides lorsque prévu.

Les dépôts en mémoire peuvent servir de fake dans les tests.

### Étape 3 - Règles métier

Compléter les tests de `RegleLocation` : location autorisée, vélo indisponible, quota atteint, client juste sous le quota et cas invalides pertinents.

Utiliser `assertThrows` pour les exceptions attendues.

### Étape 4 - Tests paramétrés

Compléter `CalculateurTarif` et ses tests avec `@ParameterizedTest` et `@CsvSource`, puis couvrir les entrées négatives.

### Étape 5 - Mockito et notifications

Compléter `NotificateurRetard` et `NotificateurLocation` ainsi que leurs tests.

Pour le retard, vérifier l'envoi et l'absence d'envoi avec `verify(..., never())`. Utiliser `contains(...)` lorsque cela rend le test moins fragile ; `ArgumentCaptor` peut être utilisé lorsqu'un argument doit être inspecté précisément.

### Étape 6 - Service de location

Compléter `GestionnaireLocation`. Le service doit :

- Rechercher le vélo ;
- Rechercher le client ;
- Appliquer les règles métier ;
- Modifier les états ;
- Déclencher la notification.

La date de retour prévue est fixée à J + 7 jours dans le projet de départ.

Tester notamment : location nominale, vélo inconnu, client inconnu, vélo indisponible, quota atteint et absence de notification en cas d'échec.

Utiliser des mocks pour les dépôts et le notificateur. Garder `RegleLocation` réelle : il n'est pas nécessaire de mocker une logique métier simple et déterministe.

### Étape 7 - Service de retour

Compléter `GestionnaireRetour` et son test. Le scénario nominal doit vérifier que le vélo redevient disponible et que le compteur du client diminue. Ajouter les scénarios d'erreur pertinents.

## 7. Contraintes de test

Les tests doivent être indépendants, rapides et déterministes. Éviter les états statiques partagés. Les noms des tests doivent exprimer le comportement vérifié.

Utiliser AAA lorsque cela améliore la lisibilité et ne pas mocker systématiquement toutes les classes : les dépendances externes et effets de bord sont de bons candidats au mocking, tandis que les objets métier simples et règles déterministes peuvent rester réels.

## 8. Critères de réussite

Le projet est terminé lorsque :

- Le projet compile avec Maven ;
- `mvn test` s'exécute sans erreur ;
- Les TODO pédagogiques pertinents sont traités ;
- Les comportements nominaux sont couverts ;
- Les principales limites et erreurs sont couvertes ;
- Les interactions importantes avec les mocks sont vérifiées ;
- Les tests restent lisibles et indépendants ;
- Aucun test ne dépend d'un système externe réel.

La qualité et la pertinence des tests comptent autant que leur nombre.

## 9. Commandes utiles

```bash
mvn compile
mvn test
mvn test -DtrimStackTrace=false
```

## 10. Livrable attendu

Le livrable final contient `pom.xml`, `src/main/java/...` et `src/test/java/...` avec le code métier et les tests complétés, des noms explicites, des assertions pertinentes et les mocks nécessaires.

Le fichier `README.md` sert de guide de démarrage ; `ENONCE.md` constitue l'énoncé de référence.
