package ejecucion;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
	
	public static final String RUTA = "D:\\eclipse\\workspace\\PruebaBatchJava\\batch.bat";
	
	
		 public static void main(String[] args) throws IOException {
		        leeFichero(RUTA);
		    }
		 public static void leeFichero(String RUTA) throws IOException{
			
			 ProcessBuilder p = new ProcessBuilder(RUTA,"Juan");
			 final Process command = p.start();
			
			 String cadena;
		      BufferedReader b = new BufferedReader(new InputStreamReader(command.getInputStream()));
		      while((cadena = b.readLine())!=null) {
		          System.out.println(cadena);
		      }
		      b.close();
			}
		
}
