package controlador;

import modelo.ClassB;
import modelo.ClassA;

public class Main {


	public static void main(String[] args) {

		ClassA a = new ClassA();

		Thread b1 = new Thread(new ClassB(a));
		b1.setName("hilo1");
		b1.start();
		
		Thread b2 = new Thread(new ClassB(a));
		b2.setName("hilo2");
		b2.start();
		
		Thread b3 = new Thread(new ClassB(a));
		b3.setName("hilo3");
		b3.start();
		
		try {
			b1.join();
			b2.join();
			b3.join();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		
	}

}
