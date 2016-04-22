package collections;

import pt.runtime.TaskID;

public class PtCollectionObject<T> {

	private enum ElementType {OBJECT, TASKID};
	private T thisObject;
	private TaskID<T> thisTaskID;
	private ElementType elementType;
	
	public PtCollectionObject(T object){
		elementType = ElementType.OBJECT;
		thisObject = object;
		thisTaskID = null;		
	}
	
	public PtCollectionObject(TaskID<T> taskID){
		elementType = ElementType.TASKID;
		thisTaskID = taskID;
		thisObject = null;
	}
	
	public void setObject(T object){
		elementType = ElementType.OBJECT;
		thisObject = object;
		thisTaskID = null;
	}
	
	public void setObject(TaskID<T> taskID){
		elementType = ElementType.TASKID;
		thisTaskID = taskID;
		thisObject = null;
	}
	
	public T  getObject(){
		switch (elementType){
			case OBJECT:
				return thisObject;
			case TASKID:
				return thisTaskID.getReturnResult();
			default:
				return null;			
		}
	}
	
	public boolean contains(Object obj){
		if(thisObject.equals(obj))
			return true;
		else if (thisTaskID.equals(obj))
			return true;
		return false;
	}
}
