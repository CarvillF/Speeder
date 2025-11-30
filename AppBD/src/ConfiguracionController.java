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
    private Button btnConfigSucursales;

    @FXML
    private Button btnGestionPago;

    @FXML
    private Button btnVolverMenu;

    @FXML
    private void onCambiarUsuario() {
        cambiarEscena("configuracion_usuario.fxml", "Configuración de usuario", 800, 600);
    }

    @FXML
    private void onCambiarEmprendimiento() {
        cambiarEscena("configuracion_emprendimiento.fxml", "Configuración de emprendimientos", 800, 600);
    }

    @FXML
    private void onConfigSucursales() {
        cambiarEscena("configuracion_sucursales.fxml", "Configuración de sucursales", 800, 600);
    }

    @FXML
    private void onGestionPago() {
        cambiarEscena("configuracion_metodos_pago.fxml", "Gestión de métodos de pago", 800, 600);
    }

    @FXML
    private void onVolverMenuUsuario() {
        cambiarEscena("menu_usuario.fxml", "Menú usuario", 800, 600);
    }

    private void cambiarEscena(String fxml, String titulo, int w, int h) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = (Stage) btnCambiarUsuario.getScene().getWindow();
            stage.setScene(new Scene(root, w, h));
            stage.setTitle(titulo);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}