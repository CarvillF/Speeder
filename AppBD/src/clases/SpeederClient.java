package clases;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SpeederClient {

    private static SpeederClient instance;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Gson gson;

    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 8080;

    private SpeederClient() {
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            gson = new Gson();
            System.out.println("Conectado al servidor");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error conectando al servidor");
        }
    }

    public static synchronized SpeederClient getInstance() {
        if (instance == null) {
            instance = new SpeederClient();
        }
        return instance;
    }

    public Response sendRequest(Request request) {
        if (socket == null || socket.isClosed()) {
            return new Response("ERROR", "Sin conexión con el servidor");
        }

        try {
            String jsonRequest = gson.toJson(request);
            out.println(jsonRequest);

            String jsonResponse = in.readLine();
            return gson.fromJson(jsonResponse, Response.class);

        } catch (IOException e) {
            e.printStackTrace();
            return new Response("ERROR", "Fallo de comunicación: " + e.getMessage());
        }
    }

    public void closeConnection() {
        try {
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}