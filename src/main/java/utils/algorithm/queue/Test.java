package utils.algorithm.queue;


/**
 * @author zhangjianxin
 * @version 2019-12-31.
 * @url github.com/uk0
 * @project vladimir
 * @since JDK1.8.
 */
public class Test {

    public static void main(String[] args) {
        StackQueue stackQueue  = new StackQueue();
        stackQueue.enqueue("111111");
        stackQueue.enqueue("222222");
        stackQueue.enqueue("222222");

        System.out.println(stackQueue.dequeue());
        System.out.println(stackQueue.dequeue());
    }
}
