import clases.ProtocolActions;
import clases.Request;
import clases.Response;
import clases.Shipment;
import clases.SpeederClient;
import clases.Vehicle;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AceptarEnviosTransportistaController {

    @FXML
    private TableView<Shipment> tableEnviosDisponibles;

    @FXML
    private TableColumn<Shipment, Number> colId;

    @FXML
    private TableColumn<Shipment, String> colDestino;

    @FXML
    private TableColumn<Shipment, String> colEstado;

    @FXML
    private TableColumn<Shipment, Number> colPrecio;

    @FXML
    private TableColumn<Shipment, String> colDescripcion;

    @FXML
    private void initialize() {
        if (colId != null) {
            colId.setCellValueFactory(c -> c.getValue().idProperty());
        }
        if (colDestino != null) {
            colDestino.setCellValueFactory(c -> c.getValue().destinoProperty());
        }
        if (colEstado != null) {
            colEstado.setCellValueFactory(c -> c.getValue().estadoProperty());
        }
        if (colPrecio != null) {
            colPrecio.setCellValueFactory(c -> c.getValue().precioEstimadoProperty());
        }
        if (colDescripcion != null) {
            colDescripcion.setCellValueFactory(c -> c.getValue().descripcionProperty());
        }

        cargarEnviosDisponibles();
    }

    @FXML
    private void onVolverMenuTransportista() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menu_transportista.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tableEnviosDisponibles.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Menú transportista");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarError("No se pudo volver al menú de transportista.");
        }
    }

    @FXML
    private void onAceptarEnvio() {
        Shipment seleccionado = tableEnviosDisponibles.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAdvertencia("Seleccione un envío para aceptarlo.");
            return;
        }

        new Thread(() -> {
            List<Vehicle> vehiculos = obtenerVehiculosDesdeServidor();
            if (vehiculos == null || vehiculos.isEmpty()) {
                Platform.runLater(() ->
                        mostrarAdvertencia("No tienes vehículos registrados para asignar a este envío.")
                );
                return;
            }

            Platform.runLater(() -> {
                ChoiceDialog<Vehicle> dialog = new ChoiceDialog<>(vehiculos.get(0), vehiculos);
                dialog.setTitle("Seleccionar vehículo");
                dialog.setHeaderText(null);
                dialog.setContentText("Elija el vehículo para este envío:");

                Optional<Vehicle> result = dialog.showAndWait();
                result.ifPresent(vehiculoElegido -> aceptarEnvioServidor(seleccionado, vehiculoElegido));
            });
        }).start();
    }

    @FXML
    private void onOcultarEnvio() {
        Shipment seleccionado = tableEnviosDisponibles.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAdvertencia("Seleccione un envío para ocultarlo.");
            return;
        }

        Request request = new Request(ProtocolActions.HIDE_AVAILABLE_SHIPMENT, seleccionado.getId());

        new Thread(() -> {
            Response response = SpeederClient.getInstance().sendRequest(request);
            Platform.runLater(() -> {
                if (response != null && "SUCCESS".equalsIgnoreCase(response.getStatus())) {
                    tableEnviosDisponibles.getItems().remove(seleccionado);
                    mostrarInfo("El envío ha sido ocultado de tu lista.");
                } else {
                    mostrarError("No se pudo ocultar el envío.");
                }
            });
        }).start();
    }

    private void cargarEnviosDisponibles() {
        Request request = new Request(ProtocolActions.GET_AVAILABLE_SHIPMENTS, null);

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
                tableEnviosDisponibles.getItems().setAll(envios);
            });
        }).start();
    }

    private List<Vehicle> obtenerVehiculosDesdeServidor() {
        Request request = new Request(ProtocolActions.GET_MY_VEHICLES, null);
        Response response = SpeederClient.getInstance().sendRequest(request);
        if (response == null || response.getData() == null) {
            return null;
        }
        Gson gson = new Gson();
        String json = gson.toJson(response.getData());
        Type listType = new TypeToken<ArrayList<Vehicle>>() {}.getType();
        return gson.fromJson(json, listType);
    }

    private void aceptarEnvioServidor(Shipment envio, Vehicle vehiculo) {
        AceptarEnvioPayload payload = new AceptarEnvioPayload(envio.getId(), vehiculo.getIdVehiculo());
        Request request = new Request(ProtocolActions.ACCEPT_AVAILABLE_SHIPMENT, payload);

        Response response = SpeederClient.getInstance().sendRequest(request);
        Platform.runLater(() -> {
            if (response != null && "SUCCESS".equalsIgnoreCase(response.getStatus())) {
                tableEnviosDisponibles.getItems().remove(envio);
                mostrarInfo("Envío aceptado y asignado al vehículo seleccionado.");
            } else {
                mostrarError("No se pudo aceptar el envío.");
            }
        });
    }

    private void mostrarAdvertencia(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void mostrarError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void mostrarInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static class AceptarEnvioPayload {
        private int idEnvio;
        private int idVehiculo;

        public AceptarEnvioPayload(int idEnvio, int idVehiculo) {
            this.idEnvio = idEnvio;
            this.idVehiculo = idVehiculo;
        }

        public int getIdEnvio() {
            return idEnvio;
        }

        public int getIdVehiculo() {
            return idVehiculo;
        }
    }
}