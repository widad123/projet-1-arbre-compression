import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;

public class CompressionBenchmarkApp extends JFrame {

    private JLabel directoryLabel;
    private JTable resultTable;
    private DefaultTableModel tableModel;
    private Map<String, Map<String, String>> compressedTextMap = new HashMap<>();
    private Map<String, Map<String, String>> decompressedTextMap = new HashMap<>();

    public Map<String, Color> fileColorMap = new HashMap<>();

    private List<Color> colorList = Arrays.asList(
            new Color(255, 230, 230), // Light red
            new Color(230, 255, 230), // Light green
            new Color(230, 230, 255), // Light blue
            new Color(255, 255, 230), // Light yellow
            new Color(255, 230, 255), // Light pink
            new Color(230, 255, 255)  // Light cyan
    );

    public CompressionBenchmarkApp() {
        setTitle("Text Compression Benchmark");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton selectDirButton = new JButton("Select HTML Directory");
        topPanel.add(selectDirButton);

        directoryLabel = new JLabel("No directory selected");
        topPanel.add(directoryLabel);

        JButton huffmanButton = new JButton("Huffman Coding");
        JButton lzwButton = new JButton("LZW");
        JButton bzip2Button = new JButton("Bzip2");
        JButton deflateButton = new JButton("Deflate");

        huffmanButton.setToolTipText("Click to learn about Huffman Coding");
        lzwButton.setToolTipText("Click to learn about LZW Compression");
        bzip2Button.setToolTipText("Click to learn about Bzip2 Compression");
        deflateButton.setToolTipText("Click to learn about Deflate Compression");

        JPanel algorithmButtonsPanel = new JPanel();
        algorithmButtonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        algorithmButtonsPanel.add(huffmanButton);
        algorithmButtonsPanel.add(lzwButton);
        algorithmButtonsPanel.add(bzip2Button);
        algorithmButtonsPanel.add(deflateButton);

        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BorderLayout());
        topContainer.add(topPanel, BorderLayout.NORTH);
        topContainer.add(algorithmButtonsPanel, BorderLayout.SOUTH);

        add(topContainer, BorderLayout.NORTH);

        huffmanButton.addActionListener(e -> showAlgorithmExplanation("Huffman"));
        lzwButton.addActionListener(e -> showAlgorithmExplanation("LZW"));
        bzip2Button.addActionListener(e -> showAlgorithmExplanation("Bzip2"));
        deflateButton.addActionListener(e -> showAlgorithmExplanation("Deflate"));


        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == getColumnCount() - 1;
            }
        };
        tableModel.addColumn("File Name");
        tableModel.addColumn("Algorithm");
        tableModel.addColumn("Compression Ratio (%)");
        tableModel.addColumn("Compression Time (micro s)");
        tableModel.addColumn("Decompression Ratio (%)");
        tableModel.addColumn("Decompression Time (micro s)");
        tableModel.addColumn("Decompression Successful");
        tableModel.addColumn("");

        resultTable = new JTable(tableModel);
        resultTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer());

        int detailsColumnIndex = resultTable.getColumnModel().getColumnCount() - 1;
        resultTable.getColumnModel().getColumn(detailsColumnIndex).setCellRenderer(new DetailsButtonRenderer());
        resultTable.getColumnModel().getColumn(detailsColumnIndex).setCellEditor(new DetailsButtonEditor());
        resultTable.getColumnModel().getColumn(detailsColumnIndex).setPreferredWidth(50);

        JScrollPane scrollPane = new JScrollPane(resultTable);
        add(scrollPane, BorderLayout.CENTER);

        selectDirButton.addActionListener(e -> {
            JFileChooser directoryChooser = new JFileChooser();
            directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int option = directoryChooser.showOpenDialog(CompressionBenchmarkApp.this);
            if (option == JFileChooser.APPROVE_OPTION) {
                File selectedDirectory = directoryChooser.getSelectedFile();
                directoryLabel.setText("Selected Directory: " + selectedDirectory.getAbsolutePath());
                processFiles(selectedDirectory);
            }
        });

        resultTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = resultTable.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    String fileName = (String) tableModel.getValueAt(row, 0);
                    String algorithm = (String) tableModel.getValueAt(row, 1);
                    showCompressedTextPopup(fileName, algorithm);
                }
            }
        });
    }

    private void showAlgorithmExplanation(String algorithmName) {
        try {
            JFrame explanationFrame = new JFrame(algorithmName + " Explanation");
            explanationFrame.setSize(700, 500);
            explanationFrame.setLocationRelativeTo(this);

            // Use a JTextPane to display rich text
            JTextPane textPane = new JTextPane();
            textPane.setContentType("text/html");
            textPane.setEditable(false);

            String htmlContent = getAlgorithmExplanationHTML(algorithmName);
            textPane.setText(htmlContent);

            JScrollPane scrollPane = new JScrollPane(textPane);
            explanationFrame.add(scrollPane);

            explanationFrame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while displaying the explanation.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getAlgorithmExplanationHTML(String algorithmName) {
        String htmlContent = "";
        switch (algorithmName) {
            case "Huffman":
                htmlContent = getHuffmanExplanation();
                break;
            case "LZW":
                htmlContent = getLZWExplanation();
                break;
            case "Bzip2":
                htmlContent = getBzip2Explanation();
                break;
            case "Deflate":
                htmlContent = getDeflateExplanation();
                break;
        }
        return htmlContent;
    }

    private String getHuffmanExplanation() {
        return "<html>"
                + "<h1 style='text-align:center;'>Huffman Coding</h1>"
                + "<p>Huffman Coding is a lossless data compression algorithm.</p>"
                + "<p>It uses variable-length codes to represent characters based on their frequencies.</p>"
                + "<h2>How It Works:</h2>"
                + "<ol>"
                + "<li>Calculate the frequency of each character in the input data.</li>"
                + "<li>Build a Huffman Tree from the frequency data.</li>"
                + "<li>Generate Huffman Codes by traversing the tree.</li>"
                + "<li>Encode the input data using the generated codes.</li>"
                + "</ol>"
                + "<h2>Example:</h2>"
                + "<pre>"
                + "Example Input: 'hello world'\n"
                + "\n"
                + "Character Frequencies:\n"
                + "h:1 e:1 l:3 o:2  :1 w:1 r:1 d:1\n"
                + "\n"
                + "Huffman Tree:\n"
                + "        (*,11)\n"
                + "       /      \\\n"
                + "    (*,5)     (*,6)\n"
                + "    /   \\     /    \\\n"
                + " (l,3)(o,2)(*,3)  (d,1)\n"
                + "           /   \\\n"
                + "       (h,1) (e,1)\n"
                + "</pre>"
                + "</html>";
    }

    private String getLZWExplanation() {
        return "<html>"
                + "<h1 style='text-align:center;'>LZW Compression</h1>"
                + "<p>LZW is a dictionary-based lossless data compression algorithm.</p>"
                + "<h2>How It Works:</h2>"
                + "<ol>"
                + "<li>Initialize the dictionary with all possible single-character strings.</li>"
                + "<li>Scan the input data to find the longest string 'w' that exists in the dictionary.</li>"
                + "<li>Output the code for 'w' and add 'w' concatenated with the next character to the dictionary.</li>"
                + "<li>Repeat the process for the remaining input data.</li>"
                + "</ol>"
                + "<h2>Example:</h2>"
                + "<pre>"
                + "Example Input: 'ABABBABCABABBA'\n"
                + "\n"
                + "Dictionary Build Process:\n"
                + "256: 'A'\n"
                + "257: 'B'\n"
                + "258: 'AB'\n"
                + "259: 'BA'\n"
                + "260: 'BAB'\n"
                + "261: 'ABA'\n"
                + "</pre>"
                + "</html>";
    }

    private String getBzip2Explanation() {
        return "<html>"
                + "<h1 style='text-align:center;'>Bzip2 Compression</h1>"
                + "<p>Bzip2 uses the Burrows-Wheeler Transform and Huffman Coding for compression.</p>"
                + "<h2>How It Works:</h2>"
                + "<ol>"
                + "<li>Apply the Burrows-Wheeler Transform (BWT) to the input data.</li>"
                + "<li>Perform Move-to-Front Transform to reduce entropy.</li>"
                + "<li>Apply Run-Length Encoding to compress repeated characters.</li>"
                + "<li>Use Huffman Coding to encode the data.</li>"
                + "</ol>"
                + "<h2>Example:</h2>"
                + "<pre>"
                + "Example Input: 'banana'\n"
                + "\n"
                + "Burrows-Wheeler Transform Result:\n"
                + "annb$aa\n"
                + "</pre>"
                + "</html>";
    }

    private String getDeflateExplanation() {
        return "<html>"
                + "<h1 style='text-align:center;'>Deflate Compression</h1>"
                + "<p>Deflate combines LZ77 and Huffman Coding algorithms.</p>"
                + "<h2>How It Works:</h2>"
                + "<ol>"
                + "<li>Use LZ77 to find and replace duplicate strings with pointers.</li>"
                + "<li>Compress the sequence of pointers and literals using Huffman Coding.</li>"
                + "</ol>"
                + "<h2>Example:</h2>"
                + "<pre>"
                + "Example Input: 'aabaaab'\n"
                + "\n"
                + "LZ77 Output Triplets (Offset, Length, Next Char):\n"
                + "(0,0,'a')\n"
                + "(1,1,'b')\n"
                + "(2,2,'b')\n"
                + "</pre>"
                + "</html>";
    }

    private void showHuffmanInteractive() {
        JFrame frame = new JFrame("Interactive Huffman Coding");
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel();
        JLabel inputLabel = new JLabel("Enter Text: ");
        JTextField inputField = new JTextField(30);
        JButton generateButton = new JButton("Generate Huffman Tree");
        inputPanel.add(inputLabel);
        inputPanel.add(inputField);
        inputPanel.add(generateButton);

        mainPanel.add(inputPanel, BorderLayout.NORTH);

        JTextArea outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane outputScrollPane = new JScrollPane(outputArea);

        mainPanel.add(outputScrollPane, BorderLayout.CENTER);

        frame.add(mainPanel);
        frame.setVisible(true);

        generateButton.addActionListener(e -> {
            String text = inputField.getText();
            if (!text.isEmpty()) {
                HuffmanCompression huffman = new HuffmanCompression();
                Map<Character, String> huffmanCode = huffman.buildHuffmanTree(text);
                StringBuilder steps = new StringBuilder();

                steps.append("Character Frequencies:\n");
                Map<Character, Integer> freqMap = huffman.getFrequencyMap(text);
                for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) {
                    steps.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                }

                steps.append("\nHuffman Codes:\n");
                for (Map.Entry<Character, String> entry : huffmanCode.entrySet()) {
                    steps.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                }

                outputArea.setText(steps.toString());
            }
        });
    }


    private void processFiles(File directory) {
        tableModel.setRowCount(0);
        compressedTextMap.clear();
        fileColorMap.clear();

        File[] files = directory.listFiles((dir, name) ->
                name.toLowerCase().endsWith(".html") || name.toLowerCase().endsWith(".htm")
        );

        if (files == null || files.length == 0) {
            JOptionPane.showMessageDialog(this, "No HTML files found in the selected directory.");
            return;
        }

        int fileIndex = 0;

        for (File file : files) {
            try {
                Color fileColor = colorList.get(fileIndex % colorList.size());


                fileColorMap.put(file.getName(), fileColor);
                fileIndex++;

                String text = HtmlTextExtractor.extractText(file);
                int originalSize = text.getBytes("UTF-8").length;

                benchmarkAlgorithm(file.getName(), "Huffman", text, originalSize);
                benchmarkAlgorithm(file.getName(), "LZW", text, originalSize);
                benchmarkAlgorithm(file.getName(), "Bzip2", text, originalSize);
                benchmarkAlgorithm(file.getName(), "Deflate", text, originalSize);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Color generateColor(int index) {
        float hue = (index * 0.618033988749895f) % 1;
        return Color.getHSBColor(hue, 0.5f, 0.95f);
    }

    private void benchmarkAlgorithm(String fileName, String algorithmName, String text, int originalSize) {
        try {
            long startTime = System.currentTimeMillis();
            byte[] compressedData = null;
            String compressedText = "";
            String decompressedText = "";
            long compressionTime = 0;
            long decompressionTime = 0;

            switch (algorithmName) {
                case "Huffman":
                    HuffmanCompression huffman = new HuffmanCompression();
                    Map<Character, String> huffmanCode = huffman.buildHuffmanTree(text);

                    long compStart = System.currentTimeMillis();
                    compressedData = huffman.compress(text, huffmanCode);
                    compressionTime = System.currentTimeMillis() - compStart;

                    compressedText = huffman.getCompressedBitString(text, huffmanCode);

                    long decompStart = System.currentTimeMillis();
                    decompressedText = huffman.decompress(compressedData, huffmanCode);
                    decompressionTime = System.currentTimeMillis() - decompStart;
                    break;

                case "LZW":
                    LZWCompression lzw = new LZWCompression();

                    long lzwCompStart = System.currentTimeMillis();
                    List<Integer> lzwCompressed = lzw.compress(text);
                    compressionTime = System.currentTimeMillis() - lzwCompStart;

                    compressedData = toByteArray(lzwCompressed);
                    compressedText = lzwCompressed.toString();

                    long lzwDecompStart = System.currentTimeMillis();
                    decompressedText = lzw.decompress(lzwCompressed);
                    decompressionTime = System.currentTimeMillis() - lzwDecompStart;
                    break;

                case "Bzip2":
                    Bzip2Compression bzip2 = new Bzip2Compression();

                    long bzip2CompStart = System.currentTimeMillis();
                    compressedData = bzip2.compress(text);
                    compressionTime = System.currentTimeMillis() - bzip2CompStart;

                    compressedText = Base64.getEncoder().encodeToString(compressedData);

                    long bzip2DecompStart = System.currentTimeMillis();
                    decompressedText = bzip2.decompress(compressedData);
                    decompressionTime = System.currentTimeMillis() - bzip2DecompStart;
                    break;

                case "Deflate":
                    DeflateCompression deflate = new DeflateCompression();

                    long deflateCompStart = System.currentTimeMillis();
                    compressedData = deflate.compress(text);
                    compressionTime = System.currentTimeMillis() - deflateCompStart;

                    compressedText = Base64.getEncoder().encodeToString(compressedData);

                    long deflateDecompStart = System.currentTimeMillis();
                    decompressedText = deflate.decompress(compressedData);
                    decompressionTime = System.currentTimeMillis() - deflateDecompStart;
                    break;
            }

            boolean isSuccessful = text.equals(decompressedText);

            double compressionRatio = ((double) compressedData.length / originalSize) * 100;
            double decompressionRatio = ((double) decompressedText.getBytes("UTF-8").length / originalSize) * 100;

            tableModel.addRow(new Object[]{
                    fileName,
                    algorithmName,
                    String.format("%.2f", compressionRatio),
                    compressionTime * 1000,
                    String.format("%.2f", decompressionRatio),
                    decompressionTime * 1000,
                    isSuccessful ? "Yes" : "No",
                    "Details"
            });

            compressedTextMap.computeIfAbsent(fileName, k -> new HashMap<>()).put(algorithmName, compressedText);
            decompressedTextMap.computeIfAbsent(fileName, k -> new HashMap<>()).put(algorithmName, decompressedText);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showCompressedTextPopup(String fileName, String algorithmName) {
        try {
            String compressedText = getCompressedText(fileName, algorithmName);
            String decompressedText = getDecompressedText(fileName, algorithmName);

            JTextArea compressedTextArea = new JTextArea(compressedText);
            compressedTextArea.setEditable(false);
            JScrollPane compressedScrollPane = new JScrollPane(compressedTextArea);

            JTextArea decompressedTextArea = new JTextArea(decompressedText);
            decompressedTextArea.setEditable(false);
            JScrollPane decompressedScrollPane = new JScrollPane(decompressedTextArea);

            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.addTab("Compressed Text", compressedScrollPane);
            tabbedPane.addTab("Decompressed Text", decompressedScrollPane);

            tabbedPane.setPreferredSize(new Dimension(600, 400));

            JOptionPane.showMessageDialog(this, tabbedPane, "Data for " + algorithmName, JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getCompressedText(String fileName, String algorithmName) {
        return compressedTextMap.getOrDefault(fileName, Collections.emptyMap()).getOrDefault(algorithmName, "No data available");
    }

    private String getDecompressedText(String fileName, String algorithmName) {
        return decompressedTextMap.getOrDefault(fileName, Collections.emptyMap())
                .getOrDefault(algorithmName, "No data available");
    }

    private byte[] toByteArray(List<Integer> list) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        for (int value : list) {
            byteStream.write(value);
        }
        return byteStream.toByteArray();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CompressionBenchmarkApp app = new CompressionBenchmarkApp();
            app.setVisible(true);
        });
    }

    class CustomTableCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int col) {

            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

            String fileName = (String) table.getValueAt(row, 0);

            Color backgroundColor = fileColorMap.getOrDefault(fileName, Color.WHITE);

            setBackground(backgroundColor);

            if (isSelected) {
                setBackground(table.getSelectionBackground());
            }

            return this;
        }
    }
    class DetailsButtonRenderer extends JButton implements TableCellRenderer {

        public DetailsButtonRenderer() {
            setText("...");
            setMargin(new Insets(0, 0, 0, 0));
            setToolTipText("Click to view details");
            setBorderPainted(false);
            setContentAreaFilled(false);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                setBackground(table.getBackground());
            }
            return this;
        }
    }

    class DetailsButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private JButton button;
        private int row;

        public DetailsButtonEditor() {
            button = new JButton("...");
            button.setMargin(new Insets(0, 0, 0, 0));
            button.setToolTipText("Click to view details");
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                    String fileName = (String) tableModel.getValueAt(row, 0);
                    String algorithm = (String) tableModel.getValueAt(row, 1);
                    showCompressedTextPopup(fileName, algorithm);
                }
            });
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
                                                     int row, int column) {
            this.row = row;
            if (isSelected) {
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setBackground(table.getBackground());
            }
            return button;
        }
    }
}


