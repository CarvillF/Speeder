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
    private Button btnLogoutAdmin;

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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin_informes.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnInforme.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 600));
            stage.setTitle("Informes");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onCrearUsuario() {
        abrirVentana("registro_empresario.fxml", "Registrar empresario");
    }

    @FXML
    private void onEliminarUsuario() {
    }

    @FXML
    private void onCrearTransportista() {
        abrirVentana("registro_transportista.fxml", "Registrar transportista");
    }

    @FXML
    private void onEliminarTransportista() {
    }

    @FXML
    private void onCrearAdmin() {
        abrirVentana("registro_admin.fxml", "Registrar administrador");
    }

    @FXML
    private void onEliminarAdmin() {
    }

    @FXML
    private void onAnularEnvio() {
    }

    @FXML
    private void onLogoutAdmin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnGeneral.getScene().getWindow();
            Scene scene = new Scene(root, 900, 500);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setTitle("Sistema de Paquetería - Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void abrirVentana(String fxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root, 800, 600));
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}