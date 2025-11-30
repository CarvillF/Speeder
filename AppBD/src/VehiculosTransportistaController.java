import clases.ProtocolActions;
import clases.Request;
import clases.Response;
import clases.SpeederClient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.HashMap;
import java.util.Map;

public class VehiculosTransportistaController {

    @FXML
    private TableView<?> tableVehiculos;

    @FXML
    private TableColumn<?, ?> colIdVehiculo;

    @FXML
    private TableColumn<?, ?> colModelo;

    @FXML
    private TableColumn<?, ?> colAnio;

    @FXML
    private TableColumn<?, ?> colMatricula;

    @FXML
    private TableColumn<?, ?> colVolumen;

    @FXML
    private TextField tfBuscar;

    @FXML
    private TextField tfModelo;

    @FXML
    private TextField tfAnio;

    @FXML
    private TextField tfMatricula;

    @FXML
    private TextField tfVolumen;

    @FXML
    private void initialize() {
    }

    private void sendRequestAsync(Request request, String successMsg, String errorPrefix) {
        new Thread(() -> {
            Response response = SpeederClient.getInstance().sendRequest(request);
            Platform.runLater(() -> {
                Alert alert;
                if (response != null && "SUCCESS".equals(response.getStatus())) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText(successMsg);
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
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
    private void onActualizarLista(ActionEvent event) {
        String texto = tfBuscar != null ? tfBuscar.getText() : "";
        Map<String, String> payload = new HashMap<>();
        payload.put("buscar", texto);

        Request request = new Request(ProtocolActions.GET_MY_VEHICLES, payload);
        sendRequestAsync(request, "Lista de vehículos actualizada", "Error al obtener vehículos");
    }

    @FXML
    private void onAgregarVehiculo(ActionEvent event) {
        Map<String, String> payload = new HashMap<>();
        payload.put("modelo", tfModelo != null ? tfModelo.getText() : "");
        payload.put("anio", tfAnio != null ? tfAnio.getText() : "");
        payload.put("matricula", tfMatricula != null ? tfMatricula.getText() : "");
        payload.put("volumen", tfVolumen != null ? tfVolumen.getText() : "");

        Request request = new Request(ProtocolActions.CREATE_VEHICLE, payload);
        sendRequestAsync(request, "Vehículo creado correctamente", "Error al crear vehículo");
    }

    @FXML
    private void onGuardarCambios(ActionEvent event) {
        Object seleccionado = tableVehiculos != null
                ? tableVehiculos.getSelectionModel().getSelectedItem()
                : null;

        Map<String, Object> payload = new HashMap<>();
        payload.put("vehiculoSeleccionado", seleccionado);
        payload.put("modelo", tfModelo != null ? tfModelo.getText() : "");
        payload.put("anio", tfAnio != null ? tfAnio.getText() : "");
        payload.put("matricula", tfMatricula != null ? tfMatricula.getText() : "");
        payload.put("volumen", tfVolumen != null ? tfVolumen.getText() : "");

        Request request = new Request(ProtocolActions.UPDATE_VEHICLE, payload);
        sendRequestAsync(request, "Vehículo actualizado", "Error al actualizar vehículo");
    }

    @FXML
    private void onEliminarVehiculo(ActionEvent event) {
        Object seleccionado = tableVehiculos != null
                ? tableVehiculos.getSelectionModel().getSelectedItem()
                : null;

        Map<String, Object> payload = new HashMap<>();
        payload.put("vehiculoSeleccionado", seleccionado);

        Request request = new Request(ProtocolActions.DELETE_VEHICLE, payload);
        sendRequestAsync(request, "Vehículo eliminado", "Error al eliminar vehículo");
    }
}
