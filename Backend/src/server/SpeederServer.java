package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Servidor principal de Speeder.
 * Escucha conexiones entrantes y delega su manejo a ClientHandler.
 */
public class SpeederServer {
    private static final int PORT = 5000;

    public static void main(String[] args) {
        System.out.println("Iniciando servidor Speeder en el puerto " + PORT + "...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor iniciado. Esperando conexiones...");

            while (true) {
                try {
                    // Aceptar nueva conexión
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Nuevo cliente conectado: " + clientSocket.getInetAddress());

                    // Crear y lanzar el manejador de clientes en un nuevo hilo
                    ClientHandler clientHandler = new ClientHandler(clientSocket);
                    Thread thread = new Thread(clientHandler);
                    thread.start();

                } catch (IOException e) {
                    System.err.println("Error al aceptar conexión: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("No se pudo iniciar el servidor en el puerto " + PORT);
            e.printStackTrace();
        }
    }
}
