import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class ConfiguracionController {

    @FXML
    private Button btnCambiarUsuario;

    @FXML
    private Button btnCambiarEmprendimiento;

    @FXML
    private Button btnGestionPago;

    @FXML
    private Button btnVolverMenu;

    @FXML
    private void initialize() {
    }

    @FXML
    private void onCambiarUsuario() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("config_usuario.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnCambiarUsuario.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Cambiar datos de usuario");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onCambiarEmprendimiento() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("config_emprendimiento.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnCambiarEmprendimiento.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Cambiar datos de emprendimiento");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onGestionPago() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("config_metodos_pago.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnGestionPago.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Gestionar métodos de pago");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onVolverMenuUsuario() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menu_usuario.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnVolverMenu.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Menú de usuario - Sistema de Paquetería");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}