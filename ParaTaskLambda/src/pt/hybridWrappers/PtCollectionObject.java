package pt.hybridWrappers;

import pt.runtime.TaskID;

public class PtCollectionObject<E> {

	private enum ElementType {OBJECT, TASKID};
	private E thisObject;
	private TaskID<E> thisTaskID;
	private ElementType elementType;
	
	public PtCollectionObject(E object){
		elementType = ElementType.OBJECT;
		thisObject = object;
		thisTaskID = null;		
	}
	
	public PtCollectionObject(TaskID<E> taskID){
		elementType = ElementType.TASKID;
		thisTaskID = taskID;
		thisObject = null;
	}
	
	public void setObject(E object){
		elementType = ElementType.OBJECT;
		thisObject = object;
		thisTaskID = null;
	}
	
	public void setObject(TaskID<E> taskID){
		elementType = ElementType.TASKID;
		thisTaskID = taskID;
		thisObject = null;
	}
	
	public E  getObject(){
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
		switch(elementType){
		case OBJECT:
			return thisObject.equals(obj);
		case TASKID:
			return thisTaskID.equals(obj);
		default:
			return false;
		}
	}
	
	@Override
	public String toString(){
		return getObject().toString();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public boolean equals(Object obj){
		if (!(obj instanceof PtCollectionObject))
			return false;
		return this.getObject().equals(((PtCollectionObject<E>)obj).getObject());
	}
}
