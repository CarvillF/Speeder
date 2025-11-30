import clases.ProtocolActions;
import clases.Request;
import clases.Response;
import clases.SpeederClient;
import clases.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class RegistroAdminController {

    @FXML
    private TextField tfCedula;

    @FXML
    private TextField tfNombre;

    @FXML
    private TextField tfApellidos;

    @FXML
    private TextField tfCorreo;

    @FXML
    private TextField tfNumero;

    @FXML
    private TextField tfCodigoEmpleado;

    @FXML
    private ChoiceBox<String> cbEstadoCuenta;

    @FXML
    private PasswordField pfContrasena;

    @FXML
    private Button btnRegistrarAdmin;

    @FXML
    private Button btnVolver;

    @FXML
    private void initialize() {
        cbEstadoCuenta.getItems().addAll("SuperAdmin", "Gerente", "Soporte", "Auditor");
        cbEstadoCuenta.getSelectionModel().select("Soporte");
    }

    @FXML
    private void onRegistrarAdmin() {
        String cedula = tfCedula.getText().trim();
        String nombre = tfNombre.getText().trim();
        String apellidos = tfApellidos.getText().trim();
        String correo = tfCorreo.getText().trim();
        String numero = tfNumero.getText().trim();
        String codigoEmp = tfCodigoEmpleado.getText().trim();
        String estadoCuenta = cbEstadoCuenta.getValue();
        String contrasena = pfContrasena.getText();

        if (cedula.isEmpty() || nombre.isEmpty() || correo.isEmpty()
                || codigoEmp.isEmpty() || contrasena.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Datos incompletos",
                    "Complete al menos cédula, nombre, correo, código empleado y contraseña.");
            return;
        }

        User admin = new User();
        admin.setCedula(cedula);
        admin.setNombre(nombre);
        admin.setApellidos(apellidos);
        admin.setCorreo(correo);
        admin.setTelefono(numero);
        admin.setPassword(contrasena);
        admin.setRole("ADMIN");
        admin.setCodigoEmpleado(codigoEmp);
        admin.setEstadoCuenta(estadoCuenta);

        Request request = new Request(ProtocolActions.REGISTER_ADMIN, admin);

        new Thread(() -> {
            Response response = SpeederClient.getInstance().sendRequest(request);
            Platform.runLater(() -> {
                if (response != null && "SUCCESS".equalsIgnoreCase(response.getStatus())) {
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Administrador creado",
                            response.getMessage() != null ? response.getMessage()
                                    : "El administrador fue registrado correctamente.");
                    limpiarCampos();
                } else {
                    String msg = response != null && response.getMessage() != null
                            ? response.getMessage()
                            : "No se pudo registrar el administrador.";
                    mostrarAlerta(Alert.AlertType.ERROR, "Error", msg);
                }
            });
        }).start();
    }

    @FXML
    private void onVolverAdminGeneral() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin_general.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnVolver.getScene().getWindow();
            stage.setScene(new Scene(root, 1200, 700));
            stage.setTitle("Panel administrador");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void limpiarCampos() {
        tfCedula.clear();
        tfNombre.clear();
        tfApellidos.clear();
        tfCorreo.clear();
        tfNumero.clear();
        tfCodigoEmpleado.clear();
        pfContrasena.clear();
        cbEstadoCuenta.getSelectionModel().select("Soporte");
    }

    private void mostrarAlerta(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
