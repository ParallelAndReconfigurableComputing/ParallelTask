package taskIDGroupNotifyTest;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;

import pt.functionalInterfaces.FunctorNoArgsWithReturn;
import pt.functionalInterfaces.FunctorOneArgNoReturn;
import pt.functionalInterfaces.FunctorOneArgWithReturn;
import pt.runtime.ParaTask;
import pt.runtime.TaskIDGroup;
import pt.runtime.TaskInfoNoArgs;
import pt.runtime.ParaTask.TaskType;
import pt.runtime.TaskID;
import pu.RedLib.IntegerSum;
import pt.runtime.TaskInfoOneArg;
import pu.RedLib.Reducer;
import pu.RedLib.Reducer.OPERATION;

public class Main {
	
	public static int IOTask(int num){
		int randNo = 0;
		Random rand = new Random();
		
		if(num == 0)
			randNo = 1;
		else	
			randNo = 6*rand.nextInt(num);
		
		try{
			System.out.println((num+1) + "- thread " + Thread.currentThread().getId() + " sleeping for " + randNo + " seconds."); 
			Thread.sleep(randNo*1000);
		}catch(Exception e){
			e.printStackTrace();
		}
	
		System.out.println((num+1) + "-> thread " + Thread.currentThread().getId() + " slept for " + randNo + " seconds."); 	
		return randNo;
	}
	
	public static Map<String, Integer> wordToInt(String word){
		int multiplier = (int) Thread.currentThread().getId();
		int wordLetters = word.length() * multiplier;
		word = word + ", " + multiplier;
		Map<String, Integer> wordLength = new HashMap<>();
		wordLength.put(word, wordLetters);
		return wordLength;
	}
	
	public static void updateGui(){
		System.out.println("GUI updated by thread " + Thread.currentThread().getId());
	}
	
	public static void updateDB(){
		System.out.println("DataBase updated by thread " + Thread.currentThread().getId());
	}
	
	public static void reportResult(int sum){
		System.out.println("Sum of the results is: " + sum);
	}

	public static void main(String[] args) {
	//	testFutureGroup();
		testParallelFuture();
	}
	
	@SuppressWarnings("unchecked")
	public static void testFutureGroup(){
		ParaTask.init();
		TaskIDGroup<Integer> group = new TaskIDGroup<>();
		ParaTask.registerReduction(group, new IntegerSum());
		for(int i = 0; i < 20; i++){
			TaskInfoOneArg<Integer, Integer> taskInfo = (TaskInfoOneArg<Integer, Integer>) ParaTask.asTask(TaskType.ONEOFF, 
					(FunctorOneArgWithReturn<Integer, Integer>)(x)->IOTask(x));
			group.addInnerTask(taskInfo.start(i));
		}
		
		TaskInfoOneArg<Integer, TaskIDGroup<Integer>> reducer = (TaskInfoOneArg<Integer, TaskIDGroup<Integer>>) ParaTask.asTask(TaskType.ONEOFF, 
				(FunctorOneArgWithReturn<Integer, TaskIDGroup<Integer>>)(taskIDGroup)->(Reducer.reduce(taskIDGroup.getResultsAsArray(new Integer[2]), OPERATION.SUM)));
		ParaTask.registerDependences(reducer, group);
		ParaTask.registerSlotToNotify(reducer, (x)->reportResult(x));
		TaskID<Integer> reducerID = reducer.start(group);
		System.out.println("Enqueuing thread is processing here!");
		try{
			reducerID.waitTillFinished();
			System.out.println("reducer result: " + reducerID.getReturnResult());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void printAllMaps(Map<String, Integer>[] maps){
		int counter = 1;
		for(Map<String, Integer> map : maps){
			System.out.println("For map number " + counter++);
			for(Entry<String, Integer> entry : map.entrySet()){
				System.out.print(entry.getKey() + ", " + entry.getValue() + "\n");
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void testParallelFuture(){
		ParaTask.init();
		ConcurrentLinkedDeque<String> myList = new ConcurrentLinkedDeque<>();
		myList.add("hello"); myList.add("forget"); myList.add("love"); myList.add("saint");
		TaskInfoNoArgs<Map<String, Integer>> multiTask = (TaskInfoNoArgs<Map<String,Integer>>) ParaTask.asTask(TaskType.MULTI, 
				(FunctorNoArgsWithReturn<Map<String, Integer>>)(()->{
					Map<String, Integer> result = new HashMap<>();
					for(String word : myList){
						result.putAll(wordToInt(word));
					}
					return result;
				}));
		TaskIDGroup<Map<String, Integer>> multiGroup = (TaskIDGroup<Map<String, Integer>>) multiTask.start();
		TaskInfoOneArg<Void, TaskIDGroup<Map<String, Integer>>> printTask = (TaskInfoOneArg<Void, TaskIDGroup<Map<String,Integer>>>) ParaTask.asTask(TaskType.ONEOFF,
				(FunctorOneArgNoReturn<TaskIDGroup<Map<String, Integer>>>)(taskIDGroup)->printAllMaps(taskIDGroup.getResultsAsArray(new HashMap[2])));
		ParaTask.registerDependences(printTask, multiGroup);
		ParaTask.registerSlotToNotify(printTask, ()->updateGui());
		TaskID<Void> printTaskID = printTask.start(multiGroup);
		System.out.println("Enqueuing thread is processing here!");
		try{
			printTaskID.waitTillFinished();
		}catch(Exception e){
			e.printStackTrace();
		}		
	}
}
