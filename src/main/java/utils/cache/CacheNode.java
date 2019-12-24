package utils.cache;

/**
 * @author zhangjianxin
 * @version 2019-12-24.
 * @url github.com/uk0
 * @project vladimir
 * @since JDK1.8.
 */
public class CacheNode {
    int key;
    int value;
    CacheNode prev;
    CacheNode next;

    public CacheNode(int key, int value) {
        this.key = key;
        this.value = value;
        prev  = this;
        next  = this;
    }
}
