package ejercicio7;

import java.util.concurrent.atomic.AtomicInteger;

public class Counter {
	

	private AtomicInteger contador;
	
	
	public Counter() {
		this.contador = new AtomicInteger(0);
	}
	
	public void increment() {
		
		contador.getAndIncrement();
		
	}

	public int getContador() {
		
		return contador.get();
		
	}
	
	
	
	
	
	
}
