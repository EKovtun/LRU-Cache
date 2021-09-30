import java.util.*;

public class ThreadSafeLRUCache<K, V> {
    private final Map<K, V> cacheMap;

    public ThreadSafeLRUCache(int cacheSize) {
        Map<K, V> map = new LinkedHashMap<>(cacheSize) {
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > cacheSize;
            }
        };
        cacheMap = Collections.synchronizedMap(map);
    }

    public void put(K key, V value) {
        if (key == null || value == null) return;
        cacheMap.remove(key);
        cacheMap.put(key, value);
    }

    public V get(K key) {
        return cacheMap.get(key);
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadSafeLRUCache<String, Integer> cache = new ThreadSafeLRUCache<>(3);
        List<Thread> threads = new ArrayList<>();


        threads.add(new Thread(() -> cache.put("1", 1)));
        threads.add(new Thread(() -> cache.put("2", 2)));
        threads.add(new Thread(() -> cache.put("3", 3)));

        threads.forEach(Thread::start);
        for (Thread thread : threads) {
            thread.join();
        }
        threads.clear();

        assert cache.get("1") == 1;
        assert cache.get("2") == 2;
        assert cache.get("3") == 3;


        threads.add(new Thread(() -> cache.put("1", 1)));
        threads.add(new Thread(() -> cache.put("2", 2)));
        threads.add(new Thread(() -> cache.put("4", 4)));

        threads.forEach(Thread::start);
        for (Thread thread : threads) {
            thread.join();
        }
        threads.clear();

        assert cache.get("1") == 1;
        assert cache.get("2") == 2;
        assert cache.get("3") == null;
        assert cache.get("4") == 4;
    }
}
