package test;

import com.google.gson.Gson;
import dao.UserDAO;
import clases.Empresario;
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
import java.util.UUID;

public class TestLoginE2E {

    private static final int PORT = 5000;
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        System.out.println("=== INICIANDO TEST E2E DE LOGIN ===");

        // 1. Registrar un usuario de prueba (Empresario)
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        String testCedula = uniqueId; // 8 chars
        String testEmail = "test_" + uniqueId + "@example.com";
        String testPassword = "password123";

        // Empresario(cedula, nombre, apellidos, correo, contrasena, numero_telefono,
        // id_direccion_principal, cargo_empresa, correo_empresarial)
        Empresario testUser = new Empresario(
                testCedula,
                "TestUser",
                "E2E",
                testEmail,
                testPassword,
                "555-0000",
                0,
                "CEO",
                "business_" + uniqueId + "@example.com");

        System.out.println("[SETUP] Registrando usuario de prueba: " + testEmail);
        boolean registered = UserDAO.registerBusiness(testUser);
        if (!registered) {
            System.err.println("❌ Error: No se pudo registrar el usuario de prueba. Abortando.");
            // Intentar continuar por si el usuario ya existía
        } else {
            System.out.println("✅ Usuario registrado correctamente.");
        }

        // 2. Iniciar el servidor en un hilo separado
        Thread serverThread = new Thread(() -> {
            SpeederServer.main(new String[] {});
        });
        serverThread.setDaemon(true);
        serverThread.start();

        // Esperar a que el servidor arranque
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 3. Ejecutar prueba de login exitoso
        testLoginSuccess(testEmail, testPassword);

        // 4. Ejecutar prueba de login fallido
        testLoginFailure(testEmail, "wrong_password");

        System.out.println("=== TEST E2E FINALIZADO ===");
    }

    private static void testLoginSuccess(String email, String password) {
        System.out.println("\n[TEST] Probando LOGIN EXITOSO...");
        try (Socket socket = new Socket("localhost", PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Preparar Request
            Map<String, Object> payload = new HashMap<>();
            payload.put("username", email);
            payload.put("password", password);
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
                    System.out.println("✅ PASSED: Login exitoso como se esperaba.");
                    System.out.println("   User Type: " + response.getData());
                } else {
                    System.out.println("❌ FAILED: Se esperaba SUCCESS pero se recibió: " + response.getStatus());
                    System.out.println("   Mensaje: " + response.getMessage());
                }
            } else {
                System.out.println("❌ FAILED: No se recibió respuesta del servidor.");
            }

        } catch (IOException e) {
            System.out.println("❌ Error de conexión: " + e.getMessage());
        }
    }

    private static void testLoginFailure(String email, String password) {
        System.out.println("\n[TEST] Probando LOGIN FALLIDO...");
        try (Socket socket = new Socket("localhost", PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Preparar Request
            Map<String, Object> payload = new HashMap<>();
            payload.put("username", email);
            payload.put("password", password);
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
                if ("ERROR".equals(response.getStatus())) {
                    System.out.println("✅ PASSED: Login fallido como se esperaba.");
                    System.out.println("   Mensaje: " + response.getMessage());
                } else {
                    System.out.println("❌ FAILED: Se esperaba ERROR pero se recibió: " + response.getStatus());
                }
            } else {
                System.out.println("❌ FAILED: No se recibió respuesta del servidor.");
            }

        } catch (IOException e) {
            System.out.println("❌ Error de conexión: " + e.getMessage());
        }
    }
}
