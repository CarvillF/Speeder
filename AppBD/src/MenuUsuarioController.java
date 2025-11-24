import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MenuUsuarioController {

    @FXML
    private Button btnRevisarEnvios;

    @FXML
    private Button btnConfigUsuario;

    @FXML
    private Button btnGestionEnvios;

    @FXML
    private Button btnLogout;

    @FXML
    private Label sectionTitleLabel;

    @FXML
    private Label sectionDescriptionLabel;

    @FXML
    private void initialize() {
        sectionTitleLabel.setText("Seleccione una opción del menú");
        sectionDescriptionLabel.setText("Aquí se mostrará la información de la opción seleccionada.");
    }

    @FXML
    private void onRevisarEnvios() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("revisar_envios.fxml"));
            Stage stage = (Stage) btnRevisarEnvios.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Revisar envíos actuales - Sistema de Paquetería");
            stage.show();
        } catch (IOException e) {
        e.printStackTrace();
    }
}

    @FXML
    private void onConfigUsuario() {
        System.out.println("[MenuUsuario] CLICK Configuración de usuario");

        // Buscar el FXML
        URL fxmlLocation = getClass().getResource("configuracion.fxml");
        System.out.println("[MenuUsuario] URL configuracion.fxml = " + fxmlLocation);

        if (fxmlLocation == null) {
            System.out.println("[MenuUsuario] ERROR: No se encontró configuracion.fxml en el mismo paquete.");
            return;
        }

        try {
            Parent root = FXMLLoader.load(fxmlLocation);
            Stage stage = (Stage) btnConfigUsuario.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Configuración - Sistema de Paquetería");
            stage.show();
        } catch (IOException e) {
            System.out.println("[MenuUsuario] ERROR cargando configuracion.fxml");
            e.printStackTrace();
        }
    }

    @FXML
    private void onGestionEnvios() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("gestion_envios.fxml"));
            Stage stage = (Stage) btnGestionEnvios.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Gestión de envíos - Sistema de Paquetería");
            stage.show();
        } catch (IOException e) {
        e.printStackTrace();
    }
}

    @FXML
    private void onLogout() {
        sectionTitleLabel.setText("Sesión cerrada (simulado)");
        sectionDescriptionLabel.setText("Luego esta opción te devolverá a la pantalla de login.");
    }
}