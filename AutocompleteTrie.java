import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AutocompleteTrie {
    private TrieNode root;
    private int size;

    public AutocompleteTrie() {
        root = new TrieNode();
        size = 0;
    }

    public boolean addWord(String word) {
        if (word == null) {
            return false;
        }

        String normalized = word.trim().toLowerCase(Locale.ROOT);

        if (normalized.isEmpty() || isWord(normalized)) {
            return false;
        }

        HashMap<Character, TrieNode> children = root.children;

        for (int i = 0; i < normalized.length(); i++) {
            char c = normalized.charAt(i);
            TrieNode current;

            if (children.containsKey(c)) {
                current = children.get(c);
            } else {
                current = new TrieNode(c);
                children.put(c, current);
            }

            if (i == normalized.length() - 1) {
                current.setEndOfWord(true);
                size++;
            }

            children = current.children;
        }

        return true;
    }

    public int size() {
        return size;
    }

    public boolean isWord(String s) {
        if (s == null) {
            return false;
        }

        String normalized = s.trim().toLowerCase(Locale.ROOT);
        if (normalized.isEmpty()) {
            return false;
        }

        TrieNode node = searchNode(normalized);
        return node != null && node.isEndOfWord();
    }

    public TrieNode searchNode(String str) {
        if (str == null) {
            return null;
        }

        HashMap<Character, TrieNode> children = root.children;
        TrieNode current = root;

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            if (!children.containsKey(c)) {
                return null;
            }

            current = children.get(c);
            children = current.children;
        }

        return current;
    }

    /**
     * Returns every complete dictionary word beginning with prefix.
     */
    public List<String> fetchAll(String prefix) {
        if (prefix == null) {
            return Collections.emptyList();
        }

        String normalized = prefix.trim().toLowerCase(Locale.ROOT);
        TrieNode prefixNode = searchNode(normalized);

        if (prefixNode == null) {
            return Collections.emptyList();
        }

        List<String> results = new ArrayList<>();
        collectWords(prefixNode, normalized, results);
        Collections.sort(results);
        return results;
    }

    private void collectWords(TrieNode node, String currentWord, List<String> results) {
        if (node.isEndOfWord()) {
            results.add(currentWord);
        }

        ArrayList<Character> nextCharacters =
                new ArrayList<>(node.getChildCharacters());
        Collections.sort(nextCharacters);

        for (Character c : nextCharacters) {
            TrieNode child = node.getChild(c);
            collectWords(child, currentWord + c, results);
        }
    }
}
