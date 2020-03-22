package jServidor;

import java.io.*;
import java.util.*;
import java.net.*; 

public class ThreadServidor extends Thread {

    private String nombreCliente; // alias para el usuario
    private long id;
    // El String historial da el camino del archivo donde se guardaran las conexiones y
    // desconexiones.
    // En este caso, la estructura de directorios corresponde a un sistema Windows.
    private static String historial ="."+File.separatorChar +"historial.txt";
   
    // Lista de clientes activos. ArrayList es una colección NO sincronizada.
    private static List clientesActivos = new ArrayList(); // contiene objetos ThreadServidor
   
    private Socket socket;
   
    // flujos de E/S
    private BufferedReader entrada;
    private PrintWriter salida; 

    /**
 * Constructor. Este método crea un hilo a partir de un socket
 * pasado por la clase ServidorChat. Cualquier error al tratar
 * de obtener los flujos de E/S asociados al socket provocará
 * una excepción.
 *
 * @param socket socket para el cliente
 * @throws IOException
 */
 public ThreadServidor(Socket socket) throws IOException {
    this.socket = socket; 
    PrintWriter salidaArchivo = null;

    // Se crea un PrintWriter asociado al flujo de salida orientada
    // a bytes del socket. El PrintWriter convierte los caracteres
    // leídos en bytes, siguiendo el juego de caracteres por defecto
    // de la plataforma. Se establece el flush() automático.
    salida = new PrintWriter(socket.getOutputStream(), true);
   
    // Se crea un BufferedReader asociado al flujo de entrada
    // orientada a bytes del socket. El InputStreamReader convierte en caracteres
    // los bytes leídos del socket, siguiendo el juego de caracteres por omisión
    // de la plataforma.
    // La clase BufferedReader proporciona el método readLine().
    entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
   
    escribirHistorial("Conexión desde la dirección");
   
    // Se lanza el hilo. A partir de este punto, el tratamiento de las
    // excepciones queda en mano de run().
    start();
    } 
/**
 * Se encarga de leer los mensajes de los usuarios y de reenviarlos. Cualquier excepción acaba
 * provocando la muerte del hilo.
 */
public void run() {
    String textoUsuario = "";
   
    try {
    salida.println("> Bienvenido a este chat.");
    salida.println("> Introduzca su nombre, por favor.");
   
    // Si el cliente no introduce su nombre, se le asigna "Invitado".
    // Si el nombre esta repetido, se le añade al nombre el número del puerto.
    nombreCliente = (entrada.readLine()).trim();
    if ( (nombreCliente.equals("")) || (nombreCliente == null) ) {
    nombreCliente = "Invitado";
    }
    Iterator it = clientesActivos.iterator();
    while (it.hasNext()) {
    if (nombreCliente.equals(( (ThreadServidor) it.next()).nombreCliente)) {
    nombreCliente = nombreCliente + socket.getPort();
    break;
    }
    }
   
    // Se añade la conexión a la lista de clientes activos.
    anyadirConexion(this);
   
    // Se informa al usuario de su alias.
    salida.println("> Se le asignado el alias de " + nombreCliente);
   
    // Se establece el límite de tiempo sin actividad para la desconexión.
    // Transcurridos TIEMPO_DESCONEXION_AUTOMATICA milisegundos sin
    // ninguna llamada al método readLine(), se lanzará una excepción.
    socket.setSoTimeout(ServidorChat.TIEMPO_DESCONEXION_AUTOMATICA);
   
    // Se lee la entrada y se comprueba si corresponde a las órdenes "#EXIT" ,
    // "#LISTAR", "#HELP" O "#CHARLAR".
    while ( (textoUsuario=entrada.readLine()) != null ) {
        String nickname= "";
    if ((textoUsuario.equals("#EXIT"))) {
    // Se envía mensaje al usuario, se registra la desconexión y se sale del bucle while.
    salida.println("> ***********HASTA LA VISTA****************");
    escribirHistorial("Desconexión voluntaria desde la dirección:");
    break;
    }
    else if ((textoUsuario.equals("#LISTAR"))) {
    // Se escribe la lista de usuarios activos
    escribirCliente(this,"> " + listarClientesActivos());
    }
    else if ((textoUsuario.equals("#AYUDA"))) {
        // Se escribe la lista de comendos posibles
        salida.println("> #AYUDA -- Ayuda sintaxis de comandos \n #LISTAR -- Listar usuarios en linea \n #CHARLAR -- Para mandar un mensaje privado \n #EXIT -- Salir");
        }
    else if ((textoUsuario.equals("#CHARLAR"))) {
            // Se escribe la lista de usuarios
            escribirCliente(this,"> " + listarClientesActivos());
            // El usuario introduce el nombre del destinatario
            nickname = entrada.readLine();
            escribirCliente(this,">El siguiente mensaje solo sera enviado a"+ nickname );
           String conversacionprivada = entrada.readLine();
           // Se envia el mensaje
                 escribirAUno(nombreCliente+"> "+conversacionprivada, nickname);
        }
    else {
    // Se considera que estamos ante un mensaje normal y se envía
    // a todos los usuarios.
    escribirATodos(nombreCliente+"> "+ textoUsuario);
    }
    } // fin del while
} // fin del try

catch (java.io.InterruptedIOException e1) {
// Las excepciones porque se ha alcanzado el tiempo máximo permitido sin
// actividad se tratan enviando un mensaje de conexión cerrada al cliente.
escribirCliente(this, "> "+ "***************************************");
escribirCliente(this, "> "+ "Se le pasó el tiempo: Conexión cerrada");
escribirCliente(this, "> "+ "Si desea continuar, abra otra sesión");
escribirCliente(this, "> "+ "*****************ADIOS*****************");

// Se registra la desconexión por inactividad.
escribirHistorial("Desconexión por fin de tiempo desde la dirección:");
}
catch (IOException e2) {
escribirHistorial("Desconexión involuntaria desde la dirección:");
}

// Ocurra lo que ocurra (excepción por final de tiempo o de otro tipo), deben
// cerrarse los sockets y los flujos (o, al menos, intentarlo).
finally {
eliminarConexion(this);
limpiar();
} // fin try-catch-finally
} 

 /**
 * Se encarga de cerrar el socket y los flujos de E/S asociados.
 */
private void limpiar() {
    // Si se llega a usar este método es porque ha habido una excepción (ya sea por un verdadero
    // error o porque el cliente ha terminado la conexión o porque han pasado
    // TIEMPO_DESCONEXION_AUTOMATICA milisegundos sin comunicación del cliente).
    // Nótese que el código de cierre es riguroso: una versión más abreviada podría limitarse
    // a intentar cerrar el socket (los flujos de E/S se cierran al cerrarse éste); el único problema
    // que podría aparecer en esa versión del código es que podrían quedarse abiertos los flujos si
    // no se pudiera cerrar el socket (esta posibilidad es inhabitual: normalmente, si el socket no
    // se puede cerrar es porque ya se había cerrado antes).
    // El igualar entrada, salida y socket a null es una buena práctica cuando se puede llamar
    // varias veces a limpiar(); así se evita intentar cerrar dos veces un objeto.
    if ( entrada != null ) {
    try {
    entrada.close();
    }
    catch (IOException e1) {}
    entrada = null;
    }
    if ( salida != null ) {
    salida.close();
    salida = null;
    }
    if ( socket != null ) {
    try {
    socket.close();
    } 
    catch (IOException e2) {}
 socket = null;
 }
 } 
     /**
 * Método sincronizado.
 * Elimina la conexión representada por el argumento threadServidor de la lista de
 * clientes activos.
 *
 * @param threadServidor hilo que se va a eliminar de la lista de clientes activos.
 */
 private static synchronized void eliminarConexion(ThreadServidor threadServidor) {
    clientesActivos.remove(threadServidor);
    } 

     /**
 * Método sincronizado.
 * Añade la conexión representada por el argumento threadServidor a la lista de
 * clientes activos.
 *
 * @param threadServidor hilo (correspondiente a una nueva conexión) que
 * se añade a la lista de clientes activos
 */
 private static synchronized void anyadirConexion(ThreadServidor threadServidor) {
    clientesActivos.add(threadServidor);
    }
    /**
    * Método sincronizado.
    * Escribe la cadena de texto textoUsuario en todas las conexiones de la lista de clientes
    * activos, exceptuando el cliente activo que envía el mensaje.
    *
    * @param textoUsuario texto que se envía a los clientes.
    */
    private synchronized void escribirATodos(String textoUsuario) {
    Iterator it = clientesActivos.iterator();
   
    // Se envía el texto a todos los usuarios, excepto al que lo ha escrito.
    while (it.hasNext()) {
    ThreadServidor tmp = (ThreadServidor) it.next();
    if ( !(tmp.equals(this)) )
    escribirCliente(tmp, textoUsuario);
    }
}
/**
    * Método sincronizado.
    * Escribe la cadena de texto textoUsuario en la conexion que se le indica
    *
    * @param textoUsuario texto que se envía a los clientes.
    *  @param nickname destinatario del mensaje, tiene que concordar con un usuario registrado en la lista
    */
    private synchronized void escribirAUno(String textoUsuario, String nickname) {
        Iterator it = clientesActivos.iterator();
       
        // Se envía el texto a todos los usuarios, excepto al que lo ha escrito.
        while (it.hasNext()) {
        ThreadServidor tmp = (ThreadServidor) it.next();
        
        if ( (tmp.nombreCliente.equals(nickname)) )
        escribirCliente(tmp, textoUsuario);
        }
        
    }
    
    /**
    * Método sincronizado.
    * Escribe la cadena de texto textoUsuario en la conexión representada por el hilo threadServidor.
    *
    * @param threadServidor hilo (cliente) al que se envía el mensaje.
    * @param textoUsuario texto para enviar.
    */
    private synchronized void escribirCliente(ThreadServidor threadServidor, String textoUsuario) {
    (threadServidor.salida).println(textoUsuario);
    } 

    /**
 * Método sincronizado.
 * Escribe los alias de todos los clientes activos en la conexión representada por el hilo
 * threadServidor que los solicita.
 *
 * @return StringBuffer lista de alias de los clientes activos.
 */
 private static synchronized StringBuffer listarClientesActivos() {
    // Se usa StringBuffer por eficacia.
    StringBuffer cadena = new StringBuffer();
   
    for (int i = 0; i < clientesActivos.size(); i++) {
    ThreadServidor tmp = (ThreadServidor) (clientesActivos.get(i));
    cadena.append(
    (((ThreadServidor) clientesActivos.get(i)).nombreCliente)).append("||") ;
    }
    return cadena;
    } 

/**
 * Método sincronizado.
 * Escribe en el archivo de historial el texto mensaje, asociado a conexiones y
 * desconexiones de los clientes.
 *
 * @param mensaje texto que se escribe en el archivo de historial.
 */
private synchronized void escribirHistorial(String mensaje ) {
    PrintWriter salidaArchivo = null;
    try {
    salidaArchivo = new PrintWriter(new BufferedWriter(new FileWriter(historial, true))); // true = autoflush
    salidaArchivo.println(mensaje + " " + socket.getInetAddress().getHostName() + " por el puerto " + socket.getPort() + " en la fecha " + new Date());
    }
    catch (IOException e1) {
    System.out.println( "Fallo en el archivo de historial.");
    }
    finally {
    // limpieza de salidaArchivo
    if (salidaArchivo != null) {
    salidaArchivo.close();
    salidaArchivo = null;
    }
    }
    }
   
   } 
