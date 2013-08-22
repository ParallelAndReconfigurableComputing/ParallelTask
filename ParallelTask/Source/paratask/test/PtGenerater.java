package paratask.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class PtGenerater {

	private static final String[] TEMPLATE_FILE = {"TestOneoffTaskTemplate"};
	
	private static final String[] GENERATE_FILE = {"TestOneoffTask.ptjava"};
	
	private static final String CALL_M_PATTERN = "^#call-Multi-task$";
	private static final String CALL_O_PATTERN = "^#call-Oneoff-task$";
	private static final String REPEAT_M_PATTERN = "^#repeat-Multi-task$";
	private static final String REPEAT_O_PATTERN = "^#repeat-Oneoff-task$";
	
	private static BufferedReader fileReader; 
	
	private static BufferedWriter fileWriter;
	
	public static void main(String[] args) {
		if (null == args || args.length != 1) {
			return ; 
		}
		
		if (TEMPLATE_FILE.length != GENERATE_FILE.length) {
			return;
		}
		
		String templateFileName = null;
		String generateFileName = null;
		
		for (int index = 0; index < TEMPLATE_FILE.length; index++) {
			templateFileName = TEMPLATE_FILE[index];
			generateFileName = GENERATE_FILE[index];
			
			try {
				fileReader = new BufferedReader(new FileReader(new File(templateFileName))) ;
				fileWriter = new BufferedWriter(new FileWriter(new File(generateFileName)));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			int taskNum = Integer.valueOf(args[0]);
			
			String currentLine = null;
			
			boolean isMatch_call_multi_task = false;
			boolean isMatch_repeat_multi_task = false;
			String call_multi_task = null;
			int[] multi_task_order = new int[taskNum/2];
			int multi_task_order_index = 0;
			
			boolean isMatch_call_oneoff_task = false;
			boolean isMatch_repeat_oneoff_task = false;
			String call_oneoff_task = null;
			int[] oneoff_task_order = new int[taskNum%2==0?taskNum/2:(taskNum/2)+1];
			int oneoff_task_order_index = 0;
			
			
			try {
				while (null != (currentLine = fileReader.readLine())) {
					if (currentLine.trim().matches(CALL_M_PATTERN)) {
						isMatch_call_multi_task = true;
						continue;
					}
					
					if (currentLine.trim().matches(CALL_O_PATTERN)) {
						isMatch_call_oneoff_task = true;
						continue;
					}
					
					if (isMatch_call_multi_task) {
						call_multi_task = currentLine;
						isMatch_call_multi_task = false;
						continue;
					}
					
					if (isMatch_call_oneoff_task) {
						call_oneoff_task = currentLine;
						isMatch_call_oneoff_task = false;
						continue;
					}
					
					if (null != call_multi_task && null != call_oneoff_task) {
						int[] randomSerial = random_serial(taskNum);
						for (int i = 0; i < randomSerial.length; i++) {
							if (randomSerial[i] % 2 == 0) {
								String new_call_multi_task = call_multi_task.replaceAll("@", String.valueOf(i));
								fileWriter.write(new_call_multi_task + "\n");
								fileWriter.flush();
								multi_task_order[multi_task_order_index] = i;
								multi_task_order_index++;
							}else {
								String new_call_oneoff_task = call_oneoff_task.replaceAll("@", String.valueOf(i));
								fileWriter.write(new_call_oneoff_task + "\n");
								fileWriter.flush();
								oneoff_task_order[oneoff_task_order_index] = i;
								oneoff_task_order_index++;
							}
						}
						
						call_multi_task = null;
						call_oneoff_task = null;
						continue;
					}
					
					if (currentLine.trim().matches(REPEAT_M_PATTERN)) {
						isMatch_repeat_multi_task = true;
						continue;
					}
					
					if (currentLine.trim().matches(REPEAT_O_PATTERN)) {
						isMatch_repeat_oneoff_task = true;
						continue;
					}
					
					if (isMatch_repeat_multi_task) {
						for (int i = 0; i < multi_task_order.length; i++) {
							String newLine = currentLine.replaceAll("@", String.valueOf(multi_task_order[i]));
							fileWriter.write(newLine + "\n");
							fileWriter.flush();
						}
						isMatch_repeat_multi_task = false;
						continue;
					}
					
					if (isMatch_repeat_oneoff_task) {
						for (int i = 0; i < oneoff_task_order.length; i++) {
							String newLine = currentLine.replaceAll("@", String.valueOf(oneoff_task_order[i]));
							fileWriter.write(newLine + "\n");
							fileWriter.flush();
						}
						isMatch_repeat_oneoff_task = false;
						continue;
					}
					
					fileWriter.write(currentLine + "\n");
					fileWriter.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					fileReader.close();
					fileWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static int[] random_serial(int limit) {
		int[] result = new int[limit];
		for (int i = 0; i < limit; i++)
			result[i] = i;
		int w;
		Random rand = new Random();
		for (int i = limit - 1; i > 0; i--) {
			w = rand.nextInt(i);
			int t = result[i];
			result[i] = result[w];
			result[w] = t;
		}
		return result;
	}
}
