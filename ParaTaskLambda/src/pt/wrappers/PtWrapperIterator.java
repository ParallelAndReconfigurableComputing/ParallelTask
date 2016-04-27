package pt.wrappers;

import java.util.Collection;
import java.util.Iterator;

public class PtWrapperIterator<T> implements Iterator<T> {
	
	Iterator<PtCollectionObject<T>> thisIterator;
	Collection<PtCollectionObject<T>> thisCollection;
	
	public PtWrapperIterator(Collection<PtCollectionObject<T>> collection){
		thisCollection = collection;
		thisIterator = thisCollection.iterator();
	}

	@Override
	public boolean hasNext() {
		return thisIterator.hasNext();
	}

	@Override
	public T next() {
		PtCollectionObject<T> nextObject = thisIterator.next();
		return nextObject.getObject();
	}

	@Override
	public void remove(){
		thisIterator.remove();
	}
}
