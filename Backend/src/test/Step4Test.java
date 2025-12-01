package test;

import com.google.gson.Gson;
import protocol.Request;
import protocol.Response;
import server.SpeederServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Step4Test {

    private static final int PORT = 5000;
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        System.out.println("=== INICIANDO PRUEBAS DEL PASO 4 ===");

        // 1. Iniciar el servidor en un hilo separado
        Thread serverThread = new Thread(() -> {
            SpeederServer.main(new String[] {});
        });
        serverThread.setDaemon(true); // Para que se cierre al terminar el main
        serverThread.start();

        // Esperar a que el servidor arranque
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 2. Ejecutar pruebas
        testLogin();
        testCreateOrder();

        System.out.println("=== PRUEBAS FINALIZADAS ===");
    }

    private static void testLogin() {
        System.out.println("\n[TEST] Probando LOGIN...");
        try (Socket socket = new Socket("localhost", PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Preparar Request
            Map<String, Object> payload = new HashMap<>();
            payload.put("username", "admin@example.com"); // Ajustar según datos de prueba reales
            payload.put("password", "1234");
            Request request = new Request("LOGIN", payload);

            // Enviar
            String jsonRequest = gson.toJson(request);
            System.out.println("Enviando: " + jsonRequest);
            out.println(jsonRequest);

            // Recibir
            String jsonResponse = in.readLine();
            System.out.println("Recibido: " + jsonResponse);

            // Validar
            if (jsonResponse != null) {
                Response response = gson.fromJson(jsonResponse, Response.class);
                if ("SUCCESS".equals(response.getStatus())) {
                    System.out.println("✅ LOGIN Exitoso (o al menos respondió SUCCESS)");
                } else {
                    System.out.println("❌ LOGIN Fallido: " + response.getMessage());
                }
            } else {
                System.out.println("❌ No se recibió respuesta del servidor.");
            }

        } catch (IOException e) {
            System.out.println("❌ Error de conexión: " + e.getMessage());
        }
    }

    private static void testCreateOrder() {
        System.out.println("\n[TEST] Probando CREATE_ORDER...");
        try (Socket socket = new Socket("localhost", PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Preparar Request
            Map<String, Object> payload = new HashMap<>();
            payload.put("cliente", "Juan Perez");
            payload.put("producto", "Pizza");
            Request request = new Request("CREATE_ORDER", payload);

            // Enviar
            String jsonRequest = gson.toJson(request);
            System.out.println("Enviando: " + jsonRequest);
            out.println(jsonRequest);

            // Recibir
            String jsonResponse = in.readLine();
            System.out.println("Recibido: " + jsonResponse);

            // Validar
            if (jsonResponse != null) {
                Response response = gson.fromJson(jsonResponse, Response.class);
                if ("SUCCESS".equals(response.getStatus())) {
                    System.out.println("✅ CREATE_ORDER Exitoso");
                } else {
                    System.out.println(
                            "❌ CREATE_ORDER Fallido (Esperado si no está implementado): " + response.getMessage());
                }
            } else {
                System.out.println("❌ No se recibió respuesta del servidor.");
            }

        } catch (IOException e) {
            System.out.println("❌ Error de conexión: " + e.getMessage());
        }
    }
}
