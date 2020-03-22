package jServidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;



/**
 * Esta clase actúa como servidor para el chat. El puerto que se usa para escuchar peticiones de
 * los clientes es PUERTO. Asigna un hilo a cada cliente que se conecta.
 * Cada hilo hace lo siguiente:
 * <p> 1) espera datos de entrada; </p>
 * <p> 2) cuando los recibe, los envía a todos los clientes conectados (excepto a él mismo); </p>
 * <p> 3) vuelve a esperar datos. </p>
 *
 * Los clientes pueden enviar el texto DESCONECTAR para abandonar el chat, y pueden saber
 * los alias de los clientes conectados enviando LISTAR.
 * Si el servidor no recibe ningún mensaje de un cliente en
 * TIEMPO_DESCONEXION_AUTOMATICA milisegundos, lo desconectará automáticamente.
 */

public class ServidorChat {

    private static final int PUERTO= 9000;

    // Tiempo de desconexión automática si el cliente no tiene actividad (600000=10 minutos).
    protected static final int TIEMPO_DESCONEXION_AUTOMATICA = 600000;
    private ServerSocket socketServidor; // socket de servidor (pasivo)



  /** Punto de entrada a la aplicación. En este método se arranca el servidor
 * de chat y se comienza el procesado de las conexiones y mensajes de los clientes.
 *
 * @param args
 */
 public static void main(String[] args) {
    new ServidorChat();
    } 



/** Constructor
 */
public ServidorChat() {
    System.out.println("Arrancando el servidor por el puerto " + PUERTO);
    arrancarServidor();
    procesarClientes(); 
}

 /**
 * Arranca el servidor: crea el ServerSocket y lo vincula al número de puerto
 * PUERTO.
 */
private void arrancarServidor() {
    // Se intenta crear un socket de servidor en el número de puerto PUERTO.
    // Cualquier excepción en la creación del socket de servidor es fatal para el programa. Para dar
    // la mayor información posible al usuario se tratan los tres tipos posibles de excepciones:
    // 1) Excepción al intentar ligar el socket de servidor al número de puerto PUERTO.
    // 2) Excepción debida a restricciones de seguridad en el ordenador servidor.
    // 3) Excepción general de Entrada/Salida.
    try {
    socketServidor = new ServerSocket(PUERTO);
    System.out.println("El servidor está en marcha: escucha por el puerto " + PUERTO);
    }
    catch (java.net.BindException e1) {
    // No se puede arrancar el servidor. Error irrecuperable: se sale del programa.
    // No se limpia el socket de servidor porque no ha llegado a crearse.
    String mensaje = "No puede arrancarse el servidor por el puerto " + PUERTO +
    ". Seguramente, el puerto está ocupado.";
    errorFatal(e1, mensaje);
    }
    catch (java.lang.SecurityException e2) {
    // No se puede arrancar el servidor. Error irrecuperable: se sale del programa.
    // No se limpia el socket de servidor porque no ha llegado a crearse.
    String mensaje = "No puede arrancarse el servidor por el puerto " + PUERTO +
    ". Seguramente, hay restricciones de seguridad.";
    errorFatal(e2, mensaje);
    }
    catch (IOException e3) {
    // No se puede arrancar el servidor. Error irrecuperable: se sale del programa.
    // No se limpia el socket de servidor porque no ha llegado a crearse.
    String mensaje = "No puede arrancarse el servidor por el puerto " + PUERTO;
    errorFatal(e3, mensaje);
    } // fin del try-catch
    }
    
    /** Procesa las conexiones entrantes de los clientes. A cada cliente que se conecta le
 * asigna un hilo ThreadServidor, que se encargará de gestionar el envío y la recepción
 * de mensajes.
 */
 private void procesarClientes() {
    Socket socketCliente = null; // socket (activo) para el cliente que se conecta
    // Se entra en un bucle infinito, que sólo se romperá si se producen excepciones
    // debidas a restricciones de seguridad en el ordenador servidor.
    while (true) {
    try {
    socketCliente = socketServidor.accept(); // Se asigna un socket a cada petición entrante.
    try {
    new ThreadServidor(socketCliente); // Se crea un hilo para cada conexión entrante.
}
catch (IOException e1) {
// Excepción en el constructor de ThreadServidor: seguramente se debe a que el
// cliente cerró la comunicación nada más hecha la conexión con el servidor.
// Se intenta cerrar el socket de cliente.
// Si no se puede cerrrar, no se hace nada. Posiblemente, el socket fue cerrado
// por el cliente. Se volverán a esperar conexiones.
if (socketCliente != null) {
try {
socketCliente.close();
}
catch (IOException e2) {} // No se hace nada.
}
}
} // fin del try-catch interno
catch (java.lang.SecurityException e3) {
// Excepción debida a restricciones de seguridad. Error irrecuperable: se sale del
// programa. Si el socket de servidor no es nulo, se intenta cerrarlo antes de salir.

if (socketServidor != null) {
try {
socketServidor.close();
}
catch (IOException e4) {} // No se hace nada
}

String mensaje = "Con su configuración de seguridad, los clientes no pueden " +
"conectarse por el puerto " + PUERTO;
errorFatal(e3, mensaje);
}
catch (IOException e5) {
// No se hace nada: el socket de cliente no llegó a crearse en accept(). Se volverán a
// esperar conexiones.
} // fin del try-catch externo
} // fin del while
} 

 /**
 * Informa al usuario de la excepción arrojada y sale de la aplicación.
 *
 * @param excepcion excepción cuya información se va a mostrar.
 * @param mensajeError mensaje orientativo sobre la excepción.
 */
private static void errorFatal(Exception excepcion, String mensajeError) {
    excepcion.printStackTrace();
    System.out.println("Error fatal."+ System.getProperty("line.separator") +
    mensajeError);
    System.exit(-1);
    } 
}