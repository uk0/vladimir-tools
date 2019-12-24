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
     *
     * */
    int get(int key);
    boolean put(int key, int value);
}
