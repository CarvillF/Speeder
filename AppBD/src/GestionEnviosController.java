import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class GestionEnviosController {

    @FXML
    private Button btnVisualizarEnvios;

    @FXML
    private Button btnAgregarEnvio;

    @FXML
    private Button btnEliminarEnvio;

    @FXML
    private Button btnEditarUbicacion;

    @FXML
    private Button btnVolverMenu;

    @FXML
    private void initialize() {
    }

    @FXML
    private void onVisualizarEnvios() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Visualización de envíos");
        alert.setHeaderText(null);
        alert.setContentText("Aquí se mostrarán los envíos actuales en la tabla (más adelante con base de datos).");
        alert.showAndWait();
    }

    @FXML
    private void onAgregarEnvio() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Añadir envío");
        alert.setHeaderText("Datos del envío + precio estimado");
        alert.setContentText("Aquí aparecería un formulario para ingresar los datos del envío y calcular un precio estimado.");
        alert.showAndWait();
    }

    @FXML
    private void onEliminarEnvio() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminar envío");
        alert.setHeaderText("Advertencia");
        alert.setContentText("Aquí se pediría confirmación antes de eliminar un envío seleccionado.");
        alert.showAndWait();
    }

    @FXML
    private void onEditarUbicacion() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Editar ubicación");
        alert.setHeaderText(null);
        alert.setContentText("Aquí se podría cambiar la ubicación/estado del envío seleccionado.");
        alert.showAndWait();
    }

    @FXML
    private void onVolverMenuUsuario() {
        try {
            URL fxml = getClass().getResource("menu_usuario.fxml");
            Parent root = FXMLLoader.load(fxml);
            Stage stage = (Stage) btnVolverMenu.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Menú de usuario - Sistema de Paquetería");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}