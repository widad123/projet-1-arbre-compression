import java.util.*;

public class HuffmanCompression {
    static class Node implements Comparable<Node> {
        char key;
        int frequency;
        Node left;
        Node right;

        public Node(char key, int frequency) {
            this.key = key;
            this.frequency = frequency;
            this.left = null;
            this.right = null;
        }

        public Node(int frequency, Node left, Node right) {
            this.key = '\0'; // Utilisation de '\0' pour un nœud interne
            this.frequency = frequency;
            this.left = left;
            this.right = right;
        }

        @Override
        public int compareTo(Node other) {
            return this.frequency - other.frequency;
        }
    }

    // Fonction pour générer l'arbre de Huffman
    public static Node buildHuffmanTree(Map<Character, Integer> frequencies) {
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();

        for (Map.Entry<Character, Integer> entry : frequencies.entrySet()) {
            priorityQueue.add(new Node(entry.getKey(), entry.getValue()));
        }

        while (priorityQueue.size() > 1) {
            Node left = priorityQueue.poll();
            Node right = priorityQueue.poll();
            Node parent = new Node(left.frequency + right.frequency, left, right);
            priorityQueue.add(parent);
        }

        return priorityQueue.poll();
    }

    // Fonction pour générer le code de Huffman
    public static void generateCodes(Node root, String code, Map<Character, String> huffmanCodes) {
        if (root == null) return;

        if (root.left == null && root.right == null) {
            huffmanCodes.put(root.key, code);
        }

        generateCodes(root.left, code + "0", huffmanCodes);
        generateCodes(root.right, code + "1", huffmanCodes);
    }

    // Fonction principale pour la compression
    public static Map<Character, String> compress(String input) {
        Map<Character, Integer> frequencies = new HashMap<>();
        for (char c : input.toCharArray()) {
            frequencies.put(c, frequencies.getOrDefault(c, 0) + 1);
        }

        Node root = buildHuffmanTree(frequencies);
        Map<Character, String> huffmanCodes = new HashMap<>();
        generateCodes(root, "", huffmanCodes);

        return huffmanCodes;
    }

    // Fonction pour générer le texte compressé
    public static String getCompressedText(String input, Map<Character, String> huffmanCodes) {
        StringBuilder compressedText = new StringBuilder();

        for (char c : input.toCharArray()) {
            compressedText.append(huffmanCodes.get(c));
        }

        return compressedText.toString();
    }

    // Fonction pour calculer la taille en bits
    public static void calculateCompression(String input, String compressedText) {
        int originalSize = input.length() * 8; // Taille originale en bits
        int compressedSize = compressedText.length(); // Taille compressée en bits

        System.out.println("Taille originale en bits : " + originalSize);
        System.out.println("Taille compressée en bits : " + compressedSize);
        System.out.printf("Ratio de compression : %.2f%%\n",
                100.0 * (originalSize - compressedSize) / originalSize);
    }

    public static void main(String[] args) {
        String input = "AMELJBELI"; // Exemple de chaîne à compresser
        Map<Character, String> huffmanCodes = compress(input);

        // Affichage des codes de Huffman
        System.out.println("Codes de Huffman :");
        for (Map.Entry<Character, String> entry : huffmanCodes.entrySet()) {
            System.out.println("Character: " + entry.getKey() + " Code: " + entry.getValue());
        }

        // Générer et afficher le texte compressé
        String compressedText = getCompressedText(input, huffmanCodes);
        System.out.println("Compressed text : " + compressedText);

        // Calculer et afficher la taille en bits
        calculateCompression(input, compressedText);
    }
}
