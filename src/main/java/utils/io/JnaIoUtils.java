package utils.io;

import utils.io.directio.JNAOutputStream;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * @author zhangjianxin
 * @version 2019-12-24.
 * @url github.com/uk0
 * @project vladimir
 * @since JDK1.8.
 */
public class JnaIoUtils {

    public static void writeLineToFile(DataOutputStream out, String dataLine) throws IOException {
        out.writeUTF(dataLine);
        out.flush();
        out.close();
    }

    public static long write(DataOutputStream out, int[] data) throws IOException {
        long t = System.nanoTime();
        for (int i = 0; i < data.length; i++) {
            out.writeInt(data[i]);
        }
        out.flush();
        return System.nanoTime() - t;
    }

    public static long write(DataOutputStream out, byte[] data) throws IOException {
        long t = System.nanoTime();
        for (int i = 0; i < data.length; i++) {
            out.write(data[i]);
        }
        out.flush();
        return System.nanoTime() - t;
    }


    public static long read(DataInputStream in, int[] data) throws IOException {
        long t = System.nanoTime();
        for (int i = 0; i < data.length; i++) {
            if (data[i] != in.readInt()) {
                throw new IOException("wrong!");
            }
        }
        return System.nanoTime() - t;
    }

}
