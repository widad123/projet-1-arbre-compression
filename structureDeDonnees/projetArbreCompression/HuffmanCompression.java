package projetArbreCompression;

import org.jsoup.Jsoup;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HuffmanCompression {

    public static final String FILE_SEPARATOR = "§§§";

    public static String lireFichierHTML(String cheminFichier) throws IOException {
        File fichier = new File(cheminFichier);
        String texte = Jsoup.parse(fichier, "UTF-8").outerHtml();
        return texte;
    }


    public static List<String> lirePlusieursFichiersHTML(List<String> cheminsFichiers) throws IOException {
        List<String> textes = new ArrayList<>();
        for (String chemin : cheminsFichiers) {
            String texte = lireFichierHTML(chemin);
            textes.add(texte);
        }
        return textes;
    }

    public static Map<Character, Integer> calculerFrequences(String texte) {
        Map<Character, Integer> frequences = new HashMap<>();
        for (char c : texte.toCharArray()) {
            frequences.put(c, frequences.getOrDefault(c, 0) + 1);
        }
        return frequences;
    }
    public static List<HuffmanNode> creerListeDesArbres(Map<Character, Integer> frequences) {
        List<HuffmanNode> liste = new ArrayList<>();
        for (Map.Entry<Character, Integer> entry : frequences.entrySet()) {
            liste.add(new HuffmanNode(entry.getKey(), entry.getValue()));
        }
        return liste;
    }

    public static void trierListe(List<HuffmanNode> liste) {
        liste.sort(new HuffmanComparator());
    }

    public static HuffmanNode construireArbreHuffman(Map<Character, Integer> frequences) {
        List<HuffmanNode> liste = creerListeDesArbres(frequences);
        while (liste.size() > 1) {
            trierListe(liste);
            HuffmanNode gauche = liste.remove(0);
            HuffmanNode droite = liste.remove(0);
            HuffmanNode parent = new HuffmanNode(gauche.getFrequency() + droite.getFrequency());
            parent.setGauche(gauche);
            parent.setDroite(droite);
            liste.add(parent);
        }
        return liste.get(0);
    }

    public static void genererCodes(HuffmanNode root, String code, Map<Character, String> codes) {
        if (root == null) {
            return;
        }
        if (root.getRacine() != null) {
            codes.put(root.getRacine(), code);
        }
        genererCodes(root.getGauche(), code + "0", codes);
        genererCodes(root.getDroite(), code + "1", codes);
    }
    public static String compresserTexte(String texte, Map<Character, String> codes) {
        StringBuilder resultat = new StringBuilder();
        for (char c : texte.toCharArray()) {
            resultat.append(codes.get(c));
        }
        return resultat.toString();
    }

    public static String compresserPlusieursFichiers(List<String> fichiers) throws IOException {
        StringBuilder compressionComplete = new StringBuilder();
        Map<Character, Integer> frequencesGlobales = new HashMap<>();

        for (String fichier : fichiers) {
            String texte = lireFichierHTML(fichier);
            Map<Character, Integer> frequences = calculerFrequences(texte);
            for (Map.Entry<Character, Integer> entry : frequences.entrySet()) {
                frequencesGlobales.put(entry.getKey(), frequencesGlobales.getOrDefault(entry.getKey(), 0) + entry.getValue());
            }
        }

        HuffmanNode racine = construireArbreHuffman(frequencesGlobales);

        Map<Character, String> codes = new HashMap<>();
        genererCodes(racine, "", codes);

        for (String fichier : fichiers) {
            String texte = lireFichierHTML(fichier);
            compressionComplete.append(compresserTexte(texte, codes));
            compressionComplete.append(FILE_SEPARATOR);
        }

        return compressionComplete.toString();
    }

    public static String decompresserTexte(String texteCompresse, Map<String, Character> codesInverse) {
        StringBuilder resultat = new StringBuilder();
        StringBuilder symboleTemp = new StringBuilder();

        for (char c : texteCompresse.toCharArray()) {
            symboleTemp.append(c);
            if (codesInverse.containsKey(symboleTemp.toString())) {
                resultat.append(codesInverse.get(symboleTemp.toString()));
                symboleTemp.setLength(0);
            }
        }

        return resultat.toString();
    }

    public static List<String> decompresserPlusieursFichiers(String texteCompresse, Map<String, Character> codesInverse) {
        List<String> fichiersDecompresse = new ArrayList<>();
        String[] fichiersCompresse = texteCompresse.split(FILE_SEPARATOR);

        for (String fichierCompresse : fichiersCompresse) {
            fichiersDecompresse.add(decompresserTexte(fichierCompresse, codesInverse));
        }

        return fichiersDecompresse;
    }


    public static void ecrireFichierHTML(String nomFichier, String contenu) throws IOException {
        FileWriter writer = new FileWriter("/Users/boukricelina/Desktop/" + nomFichier, StandardCharsets.UTF_8);
        writer.write(contenu);
        writer.close();
    }
    public static long tailleFichierOriginal(String cheminFichier) {
        File fichier = new File(cheminFichier);
        long tailleOctets = fichier.length();
        return tailleOctets * 8;
    }
    public static long tailleFichierCompresse(String texteCompresse, Map<Character, String> codes) {
        long tailleCompressee = 0;


        for (char c : texteCompresse.toCharArray()) {
            tailleCompressee += codes.get(c).length();
        }

        return tailleCompressee;
    }

    public static double calculeRatio(long tailleOriginale,long tailleCompressee ){
        return (100*(tailleOriginale - tailleCompressee) / tailleOriginale);
    }
}
