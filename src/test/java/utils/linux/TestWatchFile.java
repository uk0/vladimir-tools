package utils.linux;

import org.junit.Test;
import utils.linux.inotify.jdk.IInotify;
import utils.linux.inotify.jdk.PathObservables;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author zhangjianxin
 * @version 2019-12-26.
 * @url github.com/uk0
 * @project vladimir
 * @since JDK1.8.
 */
public class TestWatchFile {
    //should be public
    @Test
    public void TestWatch1() throws IOException {
        Path dir = Paths.get("/Users/zhangjianxin/home/development/code/github/vladimir");
        new PathObservables(dir).processEvents(new IInotify() {
            @Override
            public void create(Object o) {
                System.out.println(o);
            }
        });
    }
}