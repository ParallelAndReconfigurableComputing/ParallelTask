package sp.annotations;

import java.util.*;

public class FutureGroupTest {
	
	class TestFuture{
			
			int numb = 6;
			private void myMethod(){
				
				@FutureGroup()
				int[][] array = new int[numb][numb];
				
				for (int i = 0; i < array.length; i++)
					for(int j = 0; j < array[0].length; j++)
						array[i][j] = (i * j);
				
				
				for(int i = 0; i < numb; i++)
					array[0][i] = i*10;
				
				for (int j = 0; j < numb; j++){
					//int length = array[j].length;
					for (int i = 0; i < array[j].length; i++)
						System.out.println("The element of the array is: " + array[j][i]);
				}
			}
		}
		
	
	
	
	public static void main(String[] args) {
		
		TestFuture tf = new TestFuture();
		tf.myMethod();
		
	}

}
