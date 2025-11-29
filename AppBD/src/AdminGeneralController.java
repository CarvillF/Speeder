import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;

public class AdminGeneralController {

    @FXML private ChoiceBox<String> cbFiltroUsuarios;
    @FXML private ChoiceBox<String> cbFiltroTransportistas;
    @FXML private ChoiceBox<String> cbFiltroAdmins;
    @FXML private ChoiceBox<String> cbFiltroEnvios;

    @FXML private ListView<String> listEnviosPendientes;

    @FXML
    private void initialize() {
        cbFiltroUsuarios.getItems().addAll("Nombre", "Cédula", "Correo");
        cbFiltroUsuarios.setValue("Nombre");

        cbFiltroTransportistas.getItems().addAll("Nombre", "Licencia", "Zona");
        cbFiltroTransportistas.setValue("Nombre");

        cbFiltroAdmins.getItems().addAll("Nombre", "Correo");
        cbFiltroAdmins.setValue("Nombre");

        cbFiltroEnvios.getItems().addAll("Empresario", "Transportista");
        cbFiltroEnvios.setValue("Empresario");

        // Datos simulados solo para visualizar (puedes borrar si quieres vacío total)
        listEnviosPendientes.getItems().addAll(
                "Envio #101 - Quito → Empresa A (Pendiente)",
                "Envio #102 - Tumbaco → Empresa B (Pendiente)",
                "Envio #103 - Cumbayá → Empresa C (Pendiente)"
        );
    }

    @FXML private void onGeneralClicked() {}
    @FXML private void onInformeClicked() {
        alerta("Informe", "Luego aquí abrirás la pestaña Informe.");
    }

    @FXML private void onCrearUsuario() { alerta("Usuarios", "Crear usuario (simulado)."); }
    @FXML private void onEliminarUsuario() { alerta("Usuarios", "Eliminar usuario (simulado)."); }

    @FXML private void onCrearTransportista() { alerta("Transportistas", "Crear transportista (simulado)."); }
    @FXML private void onEliminarTransportista() { alerta("Transportistas", "Eliminar transportista (simulado)."); }

    @FXML private void onCrearAdmin() { alerta("Admin", "Crear admin (simulado)."); }
    @FXML private void onEliminarAdmin() { alerta("Admin", "Eliminar admin (simulado)."); }

    @FXML
    private void onAnularEnvio() {
        String seleccionado = listEnviosPendientes.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            alerta("Envíos", "Seleccione un envío para anular.");
            return;
        }
        alerta("Envíos", "Anular envío (simulado):\n" + seleccionado);
    }

    private void alerta(String titulo, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(titulo);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}