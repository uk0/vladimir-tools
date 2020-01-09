package utils.algorithm.trie;

/**
 * @author zhangjianxin
 * @version 2019-12-30.
 * @url github.com/uk0
 * @project vladimir
 * @since JDK1.8.
 */
public class Trie {

    private TrieNode root;
    private boolean startWith;

    public Trie() {
        root = new TrieNode();
    }

    // Inserts a word into the trie.
    public void insert(String word) {
        insert(word,root,0);
    }

    private void insert(String word, TrieNode root, int idx){
        if(idx==word.length()){
            root.isWord=true;
            return;
        }
        int index = word.charAt(idx)-'a';
        if(root.children[index]==null)
            root.children[index]=new TrieNode();
        insert(word, root.children[index], idx+1);
    }

    // Returns if the word is in the trie.
    public boolean search(String word) {
        return search(word, root, 0);
    }

    public boolean search(String word, TrieNode root, int idx){
        if(idx==word.length()) {
            startWith = true;
            return root.isWord;
        }
        int index = word.charAt(idx)-'a';
        if(root.children[index]==null) {
            startWith = false;
            return false;
        }

        return  search(word,root.children[index],idx+1);
    }

    // Returns if there is any word in the trie
    // that starts with the given prefix.
    public boolean startsWith(String prefix) {
        search(prefix);
        return startWith;
    }

    public static void main(String[] args) {
        Trie trie = new Trie();
        trie.insert("test");
        trie.insert("thread");
        System.out.println(trie.search("t"));
        System.out.println(trie.startsWith("t"));
    }
}

