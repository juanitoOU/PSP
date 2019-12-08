package ejercicio7;

public class Counter {

	private int contador;
	
	
	public Counter() {
		this.contador = 0;
	}
	
	

	public int getContador() {
		
		return contador;
		
	}
	
	
	// El poner synchronized hace que antes de entrar al bloque sincronizado los hilos
		// pregunten si algún otro hilo que este usando ese bloque sincronizado, si es asi,
		// esperara para ejecutarlo.
	
	public synchronized void increment() {
		
		this.contador++;
		
	}
	
	
}
