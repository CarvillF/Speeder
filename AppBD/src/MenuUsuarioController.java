import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuUsuarioController {

    @FXML
    private Button btnRevisarEnvios;

    @FXML
    private Button btnConfigUsuario;

    @FXML
    private Button btnGestionEnvios;

    @FXML
    private Button btnLogout;

    @FXML
    private Label sectionTitleLabel;

    @FXML
    private Label sectionDescriptionLabel;

    @FXML
    private void initialize() {
        if (sectionTitleLabel != null) {
            sectionTitleLabel.setText("Seleccione una opción del menú");
        }
        if (sectionDescriptionLabel != null) {
            sectionDescriptionLabel.setText("Aquí se mostrará la información de la opción seleccionada.");
        }
    }

    @FXML
    private void onRevisarEnvios() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("revisar_envios.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnRevisarEnvios.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Revisar envíos");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            sectionDescriptionLabel.setText("Error al abrir la vista de envíos.");
        }
    }

    @FXML
    private void onConfigUsuario() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("configuracion.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnConfigUsuario.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Configuración");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            sectionDescriptionLabel.setText("Error al abrir configuración.");
        }
    }

    @FXML
    private void onGestionEnvios() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("gestion_envios.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnGestionEnvios.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Gestión de envíos");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            sectionDescriptionLabel.setText("Error al abrir gestión de envíos.");
        }
    }

    @FXML
    private void onLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnLogout.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
            stage.setTitle("Sistema de Paquetería");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            sectionDescriptionLabel.setText("Error al cerrar sesión.");
        }
    }
}