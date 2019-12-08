package ejercicio7;

public class MyTask extends Thread {
	
	
	private static final int t = 1000;
	private Counter c;
	
	public MyTask(Counter c) {
		this.c = c;
	}
	
	
	@Override
	public void run() {
		try {
			System.out.println(Thread.currentThread().getName());
			Thread.sleep((long)(Math.random() * t));
			c.increment();
		}
		catch(InterruptedException e) {
			
		}
	}
	
}
