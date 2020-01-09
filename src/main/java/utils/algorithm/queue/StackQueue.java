package utils.algorithm.queue;

import com.sun.org.apache.xpath.internal.operations.String;

import java.util.Stack;

/**
 * @author zhangjianxin
 * @version 2019-12-31.
 * @url github.com/uk0
 * @project vladimir
 * @since JDK1.8.
 */
public class StackQueue<T> implements Action<T>{

   private Stack<T> in = new Stack<T>();
   private Stack<T> out = new Stack<T>();

    //入队

    @Override
    public void enqueue(T o) {
        in.push(o);
    }
    //出队

    @Override
    public T dequeue() {
        T temp = null;
        if (!out.isEmpty()) {
            temp = out.pop();
        } else {

            while (!in.isEmpty()) {
                temp = in.pop();
                out.push(temp);
            }

            if (!out.isEmpty()) {
                temp = out.pop();
            }
        }
        return temp;

    }

    @Override
    public boolean IsEmpty() {
        //如果两个栈都为空，则返回1，否则返回0
        return (in.isEmpty() && out.isEmpty());
    }
}
