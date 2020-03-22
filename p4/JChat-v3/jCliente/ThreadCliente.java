package jCliente;

import java.net.*;
import java.io.*;
import java.awt.*;

class ThreadCliente extends Thread {

    private BufferedReader entrada;
   
    /**
    * Constructor.
    *
    * @param entrada BufferedReader bufferedReader con la entrada que llega al socket de
    * ClienteChat
    * @throws IOException
    */
    public ThreadCliente (BufferedReader entrada) throws IOException {
    this.entrada=entrada;
   
    start(); // Se arranca el hilo.
    }
    /**
    * Muestra por consola los mensajes enviados por el servidor.
    */
    public void run() {
    // Última línea del aviso de desconexión por falta de actividad.
    String fin1 = "> *****************ADIOS*****************";
   
    // Última línea del aviso de desconexión por uso de la orden DESCONECTAR.
    String fin2 = "> ***********HASTA LA VISTA****************";
   
    String linea = null; 
    try {
        while( ( linea=entrada.readLine() ) != null ) {
        System.out.println(linea);
        // Si se produce una desconexión por que se ha terminado el tiempo permitido de
        // inactividad o porque se ha dado previamente la orden DESCONECTAR, se sale del
        // bucle.
        if ( linea.equals(fin1) || linea.equals(fin2) )
        break;
        }
        }
        catch (IOException e1) {
        e1.printStackTrace();
        }
        finally {
        if (entrada !=null) {
        try {
        entrada.close();
        }
        catch (IOException e2) {} // No se hace nada con la excepción
        }
       
        System.exit(-1);
        }
        } // fin try-catch-finally
       
       } 