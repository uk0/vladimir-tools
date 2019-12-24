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

    @Test
    public void TestPutCacheUseTtl() {
        BaseCache baseCache = new BaseCache(1000000);
        int index = 1;
        baseCache.put(1, 1);
        baseCache.put(2, 1);
        baseCache.put(3, 1);
        baseCache.put(4, 1);
        baseCache.put(5, 1);


        System.out.println(baseCache.get(1));
        ;


    }
}
