package utils.algorithm.queue;


/**
 * @author zhangjianxin
 * @version 2019-12-31.
 * @url github.com/uk0
 * @project vladimir
 * @since JDK1.8.
 */
public interface Action<T> {

    void enqueue(T o);

    T dequeue();

    boolean IsEmpty();
}
