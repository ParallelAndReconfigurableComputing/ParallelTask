package benchmarks.queens;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingWorker;

import pt.runtime.ParaTask;
import pt.runtime.ParaTask.ScheduleType;
import pt.runtime.ParaTask.ThreadPoolType;
import pt.runtime.TaskID;
import pt.runtime.TaskIDGroup;
import pt.runtime.TaskInfoNoArgs;

public class Queens {
	//private enum TestMode{ParaTask, SwingWorker, ExecutorService, Threads};
	private final int n;
	private int newtonNValue = 2;
	private int threshold = 4;
	private long numOfResults = 0;
	
	private static boolean shouldPrintResults = true;
	
	public Queens(int n, int newtonValue, int threshold){
		this.n = n;
		this.newtonNValue = newtonValue;
		this.threshold = threshold;
	}
	
	private boolean safe(int i, int j, int[] config){
		for (int counter = 0; counter < i; counter++){
			int s = config[counter];
			if (j == s || i-counter == j-s || i-counter == s-j)
				return false;
		}
		return true;
	}
	
	private int[] nQueensSeq(int[] config, int i){
		if (this.newtonNValue > 0)
			new NewtonChaos().newtonPartial(newtonNValue);
		if (i == n){
			if (shouldPrintResults){
				System.out.print("Got a result (in nqueenSeq: ");
				for (int v : config)
					System.out.print(v + " ");
				System.out.println();
			}
			numOfResults++;
			return config;
		}
		for (int j = 0; j < n; j++){
			int[] nconfig = new int[n];
			System.arraycopy(config, 0, nconfig, 0, n);
			nconfig[i] = j;
			if (safe(i, j, nconfig))
				nQueensSeq(nconfig, i+1);
		}
		return null;
	}
	
	private int[] nQueens(int[] config, int i){
		if (this.newtonNValue > 0)
			new NewtonChaos().newtonPartial(newtonNValue);
		if (i == n){
			if (shouldPrintResults){
				System.out.print("Got a result: ");
				for (int v : config)
					System.out.print(v + " ");
				System.out.println();
			}
			numOfResults++;
			return config;
		}
		int groupSize = 0;
		for (int j = 0; j < n; j++){
			int[] nconfig = new int[n];
			System.arraycopy(config, 0, nconfig, 0, n);
			nconfig[i] = j;
			
			if(safe(i, j, nconfig)){
				if(this.newtonNValue > 0)
					groupSize++;
				else if(n-i > this.threshold){
						groupSize++;
				}
			}
		}
		TaskIDGroup<int[]> group = new TaskIDGroup<>(groupSize);
		for (int j = 0; j < n; j++){
			int[] nconfig = new int[n];
			System.arraycopy(config, 0, nconfig, 0, n);
			nconfig[i] = j;
			
			if(safe(i, j, nconfig)){
				if(this.newtonNValue > 0){
					TaskInfoNoArgs<int[]> task = (TaskInfoNoArgs<int[]>) ParaTask.asTask(()->nQueens(nconfig, i+1));
					TaskID<int[]> id = task.start();
					group.addInnerTask(id);
				}else{
					if(n - i <= this.threshold){
						nQueensSeq(nconfig, i+1);
					}else{
						TaskInfoNoArgs<int[]> task = (TaskInfoNoArgs<int[]>) ParaTask.asTask(()->nQueens(nconfig,i+1));
						TaskID<int[]> id = task.start();
						group.addInnerTask(id);
					}
				}
			}
		}
		try{
			group.waitTillFinished();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	private int[] nQueensExecutor(int[] config, int i, int numThreads){
		ExecutorService executor = Executors.newFixedThreadPool(numThreads);
		//ExecutorService executor = Executors.newCachedThreadPool();
		//ExecutorService executor = Executors.newWorkStealingPool();
		
		
		if (this.newtonNValue > 0)
			new NewtonChaos().newtonPartial(newtonNValue);
		if (i == n){
			if (shouldPrintResults){
				System.out.print("Got a result: ");
				for (int v : config)
					System.out.print(v + " ");
				System.out.println();
			}
			numOfResults++;
			return config;
		}

		for (int j = 0; j < n; j++){
			int[] nconfig = new int[n];
			System.arraycopy(config, 0, nconfig, 0, n);
			nconfig[i] = j;
			
			if(safe(i, j, nconfig)){
				if(this.newtonNValue > 0){
					executor.execute(()->nQueensExecutor(nconfig, i+1, numThreads));
				}else{
					if(n - i <= this.threshold){
						nQueensSeq(nconfig, i+1);
					}else{
						executor.execute(()->nQueensExecutor(nconfig, i+1, numThreads));
					}
				}
			}
		}
		try{
			executor.shutdown();
			if(!executor.awaitTermination(20, TimeUnit.MINUTES))
				System.out.println("Stopped due to timeout");			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	private int[] nQueensThread(int[] config, int i){
			
		if (this.newtonNValue > 0)
			new NewtonChaos().newtonPartial(newtonNValue);
		if (i == n){
			if (shouldPrintResults){
				System.out.print("Got a result: ");
				for (int v : config)
					System.out.print(v + " ");
				//System.out.println();
			}
			numOfResults++;
			System.out.println(" *** " + numOfResults + " *** ");
			return config;
		}

		for (int j = 0; j < n; j++){
			int[] nconfig = new int[n];
			System.arraycopy(config, 0, nconfig, 0, n);
			nconfig[i] = j;
			
			if(safe(i, j, nconfig)){
				if(this.newtonNValue > 0){
					Thread thread = new Thread(()->nQueensThread(nconfig, i+1));
					thread.start();
				}else{
					if(n - i <= this.threshold){
						nQueensSeq(nconfig, i+1);
					}else{
						Thread thread = new Thread(()->nQueensThread(nconfig, i+1));
						thread.start();
					}
				}
			}
		}
		return null;
	}
	
	
	private void report(){
		System.out.println(numOfResults + " resuls were found in this process!");
	}
	
	public static void solveQueensPuzzleWithNewtonChaos(int nQueen, int numThreads,
			ScheduleType scheduleType, int nNewton) throws IllegalAccessException{
		assert nNewton >= 0;
		solveQueensPuzzle(nQueen, numThreads, scheduleType, nNewton, 0);
	}
	
	public static void solveQueensPuzzleWithThreshold(int nQueen, int numThreads,
			ScheduleType scheduleType, int threshold) throws IllegalAccessException{
		assert threshold >= 0;
		solveQueensPuzzle(nQueen, numThreads, scheduleType, 0, threshold);
	}
	
	private static void solveQueensPuzzle(int nQueen, int numThreads, ScheduleType scheduleType,
			int nNewton, int threshold) throws IllegalAccessException{
		ParaTask.setSchedulingType(scheduleType);
		ParaTask.setThreadPoolSize(ThreadPoolType.ALL, numThreads);
		Queens queens = new Queens(nQueen, nNewton, threshold);
		
		int[] config = new int[nQueen];
		queens.nQueens(config, 0); 
	}
	
	public static void solveQueensPuzzleSequentially(int nQueen, int nNewton){
		Queens queens = new Queens(nQueen, nNewton, 0);
		int[] config = new int[nQueen];
		queens.nQueensSeq(config, 0);
	}
	
	public static void main(String[] args) throws IllegalAccessException{
		//Usage: (pt-steal|thread|pt-share|seq|executor) (Nvalue) (numThreads) (newtonValue)
//		String appType = "pt-steal";
//		String appType = "pt-share";
//		String appType = "seq";
		String appType = "executor";
//		String appType = "thread";                                                                                                                                             		
		int nValue = 12;
		int numThreads = 4;
		int newtonValue = 2;
		
		if (appType.equals("pt-shar"))
			ParaTask.setSchedulingType(ScheduleType.WorkSharing);
		else if (appType.equals("pt-steal"))
			ParaTask.setSchedulingType(ScheduleType.WorkStealing);
		else if (appType.equals("pt-mix"))
			ParaTask.setSchedulingType(ScheduleType.MixedSchedule);
		
		if (numThreads > 0) ParaTask.setThreadPoolSize(ThreadPoolType.ALL, numThreads);
		
		Queens queens = new Queens(nValue, newtonValue, 0);
		System.gc();
		
		int[] config = new int[nValue];
		int[] result = null;
		
		System.out.println("CountQueens(" + nValue + ") ");
		
		if (appType.startsWith("pt-")){
			long start = System.currentTimeMillis();
			result = queens.nQueens(config, 0);
			long end = System.currentTimeMillis();
			queens.report();
			if (appType.equals("pt-share"))
				System.out.print("PT (work-sharing) ");
			else if (appType.equals("pt-steal"))
				System.out.print("PT (work-stealing) ");
			else
				System.out.print("PT (mixed-schedule) ");
			System.out.println(numThreads + " threads = " + (end - start));
		}
		else if (appType.equals("seq")){
			long start = System.currentTimeMillis();
			result = queens.nQueensSeq(config, 0);
			long end = System.currentTimeMillis();
			queens.report();
			System.out.println("seq = " + (end - start));
		}
		
		else if (appType.equals("thread")){
			long start = System.currentTimeMillis();
			result = queens.nQueensThread(config, 0);
			long end = System.currentTimeMillis();
			queens.report();
			System.out.println("thread = " + (end - start));
		}
		
		else if (appType.equals("executor")){
			long start = System.currentTimeMillis();
			result = queens.nQueensExecutor(config, 0, numThreads);
			long end = System.currentTimeMillis();
			queens.report();
			System.out.println("Executor " + numThreads + " threads = " + (end - start));
		}
		else{
			System.err.println("Aborting performance testing, unknown application: " + appType);
			System.exit(-1);
		}
		
	}
}
