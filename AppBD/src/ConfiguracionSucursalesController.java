import clases.Branch;
import clases.ProtocolActions;
import clases.Request;
import clases.Response;
import clases.SpeederClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
    private TableColumn<Branch, String> colRuc;

    @FXML
    private TableColumn<Branch, String> colDireccion;

    @FXML
    private TableColumn<Branch, Boolean> colActiva;

    @FXML
    private TextField tfRuc;

    @FXML
    private ChoiceBox<String> cbCiudad;

    @FXML
    private TextField tfCallePrincipal;

    @FXML
    private TextField tfCalleSecundaria;

    @FXML
    private TextField tfNumeroEdificacion;

    @FXML
    private TextField tfDetalleDireccion;

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
        if (colRuc != null) {
            colRuc.setCellValueFactory(c -> c.getValue().companiaRucProperty());
        }

        if (colDireccion != null) {
            colDireccion.setCellValueFactory(c -> {
                Branch b = c.getValue();
                String ciudad = b.getCiudad() != null ? b.getCiudad() : "";
                String calleP = b.getCallePrincipal() != null ? b.getCallePrincipal() : "";
                String calleS = b.getCalleSecundaria() != null ? b.getCalleSecundaria() : "";
                String numero = b.getNumeroEdificacion() != null ? b.getNumeroEdificacion() : "";
                String detalle = b.getDetalleDireccion() != null ? b.getDetalleDireccion() : "";

                String texto = ciudad;
                if (!calleP.isBlank() || !calleS.isBlank()) {
                    if (!texto.isBlank()) texto += ", ";
                    texto += calleP;
                    if (!calleS.isBlank()) {
                        texto += " y " + calleS;
                    }
                }
                if (!numero.isBlank()) {
                    if (!texto.isBlank()) texto += ", ";
                    texto += "N° " + numero;
                }
                if (!detalle.isBlank()) {
                    if (!texto.isBlank()) texto += " - ";
                    texto += detalle;
                }
                return new ReadOnlyStringWrapper(texto);
            });
        }

        if (colActiva != null) {
            colActiva.setCellValueFactory(c -> boleanPropertyFromBranch(c.getValue()));
            colActiva.setCellFactory(CheckBoxTableCell.forTableColumn(colActiva));
        }

        tableSucursales.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSel, newSel) -> mostrarDetalleSucursal(newSel)
        );

        if (cbCiudad != null && cbCiudad.getItems().isEmpty()) {
            cbCiudad.getItems().addAll("Quito", "Guayaquil", "Cuenca");
        }

        cargarSucursalesDesdeServidor();
    }

    private javafx.beans.property.BooleanProperty boleanPropertyFromBranch(Branch branch) {
        return branch.activaProperty();
    }

    @FXML
    private void onAgregarSucursal() {
        String ruc = tfRuc.getText().trim();
        String ciudad = cbCiudad.getValue() != null ? cbCiudad.getValue().trim() : "";
        String callePrincipal = tfCallePrincipal.getText().trim();
        String calleSecundaria = tfCalleSecundaria.getText().trim();
        String numero = tfNumeroEdificacion.getText().trim();
        String detalle = tfDetalleDireccion.getText().trim();
        boolean activa = chkActiva.isSelected();

        if (ruc.isEmpty() || ciudad.isEmpty() || callePrincipal.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Datos incompletos",
                    "RUC, ciudad y calle principal son obligatorios.");
            return;
        }

        Branch nueva = new Branch();
        nueva.setCompaniaRuc(ruc);
        nueva.setCiudad(ciudad);
        nueva.setCallePrincipal(callePrincipal);
        nueva.setCalleSecundaria(calleSecundaria);
        nueva.setNumeroEdificacion(numero);
        nueva.setDetalleDireccion(detalle);
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

        String ruc = tfRuc.getText().trim();
        String ciudad = cbCiudad.getValue() != null ? cbCiudad.getValue().trim() : "";
        String callePrincipal = tfCallePrincipal.getText().trim();
        String calleSecundaria = tfCalleSecundaria.getText().trim();
        String numero = tfNumeroEdificacion.getText().trim();
        String detalle = tfDetalleDireccion.getText().trim();
        boolean activa = chkActiva.isSelected();

        if (ruc.isEmpty() || ciudad.isEmpty() || callePrincipal.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Datos incompletos",
                    "RUC, ciudad y calle principal son obligatorios.");
            return;
        }

        seleccionada.setCompaniaRuc(ruc);
        seleccionada.setCiudad(ciudad);
        seleccionada.setCallePrincipal(callePrincipal);
        seleccionada.setCalleSecundaria(calleSecundaria);
        seleccionada.setNumeroEdificacion(numero);
        seleccionada.setDetalleDireccion(detalle);
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
        tfRuc.setText(b.getCompaniaRuc());
        if (b.getCiudad() != null) {
            cbCiudad.setValue(b.getCiudad());
        } else {
            cbCiudad.setValue(null);
        }
        tfCallePrincipal.setText(b.getCallePrincipal());
        tfCalleSecundaria.setText(b.getCalleSecundaria());
        tfNumeroEdificacion.setText(b.getNumeroEdificacion());
        tfDetalleDireccion.setText(b.getDetalleDireccion());
        chkActiva.setSelected(b.isActiva());
    }

    private void limpiarCampos() {
        tfRuc.clear();
        cbCiudad.setValue(null);
        tfCallePrincipal.clear();
        tfCalleSecundaria.clear();
        tfNumeroEdificacion.clear();
        tfDetalleDireccion.clear();
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