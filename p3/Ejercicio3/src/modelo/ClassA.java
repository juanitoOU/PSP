package modelo;

public class ClassA {

	
	public ClassA(){
		
	
		
	}
	
	
	public void EnterAndWait (Thread hebra){
		
		System.out.println("Iniciando " + hebra.getName());
		System.out.println("Ejecutando " + hebra.getName());
		System.out.println("Finalizando " + hebra.getName());
	}
	
	
}
