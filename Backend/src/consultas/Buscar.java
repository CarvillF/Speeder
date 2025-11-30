package consultas;

import java.sql.SQLException;

import database.DBConnection;

public class Buscar {
    // metodos de busqueda modificados de ChatGPT
    // PROMPT "Como creo una consulta en java de SQL para buscar un dato y que
    // devuelva true si lo encuentra?"

    // METODOS LOGIN
    // buscar si existe el correo y si coincide con su contrasena
    public static boolean login(String correo, String contrasena) {
        String sql = "select 1 from usuario where correo = ? and contrasena = ? limit 1";

        try (var conn = DBConnection.connect(database.UserType.ADMINISTRADOR);
                var stmt = conn.prepareStatement(sql)) {

            // buscamos correo
            stmt.setString(1, correo);
            stmt.setString(2, contrasena);
            try (var rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException ex) {
            return false;
        }
    }

    // METODOS REGISTRO
    // cedula existe
    public static boolean cedulaExiste(String cedula) {
        String sql = "select 1 from usuario where cedula = ?";

        try (var conn = DBConnection.connect(database.UserType.ADMINISTRADOR);
                var stmt = conn.prepareStatement(sql)) {

            // buscamos cedula
            stmt.setString(1, cedula);
            try (var rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException ex) {
            return false;
        }
    }

    // correo existe
    public static boolean correoExiste(String correo) {
        String sql = "select 1 from usuario where correo = ?";

        try (var conn = DBConnection.connect(database.UserType.ADMINISTRADOR);
                var stmt = conn.prepareStatement(sql)) {

            // buscamos cedula
            stmt.setString(1, correo);
            try (var rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException ex) {
            return false;
        }
    }
}