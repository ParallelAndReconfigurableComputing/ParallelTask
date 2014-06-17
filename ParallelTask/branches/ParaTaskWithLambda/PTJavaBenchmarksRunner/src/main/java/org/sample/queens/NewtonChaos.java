package org.sample.queens;
/*************************************************************************
 *  Compilation:  javac NewtonChaos.java
 *  Execution:    java NewtonChaos N
 *  Dependencies: Picture.java Complex.java
 *  
 *  Plot an N-by-N grid of points showing which of the four roots 
 *  that Newton's method converges to in the 2-by-2 box centered at (0, 0).
 *
 *************************************************************************/

import java.awt.Color;

public class NewtonChaos {
	
	public Picture newtonPartial(int N) {
		double xmin   = -1.0;
        double ymin   = -1.0;
        double width  =  2.0;
        double height =  2.0;

        Picture pic = new Picture(N, N);

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                double x = xmin + i * width  / N;
                double y = ymin + j * height / N;
                Complex z = new Complex(x, y);
                Color color = newton(z);
                pic.set(i, j, color);
            }
        }
        return pic;
	}

    // return number of Mandelbrot iterations to check z = x + iy
    static Color newton(Complex z) {
        double EPSILON = 0.00000001;
        Complex four = new Complex(4, 0);
        Complex one  = new Complex(1, 0);

        Complex root1 = new Complex( 1,  0);
        Complex root2 = new Complex(-1,  0);
        Complex root3 = new Complex( 0,  1);
        Complex root4 = new Complex( 0, -1);

        for (int i = 0; i < 100; i++) {
            Complex f  = z.times(z).times(z).times(z).minus(one);
            Complex fp = four.times(z).times(z).times(z);
            z = z.minus(f.divides(fp));
            if (z.minus(root1).abs() <= EPSILON) return Color.WHITE;
            if (z.minus(root2).abs() <= EPSILON) return Color.RED;
            if (z.minus(root3).abs() <= EPSILON) return Color.GREEN;
            if (z.minus(root4).abs() <= EPSILON) return Color.BLUE;
        }  
        return Color.BLACK;
    }


    public static void main(String[] args) {
    	int N = 0;
    	
    	if(args.length == 0)
    		N = 64;
    	else
    		N = Integer.parseInt(args[0]);
        Picture pic = new NewtonChaos().newtonPartial(N);
        pic.show();
    }

}
