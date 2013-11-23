package pt.test;

import java.util.Date;

public class _TestNestedTask {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		new _TestNestedTask().task_1();

	}

	private void task_1() {
		System.out.println("start running task 1" + new Date());

		try {
			Thread.sleep(1000 * 5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		task_2();

		System.out.println("end running task 1" + new Date());

	}

	private void task_2() {
		System.out.println("start running task 2" + new Date());

		try {
			Thread.sleep(1000 * 5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		task_3();

		System.out.println("end running task 2" + new Date());
	}

	private void task_3() {
		System.out.println("start running task 3" + new Date());

		try {
			Thread.sleep(1000 * 5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("end running task 3" + new Date());
	}

}
