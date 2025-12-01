import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root = loader.load();

        double tmpBaseWidth = 900;
        double tmpBaseHeight = 500;

        if (root instanceof Region region) {
            if (region.getPrefWidth() > 0) {
                tmpBaseWidth = region.getPrefWidth();
            }
            if (region.getPrefHeight() > 0) {
                tmpBaseHeight = region.getPrefHeight();
            }
        }

        final double baseWidth = tmpBaseWidth;
        final double baseHeight = tmpBaseHeight;

        StackPane container = new StackPane(root);
        Scene scene = new Scene(container, baseWidth, baseHeight);

        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            double scaleX = newVal.doubleValue() / baseWidth;
            double scaleY = scene.getHeight() / baseHeight;
            double scale = Math.min(scaleX, scaleY);
            root.setScaleX(scale);
            root.setScaleY(scale);
        });

        scene.heightProperty().addListener((obs, oldVal, newVal) -> {
            double scaleX = scene.getWidth() / baseWidth;
            double scaleY = newVal.doubleValue() / baseHeight;
            double scale = Math.min(scaleX, scaleY);
            root.setScaleX(scale);
            root.setScaleY(scale);
        });

        primaryStage.setTitle("Sistema de Paquetería - Speeder");
        primaryStage.setScene(scene);

        primaryStage.setMinWidth(baseWidth * 0.7);
        primaryStage.setMinHeight(baseHeight * 0.7);

        primaryStage.sizeToScene();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}