package projetArbreCompression;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static projetArbreCompression.HuffmanCompression.*;

public class Main {
    public static void main(String[] args) throws IOException {

        String cheminDossier = "/Users/khati/codingfactory/Algo/structureDeDonnees/projetArbreCompression/releves";
        String nomFichierCompressé = "/Users/khati/codingfactory/Algo/structureDeDonnees/projetArbreCompression/code/texteCompresse.txt";
        List<String> cheminsFichiers = listerFichiersHTML(cheminDossier);

        if (cheminsFichiers.isEmpty()) {
            System.out.println("Aucun fichier HTML trouvé dans le dossier : " + cheminDossier);
            return;
        }
        Map<Character, Integer> frequencesGlobales = new HashMap<>();
        Map<Character, String> codes = new HashMap<>();
        Map<String, String> fichiersCompresses = new HashMap<>();
        Map<String, Long> taillesOriginales = new HashMap<>();
        Map<String, Long> taillesCompressees = new HashMap<>();
        Map<String, Character> codesInverse = new HashMap<>();

        System.out.println(" ************** Démarrage du processus de compression ************** ");

        // Vérification de l'entête commun et calcul des fréquences globales
        String enteteCommun = verifyEntete(cheminsFichiers, frequencesGlobales);

        // Calcul des tailles originales avant compression
        System.out.println("\n--- Tailles originales des fichiers avant compression ---");
        for (String path : cheminsFichiers) {
            long tailleOriginale = tailleFichierOriginal(path);
            taillesOriginales.put(path, tailleOriginale);
            System.out.println("Taille originale de " + path + " : " + tailleOriginale + " bits");
        }

        // Construction de l'arbre de Huffman à partir des fréquences globales
        HuffmanNode racine = construireArbreHuffman(frequencesGlobales);

        // Génération des codes de Huffman
        genererCodes(racine, "", codes);

        // Compression des fichiers
        System.out.println("\n--- Compression des fichiers HTML ---");
        fichiersCompresses = compresserPlusieursFichiers(cheminsFichiers, codes);

        // Construction du texte compressé total avec l'entête
        StringBuilder texteCompresseBuilder = new StringBuilder();
        texteCompresseBuilder.append(compresserTexte(enteteCommun, codes)).append(FILE_SEPARATOR);

        for (String path : cheminsFichiers) {
            texteCompresseBuilder.append(fichiersCompresses.get(path)).append(FILE_SEPARATOR);

            long tailleCompressee = fichiersCompresses.get(path).length();
            taillesCompressees.put(path, tailleCompressee);
        }

        String texteCompresseTotal = texteCompresseBuilder.toString();
        System.out.println("\nTexte compressé total : " + texteCompresseTotal);

        // Sauvegarde du texte compressé dans un fichier
        ecrireFichierCompresse(nomFichierCompressé, texteCompresseTotal);

        // Calcul des tailles après compression
        System.out.println("\n--- Tailles des fichiers après compression ---");
        for (String path : cheminsFichiers) {
            long tailleCompressee = taillesCompressees.get(path);
            System.out.println("Taille compressée de " + path + " : " + tailleCompressee + " bits");

            long tailleOriginale = taillesOriginales.get(path);
            double ratio = calculeRatio(tailleOriginale, tailleCompressee);
            System.out.printf("Ratio de compression pour %s : %.2f%%\n", path, ratio);
        }

        // Inversion des codes pour la décompression
        for (Map.Entry<Character, String> entry : codes.entrySet()) {
            codesInverse.put(entry.getValue(), entry.getKey());
        }

        System.out.println("\n ************** Démarrage du processus de décompression ************** ");

        // Décompression des fichiers
        List<String> fichiersDecompresse = decompresserPlusieursFichiers(texteCompresseTotal, codesInverse);

        // Suppression de l'entête de la liste des fichiers décompressés
        fichiersDecompresse.remove(0);

        // Reconstitution des fichiers décompressés
        reconstitueFichiers(fichiersDecompresse, enteteCommun);

        System.out.println("\n************** Fin du processus **************");
    }

    // Méthode pour lister les fichiers HTML dans un dossier
    public static List<String> listerFichiersHTML(String dossierPath) {
        List<String> fichiersHTML = new ArrayList<>();
        File dossier = new File(dossierPath);

        if (!dossier.isDirectory()) {
            System.out.println("Le chemin spécifié n'est pas un dossier : " + dossierPath);
            return fichiersHTML;
        }

        File[] fichiers = dossier.listFiles();
        if (fichiers != null) {
            for (File fichier : fichiers) {
                if (fichier.isFile() && fichier.getName().endsWith(".html")) {
                    fichiersHTML.add(fichier.getAbsolutePath());
                }
            }
        }

        return fichiersHTML;
    }

    public static void ecrireFichierCompresse(String nomFichier, String texteCompresse) throws IOException {
        FileWriter writer = new FileWriter(nomFichier, StandardCharsets.UTF_8);
        writer.write(texteCompresse);
        writer.close();
        System.out.println("\nLe fichier compressé a été sauvegardé sous : " + nomFichier);
    }
}
