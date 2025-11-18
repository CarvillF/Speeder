import java.sql.SQLException;
import java.sql.Date;

public class Main {
    public static void main(String[] args){

        try (var connection =  MySQLConnection.connect()){
            System.out.println("Connected to the MySQL database. \n");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}