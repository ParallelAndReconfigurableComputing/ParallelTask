package pt.wrappers;

import java.util.Collection;
import java.util.Iterator;

public class PtIterator<E> implements Iterator<E> {
	
	Iterator<PtCollectionObject<E>> thisIterator;
	Collection<PtCollectionObject<E>> thisCollection;
	
	public PtIterator(Collection<PtCollectionObject<E>> collection){
		thisCollection = collection;
		thisIterator = thisCollection.iterator();
	}

	@Override
	public boolean hasNext() {
		return thisIterator.hasNext();
	}

	@Override
	public E next() {
		PtCollectionObject<E> nextObject = thisIterator.next();
		return nextObject.getObject();
	}

	@Override
	public void remove(){
		thisIterator.remove();
	}
}
