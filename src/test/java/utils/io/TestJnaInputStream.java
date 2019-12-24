package utils.io;


import org.junit.Test;
import utils.Util;
import utils.io.directio.JNAOutputStream;

import java.io.DataOutputStream;
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

public class TestJnaInputStream {

    private ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 15, 200,
            TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>());


    private Runnable runnable  = new Runnable() {
        String filewpath = "/tmp/directIO.json";

        DataOutputStream out;

        {
            try {
                out = new DataOutputStream(new JNAOutputStream(filewpath, true));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        @Override
        public void run() {
            for (long i = 0; i < 10240000000000L; i++) {
                try {

                    JnaIoUtils.write(out,"TestLine TestLine TestLine TestLine TestLine TestLine TestLine\n".getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };



    @Test
    public void TestWriteFile() throws IOException, InterruptedException {


        System.out.println(Util.pid());
        executor.execute(runnable);
        executor.execute(runnable);
        Thread.sleep(10000000L);

    }
}
