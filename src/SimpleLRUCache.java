import java.util.LinkedHashMap;
import java.util.Map;

public class SimpleLRUCache<K, V> {
    private final Map<K, V> cacheMap;

    public SimpleLRUCache(int cacheSize) {
        cacheMap = new LinkedHashMap<>(cacheSize) {
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > cacheSize;
            }
        };
    }

    public void put(K key, V value) {
        if (key == null || value == null) return;
        cacheMap.remove(key);
        cacheMap.put(key, value);
    }

    public V get(K key) {
        return cacheMap.get(key);
    }

    public static void main(String[] args) {
        SimpleLRUCache<String, Integer> cache = new SimpleLRUCache<>(3);

        cache.put("1", 1);
        cache.put("2", 2);
        cache.put("3", 3);

        assert cache.get("1") == 1;
        assert cache.get("2") == 2;
        assert cache.get("3") == 3;

        cache.put("1", 1);

        assert cache.get("1") == 1;
        assert cache.get("2") == 2;
        assert cache.get("3") == 3;

        cache.put("4", 4);

        assert cache.get("1") == 1;
        assert cache.get("2") == null;
        assert cache.get("3") == 3;
        assert cache.get("4") == 4;
    }
}
