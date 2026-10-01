import java.util.HashMap;
import java.util.Set;

public class TrieNode {
    HashMap<Character, TrieNode> children;
    private char text;
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        text = ' ';
        isWord = false;
    }

    public TrieNode(char text) {
        this();
        this.text = text;
    }

    public TrieNode getChild(Character c) {
        return children.get(c);
    }

    public TrieNode insert(Character c) {
        if (children.containsKey(c)) {
            return null;
        }

        TrieNode next = new TrieNode(c);
        children.put(c, next);
        return next;
    }

    public char getText() {
        return text;
    }

    public void setendsWord(boolean b) {
        isWord = b;
    }

    public boolean endWord() {
        return isWord;
    }

    public Set<Character> getValidNextCharacter() {
        return children.keySet();
    }
}
