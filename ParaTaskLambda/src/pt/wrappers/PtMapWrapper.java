package pt.wrappers;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PtMapWrapper<K, V> implements Map<K, V> {
	
	ConcurrentHashMap<K, PtCollectionObject<V>> thisMap = null;
	Map<K, V> thisOriginalMap = null;
	
	public PtMapWrapper(Map<K, V> originalMap){
		thisMap = new ConcurrentHashMap<>();
		thisOriginalMap = originalMap;
		if(!thisOriginalMap.isEmpty()){
			Set<K> keys = thisOriginalMap.keySet();
			for(K key : keys){
				thisMap.put(key, new PtCollectionObject<>(thisOriginalMap.get(key)));
			}
		}
	}

	@Override
	public int size() {
		return thisMap.size();
	}

	@Override
	public boolean isEmpty() {
		return thisMap.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return thisMap.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		Set<K> keys = thisMap.keySet();
		
		for(K key : keys){
			PtCollectionObject<V> obj = thisMap.get(key);
			if(obj.contains(value))
				return true;
		}
		
		return false;
	}
	
	public boolean contains(Object value){
		return containsValue(value);
	}

	@Override
	public V get(Object key) {
		return thisMap.get(key).getObject();
	}

	@Override
	public V put(K key, V value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public V remove(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<K> keySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<V> values() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*
	 * public Object clone()
	 */

}
