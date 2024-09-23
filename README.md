# Projet 1 – Arbre et Compression

## Problématique de projet

L'algorithme de Huffman, utilisé pour la compression de données, peut avoir des applications intéressantes dans le secteur bancaire, en particulier dans la gestion et la transmission efficace des informations sensibles et volumineuses.

## Compression de données bancaires

Les banques gèrent un grand volume de données, allant des transactions financières aux historiques de compte, en passant par les relevés bancaires. Afin de minimiser les coûts de stockage et de transmission, l'algorithme de Huffman peut être utilisé pour compresser ces données.

1. Soit les relevés bancaires suivants, utilisez vos connaissances sur cet algorithme pour compresser ces relevés.
2. Pouvez-vous présenter les bénéfices quantitatifs et qualitatifs sur cet échantillon ?

## Application dans la compression des relevés bancaires

Imaginons que la banque souhaite envoyer par email des relevés bancaires sous forme de fichiers PDF à ses clients. Ces relevés contiennent des informations redondantes, comme des termes financiers (montant, solde, date, etc.) qui apparaissent fréquemment. En appliquant l'algorithme de Huffman, les termes et les motifs redondants pourraient être compressés, réduisant la taille des fichiers envoyés.

1. Que pouvez-vous imaginer pour compresser davantage ces relevés ?
2. Quels sont les bénéfices ?
3. Pouvez-vous évaluer l’efficacité en termes de réduction de taille et de temps de transmission ?

## Contexte du projet

L'algorithme de Huffman est un algorithme de compression de données qui permet de réduire la taille des fichiers en attribuant des codes plus courts aux éléments fréquents et des codes plus longs aux éléments moins fréquents. Dans le secteur bancaire, où un grand volume de données est traité quotidiennement (par exemple, des relevés bancaires, des historiques de transactions), la compression de ces données peut réduire les coûts de stockage et faciliter la transmission, notamment lorsqu'il s'agit d'envoyer des relevés par courriel.

## Objectifs du projet

### 1. Utilisation de l'algorithme de Huffman :
- L’objectif est de compresser les relevés en appliquant l'algorithme de Huffman. Cela implique d'analyser les données, d'identifier les éléments redondants ou fréquemment récurrents (comme les mots "montant", "solde", "date", etc.), puis de les encoder de manière à réduire la taille globale des relevés.

### 2. Bénéfices quantitatifs et qualitatifs :
- **Quantitatifs** : En termes de réduction de taille de fichier (par exemple, si la taille initiale est de 10 Mo et qu'après compression elle est de 3 Mo, cela représente une réduction de 70%).
- **Qualitatifs** : En termes de transmission plus rapide des fichiers, de réduction des coûts de stockage, ou encore d'efficacité dans la gestion des données bancaires.

## Application dans la compression des relevés bancaires

L'idée ici est d'appliquer l'algorithme de Huffman pour réduire la taille des fichiers PDF contenant les relevés bancaires que la banque souhaite envoyer à ses clients. Les relevés comportent souvent des informations redondantes, ce qui rend la compression efficace.

## Questions supplémentaires

1. **Comment imaginer une compression encore plus optimisée ?**
    - Pour une compression plus optimisée, on peut utiliser des formats d'envoi plus adaptés, comme des fichiers compressés (par exemple en ZIP), ou en utilisant des formats de données alternatifs (comme XML compressé).

2. **Bénéfices de cette optimisation :**
    - Les bénéfices incluent une réduction des coûts de transmission des courriels (les fichiers plus légers consomment moins de bande passante), une meilleure expérience utilisateur (temps de téléchargement réduit), et une optimisation de l'espace de stockage.

3. **Évaluer l'efficacité en termes de réduction de taille et de temps de transmission :**
    - Nous devons faire une analyse comparative avant et après compression pour déterminer la taille initiale des fichiers, la taille après compression, et estimer le temps économisé lors de l'envoi de ces fichiers compressés. Un tableau ou un graphique pourrait être utile pour montrer cette différence.

## Stratégie de compression adapté pour ce projet

![Stratégie de compression](./structureDeDonnees/projetArbreCompression/res/strategie_huffman.png)

## Resultat de compression en utilisant l'algorithmes de Huffman

![Algorithmes de Huffman](./structureDeDonnees/projetArbreCompression/res/Capture_decran_compression.png)

## Comparaison avec d'autres stratégies de compression

![Autres stratégies de compression](./structureDeDonnees/projetArbreCompression/res/other_strategies.png)


