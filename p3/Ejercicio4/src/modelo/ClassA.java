package modelo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class ClassA {

	public int counter ;
	ArrayList<Long> threadIds;
	
	public ClassA(){
	
	
	}
	
	
	public synchronized void  EnterAndWait (Thread hebra){
		
		ArrayList<Long> threadIds = new ArrayList<>();
		
		System.out.println("Iniciando " + hebra.getName());
		System.out.println("Ejecutando " + hebra.getName());
		System.out.println("Finalizando " + hebra.getName());
		long id = Thread.currentThread().getId();
		threadIds.add(id);
		System.out.println(counter);
		counter--;
		
	}
	
	public boolean isFinished() {
		boolean	toret = false;
		
		if(counter==0) {
			toret = true;
		}
		
		return toret;
		
		
	}
	
	public ArrayList<Long> registro(long id){
		
		return threadIds;
		
	}
	
}
