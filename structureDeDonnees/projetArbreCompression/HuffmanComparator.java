package projetArbreCompression;

import java.util.Comparator;

class HuffmanComparator implements Comparator<HuffmanNode> {
    public int compare(HuffmanNode a, HuffmanNode b) {
        return a.getFrequency() - b.getFrequency();
    }
}