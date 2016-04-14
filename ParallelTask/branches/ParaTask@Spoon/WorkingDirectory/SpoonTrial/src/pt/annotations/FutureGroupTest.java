package pt.annotations;

public class FutureGroupTest {

	public static void main(String[] args) {
		
		int numb = 6;
		
		@FutureGroup()
		int[][] array = new int[numb][numb];
		
		for (int i = 0; i < array.length; i++){
			for(int j = 0; j < array[0].length; j++)
				array[i][j] = (i * j);
		}
		
		for (int j = 0; j < array.length; j++){
			for (int i = 0; i < array[0].length; i++)
				System.out.println("The element of the array is: " + array[j][i]);
		}
	}

}
