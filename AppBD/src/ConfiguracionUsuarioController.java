import clases.ProtocolActions;
import clases.Request;
import clases.Response;
import clases.SpeederClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfiguracionUsuarioController {

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtApellidos;

    @FXML
    private TextField txtCorreo;

    @FXML
    private TextField txtTelefono;

    @FXML
    private PasswordField txtContrasena;

    @FXML
    private void onGuardarCambios() {
        String nombre = txtNombre.getText();
        String apellidos = txtApellidos.getText();
        String correo = txtCorreo.getText();
        String telefono = txtTelefono.getText();
        String contrasena = txtContrasena.getText();

        if ((nombre == null || nombre.isBlank()) &&
            (apellidos == null || apellidos.isBlank()) &&
            (correo == null || correo.isBlank()) &&
            (telefono == null || telefono.isBlank()) &&
            (contrasena == null || contrasena.isBlank())) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Datos incompletos");
            alert.setHeaderText(null);
            alert.setContentText("Ingrese al menos un campo para actualizar.");
            alert.showAndWait();
            return;
        }

        Map<String, String> payload = new HashMap<>();
        if (nombre != null && !nombre.isBlank()) {
            payload.put("nombre", nombre);
        }
        if (apellidos != null && !apellidos.isBlank()) {
            payload.put("apellidos", apellidos);
        }
        if (correo != null && !correo.isBlank()) {
            payload.put("correo", correo);
        }
        if (telefono != null && !telefono.isBlank()) {
            payload.put("telefono", telefono);
        }
        if (contrasena != null && !contrasena.isBlank()) {
            payload.put("contrasena", contrasena);
        }

        Request request = new Request(ProtocolActions.UPDATE_PROFILE, payload);

        new Thread(() -> {
            Response response = SpeederClient.getInstance().sendRequest(request);

            Platform.runLater(() -> {
                Alert alert;
                if (response != null && "SUCCESS".equalsIgnoreCase(response.getStatus())) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Perfil actualizado");
                    alert.setHeaderText(null);
                    alert.setContentText(response.getMessage() != null
                            ? response.getMessage()
                            : "Los datos se actualizaron correctamente.");
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error al actualizar");
                    alert.setHeaderText(null);
                    String msg = (response != null && response.getMessage() != null)
                            ? response.getMessage()
                            : "No se pudo actualizar el perfil.";
                    alert.setContentText(msg);
                }
                alert.showAndWait();
            });
        }).start();
    }

    @FXML
    private void onVolverConfiguracion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("configuracion.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) txtNombre.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Configuración");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al volver");
            alert.setHeaderText(null);
            alert.setContentText("No se pudo volver al menú de configuración.");
            alert.showAndWait();
        }
    }
}