package projetArbreCompression;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static projetArbreCompression.HuffmanCompression.*;

public class Main {
    public static void main(String[] args) {
        try {
            String cheminFichier = "C:/Users/khati/Downloads/releve3.html";
            String texte = lireFichierHTML(cheminFichier);


            Map<Character, Integer> frequences = new HashMap<>();
            for (char c : texte.toCharArray()) {
                frequences.put(c, frequences.getOrDefault(c, 0) + 1);
            }

            List<HuffmanNode> listeDesArbres = creerListeDesArbres(frequences);

            HuffmanNode arbreHuffman = combinerNoeuds(listeDesArbres);

            Map<Character, String> codes = new HashMap<>();
            genererCodes(arbreHuffman, "", codes);

            System.out.println("Codes de Huffman:");
            for (Map.Entry<Character, String> entry : codes.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }

            String texteCompresse = compresserTexte(texte, codes);
            System.out.println("\nTexte compressé: " + texteCompresse);

            System.out.println("\nTaille originale: " + texte.length() * 8 + " bits");
            System.out.println("Taille compressée: " + texteCompresse.length() + " bits");

        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier: " + e.getMessage());
        }
    }
}
