package projetArbreCompression;

import org.jsoup.Jsoup;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HuffmanCompression {

    public static String lireFichierHTML(String cheminFichier) throws IOException {
        File fichier = new File(cheminFichier);
        String texte = Jsoup.parse(fichier, "UTF-8").text();
        return texte;
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

    public static HuffmanNode combinerNoeuds(List<HuffmanNode> liste) {
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
}
