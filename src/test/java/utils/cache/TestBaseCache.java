package utils.cache;

import org.junit.Test;

/**
 * @author zhangjianxin
 * @version 2019-12-24.
 * @url github.com/uk0
 * @project vladimir
 * @since JDK1.8.
 */
public class TestBaseCache {

//    @Test
    public void TestPutCacheUseTtl() throws InterruptedException {
        BaseCache baseCache = new BaseCache(10000);
        baseCache.put(1, 1);
        baseCache.put(2, 1);
        baseCache.put(3, 1);
        baseCache.put(4, 1);
        baseCache.put(5, 1);
        baseCache.put(100, 5,3);



        System.out.println("[没有过期前] GetCache = " + baseCache.get(100));
        System.out.println("[没有过期前] Get Cache Size = "+baseCache.size());

        Thread.sleep(4000);
        System.out.println("[过期后] Get Cache Size = "+baseCache.size());
        System.out.println("[过期后] GetCache = " + baseCache.get(100));
        ;


    }
}
