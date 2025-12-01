-- Script para crear usuarios de base de datos para el sistema Speeder

-- DROP USER IF EXISTS 'emprendedor_user'@'localhost';
-- DROP USER IF EXISTS 'transportista_user'@'localhost';
-- DROP USER IF EXISTS 'admin_user'@'localhost';

-- ---------------------------------------
-- USUARIO EMPRENDEDOR
-- ---------------------------------------
CREATE USER 'emprendedor_user'@'localhost' IDENTIFIED BY 'emprendedor_pass_2024';
GRANT SELECT, INSERT, UPDATE ON SchemaSpeeder.usuarios TO 'emprendedor_user'@'localhost';
GRANT SELECT, INSERT, UPDATE ON SchemaSpeeder.empresarios TO 'emprendedor_user'@'localhost';
GRANT SELECT, INSERT, UPDATE ON SchemaSpeeder.companias TO 'emprendedor_user'@'localhost';
GRANT SELECT, INSERT, UPDATE ON SchemaSpeeder.sucursales TO 'emprendedor_user'@'localhost';
GRANT SELECT, INSERT, UPDATE ON SchemaSpeeder.envios TO 'emprendedor_user'@'localhost';
GRANT SELECT, INSERT, UPDATE ON SchemaSpeeder.paquetes TO 'emprendedor_user'@'localhost';
GRANT SELECT, INSERT, UPDATE ON SchemaSpeeder.direcciones TO 'emprendedor_user'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON SchemaSpeeder.metodos_pago TO 'emprendedor_user'@'localhost';
GRANT SELECT ON SchemaSpeeder.transportistas TO 'emprendedor_user'@'localhost';
GRANT SELECT ON SchemaSpeeder.caracteristicas_paquete TO 'emprendedor_user'@'localhost';
GRANT SELECT ON SchemaSpeeder.modificadores_entrega TO 'emprendedor_user'@'localhost';
GRANT SELECT, INSERT ON SchemaSpeeder.modificadores_aplicados TO 'emprendedor_user'@'localhost';


-- ---------------------------------------
-- USUARIO TRANSPORTISTA
-- ---------------------------------------
CREATE USER 'transportista_user'@'localhost' IDENTIFIED BY 'transportista_pass_2024';
GRANT SELECT, INSERT, UPDATE ON SchemaSpeeder.usuarios TO 'transportista_user'@'localhost';
GRANT SELECT, INSERT, UPDATE ON SchemaSpeeder.transportistas TO 'transportista_user'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON SchemaSpeeder.vehiculos TO 'transportista_user'@'localhost';
GRANT SELECT, INSERT, UPDATE ON SchemaSpeeder.envios TO 'transportista_user'@'localhost';
GRANT SELECT ON SchemaSpeeder.direcciones TO 'transportista_user'@'localhost';
GRANT SELECT ON SchemaSpeeder.paquetes TO 'transportista_user'@'localhost';
GRANT SELECT ON SchemaSpeeder.sucursales TO 'transportista_user'@'localhost';
GRANT SELECT ON SchemaSpeeder.modelos TO 'transportista_user'@'localhost';
GRANT SELECT ON SchemaSpeeder.caracteristicas_paquete TO 'transportista_user'@'localhost';
GRANT SELECT ON SchemaSpeeder.modificadores_entrega TO 'transportista_user'@'localhost';
GRANT SELECT ON SchemaSpeeder.modificadores_aplicados TO 'transportista_user'@'localhost';

-- ---------------------------------------
-- USUARIO ADMINISTRADOR
-- ---------------------------------------
CREATE USER 'admin_user'@'localhost' IDENTIFIED BY 'admin_pass_2024';
GRANT ALL PRIVILEGES ON SchemaSpeeder.* TO 'admin_user'@'localhost';

-- ---------------------------------------
-- APLICAR CAMBIOS
-- ---------------------------------------
FLUSH PRIVILEGES;



-- ---------------------------------------
-- VERIFICAR USUARIOS CREADOS
-- ---------------------------------------
SELECT 
    User, 
    Host 
FROM mysql.user 
WHERE User IN ('emprendedor_user', 'transportista_user', 'admin_user');

-- ---------------------------------------
-- MENSAJE DE CONFIRMACIÓN
-- ---------------------------------------
SELECT 'Usuarios creados exitosamente!' AS Mensaje;
SELECT 'IMPORTANTE: Cambia las contraseñas en producción' AS Advertencia;
