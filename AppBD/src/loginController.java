import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

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
            return;
        }

        irMenuUsuario();
    }

    // SOLO si luego agregas un botón "Iniciar sesión como transportista"
    @FXML
    private void onLoginTransportistaClicked() {
        String usuario = usernameTextField.getText();
        String password = passwordPasswordField.getText();

        if (usuario == null || usuario.isEmpty()
                || password == null || password.isEmpty()) {
            messageLabel.setText("Por favor ingrese usuario y contraseña.");
            return;
        }

        irMenuTransportista();
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
            Parent root = FXMLLoader.load(getClass().getResource("menu_usuario.fxml"));
            Stage stage = (Stage) usernameTextField.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Menú de usuario - Sistema de Paquetería");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Error al abrir el menú de usuario.");
        }
    }

    private void irMenuTransportista() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("menu_transportista.fxml"));
            Stage stage = (Stage) usernameTextField.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Menú Transportista - Sistema de Paquetería");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Error al abrir el menú de transportista.");
        }
    }

    private void irRegistroTransportista() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("registro_transportista.fxml"));
            Stage stage = (Stage) usernameTextField.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Registro Transportista - Sistema de Paquetería");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Error al abrir registro de transportista.");
        }
    }

    private void irRegistroEmpresario() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("registro_empresario.fxml"));
            Stage stage = (Stage) usernameTextField.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Registro Empresario - Sistema de Paquetería");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Error al abrir registro de empresario.");
        }
    }
}