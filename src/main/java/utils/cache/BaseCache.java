package utils.cache;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangjianxin
 * @version 2019-12-24.
 * @url github.com/uk0
 * @project vladimir
 * @since JDK1.8.
 */
public class BaseCache implements Action {

    // 代替hash_map

    private Map<Integer, CacheNode> m;

    // 指向双链表的头结点

    private CacheNode head;

    // 指向双链表的尾结点

    private CacheNode tail;

    // Cache的容量

    private int capacity;

    // 计数

    private int count;

    /***
     * Design an LRU cache with all the operations to be done in $O(1)$ .
     * */

    public BaseCache(int capacity) {
        this.capacity = capacity;
        this.count = 0;
        head = new CacheNode(0,0);
        tail = new CacheNode(0,0);
        head.prev = new CacheNode(0,0);
        head.next = tail;
        tail.prev = head;
        tail.next = new CacheNode(0,0);
        // 初始化
        m = new ConcurrentHashMap<>();

    }

    /**
     * @param key
     */
    @Override
    public int get(int key) {
        // 没找到
        if (!m.containsKey(key)) {
            return -1;
        } else {
            CacheNode node = m.get(key);
            // 命中，移至头部
            detachNode(node);
            insertToFront(node);
            return node.value;
        }
    }

    @Override
    public boolean put(int key, int value) {
        // 没找到
        if (!m.containsKey(key)) {
            CacheNode node = new CacheNode(0,0);
            // Cache已满
            if (count == capacity) {
                removeLRUNode();
            }

            node.key = key;
            node.value = value;
            // 插入哈希表
            m.put(key, node);
            // 插入链表头部
            insertToFront(node);
            ++count;
            return true;
        } else {
            // 没有就创建key
            CacheNode node = m.get(key);
            // 移动到最前面
            detachNode(node);
            node.value = value;
            //插入到后面
            insertToFront(node);

            return true;
        }
    }

    void removeLRUNode() {
        CacheNode node = tail.prev;
        detachNode(node);
        m.remove(node.key);
        --count;
    }

    void detachNode(CacheNode node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }


    void insertToFront(CacheNode node) {
        node.next = head.next;
        node.prev = head;
        head.next = node;
        node.next.prev = node;
    }
}
