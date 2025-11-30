import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class FondosTransportistaController {

    @FXML
    private Label lblSaldoActual;

    @FXML
    private TextField tfMontoRetiro;

    @FXML
    private TableView<?> tableMovimientos;

    @FXML
    private TableColumn<?, ?> colFecha;

    @FXML
    private TableColumn<?, ?> colDescripcion;

    @FXML
    private TableColumn<?, ?> colMonto;

    @FXML
    private void initialize() {
        lblSaldoActual.setText("$0.00");
    }

    @FXML
    private void onRetirarFondos() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Retiro");
        alert.setHeaderText(null);
        alert.setContentText("Aquí iría la lógica para retirar fondos.");
        alert.showAndWait();
    }

    @FXML
    private void onVolverMenuTransportista() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menu_transportista.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) lblSaldoActual.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Menú transportista");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al volver");
            alert.setHeaderText(null);
            alert.setContentText("No se pudo volver al menú de transportista.");
            alert.showAndWait();
        }
    }
}