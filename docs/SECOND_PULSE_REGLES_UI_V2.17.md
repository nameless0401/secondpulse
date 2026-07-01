# SECOND PULSE — RÈGLES UI ACTIVES ET CONTRAT D’IMPLÉMENTATION

**Version documentaire : V2.17 / UI V2.2 LOCKED + synchronisation V2.13–V2.14**  
**Statut : DESIGN-LOCKED / IMPLEMENTATION-CONTRACT**  
**Périmètre : application Android hors ligne, récit, téléphone, Campus, Dossiers vivants, Trajectoire, Journal, sauvegarde et reprise**

## 0. Autorité et arbitrages

Sources actives, par ordre d’application :

1. décisions utilisateur verrouillées les plus récentes ;
2. référence maîtresse documentaire V2.17 ;
3. `SECOND_PULSE_IMPLEMENTATION_GUIDE_UI_V2.2_LOCKED.md` ;
4. amendement de synchronisation V2.13–V2.14 ;
5. contrats de composants et d’écrans V2.0 ;
6. `DOSSIERS_VIVANTS_TRAJECTOIRE_CONSOLIDE_V2.16.md` ;
7. matrices actives Téléphone/Campus ;
8. maquettes visuelles `REFERENCE_01` à `REFERENCE_05`.

Les maquettes fixent la hiérarchie, la densité, les proportions et le langage visuel. Leur texte illustratif n’est pas canonique. Lorsqu’une maquette montre un badge `RECOMMANDÉ` ou `RISQUÉ`, le contrat écrit plus récent prime : ces badges sont interdits.

Aucune refonte, modernisation libre, substitution par un design Material générique, réorganisation de navigation, changement de palette ou ajout de fonctionnalité n’est autorisé sans décision explicite.

## 1. Principe général

L’interface est une biographie contemporaine unique, pas un assemblage d’applications différentes.

- **Récit** : expérience présente et décisions.
- **Téléphone** : communications, réponses et silences.
- **Campus** : existence académique ou agenda selon la chronologie.
- **Dossiers** : personnes telles qu’elles sont effectivement connues.
- **Journal** : faits, promesses, échéances et conséquences manifestées.
- **Trajectoire** : transformations qualitatives du protagoniste.

Chaîne obligatoire :

```text
NarrativeState
→ ViewModel qualitatif déjà filtré
→ composants UI sans logique narrative
→ rendu
```

Les composants UI ne lisent jamais directement les variables brutes, n’évaluent aucune condition de route et ne modifient pas l’état narratif.

## 2. Direction visuelle verrouillée

- fond crème chaud texturé ;
- bleu profond comme couleur principale ;
- typographie serif lisible ;
- cartes discrètes à bord fin ;
- séparateurs fins ;
- ombres très faibles ou absentes ;
- priorité absolue à la lecture ;
- aucune couleur blanche pure ni noir pur ;
- aucune photographie de personnage ;
- aucun portrait réaliste, silhouette de visage ou avatar génératif ;
- monogrammes locaux uniquement dans les Dossiers.

La texture est procédurale ou embarquée localement, avec une opacité maximale de 4 %. Elle ne doit jamais réduire la lisibilité ni devenir nécessaire à la compréhension.

## 3. Tokens de référence

### 3.1 Couleurs

| Token | Valeur |
|---|---|
| `background` | `#F6EEDC` |
| `surface` | `#FBF6EA` |
| `surfaceAlt` | `#F2E8D5` |
| `borderSoft` | `#CFC2AA` |
| `borderStrong` | `#B9A98D` |
| `textPrimary` | `#132F5E` |
| `textSecondary` | `#4C4439` |
| `textMuted` | `#766C5D` |
| `accent` | `#123A73` |
| `accentActive` | `#0F356A` |
| `onAccent` | `#FFF9EE` |
| `iconInactive` | `#756A57` |
| `warning` | `#78664A` |
| `focus` | `#245A9B` |

### 3.2 Espacements et dimensions

- grille : multiples de `4dp` ;
- marge horizontale : `18dp` ;
- padding carte : `18dp` ;
- gap de section : `12dp` ;
- gap icône/texte : `16dp` ;
- zone tactile minimale : `48dp` ;
- barre basse : `72dp` ;
- carte de choix : minimum `72dp` ;
- carte héros Dossier : minimum `152dp` ;
- monogramme standard : `92dp` ;
- carte de section Dossier : minimum `112dp` ;
- icône de section : `44dp` ;
- tuile Campus : minimum `92dp` ;
- ligne de conversation : minimum `68dp` ;
- carte Trajectoire : minimum `132dp`.

### 3.3 Rayons et traits

- petit rayon : `10dp` ;
- onglet/carte de choix : `14dp` ;
- carte standard : `16dp` ;
- bordure : `1dp` ;
- focus : `2dp` ;
- indicateur actif : `2dp × 30dp`.

### 3.4 Typographie

Famille : serif lisible disponible légalement dans le projet ou serif système. Aucun fichier de police n’est fourni par le contrat.

| Usage | Taille | Graisse | Interligne |
|---|---:|---:|---:|
| titre écran | `30sp` | 600 | `36sp` |
| nom personnage | `28sp` | 600 | `34sp` |
| titre section | `20sp` | 600 | `25sp` |
| récit | `18sp` | 400 | `28sp` |
| texte UI | `16sp` | 400 | `23sp` |
| métadonnées | `14sp` | 400 | `19sp` |
| navigation | `13sp` | 500 | `16sp` |

Réglage interne autorisé : `0,90×`, `1,00×`, `1,15×`, `1,30×`. La taille système jusqu’à 200 % doit rester fonctionnelle. Aucun texte ne doit être tronqué ; les cartes grandissent verticalement.

## 4. Responsive

- largeur logique de référence : `393dp` ;
- hauteur utile de référence : `698dp` hors barres système ;
- largeur maximale de contenu sur tablette : `720dp` ;
- sous `360dp` : titres réduits d’un niveau et onglets horizontalement scrollables ;
- à partir de `600dp` : colonne unique centrée ;
- aucune conversion automatique en dashboard multicolonne ;
- une disposition à deux colonnes n’est autorisée que si chaque colonne conserve au moins `150dp` utiles sans troncature ;
- priorité à une seule profondeur à la fois sur petit écran.

## 5. Navigation principale

La barre basse comporte exactement cinq destinations, dans cet ordre :

1. `Récit` ;
2. `Téléphone` ;
3. `Campus` ;
4. `Dossiers` ;
5. `Journal`.

Règles :

- hauteur `72dp` ;
- fond crème opaque à environ 98 % ;
- bord supérieur `1dp` ;
- destination active : icône et texte bleu profond + indicateur inférieur `2dp × 30dp` ;
- destination inactive : brun-gris ;
- navigation identique sur tous les écrans ;
- `Trajectoire` reste interne à `Dossiers` ;
- l’ancienne navigation à quatre entrées et le menu `Plus` sont obsolètes.

## 6. En-tête commun

Composant `SpTopHeader` :

- bouton menu `48dp` à gauche ;
- titre centré ;
- bouton réglages `48dp` à droite ;
- contexte temporel sous le titre ;
- séparateur décoratif fin ;
- hauteur minimale `106dp`.

Le bandeau temporel provient exclusivement de l’état narratif :

```text
actId
chapterId
narrativePeriod
protagonistAge
academicStage
```

Formats admis :

- `Acte I • Chapitre 4 • Automne • 17 ans` ;
- `Acte III • Externat • Hiver • 21 ans`.

Un champ non pertinent peut être omis, mais l’ordre restant est stable. Toute divergence entre bandeau, scène et sauvegarde est bloquante.

## 7. Récit

### 7.1 Structure

- une seule colonne ;
- texte de récit `18sp / 28sp` ;
- paragraphes espacés de `18dp` ;
- temporalité et âge visibles discrètement ;
- modes `Défilement` et `Pagination` accessibles ;
- la pagination est responsive, pas un découpage narratif fixe ;
- `scroll_position` et `page_position` sont conservés séparément.

### 7.2 Cartes de choix

Composant `SpChoiceCard` :

- pleine largeur ;
- hauteur minimale `72dp` ;
- rayon `14dp` ;
- bordure `1dp` ;
- padding `16dp` ;
- titre, sous-texte facultatif, chevron ;
- icône facultative ;
- aucune option inéligible composée.

Données autorisées :

```text
optionId
visibleLabel
intentSubtitle?
neutralContextLabel?
icon?
isIrreversible
isKnownUnavailable
accessibilityLabel
```

Données interdites :

```text
recommendationScore
successProbability
moralRating
routeValue
relationshipGain
preferredOption
```

### 7.3 Neutralité absolue

Interdits :

- `RECOMMANDÉ`, `RISQUÉ`, `MEILLEUR`, `OPTIMAL`, `BON`, `MAUVAIS` ;
- étoiles, couronnes, vert/rouge ou animation indiquant un meilleur choix ;
- tri dynamique mettant l’option présumée optimale en premier ;
- annonce TalkBack du type « choix recommandé » ;
- probabilité de succès calculée à partir d’états cachés ;
- option secrète visible mais désactivée ;
- libellé révélant une conséquence future cachée.

Informations contextuelles autorisées uniquement si déjà connues :

- irréversibilité ;
- échéance réelle ;
- indisponibilité connue ;
- coût explicite déjà établi ;
- exigence institutionnelle connue ;
- origine consciente d’une compétence ou information (`[Médecine]`, `[Information connue]`, `[Confiance acquise]`).

Ces informations utilisent un style neutre identique. Une étiquette conditionnelle ne signifie jamais que l’option est supérieure.

### 7.4 Masquage

Une option incompatible est absente :

- du rendu ;
- de l’arbre d’accessibilité ;
- du focus ;
- de toute inspection de l’écran ;
- des journaux visibles.

Il est interdit de composer toutes les options puis de les cacher visuellement.

## 8. Téléphone

Onglets exacts :

1. `SMS` ;
2. `E-mails` ;
3. `Groupes`.

Règles :

- les canaux ne sont jamais fusionnés ;
- un message apparaît à l’ancre où le protagoniste y accède réellement ;
- aucun message, adresse, objet, heure ou pièce jointe n’est inventé ;
- les fils sont triés par le dernier élément réellement visible ;
- avatar = monogramme local ;
- nom, aperçu, timestamp et point non lu ;
- conversation en bulles sobres ;
- réponses en cartes pleine largeur ;
- réponses indisponibles totalement absentes ;
- sur petit écran, liste et conversation sont des profondeurs séparées.

### 8.1 Chronologie

Le ViewModel reçoit seulement les fils de l’`era_scope` actif. Après le retour :

- aucun fil adulte composé ;
- aucun compteur adulte ;
- aucun résultat de recherche adulte ;
- aucun nœud TalkBack adulte.

Timestamps :

- heure pour aujourd’hui ;
- `Hier` ;
- `Avant-hier` ;
- date courte ensuite ;
- une heure absente n’est jamais inventée ;
- l’horloge du téléphone réel n’est pas une source narrative.

## 9. Campus

Titre stable : `CAMPUS`.

Modes :

- `AGENDA_ONLY` : Agenda éditorial, aucune tuile étudiante ;
- `ACADEMIC_PROGRESSIVE` : Agenda présent, modules universitaires ajoutés seulement lorsqu’ils deviennent réellement accessibles.

Tuiles autorisées :

- `Notes & copies` ;
- `Classement` ;
- `Documents` ;
- `Agenda`.

Règles :

- classement public anonymisé ;
- résultats personnels séparés du classement public ;
- rang à l’épreuve distinct du rang cumulé ;
- numéro étudiant fixe ;
- notes et copies réellement consultables ;
- aucun module universitaire prématuré ;
- échéances de l’Agenda uniquement sourcées par le manuscrit ou les registres actifs.

## 10. Dossiers vivants

### 10.1 Visibilité

Un personnage apparaît après sa rencontre effective.

Exception : une figure publique ou institutionnelle peut être connue avant rencontre si son existence est légitimement connue. La fiche ne simule alors aucune relation.

Les deux chronologies ne sont jamais fusionnées.

Filtres principaux de la vie recommencée :

- `Famille` ;
- `Promo` ;
- `Admin` ou `Admin/Faculté` ;
- `Autres`.

En vie adulte :

- `Famille` ;
- `Hôpital` ;
- `Recherche/Professionnel` ;
- `Autres` ;
- aucun filtre `Promo`.

### 10.2 Identité visuelle

- monogramme circulaire de une ou deux initiales ;
- double cercle fin ;
- motif étoile/rose des vents en haut ;
- motif végétal en bas ;
- aucun chargement réseau ;
- aucune photo, aucun emplacement vide ;
- description d’accessibilité : `Dossier de {nom}`.

### 10.3 Carte personnage

- hauteur minimale `152dp` ;
- monogramme `92dp` ;
- nom ;
- rôle actuel ;
- deux lignes de synthèse qualitative ;
- chevron `24dp`.

### 10.4 Sections obligatoires

Ordre exact :

1. `FAITS CONNUS` ;
2. `IMPRESSIONS` ;
3. `EMPREINTES` ;
4. `TENSION ACTUELLE` ;
5. `CHRONOLOGIE COMMUNE`.

Les faits et impressions ne sont jamais fusionnés. Les non-dits décrivent des gestes ou silences observés, jamais un diagnostic psychologique omniscient.

Les informations futures ou secrètes sont absentes. Il est interdit d’utiliser `???`, cadenas, catégorie grisée, compteur d’indices ou placeholder. Une phrase qualitative d’incertitude est admise seulement si l’incertitude elle-même a été vécue et ne révèle pas le contenu caché.

### 10.5 Empreinte relationnelle

Aucune relation majeure n’est réduite à une barre unique.

Socle qualitatif possible :

- confiance ;
- proximité ;
- friction.

Axes contextuels possibles après émergence narrative :

- romance : attirance, intimité, sécurité émotionnelle, vulnérabilité partagée ;
- rivalité : respect, rivalité, hostilité, fascination intellectuelle ;
- famille : confiance familiale, disponibilité, inquiétude, distance ;
- institution : crédibilité, attentes, exposition, confiance professionnelle.

Aucun seuil de romance, formule, route cachée ou futur effet non observable n’est affiché.

## 11. Trajectoire

Trajectoire est une vue interne de Dossiers.

Axes autorisés, dans cet ordre :

1. `VISIBILITÉ` ;
2. `MÉTHODE` ;
3. `RÉPUTATION` ;
4. `PRESSIONS ACTIVES` ;
5. `ÉQUILIBRE PERSONNEL`.

Interdits :

- jauges génériques ;
- pourcentages ;
- nombres bruts ;
- noms techniques de variables ;
- noms de routes ;
- alignements moraux.

Chaque évolution visible doit être reliée à une cause observable.

## 12. Journal

Le Journal n’est pas une liste de boutons sélectionnés.

Il peut contenir :

- faits observés ;
- promesses ;
- échéances ;
- conséquences manifestées ;
- questions ouvertes ;
- décisions connues ;
- informations découvertes.

Il distingue :

- ce qui s’est produit ;
- ce que le protagoniste en comprend ;
- ce qui reste incertain ;
- ce qui demande encore une action.

Aucune conséquence future non manifestée ne doit être révélée.

## 13. Jalons narratifs

Composant `SpMilestoneCard`, réservé aux transformations structurantes :

- fin de semestre ou passage d’année ;
- constitution effective du noyau étudiant ;
- changement durable de réputation ou de visibilité ;
- engagement explicite dans une route ;
- fracture ou réconciliation majeure ;
- clôture d’un cycle.

Contrat :

```text
milestoneId
title
qualitativeState
causeSummary
visibleConsequences
narrativeDate
sourceEventIds
continueTarget
presentationPolicy
```

Règles :

- 40 à 110 mots hors titre ;
- au moins une cause observable ;
- une à trois conséquences visibles ;
- aucun score, seuil ou nom de route ;
- apparition unique par `milestoneId` ;
- sauvegarde avant affichage et après validation ;
- restauration sans répétition ;
- bouton unique `Continuer` ou retour contextuel.

## 14. États et animations

- pression : voile accent à 6 % ;
- sélection : fond accent + texte clair pour onglets, bordure accent pour cartes ;
- focus : contour `2dp` avec marge externe `2dp` ;
- état désactivé uniquement pour une action connue mais momentanément indisponible ;
- jamais pour révéler une route cachée ;
- animation : `120–180ms` ;
- déplacement maximum `4dp` ;
- réduction des animations : transition instantanée sans perte de contexte.

## 15. Accessibilité

- contraste WCAG AA minimum ;
- cibles tactiles `48dp` ;
- ordre de focus logique ;
- contenu caché absent de la sémantique ;
- icônes décoratives non annoncées ;
- monogramme annoncé comme dossier de la personne ;
- taille système jusqu’à 200 % sans perte fonctionnelle ;
- texture non essentielle ;
- TalkBack ne révèle ni classement de choix, ni seuil, ni route.

## 16. Sauvegarde et reprise exacte

Avant d’ouvrir Téléphone, Campus, Dossiers, Journal ou Réglages, sauvegarder au minimum :

```text
acte
chapitre
scène
bloc
ancre
mode de lecture
index/offset de défilement
position de page
choix actuellement visible
écran secondaire
communication ouverte
```

Le retour au récit restaure ces valeurs avant la première frame visible :

- aucun flash en haut de page ;
- aucune scène rejouée ;
- aucun effet réappliqué ;
- aucun choix perdu ;
- aucune ancienne route affichée brièvement.

Ouvrir un écran secondaire ne fait pas avancer le temps narratif.

## 17. Hors ligne et sécurité fonctionnelle

- aucune permission réseau non justifiée ;
- aucune ressource distante nécessaire ;
- contenu, monogrammes, texture et données intégralement locaux ;
- options fermées filtrées avant l’UI ;
- sauvegarde atomique ;
- états de chronologie isolés ;
- migrations de sauvegarde explicites ;
- aucune valeur brute modifiable depuis un composant.

## 18. Composants contractuels

- `SpTopHeader` ;
- `SpDecorativeDivider` ;
- `SpBottomNavigation` ;
- `SpStoryText` ;
- `SpChoiceCard` ;
- `SpSegmentedTabs` ;
- `SpMonogramAvatar` ;
- `SpCharacterHeroCard` ;
- `SpDossierSectionCard` ;
- `SpConversationRow` ;
- `SpMessageBubble` ;
- `SpQuickReplyCard` ;
- `SpCampusTile` ;
- `SpRankingCard` ;
- `SpPersonalResultCard` ;
- `SpTrajectoryCard` ;
- `SpMilestoneCard` ;
- `SpTemporalContextLine` ;
- `SpRelativeTimestamp` ;
- `SpAgendaItem` ;
- `SpReaderModeToggle`.

## 19. Gate de conformité UI

Une build reste **NO-GO** tant que les contrôles suivants ne sont pas réellement exécutés :

- comparaison visuelle sur les cinq écrans de référence ;
- plusieurs tailles de téléphone et tablette ;
- aucun portrait/photo ;
- navigation et ordre des sections exacts ;
- palette et typographie dans les tolérances ;
- aucun débordement à 200 % ;
- restauration exacte ;
- fonctionnement Défilement/Pagination ;
- isolation des chronologies ;
- téléphone synchronisé ;
- Campus progressif ;
- options secrètes absentes du rendu et de TalkBack ;
- TalkBack testé ;
- captures de preuve fournies.

Tolérances visuelles :

- positions principales : `±4dp` ;
- tailles typographiques : `±1sp` ;
- rayons : `±2dp` ;
- couleurs : `deltaE 2000 ≤ 3` hors texture.

## 20. Interdictions absolues

- modifier la navigation, la palette, la typographie, le logo ou la disposition sans demande explicite ;
- transformer Téléphone en imitation autonome d’Android/iOS ;
- transformer Campus en site administratif ;
- transformer Dossiers en CRM ;
- ajouter des portraits ;
- afficher une information inconnue ou future ;
- exposer une variable ou un seuil ;
- recommander un choix ;
- laisser une route incompatible dans l’arbre d’accessibilité ;
- perdre la position de lecture ;
- inventer une heure ou une communication ;
- déclarer l’UI conforme sans comparaison visuelle réelle.

## 21. Verdict documentaire

Ces règles sont exploitables comme contrat de développement. Elles ne prouvent pas qu’une APK respecte l’UI. La conformité exige un build, des captures, des tests de navigation, de reprise, d’accessibilité et une comparaison écran par écran.
