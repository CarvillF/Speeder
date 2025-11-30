package consultas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import database.DBConnection;

public class Agregar {
    // agregar un nuevo tipo de usuario
    public static void agregarUsuario(String cedula, String nombre, String apellidos, String correo, String numero,
            int i) {
        if (i == 1) {
            agregarTransportista(cedula, nombre, apellidos, correo, numero);
        } else if (i == 2) {

        } else {

        }
    }

    public static void agregarTransportista(String cedula, String nombre, String apellidos, String correo,
            String numero) {
        Connection conn = null;
        PreparedStatement stmt = null, pstmtAssignStatement = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.connect();

            if (conn == null)
                return;
            conn.setAutoCommit(false);

            stmt.setString(1, cedula);
            stmt.setString(2, nombre);
            stmt.setString(3, apellidos);
            stmt.setString(4, correo);
            stmt.setString(5, numero);

            int rowAffected = stmt.executeUpdate();

            if (rowAffected == 1) {
                rs = stmt.getGeneratedKeys();
                int ID = (rs.next()) ? rs.getInt(1) : 0;
                String sql = "insert into usuario(cedula, nombre, apellidos, correo, contrasena, numero, tDirecciones_id_direccion)"
                        +
                        " values (?, ?, ?, ?, ?, ?, ?)";
            }
        } catch (Exception e) {

        }
    }
}
