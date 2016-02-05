package spoon.examples.nton.src;

import spoon.examples.nton.annotation.Nton;

@Nton(n = 5)
public class C {

	public static void main(String[] args) {
		try {
			System.out.print(".");
			new C();
			System.out.print(".");
			new C();
			System.out.print(".");
			new C();
			System.out.print(".");
			new C();
			System.out.print(".");
			new C();
			System.out.print(".");
			new C();
		} catch (RuntimeException e) {
			System.out.flush();
			System.err.println(e.getMessage());
		}
	}

}