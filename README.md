# Tarea para PSP03.
## Detalles de la tarea de esta unidad.

La tarea de la unidad esta dividida en 2 actividades.

* Actividad 3.1. El objetivo del ejercicio es crear una aplicación cliente/servidor que se comunique por el puerto 2000 y realice lo siguiente: El servidor debe generar un número secreto de forma aleatoria entre el 0 al 100. El objetivo de cliente es solicitarle al usuario un número y enviarlo al servidor hasta que adivine el número secreto. Para ello, el servidor para cada número que le envía el cliente le indicará si es menor, mayor o es el número secreto del servidor.

```Java
package psp03;

import java.io.*;
import java.net.*;
import java.util.Random;

class Servidor {
    private final Random rmd = new Random(); //INICIALIZO RANDOM PARA GENERAR EL NÚMERO ALEATORIO
    static final int PUERTO = 2000; //DEFINO EL PUERTO DEL SERVIDOR
    public static void main(String[] arg) {
        Servidor servidor = new Servidor();
    }
    
    public Servidor() {
        int numero = rmd.nextInt(100); //GENERO UN NÚMERO ALEATORIO ENTRE 1 Y 100
        int numeroIntroducido = -1; //ESTABLEZCO UN VALOR INICIAL PARA EL NÚMERO INTRODUCIDO
        System.out.println("Número secreto: " + numero); //MUESTRO EN LA CONSOLA DEL SERVIDOR EL NÚMERO GENERADO
        try {
            ServerSocket skServidor; //INICIPO LA ESCUCHA DEL SERVIDOR
            skServidor = new ServerSocket(PUERTO);
            System.out.println("Servidor conectado. A la espera de un  cliente...");
            Socket sCliente = skServidor.accept(); //ESPERO A QUE SE CONECTE UN CLIENTE
            System.out.println("Cliente conectado. A la espera de valores...");
            OutputStream aux = sCliente.getOutputStream(); //CREO UN STREAM DE SALIDA
            DataOutputStream flujo_salida = new DataOutputStream(aux); //CREO UN FLUJO DE SALIDA
            InputStream in = sCliente.getInputStream(); //CREO UN STREAM DE ENTRADA
            DataInputStream flujo_entrada = new DataInputStream(in); //CREO UN FLUJO DE ENTRADA
            flujo_salida.writeUTF("Conectado"); //ENVÍO EL MENSAJE DE CONEXIÓN AL CLIENTE
            //System.out.println(flujo_entrada.readUTF());
            while (numeroIntroducido != numero) { //BUCLE QUE COMPRUEBA EL NÚMERO INTRODUCIDO Y ENVIA MENSAJES DE MAYOR O MENOR
                String entrada = flujo_entrada.readUTF();
                System.out.println("Valor introducido: " + entrada);
                if (entrada.matches("\\d+")) { //COMPRUEBO QUE EL VALOR INTRODUCIDO DESA DE TIPO NUMÉRICO
                       int numeroNuevo = Integer.parseInt(entrada);
                    if (numeroNuevo == numero) {
                        System.out.println("Valor correcto!!");
                        flujo_salida.writeUTF("Correcto");
                        break;
                    } else if (numeroNuevo > numero) {
                        System.out.println("El número es menor");
                        flujo_salida.writeUTF("El número es menor");
                    } else {
                        System.out.println("El número es mayor");
                        flujo_salida.writeUTF("El número es mayor");
                    }
                } else {
                    System.out.println("No es de tipo numérico");
                    flujo_salida.writeUTF("Valor inválido");
                }
            }
            sCliente.close(); // CIERRO EL SOCKET
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

}
````
*Creo la clases servidor*
```Java
package psp03;

import java.io.*;
import java.net.*;
import java.util.Scanner;

class Cliente {
    private final String URL = "127.0.0.1"; //DEFINO LA URL DEL SERVIDOR
    private final int PUERTO = 2000; //DEFINO EL PUERTO DEL SERVIDOR
    public static void main(String[] arg) {
        Cliente cliente = new Cliente();
    }
    public Cliente() {
        try (Socket sCliente = new Socket(URL, PUERTO)) {
            //CREO LOS SOCKETS DE ENTRADA Y SALIDA IGUAL QUE EN EL SERVIDOR
            InputStream in = sCliente.getInputStream();
            DataInputStream flujo_entrada = new DataInputStream(in);
            OutputStream out = sCliente.getOutputStream();
            DataOutputStream flujo_salida = new DataOutputStream(out);
            String entrada = flujo_entrada.readUTF();
            if (entrada.equals("Conectado")){
                System.out.println(entrada);
                while (!entrada.equals("Correcto")) { //BUCLE QUE SE REPETIRÁ MIENTRAS EL SERVIDOR NO DEVUELVA EL VALOR "Correcto"
                    Scanner sc = new Scanner(System.in);
                    System.out.print("Introduce un número: ");
                    String texto = sc.nextLine();
                    flujo_salida.writeUTF(texto);
                    entrada = flujo_entrada.readUTF();
                    System.out.println(entrada);
                }
            }
            sCliente.close(); //CIERRO EL SOCKET
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

```
*Creo la clase cliente*
![screenshot_18](https://user-images.githubusercontent.com/44543081/54043861-e48c1e80-41cd-11e9-9943-8404bd3fc407.png)  
![image](https://user-images.githubusercontent.com/44543081/54043914-02598380-41ce-11e9-9d79-a1054bf6c77f.png)  

* Actividad 3.2. El objetivo del ejercicio es crear una aplicación cliente/servidor que permita el envío de ficheros al cliente. Para ello, el cliente se conectará al servidor por el puerto 1500 y le solicitará el nombre de un fichero del servidor. Si el fichero existe, el servidor, le enviará el fichero al cliente y éste lo mostrará por pantalla. Si el fichero no existe, el servidor le enviará al cliente un mensaje de error. Una vez que el cliente ha mostrado el fichero se finalizará la conexión.
