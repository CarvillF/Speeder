package test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import clases.*;
import dao.*;
import database.UserType;

public class TestPaso2 {

    public static void main(String[] args) {
        System.out.println("=== Test Paso 2: DAO Layer & Domain Logic ===\n");

        long timestamp = System.currentTimeMillis();
        String randomSuffix = String.valueOf(timestamp % 10000);

        // 1. Test User Registration
        testUserRegistration(randomSuffix);

        // 2. Test Company & Branch
        testCompanyAndBranch(randomSuffix);

        // 3. Test Vehicle
        testVehicle(randomSuffix);

        // 4. Test Payment Method
        testPaymentMethod(randomSuffix);

        // 5. Test Shipment
        testShipment(randomSuffix);

        System.out.println("\n=== Fin de Tests Paso 2 ===");
    }

    private static void testUserRegistration(String suffix) {
        System.out.println("--- Test 1: User Registration ---");

        // Register Empresario
        Empresario empresario = new Empresario(
                "111" + suffix, "Emp" + suffix, "Resario", "emp" + suffix + "@mail.com", "pass123", "0999999999", 0,
                "Gerente", "info@empresa.com");
        boolean empSuccess = UserDAO.registerBusiness(empresario);
        System.out.println("Register Empresario: " + (empSuccess ? "✓ Success" : "✗ Failed"));

        // Register Transportista
        Transportistas driver = new Transportistas(
                "222" + suffix, "Trans" + suffix, "Portista", "driver" + suffix + "@mail.com", "pass123", "0988888888",
                0,
                "LIC-123", "B", "Norte", 10.5f, "Disponible", new ArrayList<>());
        boolean driverSuccess = UserDAO.registerDriver(driver);
        System.out.println("Register Driver: " + (driverSuccess ? "✓ Success" : "✗ Failed"));

        // Register Admin
        Admin admin = new Admin(
                "333" + suffix, "Ad" + suffix, "Min", "admin" + suffix + "@mail.com", "pass123", "0977777777", 0,
                "EMP-001", true);
        boolean adminSuccess = UserDAO.registerAdmin(admin);
        System.out.println("Register Admin: " + (adminSuccess ? "✓ Success" : "✗ Failed"));
        System.out.println();
    }

    private static void testCompanyAndBranch(String suffix) {
        System.out.println("--- Test 2: Company & Branch ---");
        String ownerId = "111" + suffix;
        String ruc = "179" + suffix + "001";

        Compania company = new Compania(ruc, ownerId, "Company " + suffix, "Privada", "Test Company");
        boolean compSuccess = CompanyDAO.createCompany(company);
        System.out.println("Create Company: " + (compSuccess ? "✓ Success" : "✗ Failed"));

        List<Compania> companies = CompanyDAO.getCompanies(ownerId);
        System.out.println("Companies found: " + companies.size());

        // Create Branch (needs address first, but assuming 0 or dummy for now if FK
        // allows, or we need to insert address)
        // Since we didn't implement AddressDAO, we might fail FK if strict.
        // Let's assume we can use a dummy address ID if DB has one, or we skip if too
        // complex for this test without AddressDAO.
        // For now, let's try with ID 1 assuming mockup data exists, or skip.
        // System.out.println("Skipping Branch creation (requires AddressDAO)");
    }

    private static void testVehicle(String suffix) {
        System.out.println("--- Test 3: Vehicle ---");
        String driverId = "222" + suffix;

        Vehiculo vehicle = new Vehiculo(0, driverId, "Toyota Hilux", "Blanco", "ABC-" + suffix.substring(0, 3));
        boolean vehSuccess = VehicleDAO.createVehicle(vehicle);
        System.out.println("Create Vehicle: " + (vehSuccess ? "✓ Success" : "✗ Failed"));

        List<Vehiculo> vehicles = VehicleDAO.getMyVehicles(driverId);
        System.out.println("Vehicles found: " + vehicles.size());
    }

    private static void testPaymentMethod(String suffix) {
        System.out.println("--- Test 4: Payment Method ---");
        String userId = "111" + suffix;

        MetodoPago method = new MetodoPago(0, userId, "Tarjeta", "{\"last4\":\"4242\"}", true);
        boolean paySuccess = PaymentDAO.createPaymentMethod(method);
        System.out.println("Create Payment Method: " + (paySuccess ? "✓ Success" : "✗ Failed"));
    }

    private static void testShipment(String suffix) {
        System.out.println("--- Test 5: Shipment ---");
        // This requires existing IDs (sucursal, direccion, etc.)
        // We will mock the object but the insert might fail due to FKs if we don't have
        // valid IDs.
        // We'll try to create a shipment with dummy IDs and expect failure or success
        // depending on DB state.
        // To make it work, we'd need to insert Addresses first.

        System.out.println("Skipping Shipment creation in this simple test due to FK dependencies (Address, Branch).");
        System.out.println("To test properly, we need AddressDAO implemented and populated.");
    }
}
