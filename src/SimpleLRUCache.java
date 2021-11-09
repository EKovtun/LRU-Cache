import java.util.LinkedHashMap;
import java.util.Map;

public class SimpleLRUCache<K, V> implements Cache<K, V> {
    private final Map<K, V> cacheMap;

    public SimpleLRUCache(int cacheSize) {
        cacheMap = new LinkedHashMap<>(cacheSize) {
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > cacheSize;
            }
        };
    }

    @Override
    public void put(K key, V value) {
        if (key == null || value == null) return;
        cacheMap.remove(key);
        cacheMap.put(key, value);
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
