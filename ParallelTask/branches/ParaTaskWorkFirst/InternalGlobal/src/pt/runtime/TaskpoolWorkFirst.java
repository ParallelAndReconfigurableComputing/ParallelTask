package pt.runtime;

public interface TaskpoolWorkFirst {
	
	/*
	 * 	Methods to set and get upper bound thresholds for 
	 * 	Work-First
	 */
	public void setUpperBoundThreshold(int threshold);
	
	public int getUpperBoundThreshold();
	
	/*
	 * 	Methods to get and set lower bound thresholds for
	 * 	Work-First
	 */
	
	public void setLowerBoundThreshold(int threshold);
	
	public int getLowerBoundThreshold();
}
