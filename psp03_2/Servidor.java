package psp03_2;

import java.io.*;
import java.net.*;

class Servidor {
    static final int PUERTO = 1500; //DEFINO EL PUERTO DEL SERVIDOR
    public static void main(String[] arg) {
        Servidor servidor = new Servidor();
    }
    
    public Servidor() {
        try {
            ServerSocket skServidor; //INICIPO LA ESCUCHA DEL SERVIDOR
            skServidor = new ServerSocket(PUERTO);
            System.out.println("Servidor conectado. A la espera de un  cliente...");
            Socket sCliente = skServidor.accept(); //ESPERO A QUE SE CONECTE UN CLIENTE
            System.out.println("Cliente conectado. A la espera de nombre de fichero...");
            OutputStream aux = sCliente.getOutputStream(); //CREO UN STREAM DE SALIDA
            DataOutputStream flujo_salida = new DataOutputStream(aux); //CREO UN FLUJO DE SALIDA
            InputStream in = sCliente.getInputStream(); //CREO UN STREAM DE ENTRADA
            DataInputStream flujo_entrada = new DataInputStream(in); //CREO UN FLUJO DE ENTRADA
            flujo_salida.writeUTF("Conectado"); //ENVÍO EL MENSAJE DE CONEXIÓN AL CLIENTE
            File archivo = new File(flujo_entrada.readUTF());
            if (archivo.exists()){
                InputStream ArchivoIn = new FileInputStream(archivo);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(ArchivoIn)); //CREO EL BUFFER DE LECTURA DEL FICHERO
                        String line;
                        while ((line = reader.readLine()) != null) {
                            flujo_salida.writeUTF(line); //LEO EL FICHERO LÍNEA POR LÍNEA
                        }
            }else{
                flujo_salida.writeUTF("El archivo no existe");
            }

            sCliente.close(); // CIERRO EL SOCKET
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

}
