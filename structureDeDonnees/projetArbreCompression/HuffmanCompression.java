package projetArbreCompression;

import collections.Liste;
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

    public static Map<Character, Integer> calculerFrequences(String texte) {
        Map<Character, Integer> frequences = new HashMap<>();
        for (char c : texte.toCharArray()) {
            frequences.put(c, frequences.getOrDefault(c, 0) + 1);
        }
        return frequences;
    }

    public static void getFrequencesGlobales(String texte, Map<Character, Integer> frequencesGlobales ){
        Map<Character, Integer> frequences = calculerFrequences(texte);
        for (Map.Entry<Character, Integer> entry : frequences.entrySet()) {
            frequencesGlobales.put(entry.getKey(), frequencesGlobales.getOrDefault(entry.getKey(), 0) + entry.getValue());
        }
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

    public static String[] separerEnteteEtContenu(String htmlComplet) {
        String entete = htmlComplet.substring(0, htmlComplet.indexOf("<body>") + 6);
        String contenu = htmlComplet.substring(htmlComplet.indexOf("<body>") + 6);
        return new String[]{entete, contenu};
    }

    public static String verifyEntete(List<String> filesPaths, Map<Character, Integer> frequencesGlobales) throws IOException {
        String enteteCommun = null;

        for (String path : filesPaths) {
            String text = lireFichierHTML(path);

            String[] parties = separerEnteteEtContenu(text);
            String entete = parties[0];
            String contenu = parties[1];

            if (enteteCommun == null) {
                enteteCommun = entete;
            } else if (!enteteCommun.equals(entete)) {
                System.out.println("Les fichiers n'ont pas tous le même entête !");
                return "";
            }

            getFrequencesGlobales(contenu, frequencesGlobales);
        }

        return enteteCommun;
    }


    public static String lireFichierHTML(String path) throws IOException {
        File fichier = new File(path);
        String texte = Jsoup.parse(fichier, "UTF-8").outerHtml();
        return texte;
    }


    public static void ecrireFichierHTML(String nomFichier, String contenu) throws IOException {
        FileWriter writer = new FileWriter("/Users/boukricelina/Desktop/MySpace/StructureDeDonnees/projet-1-arbre-compression/structureDeDonnees/projetArbreCompression/reconstitution/" + nomFichier, StandardCharsets.UTF_8);
        writer.write(contenu);
        writer.close();
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
            return ;
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

    public static Map<String, String> compresserPlusieursFichiers(List<String> fichiers, Map<Character, String> codes) throws IOException {
        Map<String, String> fichiersCompresses = new HashMap<>();

        for (String fichier : fichiers) {
            String texteHTMLComplet = lireFichierHTML(fichier);

            String[] parties = separerEnteteEtContenu(texteHTMLComplet);
            String contenu = parties[1];

            String texteCompresse = compresserTexte(contenu, codes);
            fichiersCompresses.put(fichier, texteCompresse);
            long tailleCompressee = tailleFichierCompresse(texteCompresse);
            System.out.println("Taille compressée de " + fichier + " : " + tailleCompressee + " bits");

        }


        return fichiersCompresses;
    }



    public static List<String> decompresserPlusieursFichiers(String texteCompresse, Map<String, Character> codesInverse) {
        List<String> fichiersDecompresse = new ArrayList<>();
        String[] fichiersCompresse = texteCompresse.split(FILE_SEPARATOR);

        for (String fichierCompresse : fichiersCompresse) {
            fichiersDecompresse.add(decompresserTexte(fichierCompresse, codesInverse));
        }

        return fichiersDecompresse;
    }

    //Reconstituer les fichiers décompréssés
    public static void reconstitueFichiers(List<String> fichiersDecompresse , String enteteCommun) throws IOException {
        for (int i = 0; i < fichiersDecompresse.size(); i++) {
            String nomFichier = "reconstitue_fichier" + (i + 1) + ".html";
            String fichierComplet = enteteCommun != null ? enteteCommun + fichiersDecompresse.get(i) : fichiersDecompresse.get(i);
            ecrireFichierHTML(nomFichier, fichierComplet);
            System.out.println("Fichier " + nomFichier + " reconstitué.");
        }

    }


    //Calcule des Tailles

    //Taille d'un fichier avant compression
    public static long tailleFichierOriginal(String path) {
        File fichier = new File(path);
        long tailleOctets = fichier.length();
        return tailleOctets * 8;
    }

    //Taille d'un fichier après compression
    public static long tailleFichierCompresse(String texteCompresse) {
        return texteCompresse.length();
    }

    //Calcule ratio de compression
    public static double calculeRatio(long tailleOriginale, long tailleCompressee) {
        return (100 * (tailleOriginale - tailleCompressee) / (double) tailleOriginale);
    }



}
