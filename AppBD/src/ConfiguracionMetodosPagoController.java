import clases.PaymentMethod;
import clases.ProtocolActions;
import clases.Request;
import clases.Response;
import clases.SpeederClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ConfiguracionMetodosPagoController {

    @FXML
    private TableView<PaymentMethod> tableMetodosPago;

    @FXML
    private TableColumn<PaymentMethod, Number> colId;

    @FXML
    private TableColumn<PaymentMethod, String> colTipo;

    @FXML
    private TableColumn<PaymentMethod, String> colDatos;

    @FXML
    private TableColumn<PaymentMethod, Boolean> colPredeterminado;

    @FXML
    private TableColumn<PaymentMethod, String> colFecha;

    @FXML
    private TextField tfTipo;

    @FXML
    private TextField tfDatos;

    private Timeline autoRefreshTimeline;

    @FXML
    private void initialize() {
        if (colId != null) {
            colId.setCellValueFactory(c -> c.getValue().idMetodoProperty());
        }
        if (colTipo != null) {
            colTipo.setCellValueFactory(c -> c.getValue().tipoProperty());
        }
        if (colDatos != null) {
            colDatos.setCellValueFactory(c -> c.getValue().datosProperty());
        }
        if (colFecha != null) {
            colFecha.setCellValueFactory(c -> c.getValue().createdAtProperty());
        }
        if (colPredeterminado != null) {
            colPredeterminado.setCellValueFactory(c -> c.getValue().predeterminadoProperty());
            colPredeterminado.setCellFactory(CheckBoxTableCell.forTableColumn(colPredeterminado));
        }

        cargarMetodosDesdeServidor();
        iniciarAutoRefresh();
    }

    private void iniciarAutoRefresh() {
        autoRefreshTimeline = new Timeline(
                new KeyFrame(Duration.seconds(15), e -> cargarMetodosDesdeServidor())
        );
        autoRefreshTimeline.setCycleCount(Animation.INDEFINITE);
        autoRefreshTimeline.play();
    }

    @FXML
    private void onAgregarMetodo() {
        String tipo = tfTipo != null ? tfTipo.getText() : null;
        String datos = tfDatos != null ? tfDatos.getText() : null;

        if (tipo == null || tipo.isBlank() || datos == null || datos.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Datos incompletos");
            alert.setHeaderText(null);
            alert.setContentText("Ingrese tipo y datos del método de pago.");
            alert.showAndWait();
            return;
        }

        PaymentMethod nuevo = new PaymentMethod();
        nuevo.setTipo(tipo);
        nuevo.setDatos(datos);
        nuevo.setPredeterminado(false);

        Request request = new Request(ProtocolActions.CREATE_PAYMENT_METHOD, nuevo);

        new Thread(() -> {
            Response response = SpeederClient.getInstance().sendRequest(request);
            Platform.runLater(() -> {
                Alert alert;
                if (response != null && "SUCCESS".equalsIgnoreCase(response.getStatus())) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Método agregado");
                    alert.setHeaderText(null);
                    alert.setContentText(response.getMessage() != null
                            ? response.getMessage()
                            : "El método de pago fue agregado.");
                    if (tfTipo != null) tfTipo.clear();
                    if (tfDatos != null) tfDatos.clear();
                    cargarMetodosDesdeServidor();
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error al agregar");
                    alert.setHeaderText(null);
                    String msg = response != null && response.getMessage() != null
                            ? response.getMessage()
                            : "No se pudo agregar el método de pago.";
                    alert.setContentText(msg);
                }
                alert.showAndWait();
            });
        }).start();
    }

    @FXML
    private void onEliminarMetodo() {
        PaymentMethod seleccionado = tableMetodosPago.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Selección requerida");
            alert.setHeaderText(null);
            alert.setContentText("Seleccione un método de pago para eliminarlo.");
            alert.showAndWait();
            return;
        }

        Request request = new Request(ProtocolActions.DELETE_PAYMENT_METHOD, seleccionado.getIdMetodoPago());

        new Thread(() -> {
            Response response = SpeederClient.getInstance().sendRequest(request);
            Platform.runLater(() -> {
                Alert alert;
                if (response != null && "SUCCESS".equalsIgnoreCase(response.getStatus())) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Método eliminado");
                    alert.setHeaderText(null);
                    alert.setContentText(response.getMessage() != null
                            ? response.getMessage()
                            : "El método de pago fue eliminado.");
                    tableMetodosPago.getItems().remove(seleccionado);
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error al eliminar");
                    alert.setHeaderText(null);
                    String msg = response != null && response.getMessage() != null
                            ? response.getMessage()
                            : "No se pudo eliminar el método de pago.";
                    alert.setContentText(msg);
                }
                alert.showAndWait();
            });
        }).start();
    }

    @FXML
    private void onVolverConfiguracion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("configuracion.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tableMetodosPago.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Configuración");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al volver");
            alert.setHeaderText(null);
            alert.setContentText("No se pudo volver al menú de configuración.");
            alert.showAndWait();
        }
    }

    private void cargarMetodosDesdeServidor() {
        Request request = new Request(ProtocolActions.GET_PAYMENT_METHODS, null);

        new Thread(() -> {
            Response response = SpeederClient.getInstance().sendRequest(request);
            if (response == null || response.getData() == null) {
                return;
            }
            Gson gson = new Gson();
            String json = gson.toJson(response.getData());
            Type listType = new TypeToken<ArrayList<PaymentMethod>>() {}.getType();
            List<PaymentMethod> metodos = gson.fromJson(json, listType);

            Platform.runLater(() -> tableMetodosPago.getItems().setAll(metodos));
        }).start();
    }
}