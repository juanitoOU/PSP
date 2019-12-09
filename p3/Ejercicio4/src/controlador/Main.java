package controlador;

import modelo.ClassB;
import modelo.ClassA;

public class Main {

	public static void main(String[] args) {

		ClassA a = new ClassA();
		
		a.counter=20;
		
		
		for(int i=0; i < 20; i++) {
			Thread b = new Thread(new ClassB(a));
			b.setName("hilo" +i);

			b.start();
			synchronized (b) {
				try {
					System.out.println("Waiting for b to complete...");
					b.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			try {
				b.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
		
		
		

//		Thread b1 = new Thread(new ClassB(a));
//		b1.setName("hilo1");
//
//		b1.start();
//		synchronized (b1) {
//			try {
//				System.out.println("Waiting for b1 to complete...");
//				b1.wait();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//
//		Thread b2 = new Thread(new ClassB(a));
//		b2.setName("hilo2");
//		b2.start();
//		synchronized (b2) {
//			try {
//				System.out.println("Waiting for b2 to complete...");
//				b2.wait();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//
//		Thread b3 = new Thread(new ClassB(a));
//		b3.setName("hilo3");
//		b3.start();
//		synchronized (b3) {
//			try {
//				System.out.println("Waiting for b3 to complete...");
//				b3.wait();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}

//		try {
//			b1.join();
//			b2.join();
//			b3.join();
//		} catch (InterruptedException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}

	}

}
