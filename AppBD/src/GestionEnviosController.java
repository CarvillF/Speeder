import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GestionEnviosController {

    private static final boolean DEMO = true;

    @FXML
    private ComboBox<String> cbCompania;

    @FXML
    private ComboBox<String> cbSucursal;

    @FXML
    private ComboBox<String> cbCiudad;

    @FXML
    private TextField tfCallePrincipal;

    @FXML
    private TextField tfCalleSecundaria;

    @FXML
    private TextField tfNumeroEdificacion;

    @FXML
    private TextField tfDetalleDireccion;

    @FXML
    private TextField tfDescripcionPaquete;

    @FXML
    private TextField tfDimX;

    @FXML
    private TextField tfDimY;

    @FXML
    private TextField tfDimZ;

    @FXML
    private TextField tfPeso;

    @FXML
    private ComboBox<String> cbTipoPaquete;

    @FXML
    private TextArea taObservaciones;

    @FXML
    private ListView<TransportistaOpcion> listTransportistas;

    @FXML
    private Button btnOpcionesDisponibles;

    @FXML
    private Button btnConfirmarEnvio;

    @FXML
    private Button btnVolverMenu;

    @FXML
    private void initialize() {
        inicializarCombosDemo();
        if (btnConfirmarEnvio != null) {
            btnConfirmarEnvio.setDisable(true);
        }
    }

    private void inicializarCombosDemo() {
        if (cbCompania != null) {
            cbCompania.getItems().setAll(
                    "Comercial Andina S.A.",
                    "TechMarket Cía. Ltda.",
                    "LogiStore Express"
            );
        }

        if (cbSucursal != null) {
            cbSucursal.getItems().setAll(
                    "Sucursal Quito - Av. Naciones Unidas",
                    "Sucursal Cumbayá - Vía Interoceánica",
                    "Sucursal Guayaquil - Cdla. Kennedy"
            );
        }

        if (cbCiudad != null) {
            cbCiudad.getItems().setAll(
                    "Quito",
                    "Guayaquil",
                    "Cuenca",
                    "Ambato",
                    "Manta"
            );
        }

        if (cbTipoPaquete != null) {
            cbTipoPaquete.getItems().setAll(
                    "Estándar",
                    "Frágil",
                    "Perecible",
                    "Documentos",
                    "Electrónicos"
            );
        }
    }

    @FXML
    private void onVolverMenuUsuario() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menu_usuario.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnVolverMenu.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Menú de usuario");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error al volver",
                    "No se pudo volver al menú de usuario.");
        }
    }

    @FXML
    private void onOpcionesDisponibles() {
        if (!validarCamposObligatorios()) {
            return;
        }

        if (DEMO) {
            cargarTransportistasDemo();
        } else {
            // aquí irá la llamada real al backend
        }
    }

    private boolean validarCamposObligatorios() {
        List<String> errores = new ArrayList<>();

        String compania = cbCompania.getSelectionModel().getSelectedItem();
        String sucursal = cbSucursal.getSelectionModel().getSelectedItem();
        String ciudad = cbCiudad.getSelectionModel().getSelectedItem();

        String callePrin = tfCallePrincipal.getText();
        String descripcion = tfDescripcionPaquete.getText();
        String peso = tfPeso.getText();
        String tipo = cbTipoPaquete.getSelectionModel().getSelectedItem();

        if (compania == null || compania.isBlank()) {
            errores.add("Seleccione la compañía que envía el paquete.");
        }
        if (sucursal == null || sucursal.isBlank()) {
            errores.add("Seleccione la sucursal de la compañía.");
        }
        if (ciudad == null || ciudad.isBlank()) {
            errores.add("Seleccione la ciudad de entrega.");
        }
        if (callePrin == null || callePrin.isBlank()) {
            errores.add("Ingrese la calle principal de la dirección de entrega.");
        }
        if (descripcion == null || descripcion.isBlank()) {
            errores.add("Ingrese la descripción del paquete.");
        }
        if (peso == null || peso.isBlank()) {
            errores.add("Ingrese el peso del paquete.");
        }
        if (tipo == null || tipo.isBlank()) {
            errores.add("Seleccione el tipo de paquete.");
        }

        if (peso != null && !peso.isBlank()) {
            try {
                Double.parseDouble(peso);
            } catch (NumberFormatException e) {
                errores.add("El peso debe ser un número válido (ej: 2.5).");
            }
        }

        if (!errores.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String err : errores) {
                sb.append("- ").append(err).append("\n");
            }
            mostrarAlerta(Alert.AlertType.WARNING, "Campos incompletos", sb.toString());
            return false;
        }

        return true;
    }

    private void cargarTransportistasDemo() {
        if (listTransportistas == null) return;

        listTransportistas.getItems().clear();

        TransportistaOpcion t1 = new TransportistaOpcion(
                "T-001", "Juan Pérez", "Entrega estimada: 3 h", "Tarifa aprox: $4.50"
        );
        TransportistaOpcion t2 = new TransportistaOpcion(
                "T-002", "María Gómez", "Entrega estimada: 1 día", "Tarifa aprox: $3.80"
        );
        TransportistaOpcion t3 = new TransportistaOpcion(
                "T-003", "TransExpress S.A.", "Entrega estimada: mismo día", "Tarifa aprox: $5.20"
        );

        listTransportistas.getItems().addAll(t1, t2, t3);

        if (btnConfirmarEnvio != null) {
            btnConfirmarEnvio.setDisable(false);
        }
    }

    @FXML
    private void onConfirmarEnvio() {
        TransportistaOpcion seleccionado = listTransportistas.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Seleccione un transportista",
                    "Debe escoger una opción de transportista antes de confirmar el envío.");
            return;
        }

        if (DEMO) {
            String resumen = String.format(
                    "Envío creado en modo DEMO.\n\nTransportista: %s\nDetalle: %s\n\nMás adelante, este flujo enviará los datos al backend.",
                    seleccionado.getNombre(), seleccionado.getDetallePrincipal()
            );
            mostrarAlerta(Alert.AlertType.INFORMATION, "Envío confirmado (DEMO)", resumen);
            limpiarFormulario();
        } else {
            // aquí irá la confirmación real con backend
        }
    }

    private void limpiarFormulario() {
        if (cbCompania != null) cbCompania.getSelectionModel().clearSelection();
        if (cbSucursal != null) cbSucursal.getSelectionModel().clearSelection();
        if (cbCiudad != null) cbCiudad.getSelectionModel().clearSelection();

        if (tfCallePrincipal != null) tfCallePrincipal.clear();
        if (tfCalleSecundaria != null) tfCalleSecundaria.clear();
        if (tfNumeroEdificacion != null) tfNumeroEdificacion.clear();
        if (tfDetalleDireccion != null) tfDetalleDireccion.clear();

        if (tfDescripcionPaquete != null) tfDescripcionPaquete.clear();
        if (tfDimX != null) tfDimX.clear();
        if (tfDimY != null) tfDimY.clear();
        if (tfDimZ != null) tfDimZ.clear();
        if (tfPeso != null) tfPeso.clear();
        if (cbTipoPaquete != null) cbTipoPaquete.getSelectionModel().clearSelection();
        if (taObservaciones != null) taObservaciones.clear();

        if (listTransportistas != null) listTransportistas.getItems().clear();
        if (btnConfirmarEnvio != null) btnConfirmarEnvio.setDisable(true);
    }

    private void mostrarAlerta(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static class TransportistaOpcion {
        private final String id;
        private final String nombre;
        private final String detallePrincipal;
        private final String tarifaTexto;

        public TransportistaOpcion(String id, String nombre,
                                   String detallePrincipal, String tarifaTexto) {
            this.id = id;
            this.nombre = nombre;
            this.detallePrincipal = detallePrincipal;
            this.tarifaTexto = tarifaTexto;
        }

        public String getId() {
            return id;
        }

        public String getNombre() {
            return nombre;
        }

        public String getDetallePrincipal() {
            return detallePrincipal;
        }

        public String getTarifaTexto() {
            return tarifaTexto;
        }

        @Override
        public String toString() {
            return nombre + " | " + detallePrincipal + " | " + tarifaTexto;
        }
    }
}


