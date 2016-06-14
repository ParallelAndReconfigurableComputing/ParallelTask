package pt.wrappers;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ConcurrentLinkedDeque;

import pt.wrappers.PtCollectionObject;
import pt.wrappers.PtIterator;
import pt.runtime.TaskID;

public class PtListWrapper<E> implements List<E> {
	//this class uses concurrent linked dequeue for a thread safe collection as it is more
	//efficient that CopyOnWriteArrayList for mutations and write operations. 
	ConcurrentLinkedDeque<PtCollectionObject<E>> thisCollection = null;
	List<E> thisOrignialCollection = null;
	
	/*
	 * Consider that for every method that receives an 'index' parameter of type "int" the
	 * program might send a TaskID<Integer>, so they need to be overridden accordingly!
	 * */
	
	public PtListWrapper() {
		thisCollection = new ConcurrentLinkedDeque<>();
	}
	
	public PtListWrapper(List<E> originalCollection){
		thisOrignialCollection = originalCollection;
		thisCollection = new ConcurrentLinkedDeque<>();
		for (E obj : originalCollection){
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
		for(PtCollectionObject<E> obj : thisCollection){
			array[index++] = obj.getObject();
		}
		return array;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] a) {
		if(a.length != thisCollection.size()){
			a = (T[]) Array.newInstance(a.getClass().getComponentType(), thisCollection.size());
		}
		
		int index = 0;
		for(PtCollectionObject<E> obj : thisCollection){
			a[index++] = (T) obj.getObject();
		}
		
		return a;
	}

	@Override
	public boolean add(E e) {
		return thisCollection.add(new PtCollectionObject<E>(e));
	}
	
	public boolean add(TaskID<E> taskID){
		return thisCollection.add(new PtCollectionObject<E>(taskID));
	}

	@Override
	public boolean remove(Object o) {
		for(PtCollectionObject<E> obj : thisCollection){
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
	public boolean addAll(Collection<? extends E> c) {
		boolean collectionChanged = false;
		for(E obj : c){
			if(this.add(obj))
				collectionChanged = true;
		}
		return collectionChanged;
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		ConcurrentLinkedDeque<PtCollectionObject<E>> newCollection = new ConcurrentLinkedDeque<>();
		boolean collectionChanged = false;
		
		if(index == thisCollection.size()){
			Iterator<? extends E> it = c.iterator();
			while(it.hasNext()){
				thisCollection.addLast(new PtCollectionObject<>(it.next()));
			}
			return true;
		}
		
		int i = 0;
		for(PtCollectionObject<E> obj : thisCollection){
			if(i == index){
				Iterator<? extends E> it = c.iterator();
				while(it.hasNext()){
					if(newCollection.add(new PtCollectionObject<E>(it.next())))
						collectionChanged = true;
				}
				break;
			}
			else{
				if(newCollection.add(obj)){
					thisCollection.pop();
					collectionChanged = true;
				}
			}
			i++;				
		}
		
		for(PtCollectionObject<E> obj : thisCollection){
			if(newCollection.add(obj)){
				thisCollection.pop();
				collectionChanged = true;
			}
		}

		thisCollection.clear();
		thisCollection = newCollection;
		return collectionChanged;
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
	public boolean retainAll(Collection<?> c) {
		boolean collectionChanged = false;
		ConcurrentLinkedDeque<PtCollectionObject<E>> newCollection = new ConcurrentLinkedDeque<>();
		Iterator<?> it = c.iterator();
		
		while(it.hasNext()){
			Object element = it.next();
			PtCollectionObject<E> wrapper = getWrapper(element);
			if(wrapper != null){
				if(newCollection.add(wrapper))
					collectionChanged = true;
			}
		}
		
		thisCollection.clear();
		thisCollection = newCollection;
		return collectionChanged;
	}
	
	private PtCollectionObject<E> getWrapper(Object o){
		for(PtCollectionObject<E> obj : thisCollection){
			if(contains(o))
				return obj;
		}
		return null;
	}

	@Override
	public void clear() {
		thisCollection.clear();
	}

	@Override
	public E get(int index) {
		if (index < 0 || index >= size())
			throw new IndexOutOfBoundsException();
		
		int i = 0;
		for(PtCollectionObject<E> obj : thisCollection){
			if(i == index)
				return obj.getObject();
			i++;
		}
		
		return null;
	}
	
	public E get(TaskID<Integer> id){
		return get(id.getReturnResult());
	}

	@Override
	public E set(int index, E element) {
		if (index < 0 || index >= thisCollection.size())
			throw new IndexOutOfBoundsException();
		int i = 0;
		for(PtCollectionObject<E> obj : thisCollection){
			if (i == index){
				E temp = obj.getObject();
				obj.setObject(element); 
				return temp;
			}
			i++;
		}
		return null;			
	}
	
	public E set(int index, TaskID<E> element){
		if(index < 0 || index >= thisCollection.size())
			throw new IndexOutOfBoundsException();
		
		int i = 0;
		for (PtCollectionObject<E> obj : thisCollection){
			if (i == index){
				E temp = obj.getObject();
				obj.setObject(element);
				return temp;
			}
			i++;
		}
		return null;
	}

	@Override
	public void add(int index, E element) {
		if(index < 0 || index > thisCollection.size())
			throw new IndexOutOfBoundsException();
		
		if(index == 0){
			thisCollection.addFirst(new PtCollectionObject<>(element));
			return;
		}
		
		if (index == thisCollection.size()){
			thisCollection.addLast(new PtCollectionObject<>(element));
			return;
		}
			
		ConcurrentLinkedDeque<PtCollectionObject<E>> newCollection = new ConcurrentLinkedDeque<>();
		
		for(int i = 0; i < index; i++)
			newCollection.add(thisCollection.poll());
				
		newCollection.add(new PtCollectionObject<>(element));
		
		for (PtCollectionObject<E> obj : thisCollection){
			newCollection.add(obj);
			thisCollection.poll();
		}
		
		thisCollection.clear();
		thisCollection = newCollection;
	}
	
	public void add(int index, TaskID<E> element) {
		if(index < 0 || index > thisCollection.size())
			throw new IndexOutOfBoundsException();
		
		if(index == 0){
			thisCollection.addFirst(new PtCollectionObject<>(element));
			return;
		}
		
		if (index == thisCollection.size()){
			thisCollection.addLast(new PtCollectionObject<>(element));
			return;
		}
		
		ConcurrentLinkedDeque<PtCollectionObject<E>> newCollection = new ConcurrentLinkedDeque<>();
		
		for(int i = 0; i < index; i++)
			newCollection.add(thisCollection.poll());
		
		newCollection.add(new PtCollectionObject<>(element));
		
		for(PtCollectionObject<E> obj : thisCollection){
			newCollection.add(obj);
			thisCollection.poll();
		}
		
		thisCollection.clear();
		thisCollection = newCollection;
	}

	@Override
	public E remove(int index) {
		if (index < 0 || index >= thisCollection.size())
			throw new IndexOutOfBoundsException();
		
		int i = 0;
		for(PtCollectionObject<E> obj : thisCollection){
			if(i == index){
				E temp = obj.getObject();
				thisCollection.remove(obj);
				return temp;
			}
			i++;
		}
		return null;
	}

	@Override
	public int indexOf(Object o) {
		int i = 0;
		for (PtCollectionObject<E> obj : thisCollection){
			if(obj.contains(o))
				return i;
			i++;
		}
		return -1;
	}

	@Override
	public int lastIndexOf(Object o) {
		int lastIndex = -1;
		int index = 0;
		for (PtCollectionObject<E> obj : thisCollection){
			if(obj.contains(o))
				lastIndex = index;
			index++;
		}
		return lastIndex;
	}

	@Override
	public ListIterator<E> listIterator() {
		return new PtListIterator<>(thisCollection);
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return new PtListIterator<>(index, thisCollection);
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		if(fromIndex > toIndex)
			throw new IllegalArgumentException(" fromIndex(" + fromIndex + ") > toIndex(" + toIndex + ")");
		if(fromIndex < 0)
			throw new IndexOutOfBoundsException(" fromIndex = " + fromIndex);
		if(toIndex > thisCollection.size())
			throw new IndexOutOfBoundsException(" toIndex = " + toIndex);
		
		List<E> tempList = new ArrayList<>();
		int index = 0;
		for (PtCollectionObject<E> obj : thisCollection){
			if(index >= fromIndex && index < toIndex)
				tempList.add(obj.getObject());
			index++;
		}		
		return tempList;
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
