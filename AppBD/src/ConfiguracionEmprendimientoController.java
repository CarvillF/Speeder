import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class ConfiguracionEmprendimientoController {

    @FXML
    private Button btnVolverMenu;

    @FXML
    private void onIrUsuario() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("configuracion.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnVolverMenu.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Configuración de usuario - Sistema de Paquetería");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onIrMetodosPago() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("configuracion_metodos_pago.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnVolverMenu.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Métodos de pago - Sistema de Paquetería");
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

