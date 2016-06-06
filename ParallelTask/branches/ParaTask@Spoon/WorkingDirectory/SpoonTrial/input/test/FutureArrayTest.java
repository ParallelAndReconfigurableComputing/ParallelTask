

import sp.annotations.Future;

public class FutureArrayTest {
	
	public static void foo(int i){
		System.out.println("number is: " + i);
	}
	
	public static void main(String[] args) {
		int n = 10;
		@Future
		int[] array = new int[n];
		for (int i = 0; i < 5; i++){
			array [i] = i * 10;
		}
		array[5] = 12;
		array[6] = 3;
		for(int i = 7; i < 10; i++){
			array[i] = i * 15;
		}
		
		int[] myarray = new int[2];
		myarray[0] = 1;
		
		System.out.println(array.length);
		
		for (int i = 0; i < array.length; i++){
			System.out.print(array[i]);
			if(i != array.length-1)
				System.out.print(", ");
		}	
		
		for (int i = 0; i < array.length; i++){
			foo(array[i]);
		}
	}
}
