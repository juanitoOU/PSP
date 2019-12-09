package modelo;

public class ClassB implements Runnable {
	private ClassA a;
	Thread hebra = Thread.currentThread();

	public ClassB(ClassA a) {
		this.a = a;
	}

	@Override
	public void run() {
		a.EnterAndWait(hebra);
		
		synchronized (hebra) {
			hebra.notifyAll();
		}
	}



}




//	@Override
//	public void run() {
//		Thread hebra = Thread.currentThread();
//		boolean sigue = true;
//		for (int i = 0; i < 100 && sigue; i++) {
//			try {
//				a.EnterAndWait(hebra);
//				Thread.sleep(2000);
//			} catch (InterruptedException e) {
//				System.out.println(hebra.getName() + " interrumpida ");
//				sigue = false;
//			}
//		}
//	}
