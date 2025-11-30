package test;

import dao.UserDAO;
import database.DBConnection;
import database.UserType;
import protocol.Request;
import protocol.Response;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase de prueba para verificar la implementación del Paso 1.
 * 
 * NOTA: Antes de ejecutar este test, asegúrate de:
 * 1. Tener MySQL corriendo
 * 2. Haber ejecutado el script create_users.sql
 * 3. Tener la base de datos 'mydb' creada con el esquema correcto
 */
public class TestPaso1 {

    public static void main(String[] args) {
        System.out.println("=== Test Paso 1: Capa de Acceso a Datos ===\n");

        // Test 1: Conexiones por tipo de usuario
        testConnections();

        // Test 2: Protocolo JSON
        testJsonProtocol();

        // Test 3: DAO (comentado porque requiere datos en la BD)
        // testUserDAO();

        System.out.println("\n=== Fin de Tests ===");
    }

    /**
     * Test 1: Verifica que se pueden crear conexiones para cada tipo de usuario
     */
    private static void testConnections() {
        System.out.println("--- Test 1: Conexiones por Tipo de Usuario ---");

        for (UserType userType : UserType.values()) {
            try (Connection conn = DBConnection.connect(userType)) {
                if (conn != null && !conn.isClosed()) {
                    System.out.println("✓ Conexión exitosa como " + userType);
                } else {
                    System.out.println("✗ Fallo al conectar como " + userType);
                }
            } catch (SQLException e) {
                System.out.println("✗ Error al conectar como " + userType + ": " + e.getMessage());
            }
        }
        System.out.println();
    }

    /**
     * Test 2: Verifica la serialización/deserialización de Request y Response
     */
    private static void testJsonProtocol() {
        System.out.println("--- Test 2: Protocolo JSON ---");

        Gson gson = new Gson();

        // Test Request
        Map<String, String> loginData = new HashMap<>();
        loginData.put("username", "test@example.com");
        loginData.put("password", "password123");

        Request request = new Request("LOGIN", loginData);
        String requestJson = gson.toJson(request);
        System.out.println("Request JSON: " + requestJson);

        Request deserializedRequest = gson.fromJson(requestJson, Request.class);
        System.out.println("Request deserializado: " + deserializedRequest);
        System.out.println("✓ Request serializado/deserializado correctamente\n");

        // Test Response
        Response response = new Response("SUCCESS", "Login correcto");
        String responseJson = gson.toJson(response);
        System.out.println("Response JSON: " + responseJson);

        Response deserializedResponse = gson.fromJson(responseJson, Response.class);
        System.out.println("Response deserializado: " + deserializedResponse);
        System.out.println("✓ Response serializado/deserializado correctamente\n");

        // Test Response con datos
        Map<String, Object> userData = new HashMap<>();
        userData.put("userId", "1234567890");
        userData.put("userType", "EMPRENDEDOR");

        Response responseWithData = new Response("SUCCESS", "Login correcto", userData);
        String responseWithDataJson = gson.toJson(responseWithData);
        System.out.println("Response con datos JSON: " + responseWithDataJson);
        System.out.println("✓ Response con datos serializado correctamente\n");
    }

    /**
     * Test 3: Verifica las operaciones del UserDAO
     * NOTA: Solo descomentar cuando tengas datos de prueba en la base de datos
     */
    private static void testUserDAO() {
        System.out.println("--- Test 3: UserDAO ---");

        // Test login con credenciales de prueba
        // String testEmail = "test@example.com";
        // String testPassword = "password123";

        // boolean loginSuccess = UserDAO.login(testEmail, testPassword);
        // System.out.println("Login con credenciales de prueba: " +
        // (loginSuccess ? "✓ Exitoso" : "✗ Fallido"));

        // Test loginAndGetUserType
        // UserType userType = UserDAO.loginAndGetUserType(testEmail, testPassword);
        // if (userType != null) {
        // System.out.println("✓ Tipo de usuario detectado: " + userType);
        // } else {
        // System.out.println("✗ No se pudo detectar el tipo de usuario");
        // }

        // Test correoExiste
        // boolean emailExists = UserDAO.correoExiste(testEmail);
        // System.out.println("Email existe: " + (emailExists ? "✓ Sí" : "✗ No"));

        System.out.println("(Tests de DAO comentados - requieren datos en BD)\n");
    }
}
