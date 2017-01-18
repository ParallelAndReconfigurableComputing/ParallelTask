package reductionTest;

import java.util.Map.Entry;
import java.util.Map;

import pu.RedLib.Reduction;

public class MapSwap<K, V> implements Reduction<Map<K, V>> {

	@Override
	public Map<K, V> reduce(Map<K, V> first, Map<K, V> second) {
		for(Entry<K, V> entry : second.entrySet()){
			K key = entry.getKey();
			if(first.containsKey(key))
				first.put(key, entry.getValue());
		}
		return first;
	}

}
