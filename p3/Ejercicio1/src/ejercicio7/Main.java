package ejercicio7;

public class Main {

	public static void main(String[] args) {
		int t = 1000;
		Counter c = new Counter();
		MyTask[] hilos = new MyTask[t];
		
		for (int i = 0 ; i<t ; i++) {
			hilos[i] = new MyTask(c);
			hilos[i].start();
		}
		
		
		for (int i=0 ; i<t ; i++) {
			
			try {
				hilos[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println(c.getContador());
	}
		
	}

