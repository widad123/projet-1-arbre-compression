package projetArbreCompression;

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

        String enteteCommun = null;



        for (String cheminFichier : cheminsFichiers) {
            String texteHTMLComplet = lireFichierHTML(cheminFichier);

            String[] parties = separerEnteteEtContenu(texteHTMLComplet);
            String entete = parties[0];
            String contenu = parties[1];

            if (enteteCommun == null) {
                enteteCommun = entete;
            } else if (!enteteCommun.equals(entete)) {
                System.out.println("Les fichiers n'ont pas tous le même entête !");
                enteteCommun = null;
                break;
            }

            long tailleOriginale = tailleFichierOriginalEnBits(cheminFichier);
            taillesOriginales.put(cheminFichier, tailleOriginale);
            System.out.println("Taille originale de " + cheminFichier + " : " + tailleOriginale + " bits");

            Map<Character, Integer> frequences = calculerFrequences(contenu);
            for (Map.Entry<Character, Integer> entry : frequences.entrySet()) {
                frequencesGlobales.put(entry.getKey(), frequencesGlobales.getOrDefault(entry.getKey(), 0) + entry.getValue());
            }
        }

        HuffmanNode racine = construireArbreHuffman(frequencesGlobales);
        Map<Character, String> codes = new HashMap<>();
        genererCodes(racine, "", codes);
        fichiersCompresses = compresserPlusieursFichiers(cheminsFichiers);

        /*for (String cheminFichier : cheminsFichiers) {
            String texteHTMLComplet = lireFichierHTML(cheminFichier);

            String[] parties = separerEnteteEtContenu(texteHTMLComplet);
            String contenu = parties[1];

            String texteCompresse = compresserTexte(contenu, codes);
            double ratio = calculeRatio(taillesOriginales.get(cheminFichier), taillesCompressees.get(cheminFichier));
            System.out.printf("Ratio de compression : %.2f%%\n", ratio);

            fichiersCompresses.put(cheminFichier, texteCompresse);
        }*/

        String texteCompresseTotal = "";
        if (enteteCommun != null) {
            texteCompresseTotal = compresserTexte(enteteCommun, codes) + FILE_SEPARATOR;
        }

        StringBuilder texteCompresseBuilder = new StringBuilder(texteCompresseTotal);
        for (String cheminFichier : cheminsFichiers) {
            texteCompresseBuilder.append(fichiersCompresses.get(cheminFichier));
            texteCompresseBuilder.append(FILE_SEPARATOR);
        }
        texteCompresseTotal = texteCompresseBuilder.toString();
        System.out.println("Texte compressé total : " + texteCompresseTotal);

        Map<String, Character> codesInverse = new HashMap<>();
        for (Map.Entry<Character, String> entry : codes.entrySet()) {
            codesInverse.put(entry.getValue(), entry.getKey());
        }

        System.out.println("Décompression des fichiers HTML...");
        List<String> fichiersDecompresse = decompresserPlusieursFichiers(texteCompresseTotal, codesInverse);
        fichiersDecompresse.remove(0);
        for (int i = 0; i < fichiersDecompresse.size(); i++) {
            String nomFichier = "reconstitue_fichier" + (i + 1) + ".html";
            String fichierComplet = enteteCommun != null ? enteteCommun + fichiersDecompresse.get(i) : fichiersDecompresse.get(i);
            ecrireFichierHTML(nomFichier, fichierComplet);
            System.out.println("Fichier " + nomFichier + " reconstitué.");
        }
    }

    public static String[] separerEnteteEtContenu(String htmlComplet) {
        String entete = htmlComplet.substring(0, htmlComplet.indexOf("<body>") + 6);
        String contenu = htmlComplet.substring(htmlComplet.indexOf("<body>") + 6);
        return new String[]{entete, contenu};
    }
}
