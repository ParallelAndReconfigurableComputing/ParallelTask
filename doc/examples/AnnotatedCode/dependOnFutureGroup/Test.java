package dependOnFutureGroup;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;

import apt.annotations.Future;
import apt.annotations.Gui;
import apt.annotations.InitParaTask;
import apt.annotations.TaskInfoType;
import pu.RedLib.Reducer.OPERATION;
import pu.RedLib.Reducer;

public class Test {
	private int IOTask(int num) throws InterruptedException{
		int randNo = 0;
		Random rand = new Random();
		if(num == 0)
			randNo = 1;
		else
			randNo = 6*rand.nextInt(num);
		
		System.out.println((num+1) + "- thread " + Thread.currentThread().getId() + 
				" is going to sleep for " + randNo + " seconds.");
		Thread.sleep(randNo*1000);
		System.out.println((num+1) + "-> thread " + Thread.currentThread().getId() + 
				" slept for " + randNo + " seconds");
		return randNo;
	}
	
	private Map<String, Integer> wordCountTask(ConcurrentLinkedDeque<String> wordList){
		Map<String, Integer> result = new HashMap<>();
		for(String word : wordList){
			result.putAll(wordToInt(word));
		}
		return result;
	}
	
	private Map<String, Integer> wordToInt(String word){
		int multiplier = (int) Thread.currentThread().getId();
		int wordLetters = word.length()*multiplier;
		word = word + ", " + multiplier;
		Map<String, Integer> wordLength = new HashMap<>();
		wordLength.put(word, wordLetters);
		return wordLength;
	}
	
	private Void printAllMaps(Map<String, Integer>[] maps){
		int counter = 1;
		for(Map<String, Integer> map : maps){
			System.out.println("For map number " + counter++);
			for(Entry<String, Integer> entry : map.entrySet()){
				System.out.println(entry.getKey() + ", " + entry.getValue() + "\n");
			}
		}
		return null;
	}
	
	private Void updateGui(){
		System.out.println("GUI updated by thread " + Thread.currentThread().getId());
		return null;
	}
	
	private Void updateDB(){
		System.out.println("DataBase updated by thread " + Thread.currentThread().getId());
		return null;
	}
	
	private Void reportResult(int sum){
		System.out.println("Sum of the results is: " + sum);
		return null;
	}
	
	public static void main(String[] args){
		Test tester = new Test();
		tester.testFutureGroup(10);
		tester.testParallelFuture();
	}
	
	@InitParaTask
	public void testFutureGroup(int num){
		@Future
		int[] group = new int[num];
		@Gui
		Void handler1 = updateGui();
		@Gui
		Void handler2 = updateDB();
		try{
			for(int i = 0; i < num; i++){
				group[i] = IOTask(i);
			}
			@Future
			int reduce = pu.RedLib.Reducer.reduce(group, OPERATION.SUM);
			@Gui(notifiedBy="reduce")
			Void handler = reportResult(reduce);
			System.out.println("Enqueueing thread is processing here!");
			System.out.println("result of reduce is: " + reduce);
		}catch(NullPointerException | InterruptedException e){
			e.printStackTrace();
		}catch(ArrayStoreException e1){
			System.out.println("ArrayStore Exception");
		}catch(Error | RuntimeException throwable){
			System.err.println("Something went wrong with " + throwable.toString());
		}finally{
			System.out.println("finished");
		}
	}
	
	@InitParaTask
	public void testParallelFuture(){
		ConcurrentLinkedDeque<String> myList = new ConcurrentLinkedDeque<>();
		myList.add("hello"); myList.add("forget"); myList.add("love"); myList.add("saint");
		
		@Future(taskType=TaskInfoType.MULTI, reduction="union(sum)")
		Map<String, Integer> parallelFuture = wordCountTask(myList);
		
		int counter = 0;
		@Future
		Map<String, Integer>[] wordMaps = new HashMap[myList.size()];
		for(String word : myList)
			wordMaps[counter++] = wordToInt(word);
		@Future
		Void printResults = printAllMaps(wordMaps);
		@Gui(notifiedBy="printResults")
		Void handler1 = updateDB();
		@Gui(notifiedBy="printResults")
		Void handler2 = updateGui();
		
		Map<String, Integer> map = wordMaps[0];
	}
}
