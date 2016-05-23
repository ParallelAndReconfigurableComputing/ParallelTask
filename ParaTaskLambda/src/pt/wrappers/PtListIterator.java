package pt.wrappers;

import java.util.List;
import java.util.ListIterator;

public class PtListIterator<E> implements ListIterator<E> {
	ListIterator<PtCollectionObject<E>> thisIterator = null;
	List<PtCollectionObject<E>> thisCollection = null;
	
	
	public PtListIterator(List<PtCollectionObject<E>> collection) {
		thisCollection = collection;
		thisIterator = thisCollection.listIterator();
	}
	
	public PtListIterator(int index, List<PtCollectionObject<E>> collection){
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
		return thisIterator.next().getObject();
	}

	@Override
	public boolean hasPrevious() {
		return thisIterator.hasPrevious();
	}

	@Override
	public E previous() {
		return thisIterator.previous().getObject();
	}

	@Override
	public int nextIndex() {
		return thisIterator.nextIndex();
	}

	@Override
	public int previousIndex() {
		return thisIterator.previousIndex();
	}

	@Override
	public void remove() {
		thisIterator.remove();
	}

	@Override
	public void set(E e) {
		thisIterator.set(new PtCollectionObject<E>(e));
	}

	@Override
	public void add(E e) {
		thisIterator.add(new PtCollectionObject<E>(e));
	}

}
