package server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dao.UserDAO;
import database.UserType;
import protocol.Request;
import protocol.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

/**
 * Maneja la comunicación con un cliente individual en un hilo separado.
 */
public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final Gson gson;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        this.gson = new Gson();
    }

    @Override
    public void run() {
        try (
                // Streams para lectura y escritura
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            String inputLine;
            // Leer mensajes del cliente mientras la conexión esté activa
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Recibido del cliente: " + inputLine);

                // Procesar la petición
                String jsonResponse = processRequest(inputLine);

                // Enviar respuesta
                out.println(jsonResponse);
            }
        } catch (IOException e) {
            System.err.println("Error en la comunicación con el cliente: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Cliente desconectado.");
        }
    }

    /**
     * Procesa una línea de texto (JSON) y retorna una respuesta JSON.
     */
    private String processRequest(String jsonRequest) {
        try {
            // Deserializar JSON a objeto Request
            Request request = gson.fromJson(jsonRequest, Request.class);

            if (request == null || request.getAction() == null) {
                return gson.toJson(new Response("ERROR", "Petición inválida o vacía"));
            }

            // Seleccionar acción
            switch (request.getAction()) {
                default:
                    return gson.toJson(new Response("ERROR", "Acción no reconocida: " + request.getAction()));
            }

        } catch (JsonSyntaxException e) {
            return gson.toJson(new Response("ERROR", "Formato JSON inválido"));
        } catch (Exception e) {
            e.printStackTrace();
            return gson.toJson(new Response("ERROR", "Error interno del servidor: " + e.getMessage()));
        }
    }

}
