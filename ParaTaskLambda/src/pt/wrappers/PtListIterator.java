package pt.wrappers;

import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;

public class PtListIterator<E> implements ListIterator<E> {
	Iterator<PtCollectionObject<E>> thisIterator = null;
	Collection<PtCollectionObject<E>> thisCollection = null;
	PtCollectionObject<E> currentElement = null;
	
	
	public PtListIterator(Collection<PtCollectionObject<E>> collection) {
		thisCollection = collection;
		thisIterator = thisCollection.iterator();
	}
	
	public PtListIterator(int index, Collection<PtCollectionObject<E>> collection){
		this(collection);
		for(int i = 0; i < index; i++){
			if(thisIterator.hasNext())
				thisIterator.next();
		}
	}
	
	@Override
	public boolean hasNext() {
		return thisIterator.hasNext();
	}

	@Override
	public E next() {
		currentElement = thisIterator.next();
		return currentElement.getObject();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean hasPrevious() {
		PtCollectionObject<E>[] elements = (PtCollectionObject<E>[]) thisCollection.toArray();
		if (currentElement.equals(elements[0]))
			return false;
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public E previous() {
		if(!hasPrevious())
			return null;
		PtCollectionObject<E>[] elements = (PtCollectionObject<E>[]) thisCollection.toArray();
		PtCollectionObject<E> temp = null;
		return null;
	}

	@Override
	public int nextIndex() {
		//return thisIterator.nextIndex();
		return 0;
	}

	@Override
	public int previousIndex() {
		//return thisIterator.previousIndex();
		return 0;
	}

	@Override
	public void remove() {
		thisIterator.remove();
	}

	@Override
	public void set(E e) {
		//thisIterator.set(new PtCollectionObject<E>(e));
	}

	@Override
	public void add(E e) {
		//thisIterator.add(new PtCollectionObject<E>(e));
	}

}
