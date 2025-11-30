import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuTransportistaController {

    @FXML
    private Button btnEnviosAsignados;

    @FXML
    private Button btnMisVehiculos;

    @FXML
    private Button btnLogout;

    @FXML
    private Label sectionTitleLabel;

    @FXML
    private Label sectionDescriptionLabel;

    @FXML
    private void initialize() {
        if (sectionTitleLabel != null) {
            sectionTitleLabel.setText("Menú transportista");
        }
        if (sectionDescriptionLabel != null) {
            sectionDescriptionLabel.setText("Seleccione una opción de la barra izquierda para continuar.");
        }
    }

    @FXML
    private void onEnviosAsignados() {
        cambiarEscena("envios_transportista.fxml", "Envíos asignados", 800, 600);
    }

    @FXML
    private void onMisVehiculos() {
        cambiarEscena("vehiculos_transportista.fxml", "Mis vehículos", 800, 600);
    }

    @FXML
    private void onLogout() {
        cambiarEscena("login.fxml", "Sistema de Paquetería", 600, 400);
    }

    private void cambiarEscena(String fxml, String titulo, int width, int height) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = (Stage) btnEnviosAsignados.getScene().getWindow();
            stage.setScene(new Scene(root, width, height));
            stage.setTitle(titulo);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}