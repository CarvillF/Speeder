import clases.Branch;
import clases.ProtocolActions;
import clases.Request;
import clases.Response;
import clases.SpeederClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ConfiguracionSucursalesController {

    @FXML
    private TableView<Branch> tableSucursales;

    @FXML
    private TableColumn<Branch, Number> colDireccionId;

    @FXML
    private TableColumn<Branch, String> colRuc;

    @FXML
    private TableColumn<Branch, Boolean> colActiva;

    @FXML
    private TextField tfDireccionId;

    @FXML
    private TextField tfRuc;

    @FXML
    private CheckBox chkActiva;

    @FXML
    private Button btnAgregarSucursal;

    @FXML
    private Button btnGuardarSucursal;

    @FXML
    private Button btnEliminarSucursal;

    @FXML
    private Button btnVolver;

    @FXML
    private void initialize() {
        if (colDireccionId != null) {
            colDireccionId.setCellValueFactory(c -> c.getValue().direccionIdProperty());
        }
        if (colRuc != null) {
            colRuc.setCellValueFactory(c -> c.getValue().companiaRucProperty());
        }
        if (colActiva != null) {
            colActiva.setCellValueFactory(c -> c.getValue().activaProperty());
            colActiva.setCellFactory(CheckBoxTableCell.forTableColumn(colActiva));
        }

        tableSucursales.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSel, newSel) -> mostrarDetalleSucursal(newSel)
        );

        cargarSucursalesDesdeServidor();
    }

    @FXML
    private void onAgregarSucursal() {
        String idTxt = tfDireccionId.getText().trim();
        String ruc = tfRuc.getText().trim();
        boolean activa = chkActiva.isSelected();

        if (idTxt.isEmpty() || ruc.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Datos incompletos",
                    "ID Dirección y RUC son obligatorios.");
            return;
        }

        int idDireccion;
        try {
            idDireccion = Integer.parseInt(idTxt);
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Formato inválido",
                    "ID Dirección debe ser un número entero.");
            return;
        }

        Branch nueva = new Branch();
        nueva.setDireccionId(idDireccion);
        nueva.setCompaniaRuc(ruc);
        nueva.setActiva(activa);

        Request request = new Request(ProtocolActions.CREATE_BRANCH, nueva);

        new Thread(() -> {
            Response response = SpeederClient.getInstance().sendRequest(request);
            Platform.runLater(() -> {
                if (response != null && "SUCCESS".equalsIgnoreCase(response.getStatus())) {
                    cargarSucursalesDesdeServidor();
                    limpiarCampos();
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Sucursal creada",
                            response.getMessage() != null ? response.getMessage()
                                    : "La sucursal fue creada.");
                } else {
                    String msg = response != null && response.getMessage() != null
                            ? response.getMessage()
                            : "No se pudo crear la sucursal.";
                    mostrarAlerta(Alert.AlertType.ERROR, "Error al crear", msg);
                }
            });
        }).start();
    }

    @FXML
    private void onGuardarSucursal() {
        Branch seleccionada = tableSucursales.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección requerida",
                    "Seleccione una sucursal para guardar los cambios.");
            return;
        }

        String idTxt = tfDireccionId.getText().trim();
        String ruc = tfRuc.getText().trim();
        boolean activa = chkActiva.isSelected();

        int idDireccion;
        try {
            idDireccion = Integer.parseInt(idTxt);
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Formato inválido",
                    "ID Dirección debe ser un número entero.");
            return;
        }

        seleccionada.setDireccionId(idDireccion);
        seleccionada.setCompaniaRuc(ruc);
        seleccionada.setActiva(activa);

        Request request = new Request(ProtocolActions.UPDATE_BRANCH, seleccionada);

        new Thread(() -> {
            Response response = SpeederClient.getInstance().sendRequest(request);
            Platform.runLater(() -> {
                if (response != null && "SUCCESS".equalsIgnoreCase(response.getStatus())) {
                    tableSucursales.refresh();
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Cambios guardados",
                            response.getMessage() != null ? response.getMessage()
                                    : "La sucursal se actualizó correctamente.");
                } else {
                    String msg = response != null && response.getMessage() != null
                            ? response.getMessage()
                            : "No se pudieron guardar los cambios.";
                    mostrarAlerta(Alert.AlertType.ERROR, "Error al guardar", msg);
                }
            });
        }).start();
    }

    @FXML
    private void onEliminarSucursal() {
        Branch seleccionada = tableSucursales.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección requerida",
                    "Seleccione una sucursal para eliminarla.");
            return;
        }

        Request request = new Request(ProtocolActions.DELETE_BRANCH, seleccionada);

        new Thread(() -> {
            Response response = SpeederClient.getInstance().sendRequest(request);
            Platform.runLater(() -> {
                if (response != null && "SUCCESS".equalsIgnoreCase(response.getStatus())) {
                    tableSucursales.getItems().remove(seleccionada);
                    limpiarCampos();
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Sucursal eliminada",
                            response.getMessage() != null ? response.getMessage()
                                    : "La sucursal fue eliminada.");
                } else {
                    String msg = response != null && response.getMessage() != null
                            ? response.getMessage()
                            : "No se pudo eliminar la sucursal.";
                    mostrarAlerta(Alert.AlertType.ERROR, "Error al eliminar", msg);
                }
            });
        }).start();
    }

    @FXML
    private void onVolverConfiguracion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("configuracion.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnVolver.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Configuración");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error al volver",
                    "No se pudo volver al menú de configuración.");
        }
    }

    private void cargarSucursalesDesdeServidor() {
        Request request = new Request(ProtocolActions.GET_BRANCHES, null);

        new Thread(() -> {
            Response response = SpeederClient.getInstance().sendRequest(request);
            if (response == null || response.getData() == null) {
                return;
            }
            Gson gson = new Gson();
            String json = gson.toJson(response.getData());
            Type listType = new TypeToken<ArrayList<Branch>>() {}.getType();
            List<Branch> lista = gson.fromJson(json, listType);

            Platform.runLater(() -> {
                tableSucursales.getItems().setAll(lista);
            });
        }).start();
    }

    private void mostrarDetalleSucursal(Branch b) {
        if (b == null) {
            limpiarCampos();
            return;
        }
        tfDireccionId.setText(String.valueOf(b.getDireccionId()));
        tfRuc.setText(b.getCompaniaRuc());
        chkActiva.setSelected(b.isActiva());
    }

    private void limpiarCampos() {
        tfDireccionId.clear();
        tfRuc.clear();
        chkActiva.setSelected(false);
    }

    private void mostrarAlerta(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}

