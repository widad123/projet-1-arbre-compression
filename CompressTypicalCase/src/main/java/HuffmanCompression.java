import java.util.*;

public class HuffmanCompression {

    private class Node implements Comparable<Node> {
        char ch;
        int freq;
        Node left, right;

        Node(char ch, int freq) {
            this.ch = ch;
            this.freq = freq;
        }

        public int compareTo(Node other) {
            return this.freq - other.freq;
        }
    }

    public Map<Character, String> buildHuffmanTree(String text) {
        Map<Character, Integer> freqMap = new HashMap<>();
        for (char ch : text.toCharArray()) {
            freqMap.put(ch, freqMap.getOrDefault(ch, 0) + 1);
        }

        PriorityQueue<Node> pq = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) {
            pq.add(new Node(entry.getKey(), entry.getValue()));
        }

        while (pq.size() > 1) {
            Node left = pq.poll();
            Node right = pq.poll();

            Node merged = new Node('\0', left.freq + right.freq);
            merged.left = left;
            merged.right = right;

            pq.add(merged);
        }

        Node root = pq.peek();
        Map<Character, String> huffmanCode = new HashMap<>();
        generateCodes(root, "", huffmanCode);

        return huffmanCode;
    }

    private void generateCodes(Node node, String code, Map<Character, String> huffmanCode) {
        if (node == null)
            return;

        // Leaf node
        if (node.left == null && node.right == null) {
            huffmanCode.put(node.ch, code);
        }

        generateCodes(node.left, code + '0', huffmanCode);
        generateCodes(node.right, code + '1', huffmanCode);
    }

    public byte[] compress(String text, Map<Character, String> huffmanCode) {
        StringBuilder sb = new StringBuilder();
        for (char ch : text.toCharArray()) {
            sb.append(huffmanCode.get(ch));
        }

        int length = (sb.length() + 7) / 8;
        byte[] compressedData = new byte[length];

        for (int i = 0; i < sb.length(); i++) {
            int index = i / 8;
            compressedData[index] <<= 1;
            if (sb.charAt(i) == '1') {
                compressedData[index] |= 1;
            } else {
                compressedData[index] |= 0;
            }
        }

        return compressedData;
    }

    public String getCompressedBitString(String text, Map<Character, String> huffmanCode) {
        StringBuilder sb = new StringBuilder();
        for (char ch : text.toCharArray()) {
            sb.append(huffmanCode.get(ch));
        }
        return sb.toString();
    }

    public String decompress(byte[] compressedData, Map<Character, String> huffmanCode) {
        Map<String, Character> reverseHuffmanCode = new HashMap<>();
        for (Map.Entry<Character, String> entry : huffmanCode.entrySet()) {
            reverseHuffmanCode.put(entry.getValue(), entry.getKey());
        }

        StringBuilder bitString = new StringBuilder();
        for (byte b : compressedData) {
            bitString.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }

        StringBuilder currentCode = new StringBuilder();
        StringBuilder decodedText = new StringBuilder();

        for (int i = 0; i < bitString.length(); i++) {
            currentCode.append(bitString.charAt(i));
            if (reverseHuffmanCode.containsKey(currentCode.toString())) {
                decodedText.append(reverseHuffmanCode.get(currentCode.toString()));
                currentCode.setLength(0);
            }
        }

        return decodedText.toString();
    }

    public Map<Character, Integer> getFrequencyMap(String text) {
        Map<Character, Integer> freqMap = new HashMap<>();
        for (char ch : text.toCharArray()) {
            freqMap.put(ch, freqMap.getOrDefault(ch, 0) + 1);
        }
        return freqMap;
    }

}
