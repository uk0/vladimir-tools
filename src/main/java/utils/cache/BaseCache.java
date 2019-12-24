package utils.cache;

import java.util.Map;
import java.util.concurrent.*;


/**
 * @author zhangjianxin
 * @version 2019-12-24.
 * @url github.com/uk0
 * @project vladimir
 * @since JDK1.8.
 */
public class BaseCache implements Action {


    // 代替hash_map

    private  Map<Integer, CacheNode> m;

    // 指向双链表的头结点

    private CacheNode head;

    // 指向双链表的尾结点

    private CacheNode tail;

    // Cache的容量

    private int capacity;

    // 计数

    private int count;


    public BaseCache(int capacity) {
        // 容量
        this.capacity = capacity;
        // 计数器
        this.count = 0;
        // 头部
        head = new CacheNode(0, 0, -1);
        // 末尾
        tail = new CacheNode(0, 0, -1);

        head.prev = new CacheNode(0, 0, -1);
        head.next = tail;
        tail.prev = head;
        tail.next = new CacheNode(0, 0, -1);
        // 初始化容器
        m = new ConcurrentHashMap<Integer, CacheNode>();

        // 1秒扫一次
        Runnable watchTTL = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        for (Map.Entry<Integer, CacheNode> k : m.entrySet()) {
                            if (k.getValue().expired != -1) {
                                if (k.getValue().expired <= System.currentTimeMillis()) {
                                    removeLRUNode(k.getValue());
                                }
                            }
                            // 1秒扫一次
                            Thread.sleep(TimeUnit.MINUTES.toSeconds(1));
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        // 执行JOB
        /***
         * Design an LRU cache with all the operations to be done in $O(1)$ .
         * */
        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 15, 200,
                TimeUnit.MILLISECONDS, new SynchronousQueue<>());
        executor.execute(watchTTL);

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
    public boolean put(int key, int value, long expired) {
        // 没找到
        if (!m.containsKey(key)) {
            // 获取当前时间
            CacheNode node = new CacheNode(0, 0, (expired==-1?-1:System.currentTimeMillis() + (expired * 1000)));
            // Cache已满
            if (count == capacity) {
                // 删除不常用的
                removeLRUNode(tail.prev);
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
            //拿到key
            CacheNode node = m.get(key);
            // 移动到最前面
            detachNode(node);
            node.value = value;
            // 超时时间
            node.expired = (expired==-1?-1:System.currentTimeMillis() + (expired * 1000));
            //插入到后面
            insertToFront(node);

            return true;
        }
    }

    /**
     * 获取大小
     *
     * @return int 提交状态
     */
    @Override
    public int size() {
        return count;
    }

    /**
     * 提交一个 KV
     *
     * @param key   键
     * @param value 值
     * @return boolean 提交状态
     */
    @Override
    public boolean put(int key, int value) {
        this.put(key, value, -1);
        return false;
    }

    void removeLRUNode(CacheNode node) {
        // 迁移Node
        detachNode(node);
        // 删除Node
        m.remove(node.key);
        // 计数器减1
        --count;
    }

    void detachNode(CacheNode node) {
        // 当前节点的 prev 节点的 next 的节点 覆盖成 当前节点的 next 节点
        node.prev.next = node.next;
        // 当前节点的 next 节点的 prev 的节点 覆盖成 当前节点的 prev 节点
        node.next.prev = node.prev;
    }


    void insertToFront(CacheNode node) {
        node.next = head.next;
        node.prev = head;
        head.next = node;
        node.next.prev = node;
    }
}
