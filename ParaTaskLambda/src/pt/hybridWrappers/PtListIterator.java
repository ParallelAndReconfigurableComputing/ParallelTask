package pt.hybridWrappers;

import java.util.Collection;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class PtListIterator<E> implements ListIterator<E> {
	ListIterator<PtCollectionObject<E>> thisListIterator = null;
	//The ListIterator wrapper copies elements into a CopyOnWriteArrayList since it 
	//is more efficient when it comes to traversing the elements, plus it provides 
	//its own ListIterator. 
	CopyOnWriteArrayList<PtCollectionObject<E>> thisCollection = null;
	
	
	public PtListIterator(Collection<PtCollectionObject<E>> collection) {
		thisCollection = new CopyOnWriteArrayList<>();
		thisCollection.addAll(collection);
		thisListIterator = thisCollection.listIterator();
	}
	
	public PtListIterator(int index, Collection<PtCollectionObject<E>> collection){
		thisCollection = new CopyOnWriteArrayList<>();
		thisCollection.addAll(collection);
		thisListIterator = thisCollection.listIterator(index);
	}
	
	@Override
	public boolean hasNext() {
		return thisListIterator.hasNext();
	}

	@Override
	public E next() {
		return thisListIterator.next().getObject();
	}

	@Override
	public boolean hasPrevious() {
		return thisListIterator.hasPrevious();
	}

	@Override
	public E previous() {
		return thisListIterator.previous().getObject();
	}

	@Override
	public int nextIndex() {
		return thisListIterator.nextIndex();
	}

	@Override
	public int previousIndex() {
		return thisListIterator.previousIndex();
	}

	@Override
	public void remove() {
		thisListIterator.remove();
	}

	@Override
	public void set(E e) {
		thisListIterator.set(new PtCollectionObject<E>(e));
	}

	@Override
	public void add(E e) {
		thisListIterator.add(new PtCollectionObject<E>(e));
	}

}
