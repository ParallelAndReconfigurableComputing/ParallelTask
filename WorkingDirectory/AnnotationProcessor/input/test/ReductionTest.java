package code;

import sp.annotations.Future;

public class ReductionTest {
	
	@Future
	int[] myArray;

	public ReductionTest(int size){
		myArray = new int[size];
	}

}
