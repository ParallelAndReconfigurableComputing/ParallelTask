package fieldFutureGroupWithReduction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import pu.RedLib.ListUnion;
import apt.annotations.Future;
import apt.annotations.ReductionMethod;

public class FieldFutureGroupClass {
	@Future(reduction="fieldFutureGroupWithReduction.MapReduction(union)")
	Map<Integer, List<String>>[] maps;
	int reps = 2;
	int size = 0;
	public FieldFutureGroupClass(int size) {
		this.size = size;
		maps = new HashMap[reps * size];
	}
	
	public Map<Integer, List<String>> task(int i){
		Map<Integer, List<String>> result = new HashMap<>();
		List<String> list = new ArrayList<>();
		Random rand = new Random();
		int randNo = rand.nextInt(1+i);
		try{
			Thread.sleep(randNo * 1000);
		}catch(Exception e){
			e.printStackTrace();
		}
		String str = "This is random No. " + randNo + " for number " + i + ", by thread: " + Thread.currentThread().getId();
		list.add(str);
		result.put(i, list);
		return result;
	}
	
	@ReductionMethod()
	public Map<Integer, List<String>> reduce(){
		Map<Integer, List<String>> result = new HashMap<>();
		MapReduction reduction = new MapReduction(new ListUnion<>());
		int length = maps.length;
		result = maps[0];
		for(int i = 1; i <length-1; i++){
			result = reduction.reduce(result, maps[i]);
		}
		result = reduction.reduce(result, maps[length-1]);
		return result;
	}
	
	public void startTasks(){
		int index = 0;
		for(int i = 0; i < reps; i++){
			for(int j = 0; j < size; j++){
				maps[index++] = task(j); 
			}
		}
		
		Map<Integer, List<String>> finalResult = new HashMap<>();
		finalResult = reduce();
		for(Entry<Integer, List<String>> entry : finalResult.entrySet()){
			Integer key = entry.getKey();
			List<String> value = entry.getValue();
			System.out.println("For key: " + key + " --> ");
			System.out.println(value);
		}		
	}	
}
