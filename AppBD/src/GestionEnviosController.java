import clases.ProtocolActions;
import clases.Request;
import clases.Response;
import clases.SpeederClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GestionEnviosController {

    @FXML
    private TableView<Map<String, Object>> tableEnvios;

    @FXML
    private TableColumn<Map<String, Object>, String> colId;
    @FXML
    private TableColumn<Map<String, Object>, String> colDestino;
    @FXML
    private TableColumn<Map<String, Object>, String> colEstado;
    @FXML
    private TableColumn<Map<String, Object>, String> colPrecio;

    private final Gson gson = new Gson();

    @FXML
    private void initialize() {
        // Aquí puedes configurar las cellValueFactory si quieres mostrar campos específicos
    }

    @FXML
    private void onVisualizarEnvios() {
        // Aquí idealmente mandarías el ID de usuario actual, por ahora null
        Request request = new Request(ProtocolActions.GET_MY_SHIPMENTS, null);

        new Thread(() -> {
            SpeederClient client = SpeederClient.getInstance();
            Response response = client.sendRequest(request);

            if (response != null && "SUCCESS".equalsIgnoreCase(response.getStatus())) {
                // Convertir data genérico a List<Map<String,Object>>
                String json = gson.toJson(response.getData());
                Type type = new TypeToken<ArrayList<Map<String, Object>>>() {}.getType();
                List<Map<String, Object>> lista = gson.fromJson(json, type);

                Platform.runLater(() -> {
                    tableEnvios.getItems().setAll(lista);
                });
            } else {
                // Podrías mostrar un label de error en la UI
                Platform.runLater(() -> {
                    System.out.println("Error al obtener envíos");
                });
            }
        }).start();
    }

    @FXML
    private void onAgregarEnvio() {
        // Parecido: crear Request con CREATE_SHIPMENT
    }

    @FXML
    private void onEliminarEnvio() {
        // Parecido: crear Request con DELETE_SHIPMENT y el id seleccionado
    }

    @FXML
    private void onEditarUbicacion() {
        // Parecido: UPDATE_SHIPMENT_LOCATION
    }
}