

package reductionTest;


public class MapSwap<K, V> implements pu.RedLib.Reduction<java.util.Map<K, V>> {
    @java.lang.Override
    public java.util.Map<K, V> reduce(java.util.Map<K, V> first, java.util.Map<K, V> second) {
        for (java.util.Map.Entry<K, V> entry : second.entrySet()) {
            K key = entry.getKey();
            if (first.containsKey(key))
                first.put(key, entry.getValue());
            
        }
        return first;
    }
}

