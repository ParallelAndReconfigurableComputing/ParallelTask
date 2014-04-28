package pt.queues;

import java.util.concurrent.LinkedBlockingDeque;

import pt.runtime.Future;

public class PipelineQueue<E> extends LinkedBlockingDeque<E> {

	private static final long serialVersionUID = -838176089092483753L;

	private Future head;
	private Future tail;

	public PipelineQueue(Future head, Future tail) {
		this.head = head;
		this.tail = tail;
	}
	
	public Future getHeadTask() {
		return head;
	}
	
	public Future getTailTask() {
		return tail;
	}
	
	public void setHeadTask(Future tid) {
		head = tid;
	}
	
	public void setTailTask(Future tid) {
		tail = tid;
	}
}
