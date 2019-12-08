package modelo;

public class ClassB implements Runnable {
	private ClassA a;


	public ClassB(ClassA a) {
		 this.a = a;
	}

	@Override
	public void run() {
		Thread hebra = Thread.currentThread();
		boolean sigue = true;
		synchronized(a)
        {
			
			try {
				a.EnterAndWait(hebra);
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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

