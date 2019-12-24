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
    long expired;

    CacheNode prev;
    CacheNode next;

    public CacheNode(int key, int value, long exp) {
        this.key = key;
        this.value = value;
        this.expired = exp;
        prev = this;
        next = this;
    }
}
