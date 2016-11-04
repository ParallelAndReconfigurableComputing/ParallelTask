package paratest;

/**
 * The ParaTest class is run by the user.
 * 
 * It parses input arguments then initialises and starts the
 * appropriate Test subclass.
 * 
 * @author Peter Nicolau
 */
public class ParaTest {

	public static void main(String[] args) {
		Test test = null;
		String testType;
		int arg1 = 0;
		int arg2 = 0;
		int arg3 = 0;

		if (args.length == 0) {
			System.out.println("No arguments provided");
			help();
		} else if (args.length == 2) {
			try {
				arg1 = Integer.parseInt(args[1]);
				if (arg1 < 1) {
					throw new NumberFormatException();
				}
			} catch (NumberFormatException e) {
				System.out.println("Test options must be positive integers");
				help();
			}

			testType = args[0];
			if (testType.equals("-i")) {
				test = new TestIterator(arg1);
			} else {
				System.out.println("Invalid test provided");
				help();
			}
		} else if (args.length == 3) {
			try {
				arg1 = Integer.parseInt(args[1]);
				if (arg1 < 1) {
					throw new NumberFormatException();
				}
			} catch (NumberFormatException e) {
				System.out.println("Test options must be positive integers");
				help();
			}

			try {
				arg2 = Integer.parseInt(args[2]);
				if (arg2 < 1) {
					throw new NumberFormatException();
				}
			} catch (NumberFormatException e) {
				System.out.println("Test options must be positive integers");
				help();
			}

			testType = args[0];
			if (testType.equals("-a")) {
				test = new TestArray(arg1, arg2);
			} else if (testType.equals("-p")) {
				test = new TestParaIterator(arg1, arg2);
			} else {
				System.out.println("Invalid test provided");
				help();
			}
		} else if (args.length == 4) {
			try {
				arg1 = Integer.parseInt(args[1]);
				if (arg1 < 1) {
					throw new NumberFormatException();
				}
			} catch (NumberFormatException e) {
				System.out.println("Test options must be positive integers");
				help();
			}

			try {
				arg2 = Integer.parseInt(args[2]);
				if (arg2 < 1) {
					throw new NumberFormatException();
				}
			} catch (NumberFormatException e) {
				System.out.println("Test options must be positive integers");
				help();
			}

			try {
				arg3 = Integer.parseInt(args[3]);
				if (arg3 < 1) {
					throw new NumberFormatException();
				}
			} catch (NumberFormatException e) {
				System.out.println("Test options must be positive integers");
				help();
			}

			testType = args[0];
			if (testType.equals("-m1")) {
				test = new TestParaArray1(arg1, arg2, arg3);
			} else if (testType.equals("-m2")) {
				test = new TestParaArray2(arg1, arg2, arg3);
			} else {
				System.out.println("Invalid test option provided");
				help();
			}			
		} else {
			System.out.println("Invalid number of arguments provided");
			help();
		}

		test.execute();
	}

	public static void help() {
		System.out.println("\nUsage: \tparatest [test] [options]");
		System.out.println("\nTest Options:");
		System.out.println("\t-a  [size] [stride] \t\tSequential array test");
		System.out.println("\t-i  [size] \t\t\tSequential iterator test");
		System.out.println("\t-m1 [threads] [size] [stride] \tParaTask based array test (alternate)");
		System.out.println("\t-m2 [threads] [size] [stride] \tParaTask based array test (split)");
		System.out.println("\t-p  [threads] [size] \t\tParaTask based iterator test");
		System.exit(0);
	}

}
