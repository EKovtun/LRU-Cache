import java.util.Map;

public interface Cache<K, V> {
    void put(K key, V value);

    V get(K key);

    Map<K, V> getCacheMap();
}