import clases.ProtocolActions;
import clases.Request;
import clases.Response;
import clases.Shipment;
import clases.SpeederClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GestionEnviosController {

    @FXML
    private TableView<Shipment> tableEnvios;

    @FXML
    private TableColumn<Shipment, Number> colId;

    @FXML
    private TableColumn<Shipment, String> colDestino;

    @FXML
    private TableColumn<Shipment, String> colEstado;

    @FXML
    private TableColumn<Shipment, Number> colPrecio;

    @FXML
    private TextField tfDestino;

    @FXML
    private TextField tfDescripcion;

    @FXML
    private TextField tfPrecio;

    @FXML
    private void initialize() {
        if (colId != null) {
            colId.setCellValueFactory(c ->
                    new ReadOnlyIntegerWrapper(c.getValue().getId()));
        }
        if (colDestino != null) {
            colDestino.setCellValueFactory(c ->
                    new ReadOnlyStringWrapper(c.getValue().getDestino()));
        }
        if (colEstado != null) {
            colEstado.setCellValueFactory(c ->
                    new ReadOnlyStringWrapper(c.getValue().getEstado()));
        }
        if (colPrecio != null) {
            colPrecio.setCellValueFactory(c ->
                    new ReadOnlyDoubleWrapper(c.getValue().getPrecioEstimado()));
        }
        cargarEnviosDesdeServidor();
    }

    @FXML
    private void onSolicitarEnvio() {
        String destino = tfDestino.getText();
        String descripcion = tfDescripcion.getText();
        String precioStr = tfPrecio.getText();

        if (destino == null || destino.isBlank()
                || descripcion == null || descripcion.isBlank()
                || precioStr == null || precioStr.isBlank()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Datos incompletos");
            alert.setHeaderText(null);
            alert.setContentText("Complete destino, descripción y precio estimado.");
            alert.showAndWait();
            return;
        }

        double precio;
        try {
            precio = Double.parseDouble(precioStr);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Precio inválido");
            alert.setHeaderText(null);
            alert.setContentText("Ingrese un valor numérico válido para el precio.");
            alert.showAndWait();
            return;
        }

        Shipment nuevo = new Shipment();
        nuevo.setDestino(destino);
        nuevo.setDescripcion(descripcion);
        nuevo.setPrecioEstimado(precio);
        nuevo.setEstado("PENDIENTE");

        Request request = new Request(ProtocolActions.CREATE_SHIPMENT, nuevo);

        new Thread(() -> {
            Response response = SpeederClient.getInstance().sendRequest(request);
            Platform.runLater(() -> {
                Alert alert;
                if (response != null && "SUCCESS".equalsIgnoreCase(response.getStatus())) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Envío creado");
                    alert.setHeaderText(null);
                    alert.setContentText(response.getMessage() != null
                            ? response.getMessage()
                            : "El envío fue creado correctamente.");
                    tfDestino.clear();
                    tfDescripcion.clear();
                    tfPrecio.clear();
                    cargarEnviosDesdeServidor();
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error al crear envío");
                    alert.setHeaderText(null);
                    String msg = (response != null && response.getMessage() != null)
                            ? response.getMessage()
                            : "No se pudo crear el envío.";
                    alert.setContentText(msg);
                }
                alert.showAndWait();
            });
        }).start();
    }

    @FXML
    private void onCancelarEnvio() {
        Shipment seleccionado = tableEnvios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Selección requerida");
            alert.setHeaderText(null);
            alert.setContentText("Seleccione un envío en la tabla para cancelarlo.");
            alert.showAndWait();
            return;
        }

        Request request = new Request(ProtocolActions.CANCEL_SHIPMENT, seleccionado.getId());

        new Thread(() -> {
            Response response = SpeederClient.getInstance().sendRequest(request);
            Platform.runLater(() -> {
                Alert alert;
                if (response != null && "SUCCESS".equalsIgnoreCase(response.getStatus())) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Envío cancelado");
                    alert.setHeaderText(null);
                    alert.setContentText(response.getMessage() != null
                            ? response.getMessage()
                            : "El envío fue cancelado.");
                    cargarEnviosDesdeServidor();
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error al cancelar");
                    alert.setHeaderText(null);
                    String msg = (response != null && response.getMessage() != null)
                            ? response.getMessage()
                            : "No se pudo cancelar el envío.";
                    alert.setContentText(msg);
                }
                alert.showAndWait();
            });
        }).start();
    }

    @FXML
    private void onVolverMenuUsuario() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menu_usuario.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tableEnvios.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Menú usuario");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al volver");
            alert.setHeaderText(null);
            alert.setContentText("No se pudo volver al menú de usuario.");
            alert.showAndWait();
        }
    }

    private void cargarEnviosDesdeServidor() {
        Request request = new Request(ProtocolActions.GET_MY_SHIPMENTS, null);

        new Thread(() -> {
            Response response = SpeederClient.getInstance().sendRequest(request);
            if (response == null || response.getData() == null) {
                return;
            }
            Gson gson = new Gson();
            String json = gson.toJson(response.getData());
            Type listType = new TypeToken<ArrayList<Shipment>>() {}.getType();
            List<Shipment> envios = gson.fromJson(json, listType);

            Platform.runLater(() -> {
                tableEnvios.getItems().setAll(envios);
            });
        }).start();
    }
}