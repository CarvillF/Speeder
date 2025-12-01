package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import clases.Compania;
import clases.Sucursal;
import database.DBConnection;
import database.UserType;

public class CompanyDAO {

    // --- Company Methods ---

    public static List<Compania> getCompanies(String ownerId) {
        List<Compania> companies = new ArrayList<>();
        String sql = "SELECT * FROM companias WHERE empresario_cedula = ?";

        try (Connection conn = DBConnection.connect(UserType.EMPRENDEDOR);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ownerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    companies.add(new Compania(
                            rs.getString("RUC"),
                            rs.getString("empresario_cedula"),
                            rs.getString("nombre_compania"),
                            rs.getString("tipo_compania"),
                            rs.getString("descripcion")));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting companies: " + e.getMessage());
        }
        return companies;
    }

    public static boolean createCompany(Compania company) {
        String sql = "INSERT INTO companias (RUC, empresario_cedula, nombre_compania, tipo_compania, descripcion) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.connect(UserType.EMPRENDEDOR);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, company.getRUC());
            stmt.setString(2, company.getEmpresarioCedula());
            stmt.setString(3, company.getNombreCompania());
            stmt.setString(4, company.getTipoCompania());
            stmt.setString(5, company.getDescripcion());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error creating company: " + e.getMessage());
            return false;
        }
    }

    // --- Branch Methods ---

    public static List<Sucursal> getBranches(String companyRUC) {
        List<Sucursal> branches = new ArrayList<>();
        String sql = "SELECT * FROM sucursales WHERE compania_RUC = ?";

        try (Connection conn = DBConnection.connect(UserType.EMPRENDEDOR);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, companyRUC);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    branches.add(new Sucursal(
                            rs.getInt("id_direccion"),
                            rs.getString("compania_RUC"),
                            rs.getBoolean("activa")));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting branches: " + e.getMessage());
        }
        return branches;
    }

    public static boolean createBranch(Sucursal branch) {
        String sql = "INSERT INTO sucursales (id_direccion, compania_RUC, activa) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.connect(UserType.EMPRENDEDOR);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, branch.getIdDireccion());
            stmt.setString(2, branch.getCompaniaRUC());
            stmt.setBoolean(3, branch.isActiva());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error creating branch: " + e.getMessage());
            return false;
        }
    }
}
