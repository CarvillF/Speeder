-- Script para crear usuarios de base de datos para el sistema Speeder

-- DROP USER IF EXISTS 'emprendedor_user'@'localhost';
-- DROP USER IF EXISTS 'transportista_user'@'localhost';
-- DROP USER IF EXISTS 'admin_user'@'localhost';

-- =====================================================
-- USUARIO EMPRENDEDOR
-- =====================================================
CREATE USER 'emprendedor_user'@'localhost' IDENTIFIED BY 'emprendedor_pass_2024';
GRANT SELECT, INSERT, UPDATE ON mydb.usuarios TO 'emprendedor_user'@'localhost';
GRANT SELECT, INSERT, UPDATE ON mydb.empresarios TO 'emprendedor_user'@'localhost';
GRANT SELECT, INSERT, UPDATE ON mydb.companias TO 'emprendedor_user'@'localhost';
GRANT SELECT, INSERT, UPDATE ON mydb.sucursales TO 'emprendedor_user'@'localhost';
GRANT SELECT, INSERT, UPDATE ON mydb.envios TO 'emprendedor_user'@'localhost';
GRANT SELECT, INSERT, UPDATE ON mydb.paquetes TO 'emprendedor_user'@'localhost';
GRANT SELECT, INSERT, UPDATE ON mydb.direcciones TO 'emprendedor_user'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON mydb.metodos_pago TO 'emprendedor_user'@'localhost';
GRANT SELECT ON mydb.transportistas TO 'emprendedor_user'@'localhost';
GRANT SELECT ON mydb.caracteristicas_paquete TO 'emprendedor_user'@'localhost';
GRANT SELECT ON mydb.modificadores_entrega TO 'emprendedor_user'@'localhost';
GRANT SELECT, INSERT ON mydb.modificadores_aplicados TO 'emprendedor_user'@'localhost';


-- =====================================================
-- USUARIO TRANSPORTISTA
-- =====================================================
CREATE USER 'transportista_user'@'localhost' IDENTIFIED BY 'transportista_pass_2024';
GRANT SELECT, INSERT, UPDATE ON mydb.usuarios TO 'transportista_user'@'localhost';
GRANT SELECT, INSERT, UPDATE ON mydb.transportistas TO 'transportista_user'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON mydb.vehiculos TO 'transportista_user'@'localhost';
GRANT SELECT, INSERT, UPDATE ON mydb.envios TO 'transportista_user'@'localhost';
GRANT SELECT ON mydb.direcciones TO 'transportista_user'@'localhost';
GRANT SELECT ON mydb.paquetes TO 'transportista_user'@'localhost';
GRANT SELECT ON mydb.sucursales TO 'transportista_user'@'localhost';
GRANT SELECT ON mydb.modelos TO 'transportista_user'@'localhost';
GRANT SELECT ON mydb.caracteristicas_paquete TO 'transportista_user'@'localhost';
GRANT SELECT ON mydb.modificadores_entrega TO 'transportista_user'@'localhost';
GRANT SELECT ON mydb.modificadores_aplicados TO 'transportista_user'@'localhost';

-- =====================================================
-- USUARIO ADMINISTRADOR
-- =====================================================
CREATE USER 'admin_user'@'localhost' IDENTIFIED BY 'admin_pass_2024';
GRANT ALL PRIVILEGES ON mydb.* TO 'admin_user'@'localhost';

-- =====================================================
-- APLICAR CAMBIOS
-- =====================================================
FLUSH PRIVILEGES;



-- =====================================================
-- VERIFICAR USUARIOS CREADOS
-- =====================================================
SELECT 
    User, 
    Host 
FROM mysql.user 
WHERE User IN ('emprendedor_user', 'transportista_user', 'admin_user');

-- =====================================================
-- MENSAJE DE CONFIRMACIÓN
-- =====================================================
SELECT 'Usuarios creados exitosamente!' AS Mensaje;
SELECT 'IMPORTANTE: Cambia las contraseñas en producción' AS Advertencia;
