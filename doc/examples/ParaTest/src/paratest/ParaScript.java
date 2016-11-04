package paratest;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The ParaScript class is run by the user.
 * 
 * It parses input arguments then generates a shell script file
 * to launch ParaTest.
 * 
 * @author Peter Nicolau
 */
public class ParaScript {

	public static void main(String[] args) {
		String script = "";
		String testType;
		int arg1 = 0;
		int arg2 = 0;
		int arg3 = 0;
		int arg4 = 0;
		int arg5 = 0;
		int arg6 = 0;
		int arg7 = 0;

		if (args.length == 0) {
			System.out.println("No arguments provided");
			help();
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
				if (arg2 < arg1) {
					throw new NumberFormatException();
				}
			} catch (NumberFormatException e) {
				System.out.println("End values must be larger then start values");
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
			if (testType.equals("-i")) {
				for (int i = arg1; i < arg2; i += arg3) {
					script = script.concat("java -jar paratest.jar -i " + i + "\n");
				}
			} else {
				System.out.println("Invalid test option provided");
				help();
			}
		} else if (args.length == 5) {
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
				arg2 = Integer.parseInt(args[1]);
				if (arg2 < 1) {
					throw new NumberFormatException();
				}
			} catch (NumberFormatException e) {
				System.out.println("Test options must be positive integers");
				help();
			}

			try {
				arg3 = Integer.parseInt(args[2]);
				if (arg3 < arg2) {
					throw new NumberFormatException();
				}
			} catch (NumberFormatException e) {
				System.out.println("End values must be larger then start values");
				help();
			}

			try {
				arg4 = Integer.parseInt(args[3]);
				if (arg4 < 1) {
					throw new NumberFormatException();
				}
			} catch (NumberFormatException e) {
				System.out.println("Test options must be positive integers");
				help();
			}

			testType = args[0];
			if (testType.equals("-p")) {
				for (int i = arg2; i < arg3; i += arg4) {
					script = script.concat("java -jar paratest.jar -p " + arg1 + " " + i + "\n");
				}
			} else {
				System.out.println("Invalid test option provided");
				help();
			}
		} else if (args.length == 7) {
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
				if (arg2 < arg1) {
					throw new NumberFormatException();
				}
			} catch (NumberFormatException e) {
				System.out.println("End values must be larger then start values");
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

			try {
				arg4 = Integer.parseInt(args[4]);
				if (arg4 < 1) {
					throw new NumberFormatException();
				}
			} catch (NumberFormatException e) {
				System.out.println("Test options must be positive integers");
				help();
			}

			try {
				arg5 = Integer.parseInt(args[5]);
				if (arg5 < arg4) {
					throw new NumberFormatException();
				}
			} catch (NumberFormatException e) {
				System.out.println("End values must be larger then start values");
				help();
			}

			try {
				arg6 = Integer.parseInt(args[6]);
				if (arg6 < 1) {
					throw new NumberFormatException();
				}
			} catch (NumberFormatException e) {
				System.out.println("Test options must be positive integers");
				help();
			}

			testType = args[0];
			if (testType.equals("-a")) {
				for (int i = arg1; i < arg2; i += arg3) {
					for (int j = arg4; j < arg5; j += arg6) {
						script = script.concat("java -jar paratest.jar -a " + i + " " + j + "\n");
					}
				}
			} else {
				System.out.println("Invalid test option provided");
				help();
			}
		} else if (args.length == 8) {
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
				arg2 = Integer.parseInt(args[1]);
				if (arg2 < 1) {
					throw new NumberFormatException();
				}
			} catch (NumberFormatException e) {
				System.out.println("Test options must be positive integers");
				help();
			}

			try {
				arg3 = Integer.parseInt(args[2]);
				if (arg3 < arg2) {
					throw new NumberFormatException();
				}
			} catch (NumberFormatException e) {
				System.out.println("End values must be larger then start values");
				help();
			}

			try {
				arg4 = Integer.parseInt(args[3]);
				if (arg4 < 1) {
					throw new NumberFormatException();
				}
			} catch (NumberFormatException e) {
				System.out.println("Test options must be positive integers");
				help();
			}

			try {
				arg5 = Integer.parseInt(args[4]);
				if (arg5 < 1) {
					throw new NumberFormatException();
				}
			} catch (NumberFormatException e) {
				System.out.println("Test options must be positive integers");
				help();
			}

			try {
				arg6 = Integer.parseInt(args[5]);
				if (arg6 < arg5) {
					throw new NumberFormatException();
				}
			} catch (NumberFormatException e) {
				System.out.println("End values must be larger then start values");
				help();
			}

			try {
				arg7 = Integer.parseInt(args[6]);
				if (arg7 < 1) {
					throw new NumberFormatException();
				}
			} catch (NumberFormatException e) {
				System.out.println("Stride increment must be a positive integer");
				help();
			}

			testType = args[0];
			if (testType.equals("-m1")) {
				for (int i = arg2; i < arg3; i += arg4) {
					for (int j = arg5; j < arg6; j += arg7) {
						script = script.concat("java -jar paratest.jar -m1 " + arg1 + " " + i + " " + j + "\n");
					}
				}
			} else if (testType.equals("-m2")) {
				for (int i = arg2; i < arg3; i += arg4) {
					for (int j = arg5; j < arg6; j += arg7) {
						script = script.concat("java -jar paratest.jar -m2 " + arg1 + " " + i + " " + j + "\n");
					}
				}
			} else {
				System.out.println("Invalid test option provided");
				help();
			}
		} else {
			System.out.println("Invalid number of arguments provided");
			help();
		}

		try{
			BufferedWriter writer = new BufferedWriter(new FileWriter("paratest.sh"));
			writer.write(script);
			writer.close();
		}catch (IOException e){
			System.out.println("Error writing to file");
		}
	}

	public static void help() {
		System.out.println("\nUsage: \tparascript [test] [options]");
		System.out.println("\nTest Options:");
		System.out.println("\t-a  [size start] [size end] [size increment] [stride start] [stride end] [stride icrement] \t\tSequential array test");
		System.out.println("\t-i  [size start] [size end] [size increment] \t\t\t\t\t\t\t\tSequential iterator test");
		System.out.println("\t-m1 [threads] [size start] [size end] [size increment] [stride start] [stride end] [stride icrement] \tParaTask based array test (alternate)");
		System.out.println("\t-m2 [threads] [size start] [size end] [size increment] [stride start] [stride end] [stride icrement] \tParaTask based array test (split)");
		System.out.println("\t-p  [threads] [size start] [size end] [size increment] \t\t\t\t\t\t\tParaTask based iterator test");
		System.exit(0);
	}

}
