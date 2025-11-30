import clases.Branch;
import clases.Company;
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

public class ConfiguracionEmprendimientoController {

    @FXML
    private TableView<Company> tableEmprendimientos;

    @FXML
    private TableColumn<Company, String> colRuc;

    @FXML
    private TableColumn<Company, String> colNombre;

    @FXML
    private TableColumn<Company, String> colTipo;

    @FXML
    private TableColumn<Company, String> colDescripcion;

    @FXML
    private TextField tfRuc;

    @FXML
    private TextField tfNombre;

    @FXML
    private TextField tfTipo;

    @FXML
    private TextField tfDescripcion;

    @FXML
    private TableView<Branch> tableSucursales;

    @FXML
    private TableColumn<Branch, Number> colSucDireccionId;

    @FXML
    private TableColumn<Branch, String> colSucRuc;

    @FXML
    private TableColumn<Branch, Boolean> colSucActiva;

    @FXML
    private TextField tfSucDireccionId;

    @FXML
    private TextField tfSucRuc;

    @FXML
    private CheckBox chkSucActiva;

    @FXML
    private void initialize() {

        if (colRuc != null) {
            colRuc.setCellValueFactory(c -> c.getValue().rucProperty());
        }
        if (colNombre != null) {
            colNombre.setCellValueFactory(c -> c.getValue().nombreProperty());
        }
        if (colTipo != null) {
            colTipo.setCellValueFactory(c -> c.getValue().tipoProperty());
        }
        if (colDescripcion != null) {
            colDescripcion.setCellValueFactory(c -> c.getValue().descripcionProperty());
        }

        if (tableEmprendimientos != null) {
            tableEmprendimientos.getSelectionModel().selectedItemProperty().addListener(
                    (obs, oldSel, newSel) -> mostrarDetalleEmprendimiento(newSel)
            );
        }


        if (colSucDireccionId != null) {
            colSucDireccionId.setCellValueFactory(c -> c.getValue().direccionIdProperty());
        }
        if (colSucRuc != null) {
            colSucRuc.setCellValueFactory(c -> c.getValue().companiaRucProperty());
        }
        if (colSucActiva != null) {
            colSucActiva.setCellValueFactory(c -> c.getValue().activaProperty());
            colSucActiva.setCellFactory(CheckBoxTableCell.forTableColumn(colSucActiva));
        }

        if (tableSucursales != null) {
            tableSucursales.getSelectionModel().selectedItemProperty().addListener(
                    (obs, oldSel, newSel) -> mostrarDetalleSucursal(newSel)
            );
        }

        cargarEmprendimientosDesdeServidor();
        cargarSucursalesDesdeServidor();
    }


    @FXML
    private void onAgregarEmprendimiento() {
        String ruc = tfRuc.getText().trim();
        String nombre = tfNombre.getText().trim();
        String tipo = tfTipo.getText().trim();
        String descripcion = tfDescripcion.getText().trim();

        if (ruc.isEmpty() || nombre.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Datos incompletos",
                    "RUC y Nombre son obligatorios.");
            return;
        }

        Company nuevo = new Company();
        nuevo.setRuc(ruc);
        nuevo.setNombre(nombre);
        nuevo.setTipo(tipo);
        nuevo.setDescripcion(descripcion);

        Request request = new Request(ProtocolActions.CREATE_COMPANY, nuevo);

        new Thread(() -> {
            Response response = SpeederClient.getInstance().sendRequest(request);
            Platform.runLater(() -> {
                if (response != null && "SUCCESS".equalsIgnoreCase(response.getStatus())) {
                    cargarEmprendimientosDesdeServidor();
                    limpiarCamposEmprendimiento();
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Emprendimiento creado",
                            response.getMessage() != null ? response.getMessage()
                                    : "El emprendimiento fue creado.");
                } else {
                    String msg = response != null && response.getMessage() != null
                            ? response.getMessage()
                            : "No se pudo crear el emprendimiento.";
                    mostrarAlerta(Alert.AlertType.ERROR, "Error al crear", msg);
                }
            });
        }).start();
    }

    @FXML
    private void onActualizarEmprendimiento() {
        Company seleccionado = (tableEmprendimientos != null)
                ? tableEmprendimientos.getSelectionModel().getSelectedItem()
                : null;

        if (seleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección requerida",
                    "Seleccione un emprendimiento para guardar los cambios.");
            return;
        }

        seleccionado.setRuc(tfRuc.getText().trim());
        seleccionado.setNombre(tfNombre.getText().trim());
        seleccionado.setTipo(tfTipo.getText().trim());
        seleccionado.setDescripcion(tfDescripcion.getText().trim());

        Request request = new Request(ProtocolActions.UPDATE_COMPANY, seleccionado);

        new Thread(() -> {
            Response response = SpeederClient.getInstance().sendRequest(request);
            Platform.runLater(() -> {
                if (response != null && "SUCCESS".equalsIgnoreCase(response.getStatus())) {
                    if (tableEmprendimientos != null) {
                        tableEmprendimientos.refresh();
                    }
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Cambios guardados",
                            response.getMessage() != null ? response.getMessage()
                                    : "Los datos se actualizaron correctamente.");
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
    private void onEliminarEmprendimiento() {
        if (tableEmprendimientos == null) {
            return;
        }

        Company seleccionado = tableEmprendimientos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección requerida",
                    "Seleccione un emprendimiento para eliminarlo.");
            return;
        }

        Request request = new Request(ProtocolActions.DELETE_COMPANY, seleccionado.getRuc());

        new Thread(() -> {
            Response response = SpeederClient.getInstance().sendRequest(request);
            Platform.runLater(() -> {
                if (response != null && "SUCCESS".equalsIgnoreCase(response.getStatus())) {
                    tableEmprendimientos.getItems().remove(seleccionado);
                    limpiarCamposEmprendimiento();
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Emprendimiento eliminado",
                            response.getMessage() != null ? response.getMessage()
                                    : "El emprendimiento fue eliminado.");
                } else {
                    String msg = response != null && response.getMessage() != null
                            ? response.getMessage()
                            : "No se pudo eliminar el emprendimiento.";
                    mostrarAlerta(Alert.AlertType.ERROR, "Error al eliminar", msg);
                }
            });
        }).start();
    }


    @FXML
    private void onAgregarSucursal() {
        if (tfSucDireccionId == null || tfSucRuc == null || chkSucActiva == null) {

            return;
        }

        String idTxt = tfSucDireccionId.getText().trim();
        String ruc = tfSucRuc.getText().trim();
        boolean activa = chkSucActiva.isSelected();

        if (idTxt.isEmpty() || ruc.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Datos incompletos",
                    "ID Dirección y RUC de compañía son obligatorios.");
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
                    limpiarCamposSucursal();
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
        if (tableSucursales == null || tfSucDireccionId == null || tfSucRuc == null || chkSucActiva == null) {
            return;
        }

        Branch seleccionada = tableSucursales.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección requerida",
                    "Seleccione una sucursal para guardar los cambios.");
            return;
        }

        String idTxt = tfSucDireccionId.getText().trim();
        String ruc = tfSucRuc.getText().trim();
        boolean activa = chkSucActiva.isSelected();

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
                                    : "Los datos de la sucursal se actualizaron.");
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
        if (tableSucursales == null) {
            return;
        }

        Branch seleccionada = tableSucursales.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección requerida",
                    "Seleccione una sucursal para eliminarla.");
            return;
        }

        Request request = new Request(ProtocolActions.DELETE_BRANCH, seleccionada.getDireccionId());

        new Thread(() -> {
            Response response = SpeederClient.getInstance().sendRequest(request);
            Platform.runLater(() -> {
                if (response != null && "SUCCESS".equalsIgnoreCase(response.getStatus())) {
                    tableSucursales.getItems().remove(seleccionada);
                    limpiarCamposSucursal();
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
            Stage stage = (Stage) tableEmprendimientos.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Configuración");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error al volver",
                    "No se pudo volver al menú de configuración.");
        }
    }


    private void cargarEmprendimientosDesdeServidor() {
        Request request = new Request(ProtocolActions.GET_COMPANIES, null);

        new Thread(() -> {
            Response response = SpeederClient.getInstance().sendRequest(request);
            if (response == null || response.getData() == null) {
                return;
            }
            Gson gson = new Gson();
            String json = gson.toJson(response.getData());
            Type listType = new TypeToken<ArrayList<Company>>() {}.getType();
            List<Company> lista = gson.fromJson(json, listType);

            Platform.runLater(() -> {
                if (tableEmprendimientos != null) {
                    tableEmprendimientos.getItems().setAll(lista);
                }
            });
        }).start();
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
                if (tableSucursales != null) {
                    tableSucursales.getItems().setAll(lista);
                }
            });
        }).start();
    }


    private void mostrarDetalleEmprendimiento(Company c) {
        if (c == null) {
            limpiarCamposEmprendimiento();
            return;
        }
        tfRuc.setText(c.getRuc());
        tfNombre.setText(c.getNombre());
        tfTipo.setText(c.getTipo());
        tfDescripcion.setText(c.getDescripcion());
    }

    private void mostrarDetalleSucursal(Branch b) {
        if (b == null || tfSucDireccionId == null || tfSucRuc == null || chkSucActiva == null) {
            limpiarCamposSucursal();
            return;
        }
        tfSucDireccionId.setText(String.valueOf(b.getDireccionId()));
        tfSucRuc.setText(b.getCompaniaRuc());
        chkSucActiva.setSelected(b.isActiva());
    }

    private void limpiarCamposEmprendimiento() {
        if (tfRuc != null) tfRuc.clear();
        if (tfNombre != null) tfNombre.clear();
        if (tfTipo != null) tfTipo.clear();
        if (tfDescripcion != null) tfDescripcion.clear();
    }

    private void limpiarCamposSucursal() {
        if (tfSucDireccionId != null) tfSucDireccionId.clear();
        if (tfSucRuc != null) tfSucRuc.clear();
        if (chkSucActiva != null) chkSucActiva.setSelected(false);
    }

    private void mostrarAlerta(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}