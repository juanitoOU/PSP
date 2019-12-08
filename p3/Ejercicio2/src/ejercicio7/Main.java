package ejercicio7;

public class Main {

	public static void main(String[] args) {
		
		
		int t = 1000;
		Counter c = new Counter();
		
		MyTask[] hebras = new MyTask[t];
		
		for (int i = 0 ; i<t ; i++) {
			hebras[i] = new MyTask(c);
			hebras[i].start();
		}
		
		
		for (int i=0 ; i<t ; i++) {
			
			try {
				hebras[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println(c.getContador());
	}
		
	}

