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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EnviosTransportistaController {

    @FXML
    private Button btnVolverMenu;

    @FXML
    private ChoiceBox<String> cbFiltroEstado;

    @FXML
    private TextField tfBuscar;

    @FXML
    private TableView<?> tableEnvios;

    @FXML
    private TableColumn<?, ?> colIdEnvio;

    @FXML
    private TableColumn<?, ?> colOrigen;

    @FXML
    private TableColumn<?, ?> colDestino;

    @FXML
    private TableColumn<?, ?> colEstado;

    @FXML
    private TableColumn<?, ?> colFechaInicio;

    @FXML
    private Button btnActualizar;

    @FXML
    private Button btnMarcarRecogido;

    @FXML
    private Button btnMarcarEntregado;

    @FXML
    private void initialize() {
        if (cbFiltroEstado != null) {
            cbFiltroEstado.getItems().addAll(
                    "Todos",
                    "PENDIENTE",
                    "EN CAMINO",
                    "ENTREGADO",
                    "CANCELADO"
            );
            cbFiltroEstado.setValue("Todos");
        }
    }

    @FXML
    private void onVolverMenuTransportista() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menu_transportista.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnVolverMenu.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Menú transportista");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendRequestAsync(Request request, String successMsg, String errorPrefix) {
        new Thread(() -> {
            Response response = SpeederClient.getInstance().sendRequest(request);
            Platform.runLater(() -> {
                Alert alert;
                if (response != null && "SUCCESS".equals(response.getStatus())) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setTitle("Operación exitosa");
                    alert.setContentText(successMsg);
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setTitle("Error");
                    String msg = errorPrefix;
                    if (response != null && response.getMessage() != null) {
                        msg += ": " + response.getMessage();
                    }
                    alert.setContentText(msg);
                }
                alert.showAndWait();
            });
        }).start();
    }

    @FXML
    private void onActualizarLista() {
        String estado = cbFiltroEstado != null ? cbFiltroEstado.getValue() : "Todos";
        String texto = tfBuscar != null ? tfBuscar.getText() : "";

        Map<String, String> payload = new HashMap<>();
        payload.put("estado", estado);
        payload.put("buscar", texto);

        Request request = new Request(ProtocolActions.GET_ASSIGNED_SHIPMENTS, payload);
        sendRequestAsync(request,
                "Lista de envíos actualizada.",
                "Error al obtener los envíos asignados");
    }

    @FXML
    private void onMarcarRecogido() {
        Object seleccionado = tableEnvios != null
                ? tableEnvios.getSelectionModel().getSelectedItem()
                : null;

        Map<String, Object> payload = new HashMap<>();
        payload.put("envioSeleccionado", seleccionado);
        payload.put("nuevoEstado", "RECOGIDA");

        Request request = new Request(ProtocolActions.UPDATE_SHIPMENT_STATE, payload);
        sendRequestAsync(request,
                "Envío marcado como recogido.",
                "Error al actualizar estado del envío");
    }

    @FXML
    private void onMarcarEntregado() {
        Object seleccionado = tableEnvios != null
                ? tableEnvios.getSelectionModel().getSelectedItem()
                : null;

        Map<String, Object> payload = new HashMap<>();
        payload.put("envioSeleccionado", seleccionado);
        payload.put("nuevoEstado", "ENTREGADO");

        Request request = new Request(ProtocolActions.UPDATE_SHIPMENT_STATE, payload);
        sendRequestAsync(request,
                "Envío marcado como entregado.",
                "Error al actualizar estado del envío");
    }
}