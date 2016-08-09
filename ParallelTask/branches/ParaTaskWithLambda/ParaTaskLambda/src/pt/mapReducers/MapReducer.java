package pt.mapReducers;

public interface MapReducer<T, E> {
	public boolean startMapReduceOperation();
	public T getFinalResult();
}
