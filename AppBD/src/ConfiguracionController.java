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
        // Por ahora no hace nada
    }

    @FXML
    private void onCambiarUsuario() {
        // Aquí luego se mostrará la vista/formulario para editar usuario
    }

    @FXML
    private void onCambiarEmprendimiento() {
        // Aquí luego se mostrará la vista/formulario para editar emprendimiento
    }

    @FXML
    private void onGestionPago() {
        // Aquí luego se mostrará la vista/formulario para métodos de pago
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

