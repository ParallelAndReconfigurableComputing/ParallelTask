package collections;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;

import pt.runtime.TaskID;

public class PtCollectionWrapper<T> implements Collection<T>, Iterable<T> {
	
	ConcurrentLinkedDeque<PtCollectionObject<T>> thisCollection;
	
	public PtCollectionWrapper() {
		thisCollection = new ConcurrentLinkedDeque<>();
	}
	
	@Override
	public Iterator<T> iterator() {
		return new PtIterator<>(thisCollection);
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
		
		for(PtCollectionObject<T> obj : thisCollection){
			if(obj.contains(o))
				return true; 
		}
		
		return false;
	}

	@Override
	public Object[] toArray() {
		Object[] array = new Object[thisCollection.size()];
	
		int index = 0;
		for(PtCollectionObject<T> obj : thisCollection){
			array[index++] = obj.getObject();
		}
		return array;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <E> E[] toArray(E[] a) {
		
		if(a.length != thisCollection.size()){
			a = (E[]) Array.newInstance(a.getClass().getComponentType(), thisCollection.size());
		}
		
		int index = 0;
		for(PtCollectionObject<T> obj : thisCollection){
			a[index++] = (E)obj.getObject();
		}
		
		return a;
	}

	@Override
	public boolean add(T e) {
		return thisCollection.add(new PtCollectionObject<T>(e));
	}
	
	public boolean add(TaskID<T> taskID){
		return thisCollection.add(new PtCollectionObject<T>(taskID));
	}

	@Override
	public boolean remove(Object o) {
		for(PtCollectionObject<T> obj : thisCollection){
			if(obj.contains(o)){
				return thisCollection.remove(obj);
			}
		}
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		Iterator<?> iterator = c.iterator();
		while(iterator.hasNext()){
			if(!(this.contains(iterator.next())))
					return false;
		}
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		boolean collectionChanged = false;
		for(T obj : c){
			if(this.add(obj))
				collectionChanged = true;
		}
		return collectionChanged;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean collectionChanged = false;
		Iterator<?> iterator = c.iterator();
		while(iterator.hasNext()){
			collectionChanged = thisCollection.remove(iterator.next());
		}
		return collectionChanged;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		boolean collectionChanged = false;
		for(PtCollectionObject<T> obj : thisCollection){
			if(!c.contains(obj.getObject())){
				collectionChanged = thisCollection.remove(obj);
			}
		}
		return collectionChanged;
	}

	@Override
	public void clear() {
		thisCollection.clear();		
	}
	
	//public Spliterator<E> spliterator()
	
	//public int indexOf(Object o)
	
	//public int lastIndexOf(Object o)
	
	//public Object clone()
	
	//public E get(int index)
	
	//public E set(int index, E element)
	
	//public void add(int index, E element)
	
	//protected void removeRange(int fromIndex, int toIndex)
	
	//public void sort(Comparator<? super E> c)
	
	//public void replaceAll(UnaryOperator<E> operator)
}
