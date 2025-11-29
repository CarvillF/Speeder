import clases.ProtocolActions;
import clases.Request;
import clases.Response;
import clases.SpeederClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminGeneralController {

    @FXML
    private Button btnGeneral;

    @FXML
    private Button btnInforme;

    @FXML
    private TabPane tabPaneEntidades;

    // Usuarios
    @FXML
    private ChoiceBox<String> cbFiltroUsuarios;
    @FXML
    private TextField tfBuscarUsuarios;
    @FXML
    private TableView<Map<String, Object>> tableUsuarios;

    // Transportistas
    @FXML
    private ChoiceBox<String> cbFiltroTransportistas;
    @FXML
    private TextField tfBuscarTransportistas;
    @FXML
    private TableView<Map<String, Object>> tableTransportistas;

    // Admins
    @FXML
    private ChoiceBox<String> cbFiltroAdmins;
    @FXML
    private TextField tfBuscarAdmins;
    @FXML
    private TableView<Map<String, Object>> tableAdmins;

    // Botones crear / eliminar
    @FXML
    private Button btnCrearUsuario;
    @FXML
    private Button btnEliminarUsuario;
    @FXML
    private Button btnCrearTransportista;
    @FXML
    private Button btnEliminarTransportista;
    @FXML
    private Button btnCrearAdmin;
    @FXML
    private Button btnEliminarAdmin;

    // Panel derecho: envíos
    @FXML
    private ChoiceBox<String> cbFiltroEnvios;
    @FXML
    private TextField tfBuscarEnvios;
    @FXML
    private ListView<Map<String, Object>> listEnviosPendientes;
    @FXML
    private Button btnAnularEnvio;

    private final Gson gson = new Gson();

    @FXML
    private void initialize() {
        if (cbFiltroUsuarios != null) {
            cbFiltroUsuarios.getItems().addAll("Todos", "Cédula", "Nombre", "Correo");
            cbFiltroUsuarios.setValue("Todos");
        }
        if (cbFiltroTransportistas != null) {
            cbFiltroTransportistas.getItems().addAll("Todos", "Cédula", "Nombre", "Zona");
            cbFiltroTransportistas.setValue("Todos");
        }
        if (cbFiltroAdmins != null) {
            cbFiltroAdmins.getItems().addAll("Todos", "Cédula", "Nombre", "Correo");
            cbFiltroAdmins.setValue("Todos");
        }
        if (cbFiltroEnvios != null) {
            cbFiltroEnvios.getItems().addAll("Todos", "Empresario", "Transportista");
            cbFiltroEnvios.setValue("Todos");
        }

        cargarUsuarios("ALL");
        cargarEnviosPendientes(null);
    }

    @FXML
    private void onGeneralClicked() {
        if (tabPaneEntidades != null) {
            tabPaneEntidades.getSelectionModel().select(0);
        }
        cargarUsuarios("ALL");
    }

    @FXML
    private void onInformeClicked() {
        // Aquí en el futuro abrirás la vista de informes
        System.out.println("Informe (pendiente de implementación)");
    }

    private void cargarUsuarios(String tipo) {
        Request request = new Request(ProtocolActions.GET_ALL_USERS, tipo);

        new Thread(() -> {
            SpeederClient client = SpeederClient.getInstance();
            Response response = client.sendRequest(request);

            if (response != null && "SUCCESS".equalsIgnoreCase(response.getStatus())) {
                String json = gson.toJson(response.getData());
                Type listType = new TypeToken<ArrayList<Map<String, Object>>>() {}.getType();
                List<Map<String, Object>> lista = gson.fromJson(json, listType);

                Platform.runLater(() -> {
                    if (tabPaneEntidades.getSelectionModel().getSelectedIndex() == 0) {
                        tableUsuarios.getItems().setAll(lista);
                    }
                    // Si quisieras dividir por rol, podrías hacerlo aquí.
                });
            } else {
                Platform.runLater(() -> System.out.println("Error al obtener usuarios"));
            }
        }).start();
    }

    private void cargarEnviosPendientes(String filtro) {
        Request request = new Request(ProtocolActions.GET_MY_SHIPMENTS, filtro);

        new Thread(() -> {
            SpeederClient client = SpeederClient.getInstance();
            Response response = client.sendRequest(request);

            if (response != null && "SUCCESS".equalsIgnoreCase(response.getStatus())) {
                String json = gson.toJson(response.getData());
                Type listType = new TypeToken<ArrayList<Map<String, Object>>>() {}.getType();
                List<Map<String, Object>> lista = gson.fromJson(json, listType);

                Platform.runLater(() -> listEnviosPendientes.getItems().setAll(lista));
            } else {
                Platform.runLater(() -> System.out.println("Error al obtener envíos"));
            }
        }).start();
    }

    @FXML
    private void onCrearUsuario() {
        System.out.println("Crear usuario (pendiente, se haría otro Request)");
    }

    @FXML
    private void onEliminarUsuario() {
        Map<String, Object> seleccionado = tableUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;

        Object id = seleccionado.get("id");
        Request request = new Request(ProtocolActions.DELETE_USER, id);

        new Thread(() -> {
            SpeederClient client = SpeederClient.getInstance();
            Response response = client.sendRequest(request);

            Platform.runLater(() -> {
                if (response != null && "SUCCESS".equalsIgnoreCase(response.getStatus())) {
                    tableUsuarios.getItems().remove(seleccionado);
                } else {
                    System.out.println("Error al eliminar usuario");
                }
            });
        }).start();
    }

    @FXML
    private void onCrearTransportista() {
        System.out.println("Crear transportista (pendiente)");
    }

    @FXML
    private void onEliminarTransportista() {
        Map<String, Object> seleccionado = tableTransportistas.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;

        Object id = seleccionado.get("id");
        Request request = new Request(ProtocolActions.DELETE_USER, id);

        new Thread(() -> {
            SpeederClient client = SpeederClient.getInstance();
            Response response = client.sendRequest(request);

            Platform.runLater(() -> {
                if (response != null && "SUCCESS".equalsIgnoreCase(response.getStatus())) {
                    tableTransportistas.getItems().remove(seleccionado);
                } else {
                    System.out.println("Error al eliminar transportista");
                }
            });
        }).start();
    }

    @FXML
    private void onCrearAdmin() {
        System.out.println("Crear admin (pendiente)");
    }

    @FXML
    private void onEliminarAdmin() {
        Map<String, Object> seleccionado = tableAdmins.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;

        Object id = seleccionado.get("id");
        Request request = new Request(ProtocolActions.DELETE_USER, id);

        new Thread(() -> {
            SpeederClient client = SpeederClient.getInstance();
            Response response = client.sendRequest(request);

            Platform.runLater(() -> {
                if (response != null && "SUCCESS".equalsIgnoreCase(response.getStatus())) {
                    tableAdmins.getItems().remove(seleccionado);
                } else {
                    System.out.println("Error al eliminar admin");
                }
            });
        }).start();
    }

    @FXML
    private void onAnularEnvio() {
        Map<String, Object> seleccionado = listEnviosPendientes.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;

        Object idEnvio = seleccionado.get("id");
        Request request = new Request(ProtocolActions.CANCEL_SHIPMENT, idEnvio);

        new Thread(() -> {
            SpeederClient client = SpeederClient.getInstance();
            Response response = client.sendRequest(request);

            Platform.runLater(() -> {
                if (response != null && "SUCCESS".equalsIgnoreCase(response.getStatus())) {
                    listEnviosPendientes.getItems().remove(seleccionado);
                } else {
                    System.out.println("Error al anular envío");
                }
            });
        }).start();
    }
}