import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    private void initialize() {
        // Ya no hacemos nada aquí porque no hay sectionTitleLabel ni sectionDescriptionLabel
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
            // Si quieres, aquí luego podemos poner un Alert en vez de usar un Label
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
        }
    }

    @FXML
    private void onLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnLogout.getScene().getWindow();
            // Tamaño del login según tu FXML (900 x 500)
            stage.setScene(new Scene(root, 900, 500));
            stage.setTitle("Sistema de Paquetería");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}