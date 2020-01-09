package utils.algorithm.trie;

/**
 * @author zhangjianxin
 * @version 2019-12-30.
 * @url github.com/uk0
 * @project vladimir
 * @since JDK1.8.
 */
public class TrieNode {
    // Initialize your data structure here.
    public TrieNode[] children;
    public boolean isWord;
    public TrieNode() {
        children = new TrieNode[26];
    }
}
