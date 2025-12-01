import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class SeleccionRolDemoController {

    @FXML
    private Button btnEmpresarioDemo;

    @FXML
    private Button btnTransportistaDemo;

    @FXML
    private Button btnAdminDemo;

    @FXML
    private Button btnVolverLogin;

    @FXML
    private void onEmpresarioDemo() {
        abrirVista("menu_usuario.fxml", "Menú usuario (demo)", btnEmpresarioDemo);
    }

    @FXML
    private void onTransportistaDemo() {
        abrirVista("menu_transportista.fxml", "Menú transportista (demo)", btnTransportistaDemo);
    }

    @FXML
    private void onAdminDemo() {
        abrirVista("admin_general.fxml", "Panel administrador (demo)", btnAdminDemo);
    }

    @FXML
    private void onVolverLogin() {
        abrirVista("login.fxml", "Sistema de Paquetería", btnVolverLogin);
    }

    private void abrirVista(String fxml, String titulo, Button sourceButton) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            Stage stage = (Stage) sourceButton.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle(titulo);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}