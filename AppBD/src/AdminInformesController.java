import clases.ProtocolActions;
import clases.Request;
import clases.Response;
import clases.SpeederClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

public class AdminInformesController {

    @FXML
    private Button btnVolverAdmin;

    @FXML
    private void onInformeEnviosMensuales() {
        Stage stage = (Stage) btnVolverAdmin.getScene().getWindow();
        downloadReport(ProtocolActions.REPORT_SHIPMENTS_MONTHLY, stage, "Informe_Envios_Mensuales.pdf");
    }

    @FXML
    private void onInformeEmpresarios() {
        Stage stage = (Stage) btnVolverAdmin.getScene().getWindow();
        downloadReport(ProtocolActions.REPORT_COMPANIES_PLATFORM, stage, "Informe_Empresarios_Plataforma.pdf");
    }

    @FXML
    private void onInformeTransportistas() {
        Stage stage = (Stage) btnVolverAdmin.getScene().getWindow();
        downloadReport(ProtocolActions.REPORT_TRANSPORTERS, stage, "Informe_Transportistas.pdf");
    }

    @FXML
    private void onInformeFlujoEmpresa() {
        Stage stage = (Stage) btnVolverAdmin.getScene().getWindow();
        downloadReport(ProtocolActions.REPORT_COMPANY_FLOW, stage, "Informe_Flujo_Empresa.pdf");
    }

    @FXML
    private void onInformeHistoricoPaquetes() {
        Stage stage = (Stage) btnVolverAdmin.getScene().getWindow();
        downloadReport(ProtocolActions.REPORT_PACKAGES_HISTORY, stage, "Informe_Historico_Paquetes.pdf");
    }

    @FXML
    private void onVolverAdmin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin_general.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnVolverAdmin.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 600));
            stage.setTitle("Panel administrador");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void downloadReport(String action, Stage parentStage, String defaultFileName) {
        new Thread(() -> {
            Request req = new Request(action, null);
            SpeederClient client = SpeederClient.getInstance();
            Response response = client.sendRequest(req);

            Platform.runLater(() -> {
                if (response != null
                        && "SUCCESS".equalsIgnoreCase(response.getStatus())
                        && response.getData() != null) {

                    Object payload = response.getData();
                    if (payload instanceof String) {
                        String base64Pdf = (String) payload;
                        savePdfToDisk(base64Pdf, parentStage, defaultFileName);
                    }
                }
            });
        }).start();
    }

    private void savePdfToDisk(String base64Data, Stage parentStage, String defaultFileName) {
        try {
            byte[] pdfBytes = Base64.getDecoder().decode(base64Data);

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Guardar informe PDF");
            fileChooser.setInitialFileName(defaultFileName);
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf")
            );

            File file = fileChooser.showSaveDialog(parentStage);
            if (file != null) {
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(pdfBytes);
                }
            }
        } catch (IllegalArgumentException | IOException e) {
            e.printStackTrace();
        }
    }
}