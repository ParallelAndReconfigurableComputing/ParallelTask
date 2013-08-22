package paratask.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class _TestRuntime {
	private static final int N_DATASIZE = 1;

	private static final String BM_METHOD = "execute";
	
	private static final Class<?>[] BM_METHOD_ARGUEMENT_TYPE = {int.class};

	private static final String MOL = "MOL";

	private static final String MOL_CLASS = "core.moldyn.Molcore";

	private static final String MON = "MON";

	private static final String MON_CLASS = "core.montecarlo.Moncore";

	private static final String RAY = "RAY";

	private static final String RAY_CLASS = "core.raytracer.Raycore";

	private static final String DATE_FORMART = "yyyyMMddHHmmss";

	private static ConcurrentLinkedQueue<Benchmark> concurrentLinkedQueue = null;
	
	private static List<Float> resultList = new ArrayList<Float>();

	/**
	 * @param args
	 *            [0] The type of JGF benchmark. It should be a 3 character
	 *            length String. The allowed value only includes {MOL, MON,
	 *            RAY}, and it is not case sensitive.
	 * 
	 * @param args
	 *            [1] The length of the benchmark set. This value define how
	 *            many benchmark will be included during one test case.
	 * 
	 * @param args
	 *            [2] The benchmark start running time. The time format must be "yyyyMMddHHmmss"
	 * 
	 * 
	 * */
	public static void main(String[] args) {
		if (null == args || args.length != 3) {
			try {
				throw new Exception("Wrong arguemnts setting");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Class<?> bmClass = getBenchmarkClass(args[0]);

		try {
			concurrentLinkedQueue = createBenchmarkSet(bmClass, Integer.valueOf(args[1]));
			
			Date startDate = new SimpleDateFormat(DATE_FORMART).parse(args[2]);
			while (startDate.after(new Date())){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			execute(concurrentLinkedQueue);

		} catch (NumberFormatException e) {
			e.printStackTrace();
		}catch (ParseException e) {
			e.printStackTrace();
		}

		float sum = 0.0f;
		for (float result : resultList) {
			sum += result;
		}
		System.out.println(sum/resultList.size());
		
	}

	private static void execute(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {
		
		Benchmark benchmark = null;
		while (null != (benchmark = benchmarkQueue.poll())) {
			resultList.add(runBM(benchmark));
		}
		
	}

	private static Float runBM(Benchmark benchmark) {
		Object returnObj = null;
		try {
			returnObj = benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return (Float) returnObj;
	}

	private static ConcurrentLinkedQueue<Benchmark> createBenchmarkSet(Class<?> bmClass, Integer setLen) {
		concurrentLinkedQueue = new ConcurrentLinkedQueue<Benchmark>();
		for (int i = 0; i < setLen; i++) {
			Object benchmark = null;
			Method method = null;
			try {
				benchmark = bmClass.newInstance();
				method = bmClass.getMethod(BM_METHOD, BM_METHOD_ARGUEMENT_TYPE);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			Object[] arguments = new Object[1];
			arguments[0] = N_DATASIZE;

			concurrentLinkedQueue.add(new Benchmark(benchmark, method, arguments));

		}
		return concurrentLinkedQueue;
	}

	private static Class<?> getBenchmarkClass(String bmName) {

		Class<?> bmClass = null;

		try {
			if (bmName.equalsIgnoreCase(MOL)) {
				bmClass = Class.forName(MOL_CLASS);
			} else if (bmName.equalsIgnoreCase(MON)) {
				bmClass = Class.forName(MON_CLASS);
			} else if (bmName.equalsIgnoreCase(RAY)) {
				bmClass = Class.forName(RAY_CLASS);
			} else {
				throw new Exception("Can not find the Benchmark " + bmName);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bmClass;
	}
}