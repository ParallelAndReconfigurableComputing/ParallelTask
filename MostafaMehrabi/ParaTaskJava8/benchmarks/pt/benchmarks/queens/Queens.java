package pt.benchmarks.queens;

import pt.runtime.ParaTask;
import pt.runtime.ParaTask.ScheduleType;
import pt.runtime.ParaTask.ThreadPoolType;
import pt.runtime.TaskID;
import pt.runtime.TaskIDGroup;
import static pt.runtime.Task.*;

public class Queens {
	static {
		ParaTask.init();
	}
	
    private final int n;
    private int newtonNValue = 2;
    
    private int threshold = 4;
    
    private static boolean shouldPrintResults = false;

    public Queens(int n, int newtonValue, int threshold) {
        this.n = n;
        this.newtonNValue = newtonValue;
        this.threshold = threshold;
    }
	
    private boolean safe(int i, int j, int[] config) {
		for(int r = 0; r < i; r++) {
		    int s = config[r];
		    if(j == s || i-r == j-s || i-r == s-j) {
			return false;
		    }
		}
		return true;
    }
    
    
    private int[] nqueensSeq(int[] config, int i)  {
    
    	if(this.newtonNValue > 0)
    		new NewtonChaos().newtonPartial(newtonNValue);  //-- adding dummy computation to show some speedup
    	
		if(i == n) {
			if(shouldPrintResults) {
				System.out.print("Got a result (in nqueensSeq): ");
	        	for (int v : config) {
	                System.out.print(v + " ");
	            }
	            System.out.println();
			}
            
		    return config;
		}
		for(int j = 0; j < n; j++) {
		    int[] nconfig = new int[n];
		    System.arraycopy(config, 0, nconfig, 0, n);
		    nconfig[i] = j;
	
		    if(safe(i, j, nconfig)) {		
				 nqueensSeq(nconfig, i+1);
		    }
		}
		return null;
    }
	
	
    private int[] nqueens(int[] config, int i)  {
    
    	if(this.newtonNValue > 0)
    		new NewtonChaos().newtonPartial(newtonNValue);  //-- adding dummy computation to show some speedup
    
		if(i == n) {
			if(shouldPrintResults) {
				System.out.print("Got a result: ");
	        	for (int v : config) {
	                System.out.print(v + " ");
	            }
	            System.out.println();
			}
		    return config;
		}
		
		int groupSize = 0;
		for(int j = 0; j < n; j++) {
		    int[] nconfig = new int[n];
		    System.arraycopy(config, 0, nconfig, 0, n);
		    nconfig[i] = j;
	
		    if(safe(i, j, nconfig)) {
		    	if(this.newtonNValue > 0)
		    		groupSize++;
		    	else {
		    		if(n - i <= this.threshold) {
		    		} else
		    			groupSize++;
		    	}
		    }
		} 
		
        TaskIDGroup<int[]> group = new TaskIDGroup<int[]>(groupSize);
		
		for(int j = 0; j < n; j++) {
		    int[] nconfig = new int[n];
		    System.arraycopy(config, 0, nconfig, 0, n);
		    nconfig[i] = j;
	
		    if(safe(i, j, nconfig)) {
		    	if(this.newtonNValue > 0) {
					TaskID<int[]> id = asTask(() -> nqueens(nconfig, i+1)).start();
					group.add(id);				
		    	} else {
		    		// no newton chaos computation, then enable the threshold
		    		if(n - i <= this.threshold) {
		    			nqueensSeq(nconfig, i + 1);
		    		} else {
		    			TaskID<int[]> id = asTask(() -> nqueens(nconfig, i+1)).start();
		    		    group.add(id);
		    		}
		    	}
		    }
		}
		
		try {
			group.waitTillFinished();  
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }
	
    public static void solveQueensPuzzleWithNewtonChaos(int nQueen, int numThreads, 
    		ScheduleType schedule, int nNewton) {
    	assert nNewton >= 0;
    	solveQueensPuzzle(nQueen, numThreads, schedule, nNewton, 0);
    }
    
    public static void solveQueensPuzzleWithThreshold(int nQueen, int numThreads, 
    		ScheduleType schedule, int threshold) {
    	assert threshold >= 0;
    	solveQueensPuzzle(nQueen, numThreads, schedule, 0, threshold);
    }    
    
    private static void solveQueensPuzzle(int nQueen, int numThreads, ScheduleType schedule,
    		int nNewton, int threshold) {
    	ParaTask.setScheduling(schedule);
    	ParaTask.setThreadPoolSize(ThreadPoolType.ALL, numThreads);
    	Queens queens = new Queens(nQueen, nNewton, threshold);
    	
    	int[] config = new int[nQueen];
        int[] result = queens.nqueens(config, 0);
        
    }
    
    public static void solveQueensPuzzleSequentially(int nQueen, int nNewton) {
    	Queens queens = new Queens(nQueen, nNewton, 0);
    	int[] config = new int[nQueen];
    	queens.nqueensSeq(config, 0);
    }
    
    public static void main2(String[] args) {
    	shouldPrintResults = true;
    	//solveQueensPuzzleSequentially(6, 2);
    	//solveQueensPuzzleWithThreshold(6, 1, ScheduleType.WorkSharing, 4);
    	solveQueensPuzzleWithNewtonChaos(6, 1, ScheduleType.WorkSharing, 2);
    }
    
    public static void main(String[] args) {
        String appType = "pt-steal";
        //appType = "seq";
        int nValue = 11;
        int numThreads = 3;
        int newtonValue = 2;
        if (args.length == 0) {
        } else if (args.length != 4) {
            System.out.println("Usage: (pt-steal|pt-mix|seq|jv-max) (Nvalue) (numThreads) (newtonValue)");
            System.exit(0);
        } else {
            appType = args[0];
            nValue = Integer.parseInt(args[1]);
            numThreads = Integer.parseInt(args[2]);
            newtonValue = Integer.parseInt(args[3]);
        }
        if (appType.equals("pt-share")) {
            ParaTask.setScheduling(ScheduleType.WorkSharing);
        } else if (appType.equals("pt-steal")) {
            ParaTask.setScheduling(ScheduleType.WorkStealing);
        }
        if (numThreads > 0) ParaTask.setThreadPoolSize(ThreadPoolType.ALL, numThreads);
        Queens queens = new Queens(nValue, newtonValue, 0);
        System.gc();
        
        int[] config = new int[nValue];
        int[] result = null;
        System.out.print("CountQueens(" + nValue + ")  ");
        if (appType.startsWith("pt-")) {
            long start = System.currentTimeMillis();
            result = queens.nqueens(config, 0);
            
            long end = System.currentTimeMillis();
            if (appType.equals("pt-share")) {
                System.out.print("PT (work-sharing) ");
            } else if (appType.equals("pt-steal")){
                System.out.print("PT (work-stealing) ");
            } else {
            	System.out.print("PT (mixed-schedule) ");
            }
            System.out.println(numThreads + " threads = " + (end - start));
        } else if (appType.equals("seq")) {
            long start = System.currentTimeMillis();
            result = queens.nqueensSeq(config, 0);
            long end = System.currentTimeMillis();
            System.out.println("seq = " + (end - start));
        } else if (appType.equals("jv-max")) {
            long start = System.currentTimeMillis();
            long end = System.currentTimeMillis();
            System.out.println("Java-max = " + (end - start));
        } else if (appType.equals("jv-min")) {
            long start = System.currentTimeMillis();
            long end = System.currentTimeMillis();
            System.out.println("Java-min " + numThreads + " threads = " + (end - start));
        } else if (appType.equals("sw")) {
            long start = System.currentTimeMillis();
            long end = System.currentTimeMillis();
            System.out.println("SW = " + (end - start));
        } else {
            System.err.println("Aborting performance testing, unknown application: " + appType);
            System.exit(-1);
        }
    }
}
