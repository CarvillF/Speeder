import clases.ProtocolActions;
import clases.Request;
import clases.Response;
import clases.SpeederClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class loginController {

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordPasswordField;

    @FXML
    private Label messageLabel;

    @FXML
    private void initialize() {
        if (messageLabel != null) {
            messageLabel.setText("");
        }
    }

    @FXML
    private void onLoginClicked() {
        String usuario = usernameTextField.getText();
        String password = passwordPasswordField.getText();

        // 1) DEMO: usuario y contraseña VACÍOS -> ir a selección de rol demo
        if ((usuario == null || usuario.isBlank())
                && (password == null || password.isBlank())) {
            irSeleccionRolDemo();
            return;
        }

        // 2) Solo uno vacío -> mensaje de error normal
        if (usuario == null || usuario.isBlank()
                || password == null || password.isBlank()) {
            messageLabel.setText("Por favor ingrese usuario y contraseña.");
            messageLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        // 3) Caso normal: login contra backend
        messageLabel.setText("Conectando...");
        messageLabel.setStyle("-fx-text-fill: black;");

        new Thread(() -> {
            Map<String, String> payload = new HashMap<>();
            payload.put("username", usuario);
            payload.put("password", password);

            Request request = new Request(ProtocolActions.LOGIN, payload);

            SpeederClient client = SpeederClient.getInstance();
            Response response = client.sendRequest(request);

            Platform.runLater(() -> {
                if (response != null && "SUCCESS".equalsIgnoreCase(response.getStatus())) {
                    messageLabel.setText("Login exitoso");
                    messageLabel.setStyle("-fx-text-fill: green;");
                    irMenuUsuario();
                } else {
                    String msg = (response != null) ? response.getMessage() : "Error de conexión";
                    messageLabel.setText("Error: " + msg);
                    messageLabel.setStyle("-fx-text-fill: red;");
                }
            });

        }).start();
    }

    @FXML
    private void onRegisterClicked() {
        irRegistroTransportista();
    }

    @FXML
    private void onRegisterClicked2() {
        irRegistroEmpresario();
    }

    // ====== NUEVO: IR A SELECCIÓN DE ROL DEMO ======
    private void irSeleccionRolDemo() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("seleccion_rol_demo.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameTextField.getScene().getWindow();

            // 🔹 SIN tamaño fijo: deja que el FXML mande
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.sizeToScene();
            stage.centerOnScreen();

            stage.setTitle("Modo demo - Seleccionar rol");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            if (messageLabel != null) {
                messageLabel.setText("Error al abrir selección de rol demo.");
                messageLabel.setStyle("-fx-text-fill: red;");
            }
        }
    }

    private void irMenuUsuario() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menu_usuario.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameTextField.getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.sizeToScene();
            stage.centerOnScreen();

            stage.setTitle("Menú de usuario - Sistema de Paquetería");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Error al abrir el menú de usuario.");
        }
    }

    private void irRegistroTransportista() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("registro_transportista.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameTextField.getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.sizeToScene();
            stage.centerOnScreen();

            stage.setTitle("Registro transportista");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Error al abrir registro transportista.");
        }
    }

    private void irRegistroEmpresario() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("registro_empresario.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameTextField.getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.sizeToScene();
            stage.centerOnScreen();

            stage.setTitle("Registro empresario");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Error al abrir registro empresario.");
        }
    }
}