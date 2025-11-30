import clases.ProtocolActions;
import clases.Request;
import clases.Response;
import clases.SpeederClient;
import clases.Vehicle;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
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

public class VehiculosTransportistaController {

    @FXML
    private TableView<Vehicle> tableVehiculos;

    @FXML
    private TableColumn<Vehicle, Number> colId;

    @FXML
    private TableColumn<Vehicle, String> colModelo;

    @FXML
    private TableColumn<Vehicle, String> colPlaca;

    @FXML
    private TableColumn<Vehicle, String> colColor;

    @FXML
    private TextField tfModelo;

    @FXML
    private TextField tfColor;

    @FXML
    private TextField tfPlaca;

    @FXML
    private void initialize() {
        if (colId != null) {
            colId.setCellValueFactory(c ->
                    new ReadOnlyIntegerWrapper(c.getValue().getIdVehiculo()));
        }
        if (colModelo != null) {
            colModelo.setCellValueFactory(c ->
                    new ReadOnlyStringWrapper(c.getValue().getNombreModelo()));
        }
        if (colPlaca != null) {
            colPlaca.setCellValueFactory(c ->
                    new ReadOnlyStringWrapper(c.getValue().getPlaca()));
        }
        if (colColor != null) {
            colColor.setCellValueFactory(c ->
                    new ReadOnlyStringWrapper(c.getValue().getColor()));
        }
        cargarVehiculosDesdeServidor();
    }

    @FXML
    private void onAgregarVehiculo() {
        String modelo = tfModelo != null ? tfModelo.getText() : null;
        String color = tfColor != null ? tfColor.getText() : null;
        String placa = tfPlaca != null ? tfPlaca.getText() : null;

        if (modelo == null || modelo.isBlank()
                || color == null || color.isBlank()
                || placa == null || placa.isBlank()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Datos incompletos");
            alert.setHeaderText(null);
            alert.setContentText("Complete modelo, color y placa.");
            alert.showAndWait();
            return;
        }

        Vehicle nuevo = new Vehicle();
        nuevo.setNombreModelo(modelo);
        nuevo.setColor(color);
        nuevo.setPlaca(placa);

        Request request = new Request(ProtocolActions.CREATE_VEHICLE, nuevo);

        new Thread(() -> {
            Response response = SpeederClient.getInstance().sendRequest(request);
            Platform.runLater(() -> {
                Alert alert;
                if (response != null && "SUCCESS".equalsIgnoreCase(response.getStatus())) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Vehículo creado");
                    alert.setHeaderText(null);
                    alert.setContentText(response.getMessage() != null
                            ? response.getMessage()
                            : "El vehículo fue creado correctamente.");
                    tfModelo.clear();
                    tfColor.clear();
                    tfPlaca.clear();
                    cargarVehiculosDesdeServidor();
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error al crear vehículo");
                    alert.setHeaderText(null);
                    String msg = (response != null && response.getMessage() != null)
                            ? response.getMessage()
                            : "No se pudo crear el vehículo.";
                    alert.setContentText(msg);
                }
                alert.showAndWait();
            });
        }).start();
    }

    @FXML
    private void onEliminarVehiculo() {
        Vehicle seleccionado = tableVehiculos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Selección requerida");
            alert.setHeaderText(null);
            alert.setContentText("Seleccione un vehículo en la tabla para eliminarlo.");
            alert.showAndWait();
            return;
        }

        Request request = new Request(ProtocolActions.DELETE_VEHICLE, seleccionado.getIdVehiculo());

        new Thread(() -> {
            Response response = SpeederClient.getInstance().sendRequest(request);
            Platform.runLater(() -> {
                Alert alert;
                if (response != null && "SUCCESS".equalsIgnoreCase(response.getStatus())) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Vehículo eliminado");
                    alert.setHeaderText(null);
                    alert.setContentText(response.getMessage() != null
                            ? response.getMessage()
                            : "El vehículo fue eliminado.");
                    cargarVehiculosDesdeServidor();
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error al eliminar");
                    alert.setHeaderText(null);
                    String msg = (response != null && response.getMessage() != null)
                            ? response.getMessage()
                            : "No se pudo eliminar el vehículo.";
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
            Stage stage = (Stage) tableVehiculos.getScene().getWindow();
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

    private void cargarVehiculosDesdeServidor() {
        Request request = new Request(ProtocolActions.GET_MY_VEHICLES, null);

        new Thread(() -> {
            Response response = SpeederClient.getInstance().sendRequest(request);
            if (response == null || response.getData() == null) {
                return;
            }
            Gson gson = new Gson();
            String json = gson.toJson(response.getData());
            Type listType = new TypeToken<ArrayList<Vehicle>>() {}.getType();
            List<Vehicle> vehiculos = gson.fromJson(json, listType);

            Platform.runLater(() -> {
                tableVehiculos.getItems().setAll(vehiculos);
            });
        }).start();
    }
}