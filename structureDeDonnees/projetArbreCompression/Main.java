import projetArbreCompression.HuffmanCompression;
import projetArbreCompression.HuffmanNode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static projetArbreCompression.HuffmanCompression.*;

public class Main {
    public static void main(String[] args) throws IOException {
        List<String> cheminsFichiers = Arrays.asList(
                "/Users/boukricelina/Desktop/releve1.html",
                "/Users/boukricelina/Desktop/releve2.html",
                "/Users/boukricelina/Desktop/releve3.html"
        );

        System.out.println("Compression des fichiers HTML...");

        Map<String, String> fichiersCompresses = new HashMap<>();
        Map<String, Long> taillesOriginales = new HashMap<>();
        Map<String, Long> taillesCompressees = new HashMap<>();

        Map<Character, Integer> frequencesGlobales = new HashMap<>();

        for (String cheminFichier : cheminsFichiers) {
            String texte = lireFichierHTML(cheminFichier);

            // Calcul de la taille originale du fichier en bits
            long tailleOriginale = tailleFichierOriginalEnBits(cheminFichier);
            taillesOriginales.put(cheminFichier, tailleOriginale);
            System.out.println("Taille originale de " + cheminFichier + " : " + tailleOriginale + " bits");

            // Calcul des fréquences de chaque fichier et ajout aux fréquences globales
            Map<Character, Integer> frequences = calculerFrequences(texte);
            for (Map.Entry<Character, Integer> entry : frequences.entrySet()) {
                frequencesGlobales.put(entry.getKey(), frequencesGlobales.getOrDefault(entry.getKey(), 0) + entry.getValue());
            }
        }

        // Construction de l'arbre Huffman global à partir des fréquences globales
        HuffmanNode racine = construireArbreHuffman(frequencesGlobales);
        Map<Character, String> codes = new HashMap<>();
        genererCodes(racine, "", codes);

        // Compression de chaque fichier et calcul des tailles compressées
        for (String cheminFichier : cheminsFichiers) {
            String texte = lireFichierHTML(cheminFichier);
            String texteCompresse = compresserTexte(texte, codes);

            // Calcul de la taille compressée en bits
            long tailleCompressee = tailleFichierCompresseEnBits(texteCompresse);
            taillesCompressees.put(cheminFichier, tailleCompressee);
            System.out.println("Taille compressée de " + cheminFichier + " : " + tailleCompressee + " bits");

            // Calcul du ratio de compression
            double ratio = calculeRatio(taillesOriginales.get(cheminFichier), taillesCompressees.get(cheminFichier));
            System.out.println("test taille org" + taillesOriginales.get(cheminFichier));
            System.out.println("test taille comp" + taillesCompressees.get(cheminFichier));

            System.out.printf("Ratio de compression : %.2f%%\n", ratio);

            // Stocker le texte compressé
            fichiersCompresses.put(cheminFichier, texteCompresse);
        }

        // Fusion des fichiers compressés en un seul texte avec séparateurs
        StringBuilder texteCompresseTotal = new StringBuilder();
        for (String cheminFichier : cheminsFichiers) {
            texteCompresseTotal.append(fichiersCompresses.get(cheminFichier));
            texteCompresseTotal.append(FILE_SEPARATOR);
        }
        System.out.println("Texte compressé total : " + texteCompresseTotal);

        // Création d'un dictionnaire de codes inversés pour la décompression
        Map<String, Character> codesInverse = new HashMap<>();
        for (Map.Entry<Character, String> entry : codes.entrySet()) {
            codesInverse.put(entry.getValue(), entry.getKey());
        }

        // Décompression des fichiers
        System.out.println("Décompression des fichiers HTML...");
        List<String> fichiersDecompresse = decompresserPlusieursFichiers(texteCompresseTotal.toString(), codesInverse);

        // Écriture des fichiers décompressés
        for (int i = 0; i < fichiersDecompresse.size(); i++) {
            String nomFichier = "reconstitue_fichier" + (i + 1) + ".html";
            ecrireFichierHTML(nomFichier, fichiersDecompresse.get(i));
            System.out.println("Fichier " + nomFichier + " reconstitué.");
        }
    }

    // Fonction pour écrire le contenu d'un fichier HTML
    public static void ecrireFichierHTML(String nomFichier, String contenu) throws IOException {
        try (FileWriter writer = new FileWriter(nomFichier)) {
            writer.write(contenu);
        }
    }

    // Méthode pour calculer la taille originale d'un fichier en bits
    public static long tailleFichierOriginalEnBits(String cheminFichier) {
        File fichier = new File(cheminFichier);
        return fichier.length() * 8; // Conversion en bits
    }

    // Méthode pour calculer la taille compressée en bits
    public static long tailleFichierCompresseEnBits(String texteCompresse) {
        return texteCompresse.length(); // Chaque caractère représente un bit dans le texte compressé
    }
}
