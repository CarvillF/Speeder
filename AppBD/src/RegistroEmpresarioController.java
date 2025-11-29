import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class RegistroEmpresarioController {

    @FXML private TextField cedulaField;
    @FXML private TextField nombreField;
    @FXML private TextField apellidosField;
    @FXML private TextField correoField;
    @FXML private PasswordField passwordField;
    @FXML private TextField numeroField;

    @FXML private TextField cargoEmpresaField;
    @FXML private TextField correoEmpresarialField;

    @FXML private Label mensajeLabel;

    @FXML
    private void initialize() {
        mensajeLabel.setText("");
    }

    @FXML
    private void onRegistrarClicked() {
        if (cedulaField.getText().isEmpty() ||
            nombreField.getText().isEmpty() ||
            apellidosField.getText().isEmpty() ||
            correoField.getText().isEmpty() ||
            passwordField.getText().isEmpty() ||
            numeroField.getText().isEmpty() ||
            cargoEmpresaField.getText().isEmpty() ||
            correoEmpresarialField.getText().isEmpty()) {

            mensajeLabel.setText("Complete todos los campos.");
            return;
        }

        mensajeLabel.setText("Registro empresario simulado.");
    }

    @FXML
    private void onVolverLoginClicked() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            Stage stage = (Stage) cedulaField.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
            stage.setTitle("Sistema de Paquetería");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}