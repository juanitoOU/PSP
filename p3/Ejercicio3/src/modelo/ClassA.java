package modelo;

public class ClassA {

	
	public ClassA(){
		
	
		
	}
	
	
	public synchronized void  EnterAndWait (Thread hebra){
		
		System.out.println("Iniciando " + hebra.getName());
		System.out.println("Ejecutando " + hebra.getName());
		System.out.println("Finalizando " + hebra.getName());
	}
	
	
}
