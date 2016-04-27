package pt.wrappers;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
//import java.util.Spliterator;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.UnaryOperator;

import pt.runtime.TaskID;

public class PtCollectionWrapper<T> implements Collection<T>, Iterable<T> {
	
	ConcurrentLinkedDeque<PtCollectionObject<T>> thisCollection;
	Collection<T> thisOriginalCollection;
	AtomicBoolean setSubClass = new AtomicBoolean(false);
	
	public PtCollectionWrapper(){
		thisCollection = new ConcurrentLinkedDeque<>();
		thisOriginalCollection = null;
	}
	
	public PtCollectionWrapper(Collection<T> originalCollection) {
		thisCollection = new ConcurrentLinkedDeque<>();
		thisOriginalCollection = originalCollection;
		
		if(thisOriginalCollection instanceof Set)
			setSubClass.set(true);
		
		if(!thisOriginalCollection.isEmpty()){
			for (T element : thisOriginalCollection){
				this.add(element);
			}
		}
	}
	
	@Override
	public Iterator<T> iterator() {
		return new PtWrapperIterator<>(thisCollection);
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
	
	public boolean contains(PtCollectionObject<T> o){
		for (PtCollectionObject<T> obj : thisCollection){
			if(obj.equals(o))
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
			a = (E[]) Array.newInstance(a.getClass(), size());
		}
		
		int index = 0;
		for(PtCollectionObject<T> obj : thisCollection){
			a[index++] = (E)obj.getObject();
		}
		
		return a;
	}

	@Override
	public boolean add(T e) {
		if(setSubClass.get() && contains(e))
			return false;
		
		return thisCollection.add(new PtCollectionObject<T>(e));
	}
	
	public boolean add(TaskID<T> taskID){
		if(setSubClass.get() && contains(taskID))
			return false;
	
		return thisCollection.add(new PtCollectionObject<T>(taskID));
	}
	
	public boolean add(PtCollectionObject<T> collectionObject){
		if(setSubClass.get() && contains(collectionObject))
			return false;
		return thisCollection.add(collectionObject);
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
	
//	public Spliterator<T> spliterator(){
//		return thisCollection.spliterator();
//	}
	
	public int indexOf(Object o){
		if (o == null)
			return -1;
		
		int index = 0;
		for(PtCollectionObject<T> obj : thisCollection){
			if(obj.contains(o))
				return index;
			else
				index++;
		}
		
		return -1;
	}
	
	public int lastIndexOf(Object o){
		if (o == null)
			return -1;
		
		int lastIndex = -1;
		int index = 0;
		for(PtCollectionObject<T> obj : thisCollection){
			if(obj.contains(o))
				lastIndex = index;
			index++;
		}
		
		return lastIndex;
	}
	
	public Object clone(){
		PtCollectionWrapper<T> newCollection = new PtCollectionWrapper<>();
		for(PtCollectionObject<T> obj : thisCollection){
			newCollection.add(obj);
		}
		return newCollection;
	}
	
	public T get(int index){
		if (index < 0 || index >= size())
			throw new IndexOutOfBoundsException();
		
		int count = 0;
		for(PtCollectionObject<T> obj : thisCollection){
			if (count == index)
				return obj.getObject();
			count++;
		}
		
		return null;
	}
	
	public T set(int index, T element){
		if (index < 0 || index >= size())
			throw new IndexOutOfBoundsException();
		
		T returnObj = null;
		int counter = 0;
		for (PtCollectionObject<T> obj : thisCollection){
			if(counter == index){
				returnObj = obj.getObject();
				obj.setObject(element);
				break;
			}
			counter++;
		}
		return returnObj;
	}
	
	public T set(int index, TaskID<T> element){
		if (index < 0 || index >= size())
			throw new IndexOutOfBoundsException();
		
		T returnObj = null;
		int counter = 0;
		for (PtCollectionObject<T> obj : thisCollection){
			if(counter == index){
				returnObj = obj.getObject();
				obj.setObject(element);
				break;
			}
			counter++;
		}
		return returnObj;
	}
	
	public void add(int index, T element){
		if(index < 0 || index >= size())
			throw new IndexOutOfBoundsException();
		
		T temp = element;
		while(index < size()){
			temp = set(index, temp);
			index++;
		}
		
		add(temp);
	}
	
	public void removeRange(int fromIndex, int toIndex){
		
		if(fromIndex < 0 || fromIndex >= size())
			throw new IndexOutOfBoundsException();
		
		if(toIndex < fromIndex || toIndex >= size())
			throw new IndexOutOfBoundsException();
		
		int index = fromIndex;
		for (; index < toIndex; index++){
			/*delete the element on the same index, because
			 * as the result of each delete, elements are 
			 * shifted one location to left!*/
			remove(get(fromIndex));
		}
	}
	
	@SuppressWarnings("unchecked")
	public void sort(Comparator<? super T> c){
			T temp = get(0);
			T[] src = (T[])Array.newInstance(temp.getClass(), size());
			
			int index = 0;
			for (PtCollectionObject<T> obj : thisCollection){
				src[index++] = obj.getObject();
			}
			if (c==null)
			    throw new RuntimeException("PtCollectionWrapper::sort method is expecting a Comparator object");
		    else
		    	Arrays.sort(src, c);
			
			for(int i = 0; i < size(); i++){
				this.set(i, src[i]);
			}
	}
	
	public void replaceAll(UnaryOperator<T> operator){
		for (PtCollectionObject<T> obj : thisCollection){
			obj.setObject(operator.apply(obj.getObject()));
		}
	}
	
	@Override
	public String toString(){
		int index = 1;
		String returnStr = "[";
		for(PtCollectionObject<T> obj : thisCollection){
			returnStr += obj.toString();
			if (index++ < thisCollection.size())
				returnStr += ", ";
		}
		returnStr += "]";
		return returnStr;
	}
	
	 public T element(){
		 return thisCollection.element().getObject();
	 }
	 
	 public void addFirst(T e){
		 thisCollection.addFirst(new PtCollectionObject<T>(e));
	 }
	 
	 public void addFirst(TaskID<T> taskID){
		 thisCollection.addFirst(new PtCollectionObject<T>(taskID));
	 }
	 
	 public void addLast(T e){
		 thisCollection.addLast(new PtCollectionObject<T>(e));
	 }
	 
	 public void addLast(TaskID<T> taskID){
		 thisCollection.addLast(new PtCollectionObject<T>(taskID));
	 }
	
	 public boolean offer(T e){
		 return thisCollection.offer(new PtCollectionObject<T>(e));
	 }
	 
	 public boolean offer(TaskID<T> taskID){
		 return thisCollection.offer(new PtCollectionObject<T>(taskID));
	 }
	 
	 public boolean offerFirst(T e){
		 return thisCollection.offerFirst(new PtCollectionObject<T>(e));
	 }
	 
	 public boolean offerFirst(TaskID<T> taskID){
		 return thisCollection.offerFirst(new PtCollectionObject<T>(taskID));
	 }
	 
	 public boolean offerLast(T e){
		 return thisCollection.offerLast(new PtCollectionObject<T>(e));
	 }
	 
	 public boolean offerLast(TaskID<T> taskID){
		 return thisCollection.offerLast(new PtCollectionObject<T>(taskID));
	 }
	 
	 public boolean removeFirstOccurrence(T t){
		 return thisCollection.removeFirstOccurrence(new PtCollectionObject<T>(t));
	 }
	 
	 public boolean removeFirstOccurrence(TaskID<T> taskID){
		 return thisCollection.removeFirstOccurrence(new PtCollectionObject<T>(taskID));
	 }
	 
	 public boolean removeLastOccurrence(T t){
		 return thisCollection.removeLastOccurrence(new PtCollectionObject<T>(t));
	 }
	 
	 public boolean removeLastOccurrence(TaskID<T> taskID){
		 return thisCollection.removeLastOccurrence(new PtCollectionObject<T>(taskID));
	 }
	 
	 public void push(T e){
		 thisCollection.push(new PtCollectionObject<T>(e));
	 }
	 
	 public void push(TaskID<T> taskID){
		 thisCollection.push(new PtCollectionObject<T>(taskID));
	 }
	 
	 public T pop(){
		 return thisCollection.pop().getObject();
	 }
	 
	 public T peek(){
		 return thisCollection.peek().getObject();
	 }
	 
	 public T peekFirst(){
		 return thisCollection.peekFirst().getObject();
	 }
	 
	 public T peekLast(){
		 return thisCollection.peekLast().getObject();
	 }
	 
	 public T poll(){
		 return thisCollection.poll().getObject();
	 }
	 
	 public T pollFirst(){
		 return thisCollection.pollFirst().getObject();
	 }
	 
	 public T pollLast(){
		 return thisCollection.pollLast().getObject();
	 }
	 
	 public T getFirst(){
		 return thisCollection.getFirst().getObject();
	 }
	 
	 public T getLast(){
		 return thisCollection.getLast().getObject();
	 }
	 
	 public T remove(){
		 return thisCollection.remove().getObject();
	 }
	 
	 public T removeFirst(){
		 return thisCollection.removeFirst().getObject();
	 }
	 
	 public T removeLast(){
		 return thisCollection.removeLast().getObject();
	 }
}
