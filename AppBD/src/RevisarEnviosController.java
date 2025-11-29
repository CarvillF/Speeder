import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class RevisarEnviosController {

    @FXML
    private VBox enviosContainer;

    @FXML
    private Button btnGestionarEnvios;

    @FXML
    private void initialize() {
        // Por ahora NO cargamos nada.
        // Más adelante, cuando tengamos la BD:
        // cargarEnviosDesdeBD();
    }

    // Esta función crea UNA tarjeta visual para un envío
    private void agregarEnvio(String titulo, String cliente, String tarifa,
                              String origenDestino, String estadoTexto) {

        HBox tarjeta = new HBox();
        tarjeta.setSpacing(10);
        tarjeta.setStyle("-fx-border-color: black; -fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 10;");

        // "Círculo" para icono / imagen
        Label icono = new Label();
        icono.setMinSize(40, 40);
        icono.setMaxSize(40, 40);
        icono.setStyle(
            "-fx-border-color: black;" +
            "-fx-border-radius: 20;" +
            "-fx-background-radius: 20;"
        );

        // Texto principal
        VBox info = new VBox(3);
        Label lblTitulo = new Label(titulo);
        lblTitulo.setStyle("-fx-font-weight: bold;");
        Label lblCliente = new Label("Cliente: " + cliente);
        Label lblTarifa = new Label("Tarifa: " + tarifa);
        Label lblOrigenDestino = new Label(origenDestino);
        info.getChildren().addAll(lblTitulo, lblCliente, lblTarifa, lblOrigenDestino);
        HBox.setHgrow(info, Priority.ALWAYS);

        // Estado a la derecha
        VBox estado = new VBox(3);
        Label lblEstado = new Label("ESTADO");
        lblEstado.setStyle("-fx-font-weight: bold;");
        Label lblEstadoTexto = new Label(estadoTexto);
        estado.getChildren().addAll(lblEstado, lblEstadoTexto);

        tarjeta.getChildren().addAll(icono, info, estado);

        enviosContainer.getChildren().add(tarjeta);
    }

    // Ejemplo de cómo se llenaría DESDE CÓDIGO (más adelante desde BD)
    private void cargarEnviosEjemplo() {
        agregarEnvio("ENVÍO DE NO SÉ PARA COMPAÑÍA 1",
                     "Juan Pérez", "$5,00",
                     "Origen: Quito  →  Destino: Compañía 1",
                     "Pendiente");

        agregarEnvio("ENVÍO DE NO SÉ PARA COMPAÑÍA 1",
                     "Ana López", "$7,50",
                     "Origen: Cumbayá  →  Destino: Compañía 1",
                     "En camino");

        agregarEnvio("ENVÍO DE NO SÉ PARA COMPAÑÍA 1",
                     "Carlos Ruiz", "$6,00",
                     "Origen: Tumbaco  →  Destino: Compañía 1",
                     "Siendo recogido");
    }

    @FXML
    private void onGestionarEnvios() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("gestion_envios.fxml"));
            Stage stage = (Stage) btnGestionarEnvios.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Gestión de envíos - Sistema de Paquetería");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}