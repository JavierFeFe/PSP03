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
