# SECOND PULSE — proposition de code source Android V0.1

## Statut réel

Projet source proposé, non compilé dans cet environnement faute de SDK Android/Gradle opérationnel. Aucun APK, aucune installation et aucune conformité visuelle finale ne sont revendiqués.

## Architecture

- Kotlin + Jetpack Compose, une activité.
- Moteur narratif déterministe séparé de l’UI.
- Contenu JSON embarqué et hors ligne.
- Conditions pures, effets atomiques, routes sous forme de machines à états.
- Options inéligibles filtrées dans le domaine avant composition UI.
- Sauvegarde JSON via `AtomicFile`.
- Position de lecture : acte, chapitre, scène, bloc, ancre, index/offset, page, écran secondaire.
- Navigation basse verrouillée : Récit, Téléphone, Campus, Dossiers, Journal.
- Trajectoire interne à Dossiers.
- Aucun portrait : monogrammes locaux.
- Aucune permission réseau.

## Lancer localement

1. Installer Android Studio avec SDK 35 et JDK 17.
2. Ouvrir ce dossier.
3. Générer ou restaurer le wrapper Gradle si nécessaire.
4. Exécuter `./gradlew test` puis `./gradlew assembleDebug`.
5. Comparer les cinq écrans aux références verrouillées sur plusieurs tailles et à 200 % de texte.

## Limites intentionnelles de cette proposition

Le fichier `story_sample.json` ne contient qu’un échantillon technique. L’import intégral des manuscrits validés exige un pipeline de transformation et de comparaison de hashes. Le logo officiel n’était pas présent comme asset exploitable dans le package consulté ; aucun logo alternatif n’a été inventé.
