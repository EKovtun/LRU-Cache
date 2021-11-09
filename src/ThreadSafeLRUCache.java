import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class ThreadSafeLRUCache<K, V> implements Cache<K, V> {
    private final Map<K, V> cacheMap;

    public ThreadSafeLRUCache(int cacheSize) {
        Map<K, V> map = new LinkedHashMap<>(cacheSize) {
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > cacheSize;
            }
        };
        cacheMap = Collections.synchronizedMap(map);
    }

    @Override
    public void put(K key, V value) {
        if (key == null || value == null) return;
        synchronized (cacheMap) {
            cacheMap.remove(key);
            cacheMap.put(key, value);
        }
    }

    @Override
    public V get(K key) {
        return cacheMap.get(key);
    }

    @Override
    public Map<K, V> getCacheMap() {
        return cacheMap;
    }
}
