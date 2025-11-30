import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminGeneralController {

    @FXML
    private TabPane tabPaneEntidades;

    @FXML
    private Button btnGeneral;

    @FXML
    private Button btnInforme;

    @FXML
    private ChoiceBox<String> cbFiltroUsuarios;
    @FXML
    private TextField tfBuscarUsuarios;
    @FXML
    private TableView<?> tableUsuarios;

    @FXML
    private ChoiceBox<String> cbFiltroTransportistas;
    @FXML
    private TextField tfBuscarTransportistas;
    @FXML
    private TableView<?> tableTransportistas;

    @FXML
    private ChoiceBox<String> cbFiltroAdmins;
    @FXML
    private TextField tfBuscarAdmins;
    @FXML
    private TableView<?> tableAdmins;

    @FXML
    private ChoiceBox<String> cbFiltroEnvios;
    @FXML
    private TextField tfBuscarEnvios;
    @FXML
    private ListView<?> listEnviosPendientes;

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

    @FXML
    private Button btnAnularEnvio;

    @FXML
    private void onGeneralClicked() {
        tabPaneEntidades.getSelectionModel().select(0);
    }

    @FXML
    private void onInformeClicked() {
        // aquí más adelante cargas la vista de informes si quieren
    }

    @FXML
    private void onCrearUsuario() {
        abrirVentana("registro_empresario.fxml", "Registrar empresario");
    }

    @FXML
    private void onEliminarUsuario() {
        // aquí luego haces el request para eliminar usuario seleccionado
    }

    @FXML
    private void onCrearTransportista() {
        abrirVentana("registro_transportista.fxml", "Registrar transportista");
    }

    @FXML
    private void onEliminarTransportista() {
        // request para eliminar transportista
    }

    @FXML
    private void onCrearAdmin() {
        abrirVentana("registro_admin.fxml", "Registrar administrador");
    }

    @FXML
    private void onEliminarAdmin() {
        // request para eliminar admin
    }

    @FXML
    private void onAnularEnvio() {
        // request para anular envío seleccionado en listEnviosPendientes
    }

    private void abrirVentana(String fxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root, 800, 600));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}