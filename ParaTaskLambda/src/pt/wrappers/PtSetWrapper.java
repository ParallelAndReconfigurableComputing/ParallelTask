package pt.wrappers;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;

import pt.runtime.TaskID;

public class PtSetWrapper<E> implements Set<E> {
	
	ConcurrentLinkedDeque<PtCollectionObject<E>> thisCollection = null;
	Set<E> thisOriginalCollection = null;
	
	public PtSetWrapper() {
		thisCollection = new ConcurrentLinkedDeque<>();
	}
	
	public PtSetWrapper(Set<E> originalSet){
		thisOriginalCollection = originalSet;
		thisCollection = new ConcurrentLinkedDeque<>();
		for(E obj : thisOriginalCollection){
			thisCollection.add(new PtCollectionObject<E>(obj));
		}
	}

	@Override
	public int size() {
		return thisCollection.size();
	}

	@Override
	public boolean isEmpty() {
		return thisCollection.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		for(PtCollectionObject<E> obj : thisCollection){
			if(obj.contains(o))
				return true;
		}
		return false;
	}

	@Override
	public Iterator<E> iterator() {
		return new PtIterator<>(thisCollection);
	}

	@Override
	public Object[] toArray() {
		Object[] array = new Object[thisCollection.size()];
		int index = 0;
		for (PtCollectionObject<E> obj : thisCollection){
			array[index++] = obj.getObject();
		}
		return array;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] a) {
		if(a.length != thisCollection.size())
			a = (T[]) Array.newInstance(a.getClass().getComponentType(), thisCollection.size());

		int index = 0;
		for(PtCollectionObject<E> obj : thisCollection){
			a[index++]	= (T) obj.getObject();
		}
		
		return a;
	}

	@Override
	public boolean add(E e) {
		if(e==null || contains(e))
			return false;
		return thisCollection.add(new PtCollectionObject<E>(e));
	}
	
	public boolean add(TaskID<E> taskID){
		if(taskID==null || contains(taskID))
			return false;
		return thisCollection.add(new PtCollectionObject<E>(taskID));
	}

	@Override
	public boolean remove(Object o) {
		for(PtCollectionObject<E> obj : thisCollection){
			if(obj.contains(o))
				return thisCollection.remove(obj);
		}
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		Iterator<?> it = c.iterator();
		while(it.hasNext()){
			if(!contains(it.next()))
				return false;
		}
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean collectionChanged = false;
		for(E obj : c){
			if(add(obj))
				collectionChanged = true;
		}
		return collectionChanged;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		boolean collectionChanged = false;
		ConcurrentLinkedDeque<PtCollectionObject<E>> newCollection = new ConcurrentLinkedDeque<>();
		Iterator<?> it = c.iterator();
		
		while(it.hasNext()){
			Object element = it.next();
			PtCollectionObject<E> wrapper = getWrapperObject(element);
			if(wrapper != null){
				if(newCollection.add(wrapper))
					collectionChanged = true;
			}
		}
		
		thisCollection.clear();
		thisCollection = newCollection;
		return collectionChanged;
	}

	private PtCollectionObject<E> getWrapperObject(Object o){
		for(PtCollectionObject<E> obj : thisCollection){
			if(obj.contains(o))
				return obj;
		}
		return null;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean collectionChanged = false;
		Iterator<?> it = c.iterator();
		while(it.hasNext()){
			Object element = it.next();
			if(remove(element))
				collectionChanged = true;
		}
		return collectionChanged;
	}

	@Override
	public void clear() {
		thisCollection.clear();
	}
	
	@Override
	public String toString(){
		int index = 1;
		String returnStr = "[";
		for(PtCollectionObject<E> obj : thisCollection){
			returnStr += obj.toString();
			if (index++ < thisCollection.size())
				returnStr += ", ";
		}
		returnStr += "]";
		return returnStr;
	}

}
