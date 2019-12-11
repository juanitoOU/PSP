package modelo;

public class ClassB implements Runnable {
	private ClassA a;
	public ClassB next;
	boolean sigue=true;
	

	public ClassB(ClassA a) {
		this.a = a;
	}

	public ClassB getNext() {
		return next;
	}

	public void setNext(ClassB next) {
		this.next = next;
	}

	@Override
	public void run() {
		while(!a.isFinished()) {
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				
				a.EnterAndWait();
		
		synchronized (next) {
			next.notify();
		}
		
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
