import java.util.*;

public class LZWCompression {

    public List<Integer> compress(String text) {
        int dictSize = 256;
        Map<String, Integer> dictionary = new HashMap<>();

        for (int i = 0; i < dictSize; i++) {
            dictionary.put("" + (char) i, i);
        }

        String w = "";
        List<Integer> result = new ArrayList<>();

        for (char c : text.toCharArray()) {
            String wc = w + c;
            if (dictionary.containsKey(wc)) {
                w = wc;
            } else {
                result.add(dictionary.get(w));
                dictionary.put(wc, dictSize++);
                w = "" + c;
            }
        }

        if (!w.equals("")) {
            result.add(dictionary.get(w));
        }

        return result;
    }

    public String decompress(List<Integer> compressedCodes) {
        int dictSize = 256;
        Map<Integer, String> dictionary = new HashMap<>();

        for (int i = 0; i < dictSize; i++) {
            dictionary.put(i, "" + (char) i);
        }

        String w = "" + (char) (int) compressedCodes.remove(0);
        StringBuilder result = new StringBuilder(w);

        for (int k : compressedCodes) {
            String entry;
            if (dictionary.containsKey(k)) {
                entry = dictionary.get(k);
            } else if (k == dictSize) {
                entry = w + w.charAt(0);
            } else {
                throw new IllegalArgumentException("Invalid compressed code: " + k);
            }

            result.append(entry);

            dictionary.put(dictSize++, w + entry.charAt(0));

            w = entry;
        }

        return result.toString();
    }
}
