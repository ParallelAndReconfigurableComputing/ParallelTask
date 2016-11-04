package paratest;

/**
 * The TestArray class is a subclass of Test.
 * 
 * It initialises a simple Array of Bytes then runs through 
 * incrementing them by one in a sequential fashion.
 * 
 * @param Size - the number of elements in the array
 * @param Stride - the number of elements between processing
 * 
 * @author Peter Nicolau
 */
public class TestArray extends Test {

	private int fSize;
	private int fStride;
	private int[] fArray;

	public TestArray(int size, int stride) {
		fSize = size;
		fStride = stride;
	}

	public void execute() {
		fArray = new int[fSize];
		long start = System.nanoTime();

		process();

		long finish = System.nanoTime();
		System.out.println(fSize + "\t" + fStride + "\t" + (finish - start));
	}

	public void process(){
		for (int i = 0; i < fSize; i += fStride) {
			fArray[i]++;
		}
	}

}
