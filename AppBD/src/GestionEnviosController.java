import clases.ProtocolActions;
import clases.Request;
import clases.Response;
import clases.SpeederClient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class GestionEnviosController {

    @FXML
    private TableView<?> tableEnvios;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colDestino;

    @FXML
    private TableColumn<?, ?> colEstado;

    @FXML
    private TableColumn<?, ?> colPrecio;

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
    private void onVerEnvios(ActionEvent event) {
        Request request = new Request(ProtocolActions.GET_MY_SHIPMENTS, null);
        sendRequestAsync(request, "Envíos actualizados correctamente", "Error al obtener los envíos");
    }

    @FXML
    private void onSolicitarEnvio(ActionEvent event) {
        Request request = new Request(ProtocolActions.CREATE_SHIPMENT, null);
        sendRequestAsync(request, "Solicitud de envío enviada", "Error al solicitar el envío");
    }

    @FXML
    private void onCancelarEnvio(ActionEvent event) {
        Request request = new Request(ProtocolActions.DELETE_SHIPMENT, null);
        sendRequestAsync(request, "Envío cancelado", "Error al cancelar el envío");
    }

    @FXML
    private void onEditarUbicacion(ActionEvent event) {
        Request request = new Request(ProtocolActions.UPDATE_SHIPMENT_LOCATION, null);
        sendRequestAsync(request, "Ubicación actualizada", "Error al actualizar la ubicación");
    }

    @FXML
    private void onVolverMenuUsuario(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menu_usuario.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tableEnvios.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Menú de usuario - Sistema de Paquetería");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}