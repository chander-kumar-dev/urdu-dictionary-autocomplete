import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class SearchWindow {
    private static final int MAX_RESULTS = 20;

    public static void main(String[] args) {
        AutocompleteTrie trieDictionary = new AutocompleteTrie();
        DictionaryLoader file = new DictionaryLoader("Urdu-words.txt");
        for (String word : file.dict()) {
            trieDictionary.addWord(word);
        }

        // 2. Build the window on Swing's UI thread
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Urdu Autocomplete");
            JTextField searchBox = new JTextField();
            DefaultListModel<String> results = new DefaultListModel<>();
            JList<String> resultList = new JList<>(results);
            JLabel status = new JLabel("Words loaded: " + trieDictionary.size());

            // Bigger font and right-to-left layout for Urdu
            Font urduFont = new Font(Font.DIALOG, Font.PLAIN, 22);
            searchBox.setFont(urduFont);
            resultList.setFont(urduFont);
            searchBox.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            resultList.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

            // 3. Search again every time the text in the box changes
            searchBox.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    showResults();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    showResults();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    showResults();
                }

                private void showResults() {
                    results.clear();
                    String prefix = searchBox.getText().trim();

                    if (prefix.isEmpty()) {
                        status.setText("Words loaded: " + trieDictionary.size());
                        return;
                    }

                    List<String> matches = trieDictionary.fetchAll(prefix);
                    for (int i = 0; i < matches.size() && i < MAX_RESULTS; i++) {
                        results.addElement(matches.get(i));
                    }
                    status.setText("Matches: " + matches.size());
                }
            });

            // 4. Put the parts on the window and show it
            frame.add(searchBox, BorderLayout.NORTH);
            frame.add(new JScrollPane(resultList), BorderLayout.CENTER);
            frame.add(status, BorderLayout.SOUTH);
            frame.setSize(500, 600);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}