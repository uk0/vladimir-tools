package utils.io;

import net.smacke.jaydio.DirectRandomAccessFile;
import sun.nio.ch.DirectBuffer;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import static utils.unsafe.UnsafeUtil.UNSAFE;

/**
 * @author zhangjianxin
 * @version 2019-12-24.
 * @url github.com/uk0
 * @project vladimir
 * @since JDK1.8.
 */
public class Mmap {
    // 一次4kb

    public static final int VALUE_LENGTH = 4 * 1024;
    public static ThreadLocal<ByteBuffer> bufferThreadLocal = ThreadLocal.withInitial(() -> ByteBuffer.allocate(VALUE_LENGTH));
    public static ThreadLocal<byte[]> byteArrayThreadLocal = ThreadLocal.withInitial(() -> new byte[VALUE_LENGTH]);


    private File file;
    private FileChannel fileChannel;
    private DirectRandomAccessFile directRandomAccessFile;

    private ByteBuffer writeBuffer;
    private boolean dioSupport;
    private long addresses;

    //mmap内存映射将会经历：3次拷贝: 1次cpu copy，2次DMA copy；
    //以及4次上下文切换
    // 初始化Mmap 文件映射

    public void init(String filePath) throws IOException {
        this.file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        this.fileChannel = new RandomAccessFile(file, "rw").getChannel();
        try {
            this.directRandomAccessFile = new DirectRandomAccessFile(file, "r");
            this.dioSupport = true;
        } catch (Exception e) {
            this.dioSupport = false;
        }
        this.writeBuffer = ByteBuffer.allocateDirect(VALUE_LENGTH);
        this.addresses = ((DirectBuffer) this.writeBuffer).address();
    }


    public void destroy() throws IOException {
        this.writeBuffer = null;
        if (this.fileChannel != null) {
            this.fileChannel.close();
        }
        if (this.directRandomAccessFile != null) {
            this.directRandomAccessFile.close();
        }
    }

    public synchronized byte[] read(long offset) throws IOException {
        if (this.dioSupport) {
            byte[] bytes = byteArrayThreadLocal.get();
            directRandomAccessFile.seek(offset);
            directRandomAccessFile.read(bytes);
            return bytes;
        } else {
            ByteBuffer buffer = bufferThreadLocal.get();
            buffer.clear();
            this.fileChannel.read(buffer, offset);
            return buffer.array();
        }
    }

    public synchronized void write4KB(byte[] data) throws Exception {
        // 复制
        UNSAFE.copyMemory(data, 16, null, addresses, VALUE_LENGTH);
        this.writeBuffer.position(0);
        try {
            this.fileChannel.write(this.writeBuffer);
        } catch (IOException e) {
            throw new Exception("IO_ERROR write data io error");
        }
    }

    public synchronized void write(byte[] data) throws Exception {
        // 复制
        UNSAFE.copyMemory(data, 16, null, addresses, data.length);
        this.writeBuffer.position(0);
        try {
            this.fileChannel.write(this.writeBuffer);
        } catch (IOException e) {
            throw new Exception("IO_ERROR write data io error");
        }
    }

    public MappedByteBuffer appendToFile(File file, String filemode, String line) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, filemode);
        byte[] bytes = line.getBytes();

        FileChannel fileChannel = randomAccessFile.getChannel();
        long size = fileChannel.size() + bytes.length;
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, size);
        mappedByteBuffer.put(bytes);
        fileChannel.close();
        randomAccessFile.close();
        int position = mappedByteBuffer.limit() - bytes.length;
        mappedByteBuffer.position(position);
        mappedByteBuffer.put(bytes);
        // sync flush disk
        mappedByteBuffer.force();
        mappedByteBuffer.flip();

        return mappedByteBuffer;
    }

}
