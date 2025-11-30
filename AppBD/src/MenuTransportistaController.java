import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuTransportistaController {

    @FXML
    private Button btnEnviosAsignados;

    @FXML
    private Button btnAceptarEnvios;

    @FXML
    private Button btnMisVehiculos;

    @FXML
    private Button btnFondos;

    @FXML
    private Button btnConfig;

    @FXML
    private Button btnLogout;

    @FXML
    private void onEnviosAsignados() {
        abrirVista("envios_transportista.fxml", "Envíos asignados");
    }

    @FXML
    private void onAceptarEnvios() {
        abrirVista("aceptar_envios_transportista.fxml", "Aceptar nuevos envíos");
    }

    @FXML
    private void onMisVehiculos() {
        abrirVista("vehiculos_transportista.fxml", "Mis vehículos");
    }

    @FXML
    private void onFondos() {
        abrirVista("fondos_transportista.fxml", "Fondos del transportista");
    }

    @FXML
    private void onConfiguracion() {
        abrirVista("configuracion_transportista.fxml", "Configuración transportista");
    }

    @FXML
    private void onLogout() {
        abrirVista("login.fxml", "Sistema de Paquetería");
    }

    private void abrirVista(String fxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = (Stage) btnEnviosAsignados.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle(titulo);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}