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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfiguracionTransportistaController {

    @FXML
    private Button btnVolver;

    @FXML
    private ChoiceBox<String> cbDisponibilidad;

    @FXML
    private TextField tfNumeroLicencia;

    @FXML
    private TextField tfTipoLicencia;

    @FXML
    private TextField tfZonaCobertura;

    @FXML
    private void initialize() {
        if (cbDisponibilidad != null) {
            cbDisponibilidad.getItems().addAll(
                    "Disponible",
                    "No disponible"
            );
            cbDisponibilidad.setValue("Disponible");
        }
        // Si luego quieres, aquí podrías cargar los datos actuales desde el servidor.
    }

    @FXML
    private void onGuardarDisponibilidad() {
        String disponibilidad = cbDisponibilidad.getValue();

        if (disponibilidad == null || disponibilidad.isBlank()) {
            mostrarAlerta(Alert.AlertType.WARNING,
                    "Dato requerido",
                    "Seleccione un estado de disponibilidad.");
            return;
        }

        Map<String, String> payload = new HashMap<>();
        payload.put("disponibilidad", disponibilidad);

        Request request = new Request(ProtocolActions.UPDATE_TRANSPORTISTA, payload);

        new Thread(() -> {
            Response response = SpeederClient.getInstance().sendRequest(request);

            Platform.runLater(() -> {
                if (response != null && "SUCCESS".equalsIgnoreCase(response.getStatus())) {
                    mostrarAlerta(Alert.AlertType.INFORMATION,
                            "Disponibilidad actualizada",
                            response.getMessage() != null
                                    ? response.getMessage()
                                    : "La disponibilidad se actualizó correctamente.");
                } else {
                    String msg = (response != null && response.getMessage() != null)
                            ? response.getMessage()
                            : "No se pudo actualizar la disponibilidad.";
                    mostrarAlerta(Alert.AlertType.ERROR,
                            "Error al actualizar",
                            msg);
                }
            });
        }).start();
    }

    @FXML
    private void onGuardarDatos() {
        String numeroLicencia = tfNumeroLicencia.getText();
        String tipoLicencia = tfTipoLicencia.getText();
        String zonaCobertura = tfZonaCobertura.getText();

        if ((numeroLicencia == null || numeroLicencia.isBlank())
                && (tipoLicencia == null || tipoLicencia.isBlank())
                && (zonaCobertura == null || zonaCobertura.isBlank())) {

            mostrarAlerta(Alert.AlertType.WARNING,
                    "Datos incompletos",
                    "Ingrese al menos uno de los campos: número de licencia, tipo de licencia o zona de cobertura.");
            return;
        }

        Map<String, String> payload = new HashMap<>();
        if (numeroLicencia != null && !numeroLicencia.isBlank()) {
            payload.put("numero_licencia", numeroLicencia);
        }
        if (tipoLicencia != null && !tipoLicencia.isBlank()) {
            payload.put("tipo_licencia", tipoLicencia);
        }
        if (zonaCobertura != null && !zonaCobertura.isBlank()) {
            payload.put("zona_cobertura", zonaCobertura);
        }

        Request request = new Request(ProtocolActions.UPDATE_TRANSPORTISTA, payload);

        new Thread(() -> {
            Response response = SpeederClient.getInstance().sendRequest(request);

            Platform.runLater(() -> {
                if (response != null && "SUCCESS".equalsIgnoreCase(response.getStatus())) {
                    mostrarAlerta(Alert.AlertType.INFORMATION,
                            "Datos actualizados",
                            response.getMessage() != null
                                    ? response.getMessage()
                                    : "Los datos del transportista se actualizaron correctamente.");
                } else {
                    String msg = (response != null && response.getMessage() != null)
                            ? response.getMessage()
                            : "No se pudieron actualizar los datos.";
                    mostrarAlerta(Alert.AlertType.ERROR,
                            "Error al actualizar",
                            msg);
                }
            });
        }).start();
    }

    @FXML
    private void onVolverMenuTransportista() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menu_transportista.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnVolver.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Menú transportista");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR,
                    "Error al volver",
                    "No se pudo volver al menú del transportista.");
        }
    }

    private void mostrarAlerta(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}