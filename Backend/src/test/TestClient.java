package test;

import com.google.gson.Gson;
import protocol.Request;
import protocol.Response;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Cliente de prueba para verificar la conexión con SpeederServer.
 */
public class TestClient {

    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 5000;

        System.out.println("Conectando a " + host + ":" + port + "...");

        try (Socket socket = new Socket(host, port);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("Conectado!");

            // 1. Crear una petición de LOGIN de prueba
            // NOTA: Asegúrate de usar credenciales que existan en tu base de datos
            Map<String, Object> payload = new HashMap<>();
            payload.put("username", "emprendedor_user"); // Usuario de ejemplo
            payload.put("password", "12345"); // Contraseña de ejemplo

            Request request = new Request("LOGIN", payload);

            // 2. Convertir a JSON
            Gson gson = new Gson();
            String jsonRequest = gson.toJson(request);
            System.out.println("Enviando: " + jsonRequest);

            // 3. Enviar al servidor
            out.println(jsonRequest);

            // 4. Leer respuesta
            String jsonResponse = in.readLine();
            System.out.println("Recibido: " + jsonResponse);

            // 5. Parsear respuesta (opcional, para verificar)
            Response response = gson.fromJson(jsonResponse, Response.class);
            if ("SUCCESS".equals(response.getStatus())) {
                System.out.println("¡Prueba exitosa! El servidor respondió correctamente.");
            } else {
                System.out.println("El servidor respondió con error: " + response.getMessage());
            }

        } catch (Exception e) {
            System.err.println("Error en el cliente de prueba: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
