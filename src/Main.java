import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final int cycles = 1000;
    private static final List<Integer> values = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
    private static final int cacheSize = 5; // cacheSize < values.size()

    public static void main(String[] args) {
        assert (simpleLruCacheWithOneThreadsTest());
        assert (concurrentLruCacheWithOneThreadsTest());
        assert (simpleLruCacheWithManyThreadsTest() == false); // Bad with concurrent - okay
        assert (concurrentLruCacheWithManyThreadsTest());
    }

    private static boolean simpleLruCacheWithOneThreadsTest() {
        return verifyResults(oneThreadTest(new SimpleLRUCache<>(cacheSize)));
    }

    private static boolean concurrentLruCacheWithOneThreadsTest() {
        return verifyResults(oneThreadTest(new ThreadSafeLRUCache<>(cacheSize)));
    }

    private static boolean simpleLruCacheWithManyThreadsTest() {
        return verifyResults(concurrentTest(new SimpleLRUCache<>(cacheSize)));
    }

    private static boolean concurrentLruCacheWithManyThreadsTest() {
        return verifyResults(concurrentTest(new ThreadSafeLRUCache<>(cacheSize)));
    }

    private static boolean verifyResults(Map<Integer, Integer> cacheMap) {
        if (cacheSize != cacheMap.size()) return false;
        for (var e : cacheMap.entrySet()) {
            if (e == null) return false;
            if (e.getKey() == null) return false;
            if (e.getValue() == null) return false;
            if (!e.getKey().equals(e.getValue())) return false;
        }
        return true;
    }

    private static Map<Integer, Integer> oneThreadTest(Cache<Integer, Integer> cache) {
        for (int i = 0; i < cycles; i++) {
            Integer key = values.get(ThreadLocalRandom.current().nextInt(values.size()));
            Integer value = cache.get(key);
            if (value == null) {
                cache.put(key, key);
            }
        }
        return cache.getCacheMap();
    }

    private static Map<Integer, Integer> concurrentTest(Cache<Integer, Integer> cache) {
        ExecutorService executorService = Executors.newFixedThreadPool(cacheSize);
        for (int i = 0; i < cycles; i++) {
            executorService.submit(() -> {
                Integer key = values.get(ThreadLocalRandom.current().nextInt(values.size()));
                Integer value = cache.get(key);
                if (value == null) {
                    cache.put(key, key);
                }
            });
        }
        try {
            executorService.shutdown();
            executorService.awaitTermination(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return cache.getCacheMap();
    }
}
