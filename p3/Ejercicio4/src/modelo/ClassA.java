package modelo;

import java.util.HashSet;
import java.util.Set;

public class ClassA {

	public int counter ;
	 Set<Long> ThreadIds = new HashSet<Long>();
	
	
	public ClassA(){
	
		this.counter = 20;
	}
	
	
	public synchronized void  EnterAndWait (){
		if(isFinished()!= true) {
			
		System.out.println("Iniciando " + Thread.currentThread().getId());
		System.out.println("Ejecutando " + Thread.currentThread().getName());
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Finalizando " + Thread.currentThread().getName());
		//threadIds.add(Thread.currentThread().getId());
		this.ThreadIds.add(Thread.currentThread().getId());
		this.counter--;
		System.out.println(counter);
		}
	}
	
	public boolean isFinished() {
		
		if(this.counter==0) {
			return true;
		}
		else {
			return false;
		}
	
		
		
	}


	public int getCounter() {
		return counter;
	}


	public void setCounter(int counter) {
		this.counter = counter;
	}


	public Set<Long> getThreadIds() {
		return this.ThreadIds;
	}


	public void setThreadIds(Set<Long> threadIds) {
		ThreadIds = threadIds;
	}




	

	
	
	
	
//	public void getRegistro() {
//		for(int i = 0;i <= threadIds.size()-1; i++) {
//			System.out.println(threadIds.get(i).toString());
//		}
//	}
	
}
