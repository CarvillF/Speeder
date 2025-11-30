import clases.ProtocolActions;
import clases.Request;
import clases.Response;
import clases.SpeederClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfiguracionTransportistaController {

    @FXML
    private ChoiceBox<String> cbDisponibilidad;

    @FXML
    private TextField tfZonaCobertura;

    @FXML
    private void initialize() {
        if (cbDisponibilidad != null) {
            cbDisponibilidad.getItems().addAll("Disponible", "No disponible");
            cbDisponibilidad.setValue("Disponible");
        }
    }

    @FXML
    private void onGuardarCambios() {
        String disponibilidad = cbDisponibilidad != null ? cbDisponibilidad.getValue() : null;
        String zona = tfZonaCobertura != null ? tfZonaCobertura.getText() : null;

        if ((disponibilidad == null || disponibilidad.isBlank()) &&
            (zona == null || zona.isBlank())) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Datos incompletos");
            alert.setHeaderText(null);
            alert.setContentText("Ingrese al menos un dato para actualizar.");
            alert.showAndWait();
            return;
        }

        Map<String, String> payload = new HashMap<>();
        if (disponibilidad != null && !disponibilidad.isBlank()) {
            payload.put("disponibilidad", disponibilidad);
        }
        if (zona != null && !zona.isBlank()) {
            payload.put("zonaCobertura", zona);
        }

        Request request = new Request(ProtocolActions.UPDATE_PROFILE, payload);

        new Thread(() -> {
            Response response = SpeederClient.getInstance().sendRequest(request);
            Platform.runLater(() -> {
                Alert alert;
                if (response != null && "SUCCESS".equalsIgnoreCase(response.getStatus())) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Configuración actualizada");
                    alert.setHeaderText(null);
                    alert.setContentText(response.getMessage() != null
                            ? response.getMessage()
                            : "Los datos del transportista se actualizaron correctamente.");
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error al actualizar");
                    alert.setHeaderText(null);
                    String msg = (response != null && response.getMessage() != null)
                            ? response.getMessage()
                            : "No se pudo actualizar la configuración.";
                    alert.setContentText(msg);
                }
                alert.showAndWait();
            });
        }).start();
    }

    @FXML
    private void onVolverMenuTransportista() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menu_transportista.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tfZonaCobertura.getScene().getWindow();
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