package jCliente;

import java.net.*;
import java.io.*;
import java.awt.*;

/**
 * Esta clase actúa como cliente para el chat. Recibe los mensajes de otros clientes y los muestra
 * por consola (la Entrada/Salida estándar).
 *
 * El programa puede ejecutarse sin argumentos (se considera que el programa servidor
 * ServidorChat.java se ejecuta en la máquina local y por el número de puerto 9000) o con dos
 * argumentos (nombre del ordenador servidor y número de puerto). Si no se introducen dos
 * argumentos, se considera que se está en el primer caso.
 * 
 * Ejemplos de uso: java ClienteChat; java ClienteChat www.mimaquina.com 1200
 *
 *
 */
public class ClienteChat {
 private static final int PUERTO = 9000; // puerto por omisión

 private Socket socketCliente; // socket para enlazar con el socket de servidor

 /** Punto de entrada a la aplicación. En este método se arranca el cliente
 * de chat y se comienza el procesado de los mensajes de los clientes.
 *
 * @param args nombre de la máquina donde se ejecuta el servidor y puerto
 * por el que escucha.
 */
 public static void main(String args[]) {
 new ClienteChat(args);
 }

 /** Constructor
 *
 * @param args nombre de la máquina donde se ejecuta el servidor y puerto
 * por el que escucha.
 */
 public ClienteChat(String args[]) {
 System.out.println("Arrancando el cliente.");

 arrancarCliente(args);
 procesarMensajes();
 } 
  /** Arranca el cliente: lo intenta conectar al servidor por el núm. de puerto PUERTO o por
 * el especificado como argumento.
 *
 * @param args nombre de la máquina donde se ejecuta el servidor y puerto
 * por el que escucha.
 */
private void arrancarCliente(String[] args) {
    // Si se introduce como argumento un nombre de máquina y un número de puerto, se
    // considera que el servidor está ubicado allí y que escucha por ese número de puerto; en caso
    // contrario, se considera que está en la máquina local (localhost) y que escucha por el
    // puerto PUERTO.
   
    // Cualquier excepción en la creación del socket es fatal para el programa. Para dar la
    // mayor información posible al usuario se tratan los cuatro tipos posibles de
    // excepciones:
    // 1) Excepción porque no se ha introducido un número puerto válido (es decir, entero).
    // 2) Excepción porque no se encuentra ningún ordenador con el nombre introducido por el
    // usuario.
    // 3) Excepción debida a restricciones de seguridad en el ordenador servidor.
    // 4) Excepción general de Entrada/Salida.
    try {
    if (args.length == 2)
    socketCliente = new Socket (args[0], Integer.parseInt(args[1]));
    else
    socketCliente = new Socket("localhost", PUERTO); // puerto del servidor por omisión
   
    System.out.println("Arrancado el cliente.");
    }
    catch (java.lang.NumberFormatException e1) {
    // No se puede arrancar el cliente porque se introdujo un número de puerto que no es entero.
    // Error irrecuperable: se sale del programa. No hace falta limpiar el socket, pues no llegó a
    // crearse.
    errorFatal(e1, "Número de puerto inválido.");
    }
    catch (java.net.UnknownHostException e2) {
    // No se puede arrancar el cliente. Error irrecuperable: se sale del programa.
    // No hace falta limpiar el socket, pues no llegó a crearse.
    errorFatal(e2, "No se localiza el ordenador servidor con ese nombre.");
    }
    catch (java.lang.SecurityException e3) {
    // No se puede arrancar el cliente. Error irrecuperable: se sale del programa.
    // No hace falta limpiar el socket, pues no llegó a crearse.
    String mensaje ="Hay restricciones de seguridad en el servidor para conectarse por el " +
    "puerto " + PUERTO;
    errorFatal(e3, mensaje);
    }
    catch (IOException e4) {
    // No se puede arrancar el cliente. Error irrecuperable: se sale del programa.
    // No hace falta limpiar el socket, pues no llegó a crearse.
    String mensaje = "No se puede conectar con el puerto " + PUERTO + " de la máquina " +
    "servidora. Asegúrese de que el servidor está en marcha.";
    errorFatal(e4, mensaje);
    }
    } 
    /** Crea los flujos de entrada y salida asociados al socket que ha llevado a cabo con éxito su
 * conexión al servidor y asocia un hilo ThreadCliente al flujo de entrada del socket. Así
 * el usuario podrá a la vez escribir y recibir mensajes.
 */
 private void procesarMensajes() {

    // flujos de Entrada/Salida
    BufferedReader entrada=null;
    PrintWriter salida=null;
   
    // Se crean los flujos de E/S: dos para el socket y uno para la entrada por consola.
    try {
    entrada= new BufferedReader(new
    InputStreamReader(socketCliente.getInputStream()));
    salida = new PrintWriter(socketCliente.getOutputStream(), true);
    BufferedReader entradaConsola = new BufferedReader(new
    InputStreamReader(System.in));
   
    // Se crea un hilo que se encarga de recibir y mostrar por consola los mensajes del
    // servidor (procedentes de otros clientes).
    new ThreadCliente(entrada);
   
    // Se entra en un bucle infinito para leer la entrada del usuario por la consola y enviarla al
    // servidor de chat.
    while (true)
    salida.println(entradaConsola.readLine());
    }
    catch (IOException e) {
    e.printStackTrace();
   
    // Limpieza del socket y de los flujos asociados para que el cierre sea "limpio".
    if ( entrada != null) {
    try {
    entrada.close();
    }
    catch (Exception e1) {
    entrada = null;
    }
    }
    if ( salida != null) {
    try {
    salida.close();
    }
    catch (Exception e1) {
    salida = null;
    }
    }
    if ( socketCliente != null) {
    try {
    socketCliente.close();
    }
    catch (Exception e1) {
    socketCliente = null;
    }
    }
    // Aviso al usuario y cierre.
 String mensaje = "Se ha perdido la comunicación con el servidor. Seguramente se debe a "+
 " que se ha cerrado el servidor o a errores de transmisión";
 errorFatal(e, mensaje);
 }
 }
 /**
 * Informa al usuario de la excepción arrojada y sale de la aplicación.
 *
 * @param excepcion excepción cuya información se va a mostrar.
 * @param mensajeError mensaje orientativo sobre la excepción .
 */
private static void errorFatal(Exception excepcion, String mensajeError) {
    excepcion.printStackTrace();
   System.out.println("Error fatal."+ System.getProperty("line.separator") + mensajeError);
    System.exit(-1);
    }
   
   } 
