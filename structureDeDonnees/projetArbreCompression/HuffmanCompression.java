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

    // La fonction calcule les fréquences d'apparition de chaque caractère dans un texte donné
    // *** Paramètres ***
        // texte : la chaîne de caractères pour laquelle on veut calculer les fréquences des caractères
    // *** Return ***
        // Une map associant chaque caractère à son nombre d'occurrences dans le texte
    public static Map<Character, Integer> calculerFrequences(String texte) {
        Map<Character, Integer> frequences = new HashMap<>();
        for (char c : texte.toCharArray()) {
            frequences.put(c, frequences.getOrDefault(c, 0) + 1);
        }
        return frequences;
    }

    // La fonction calcule les fréquences globales des caractères à partir de plusieurs textes
    // *** Paramètres ***
        // texte : le texte pour lequel on calcule les fréquences des caractères
        // frequencesGlobales : une map qui accumule les fréquences globales des caractères sur plusieurs textes
    // *** Return ***
        //  fonction void , elle met à jour la map `frequencesGlobales`

    public static void getFrequencesGlobales(String texte, Map<Character, Integer> frequencesGlobales ){
        Map<Character, Integer> frequences = calculerFrequences(texte);
        for (Map.Entry<Character, Integer> entry : frequences.entrySet()) {
            frequencesGlobales.put(entry.getKey(), frequencesGlobales.getOrDefault(entry.getKey(), 0) + entry.getValue());
        }
    }

    // La fonction crée une liste de nœuds de Huffman à partir des fréquences des caractères
    // *** Paramètres ***
        // frequences : une map associant chaque caractère à sa fréquence
    // *** Return ***
        // Une liste de nœuds de Huffman, chaque nœud représentant un caractère et sa fréquence

    public static List<HuffmanNode> creerListeDesArbres(Map<Character, Integer> frequences) {
        List<HuffmanNode> liste = new ArrayList<>();
        for (Map.Entry<Character, Integer> entry : frequences.entrySet()) {
            liste.add(new HuffmanNode(entry.getKey(), entry.getValue()));
        }
        return liste;
    }

    // La fonction trie la liste des nœuds de Huffman par ordre croissant de fréquence
    // *** Paramètres ***
        // liste : la liste de nœuds de Huffman à trier
    // *** Return ***
    // Méthode void elle modifie la liste en place

    public static void trierListe(List<HuffmanNode> liste) {
        liste.sort(new HuffmanComparator());
    }


    // La fonction sépare l'entête et le contenu d'un fichier HTML
    // *** Paramètres ***
        // htmlComplet : le texte complet du fichier HTML
    // *** Return ***
        // Un tableau de deux éléments : l'entête (avant <body>) et le contenu (après <body>)

    public static String[] separerEnteteEtContenu(String htmlComplet) {
        String entete = htmlComplet.substring(0, htmlComplet.indexOf("<body>") + 6);
        String contenu = htmlComplet.substring(htmlComplet.indexOf("<body>") + 6);
        return new String[]{entete, contenu};
    }

    // La fonction vérifie si tous les fichiers HTML fournis ont le même entête et calcule les fréquences globales des caractères dans leur contenu
    // *** Paramètres ***
        // filesPaths : une liste de chemins de fichiers HTML à vérifier
        // frequencesGlobales : une map qui associe chaque caractère à sa fréquence globale dans tous les fichiers
    // *** Return ***
        // L'entête commun si tous les fichiers ont le même entête, sinon une chaîne vide
    // *** Throws ***
        // IOException : en cas de problème d'accès à l'un des fichiers HTML

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

    // La fonction lit le contenu d'un fichier HTML à partir de son chemin et le retourne sous forme de chaîne de caractères
    // *** Paramètres ***
        // path : le chemin absolu du fichier HTML à lire
    // *** Return ***
        // Le contenu du fichier HTML sous forme de chaîne de caractères, après avoir été analysé par le parse de Jsoup
    // *** Throws ***
        // IOException : en cas de problème d'accès au fichier

    public static String lireFichierHTML(String path) throws IOException {
        File fichier = new File(path);
        String texte = Jsoup.parse(fichier, "UTF-8").outerHtml();
        return texte;
    }

    // La fonction écrit le contenu d'un fichier HTML et l'enregistre
    // *** Paramètres ***
        // nomFichier : le nom du fichier HTML à créer ou à modifier
        // contenu : le contenu à écrire dans le fichier (fichier à reconstitué)
    // *** Throws ***
        // IOException : en cas d'erreur lors de l'écriture du fichier

    public static void ecrireFichierHTML(String nomFichier, String contenu) throws IOException {
        FileWriter writer = new FileWriter("/Users/boukricelina/Desktop/MySpace/StructureDeDonnees/projet-1-arbre-compression/structureDeDonnees/projetArbreCompression/reconstitution/" + nomFichier, StandardCharsets.UTF_8);
        writer.write(contenu);
        writer.close();
    }


    // La fonction construit un arbre à partir des fréquences des caractères
    // *** Paramètres ***
        // frequences : une map associant chaque caractère à sa fréquence d'apparition
    // *** Return ***
        // La racine de l'arbre de Huffma  binaire où les feuilles représentent les caractères

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

    // La fonction génère les codes Huffman pour chaque caractère à partir d'un arbre de Huffman
    // *** Paramètres ***
        // root : le nœud racine de l'arbre de Huffman
        // code : la chaîne représentant le code binaire  pour le caractère courant
        // codes : une map qui associe chaque caractère à son code Huffman généré
    // *** Return ***
        //  méthode void , elle modifie la map `codes` pour y insérer les codes de chaque caractère.

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

    // La fonction compresse un texte en utilisant les codes Huffman associés à chaque caractère
    // *** Paramètres ***
        // texte : le texte à compresser sous forme de chaîne de caractères
        // codes : une map qui associe chaque caractère à son code Huffman sous forme de chaîne binaire
    // *** Return ***
        // Le texte compressé sous forme d'une chaîne de bits

    public static String compresserTexte(String texte, Map<Character, String> codes) {
        StringBuilder resultat = new StringBuilder();
        for (char c : texte.toCharArray()) {
            resultat.append(codes.get(c));
        }
        return resultat.toString();
    }

    //la fonction décode un texte compressé en utilisant les codes inversés de Huffman
    //*** paramètres **
        // texteCompresse  du texte compressé sous forme de symboles binaires
        //codesInverse : une map  associe les symbole  à leurs caractères correspondants
    //*** Return ***
       //Le texte du fichier décompressé

    public static String decompresserTexte(String texteCompresse, Map<String, Character> codesInverse) {


        StringBuilder resultat = new StringBuilder();
        StringBuilder symbolesTmp = new StringBuilder();

        for (char c : texteCompresse.toCharArray()) {
            symbolesTmp.append(c);
            if (codesInverse.containsKey(symbolesTmp.toString())) {
                resultat.append(codesInverse.get(symbolesTmp.toString()));
                symbolesTmp.setLength(0);
            }
        }

        return resultat.toString();
    }

    // La fonction compresse plusieurs fichiers HTML en utilisant les codes Huffman
    // *** Paramètres ***
        // fichiers : une liste de chemins de fichiers HTML à compresser
        // codes : une map qui associe chaque caractère à son code Huffman correspondant
    // *** Return ***
        // fichiersCompresses : une map qui associe chaque fichier à son texte compressé sous forme de code Huffman

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


    // La fonction décompresse plusieurs fichiers à partir d'un texte compressé en utilisant les codes Huffman inversés
    // *** Paramètres ***
        // texteCompresse : le texte compressé contenant les contenus de plusieurs fichiers, séparés par un séparateur de fichier
        // codesInverse : une map qui associe chaque séquence binaire à son caractère correspondant (codes inversés de Huffman)
    // *** Return ***
    // fichiersDecompresse : une liste contenant le contenu décompressé de chaque fichier

    public static List<String> decompresserPlusieursFichiers(String texteCompresse, Map<String, Character> codesInverse) {
        List<String> fichiersDecompresse = new ArrayList<>();
        String[] fichiersCompresse = texteCompresse.split(FILE_SEPARATOR);

        for (String fichierCompresse : fichiersCompresse) {
            fichiersDecompresse.add(decompresserTexte(fichierCompresse, codesInverse));
        }

        return fichiersDecompresse;
    }

    // La fonction reconstitue plusieurs fichiers HTML décompressés en y ajoutant l'entête en commun si y en a , puis les enregistre
    // *** Paramètres ***
        // fichiersDecompresse : une liste de contenus décompressés correspondant à chaque fichier HTML
        // enteteCommun : une chaîne représentant l'entête commun à tous les fichiers HTML (qui peut être null si aucun entête commun)
    // *** Throws ***
        // IOException : en cas d'erreur lors de l'écriture des fichiers

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

    // La fonction calcule la taille d'un fichier en bits à partir de son chemin
    // *** Paramètres ***
        // path : le chemin absolu du fichier dont on souhaite calculer la taille
    // *** Return ***
        // La taille du fichier en bits

    public static long tailleFichierOriginal(String path) {
        File fichier = new File(path);
        long tailleOctets = fichier.length();
        return tailleOctets * 8;
    }

    //Taille d'un fichier après compression
    // La fonction calcule la taille d'un fichier compressé en fonction du texte compressé
    // *** Paramètres ***
        // texteCompresse : le texte compressé sous forme de chaîne de caractères
    // *** Return ***
        // La taille du fichier compressé en bits


    public static long tailleFichierCompresse(String texteCompresse) {
        return texteCompresse.length();
    }


    // La fonction calcule le ratio de compression en pourcentage
    // *** Paramètres ***
        // tailleOriginale : la taille originale du fichier avant compression (en bits)
        // tailleCompressee : la taille du fichier après compression (en bits)
    // *** Return ***
        // Le ratio de compression en pourcentage, indiquant la réduction de la taille due à la compression

    public static double calculeRatio(long tailleOriginale, long tailleCompressee) {
        return (100 * (tailleOriginale - tailleCompressee) / (double) tailleOriginale);
    }



}
