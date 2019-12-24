package utils.io;


import org.junit.Before;
import org.junit.Test;
import utils.Util;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangjianxin
 * @version 2019-12-24.
 * @url github.com/uk0
 * @project vladimir
 * @since JDK1.8.
 */
public class TestMmapRWFIle {

    private Mmap m;

    @Before
    public void InitIO() throws IOException {
        m = new Mmap();
        m.init("/tmp/temp_jvm");
    }

    private ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 200,
            TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>(true));

    private Runnable runnableW = new Runnable() {

        /**
         * @see Thread#run()
         */
        @Override
        public void run() {
            for (; ; ) {
                try {

                    m.write("111111111111111111111".getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private Runnable runnableR = new Runnable() {

        /**
         * @see Thread#run()
         */
        @Override
        public void run() {
            for (; ; ) {
                try {
                    Thread.sleep(500);
                    System.out.println(new String(m.read(10L)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };


    @Test
    public void Test() throws InterruptedException, IOException {
        System.out.println(Util.getFreeMemory());
        executor.execute(runnableW);
        executor.execute(runnableW);
        executor.execute(runnableW);
        executor.execute(runnableW);
        executor.execute(runnableR);
        Thread.sleep(8000000);
        m.destroy();
    }
}
