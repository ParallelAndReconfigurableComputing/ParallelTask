package paratest;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * The TestIterator class is a subclass of Test.
 * 
 * It initialises a simple Iterator over an array of Bytes then 
 * runs through them in a sequential fashion.
 * 
 * @param Size - the number of elements in the array
 * 
 * @author Peter Nicolau
 */
public class TestIterator extends Test {

	private int fSize;
	private Iterator<Integer> fIterator;

	public TestIterator(int size) {
		fSize = size;
	}

	public void execute() {
		ArrayList<Integer> array = new ArrayList<Integer>();

		for (int i = 0; i < fSize; i++) {
			array.add(0);
		}

		fIterator = array.iterator();
		long start = System.nanoTime();

		process();

		long finish = System.nanoTime();
		System.out.println(fSize + "\t" + (finish - start));
	}

	public void process(){
		while (fIterator.hasNext()) {
			fIterator.next();
		}
	}

}
