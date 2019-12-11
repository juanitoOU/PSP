package controlador;

import modelo.ClassB;
import modelo.ClassA;

public class Main {

	public static void main(String[] args) {

		ClassA a = new ClassA();

		
		ClassB b1 = new ClassB(a);
		ClassB b2 = new ClassB(a);
		ClassB b3 = new ClassB(a);

		b1.setNext(b2);
		b2.setNext(b3);
		b3.setNext(b1);
	

		Thread t1 = new Thread(b1);
		Thread t2 = new Thread(b2);
		Thread t3 = new Thread(b3);

		t1.start();
		t2.start();
		t3.start();

		
		try {
		Thread.sleep(500);
		} catch(InterruptedException e){
			e.printStackTrace();
		}
		
		

		synchronized (b1) {
			b1.notify();
		}
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
			
			

		try {
			t1.join();
			t2.join();
			t3.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		


		
		System.out.println(a.getThreadIds().toString());
	}

}
