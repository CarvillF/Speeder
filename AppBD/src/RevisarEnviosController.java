import clases.ProtocolActions;
import clases.Request;
import clases.Response;
import clases.SpeederClient;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RevisarEnviosController {

    @FXML
    private TableView<EnvioRow> tableEnvios;

    @FXML
    private TableColumn<EnvioRow, String> colRucEmpresa;

    @FXML
    private TableColumn<EnvioRow, String> colEnvioCiudad;

    @FXML
    private TableColumn<EnvioRow, String> colEnvioCallePrincipal;

    @FXML
    private TableColumn<EnvioRow, String> colEnvioCalleSecundaria;

    @FXML
    private TableColumn<EnvioRow, String> colEntregaCiudad;

    @FXML
    private TableColumn<EnvioRow, String> colEntregaCallePrincipal;

    @FXML
    private TableColumn<EnvioRow, String> colEntregaCalleSecundaria;

    @FXML
    private TableColumn<EnvioRow, String> colPaquete;

    @FXML
    private TableColumn<EnvioRow, String> colFechaHoraInicio;

    @FXML
    private TableColumn<EnvioRow, String> colFechaHoraEstimadaEntrega;

    @FXML
    private TableColumn<EnvioRow, Number> colTarifa;

    @FXML
    private TableColumn<EnvioRow, String> colEstado;

    @FXML
    private void initialize() {
        if (colRucEmpresa != null) {
            colRucEmpresa.setCellValueFactory(c ->
                    new ReadOnlyStringWrapper(c.getValue().getRucEmpresa()));
        }
        if (colEnvioCiudad != null) {
            colEnvioCiudad.setCellValueFactory(c ->
                    new ReadOnlyStringWrapper(c.getValue().getEnvioCiudad()));
        }
        if (colEnvioCallePrincipal != null) {
            colEnvioCallePrincipal.setCellValueFactory(c ->
                    new ReadOnlyStringWrapper(c.getValue().getEnvioCallePrincipal()));
        }
        if (colEnvioCalleSecundaria != null) {
            colEnvioCalleSecundaria.setCellValueFactory(c ->
                    new ReadOnlyStringWrapper(c.getValue().getEnvioCalleSecundaria()));
        }
        if (colEntregaCiudad != null) {
            colEntregaCiudad.setCellValueFactory(c ->
                    new ReadOnlyStringWrapper(c.getValue().getEntregaCiudad()));
        }
        if (colEntregaCallePrincipal != null) {
            colEntregaCallePrincipal.setCellValueFactory(c ->
                    new ReadOnlyStringWrapper(c.getValue().getEntregaCallePrincipal()));
        }
        if (colEntregaCalleSecundaria != null) {
            colEntregaCalleSecundaria.setCellValueFactory(c ->
                    new ReadOnlyStringWrapper(c.getValue().getEntregaCalleSecundaria()));
        }
        if (colPaquete != null) {
            colPaquete.setCellValueFactory(c ->
                    new ReadOnlyStringWrapper(c.getValue().getPaquete()));
        }
        if (colFechaHoraInicio != null) {
            colFechaHoraInicio.setCellValueFactory(c ->
                    new ReadOnlyStringWrapper(c.getValue().getFechaHoraInicio()));
        }
        if (colFechaHoraEstimadaEntrega != null) {
            colFechaHoraEstimadaEntrega.setCellValueFactory(c ->
                    new ReadOnlyStringWrapper(c.getValue().getFechaHoraEstimadaEntrega()));
        }
        if (colTarifa != null) {
            colTarifa.setCellValueFactory(c ->
                    new ReadOnlyDoubleWrapper(c.getValue().getTarifa()));
        }
        if (colEstado != null) {
            colEstado.setCellValueFactory(c ->
                    new ReadOnlyStringWrapper(c.getValue().getEstado()));
        }

        cargarEnviosDemo();
    }

    @FXML
    private void onVolverMenuUsuario() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menu_usuario.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tableEnvios.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Menú de usuario");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al volver");
            alert.setHeaderText(null);
            alert.setContentText("No se pudo volver al menú de usuario.");
            alert.showAndWait();
        }
    }

    @FXML
    private void onCancelarEnvio() {
        EnvioRow seleccionado = tableEnvios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Selección requerida");
            alert.setHeaderText(null);
            alert.setContentText("Seleccione un envío para cancelarlo.");
            alert.showAndWait();
            return;
        }

        Request request = new Request(ProtocolActions.CANCEL_SHIPMENT, seleccionado.getIdEnvio());

        new Thread(() -> {
            SpeederClient client = SpeederClient.getInstance();
            Response response = client.sendRequest(request);

            Platform.runLater(() -> {
                Alert alert;
                if (response != null && "SUCCESS".equalsIgnoreCase(response.getStatus())) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Envío cancelado");
                    alert.setHeaderText(null);
                    String msg = response.getMessage() != null
                            ? response.getMessage()
                            : "El envío fue cancelado correctamente.";
                    alert.setContentText(msg);
                    tableEnvios.getItems().remove(seleccionado);
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("No se pudo cancelar");
                    alert.setHeaderText(null);
                    String msg = (response != null && response.getMessage() != null)
                            ? response.getMessage()
                            : "No se pudo cancelar el envío (puede que ya esté entregado o cancelado).";
                    alert.setContentText(msg);
                }
                alert.showAndWait();
            });
        }).start();
    }

    private void cargarEnviosDemo() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        tableEnvios.getItems().setAll(
                new EnvioRow(
                        1,
                        "1790012345001",
                        "Quito", "Av. Amazonas", "Mariana de Jesús",
                        "Guayaquil", "Av. 9 de Octubre", "Boyacá",
                        "Caja mediana 5 kg",
                        LocalDateTime.now().minusHours(3).format(fmt),
                        LocalDateTime.now().plusHours(2).format(fmt),
                        4.50,
                        "EN_CAMINO"
                ),
                new EnvioRow(
                        2,
                        "1790012345001",
                        "Quito", "Av. Real Audiencia", "La Prensa",
                        "Cuenca", "Av. Solano", "Remigio Tamariz",
                        "Sobre documentos",
                        LocalDateTime.now().minusDays(1).format(fmt),
                        LocalDateTime.now().minusHours(1).format(fmt),
                        3.80,
                        "ENTREGADO"
                ),
                new EnvioRow(
                        3,
                        "0999988877771",
                        "Guayaquil", "Av. Juan Tanca Marengo", "Miguel H. Alcívar",
                        "Quito", "Av. 6 de Diciembre", "Portugal",
                        "Caja pequeña 2 kg",
                        LocalDateTime.now().minusHours(5).format(fmt),
                        LocalDateTime.now().plusHours(5).format(fmt),
                        5.20,
                        "PENDIENTE"
                )
        );
    }

    public static class EnvioRow {
        private final int idEnvio;
        private final String rucEmpresa;
        private final String envioCiudad;
        private final String envioCallePrincipal;
        private final String envioCalleSecundaria;
        private final String entregaCiudad;
        private final String entregaCallePrincipal;
        private final String entregaCalleSecundaria;
        private final String paquete;
        private final String fechaHoraInicio;
        private final String fechaHoraEstimadaEntrega;
        private final double tarifa;
        private final String estado;

        public EnvioRow(int idEnvio,
                        String rucEmpresa,
                        String envioCiudad,
                        String envioCallePrincipal,
                        String envioCalleSecundaria,
                        String entregaCiudad,
                        String entregaCallePrincipal,
                        String entregaCalleSecundaria,
                        String paquete,
                        String fechaHoraInicio,
                        String fechaHoraEstimadaEntrega,
                        double tarifa,
                        String estado) {
            this.idEnvio = idEnvio;
            this.rucEmpresa = rucEmpresa;
            this.envioCiudad = envioCiudad;
            this.envioCallePrincipal = envioCallePrincipal;
            this.envioCalleSecundaria = envioCalleSecundaria;
            this.entregaCiudad = entregaCiudad;
            this.entregaCallePrincipal = entregaCallePrincipal;
            this.entregaCalleSecundaria = entregaCalleSecundaria;
            this.paquete = paquete;
            this.fechaHoraInicio = fechaHoraInicio;
            this.fechaHoraEstimadaEntrega = fechaHoraEstimadaEntrega;
            this.tarifa = tarifa;
            this.estado = estado;
        }

        public int getIdEnvio() { return idEnvio; }
        public String getRucEmpresa() { return rucEmpresa; }
        public String getEnvioCiudad() { return envioCiudad; }
        public String getEnvioCallePrincipal() { return envioCallePrincipal; }
        public String getEnvioCalleSecundaria() { return envioCalleSecundaria; }
        public String getEntregaCiudad() { return entregaCiudad; }
        public String getEntregaCallePrincipal() { return entregaCallePrincipal; }
        public String getEntregaCalleSecundaria() { return entregaCalleSecundaria; }
        public String getPaquete() { return paquete; }
        public String getFechaHoraInicio() { return fechaHoraInicio; }
        public String getFechaHoraEstimadaEntrega() { return fechaHoraEstimadaEntrega; }
        public double getTarifa() { return tarifa; }
        public String getEstado() { return estado; }
    }
}