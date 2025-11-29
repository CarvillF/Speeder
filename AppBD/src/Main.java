
import java.sql.SQLException;

import back.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Carga el FXML de la vista de Login
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage.setTitle("Sistema de Paquetería");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    public static void main(String[] args) {
        try (var connection =  DBConnection.connect()){
            System.out.println("Connected to the MySQL database. \n");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        launch(args);
    }
}

