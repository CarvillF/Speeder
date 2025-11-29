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

        if (usuario == null || usuario.isEmpty()
                || password == null || password.isEmpty()) {
            messageLabel.setText("Por favor ingrese usuario y contraseña.");
            messageLabel.setStyle("-fx-text-fill: red;");
            return;
        }

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

    private void irMenuUsuario() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menu_usuario.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameTextField.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
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
            stage.setScene(new Scene(root, 800, 600));
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
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Registro empresario");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Error al abrir registro empresario.");
        }
    }
}