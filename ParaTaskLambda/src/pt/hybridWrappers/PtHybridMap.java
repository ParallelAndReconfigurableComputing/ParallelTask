package pt.hybridWrappers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import pt.runtime.TaskID;

public class PtHybridMap<K, V> implements Map<K, V> {
	
	ConcurrentHashMap<K, PtCollectionObject<V>> thisMap = null;
	Map<K, V> thisOriginalMap = null;
	
	public PtHybridMap(Map<K, V> originalMap){
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
	
	public V get(TaskID<?> id){
		return get(id.getReturnResult());
	}

	@Override
	public V put(K key, V value) {
		V preValue = null;
		if(thisMap.containsKey(key))
			preValue = thisMap.get(key).getObject();
		thisMap.put(key, new PtCollectionObject<>(value));
		return preValue;
	}

	public V put(K key, TaskID<V> value){
		V preValue = null;
		if(thisMap.containsKey(key))
			preValue = thisMap.get(key).getObject();
		thisMap.put(key, new PtCollectionObject<>(value));
		return preValue;
	}
	
	@Override
	public V remove(Object key) {
		PtCollectionObject<V> preValue = null;
		if(key == null)
			throw new NullPointerException();
		
		preValue = thisMap.remove(key);
		if(preValue != null)
			return preValue.getObject();
		return null;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		Set<? extends K>  keys = m.keySet();
		for(K key : keys){
			thisMap.put(key, new PtCollectionObject<>(m.get(key)));
		}
	}

	@Override
	public void clear() {
		thisMap.clear();
	}

	@Override
	public Set<K> keySet() {
		return thisMap.keySet();
	}

	@Override
	public Collection<V> values() {
		List<V> values = new ArrayList<>();
		Set<K> keys = thisMap.keySet();
		for (K key : keys)
			values.add(thisMap.get(key).getObject());
		return values;
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
