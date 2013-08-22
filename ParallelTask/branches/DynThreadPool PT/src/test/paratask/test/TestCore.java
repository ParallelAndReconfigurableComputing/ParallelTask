/**
 * 
 */
package paratask.test;

import core.moldyn.Molcore;
import core.montecarlo.Moncore;
import core.raytracer.Raycore;

/**
 * @author hp
 *
 */
public class TestCore {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Molcore molcore = new Molcore();
		molcore.execute(1);
		
		/*Moncore moncore = new Moncore();
		moncore.execute(1);*/
		
		/*Raycore raycore = new Raycore();
		raycore.execute(1);*/

	}

}
