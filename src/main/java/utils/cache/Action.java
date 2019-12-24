package utils.cache;

/**
 * @author zhangjianxin
 * @version 2019-12-24.
 * @url github.com/uk0
 * @project vladimir
 * @since JDK1.8.
 */
public interface Action {
    /**
     * 命中一个key
     * @param key 键
     * @return int value
     * */
    int get(int key);
    /**
     * 提交一个 KV
     * @param key 键
     * @param value 值
     * @return boolean 提交状态
     * */
    boolean put(int key, int value);
    /**
     * 提交一个 KV
     * @param key 键
     * @param value 值
     * @param ttl 超时时间
     * @return boolean 提交状态
     * */
    boolean put(int key, int value,long ttl);
    /**
     * 获取大小
     * @return int 提交状态
     * */
    int size();
}
