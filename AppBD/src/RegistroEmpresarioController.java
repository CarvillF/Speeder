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

public class RegistroEmpresarioController {

    @FXML
    private TextField cedulaField;
    @FXML
    private TextField nombreField;
    @FXML
    private TextField apellidosField;
    @FXML
    private TextField correoField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField telefonoField;

    @FXML
    private TextField cargoEmpresaField;
    @FXML
    private TextField correoEmpresarialField;

    @FXML
    private Label statusLabel;

    @FXML
    private void initialize() {
        if (statusLabel != null) {
            statusLabel.setText("");
        }
    }

    @FXML
    private void onRegistrarClicked() {
        String cedula = cedulaField.getText();
        String nombre = nombreField.getText();
        String correo = correoField.getText();
        String pass   = passwordField.getText();

        if (cedula == null || cedula.isEmpty()
                || nombre == null || nombre.isEmpty()
                || correo == null || correo.isEmpty()
                || pass == null || pass.isEmpty()) {

            statusLabel.setText("Complete cédula, nombre, correo y contraseña.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        statusLabel.setText("Enviando registro...");
        statusLabel.setStyle("-fx-text-fill: black;");

        new Thread(() -> {
            Map<String, String> payload = new HashMap<>();
            payload.put("cedula", cedula);
            payload.put("nombre", nombre);
            payload.put("apellidos", apellidosField.getText());
            payload.put("correo", correo);
            payload.put("password", pass);
            payload.put("telefono", telefonoField.getText());

            payload.put("cargoEmpresa", cargoEmpresaField.getText());
            payload.put("correoEmpresarial", correoEmpresarialField.getText());
            payload.put("role", "BUSINESS");

            Request request = new Request(ProtocolActions.REGISTER_BUSINESS, payload);

            SpeederClient client = SpeederClient.getInstance();
            Response response = client.sendRequest(request);

            Platform.runLater(() -> {
                if (response != null && "SUCCESS".equalsIgnoreCase(response.getStatus())) {
                    statusLabel.setText("Registro de empresario exitoso.");
                    statusLabel.setStyle("-fx-text-fill: green;");
                } else {
                    String msg = (response != null) ? response.getMessage() : "Error de conexión";
                    statusLabel.setText("Error: " + msg);
                    statusLabel.setStyle("-fx-text-fill: red;");
                }
            });
        }).start();
    }

    @FXML
    private void onVolverLoginClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) cedulaField.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
            stage.setTitle("Sistema de Paquetería");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            if (statusLabel != null) {
                statusLabel.setText("Error al volver al login.");
                statusLabel.setStyle("-fx-text-fill: red;");
            }
        }
    }
}