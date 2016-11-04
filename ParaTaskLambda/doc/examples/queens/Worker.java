package benchmarks.queens;

import java.util.concurrent.CountDownLatch;

import javax.swing.SwingWorker;

import pt.functionalInterfaces.FunctorNoArgsNoReturn;
import pt.functionalInterfaces.FunctorNoArgsWithReturn;

public class Worker<R, T> extends SwingWorker<R, T> {
	
	private FunctorNoArgsWithReturn<R> functorWithReturn = null;
	private FunctorNoArgsNoReturn functorNoReturn = null;
	private CountDownLatch latch;
	
	public Worker(FunctorNoArgsWithReturn<R> functor, CountDownLatch latch){
		this.functorWithReturn = functor;
		this.latch = latch;
	}
	
	public Worker(FunctorNoArgsNoReturn functor, CountDownLatch latch){
		this.functorNoReturn = functor;
		this.latch = latch;
	}
	@Override
	protected R doInBackground() throws Exception {
		System.out.println("Worker started");
		if (functorWithReturn != null){
			R value =  functorWithReturn.exec();
			System.out.println("Counting latch down");
			latch.countDown();
			return value;
		}
		functorNoReturn.exec();
		System.out.println("Counting latch down");
		latch.countDown();
		return null;
	}
	
//	@Override
//	protected void done(){
//		System.out.println("Counting latch down");
//		latch.countDown();
//	}

}
