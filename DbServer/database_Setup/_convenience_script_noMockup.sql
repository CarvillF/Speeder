-- --------------------------
-- CREACION DE TABLAS
-- --------------------------

-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema SchemaSpeeder
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema SchemaSpeeder
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `SchemaSpeeder` DEFAULT CHARACTER SET utf8 ;
USE SchemaSpeeder ;

-- -----------------------------------------------------
-- Table `SchemaSpeeder`.`ciudades`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SchemaSpeeder`.`ciudades` (
  `nombre_ciudad` VARCHAR(45) NOT NULL,
  `coordenada_ciudad_x` DECIMAL(11,8) NULL DEFAULT 0.00000000,
  `coordenada_ciudad_y` DECIMAL(11,8) NULL DEFAULT 0.00000000,
  PRIMARY KEY (`nombre_ciudad`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SchemaSpeeder`.`direcciones`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SchemaSpeeder`.`direcciones` (
  `id_direccion` INT NOT NULL AUTO_INCREMENT,
  `ciudades_nombre_ciudad` VARCHAR(45) NOT NULL,
  `calle_principal` VARCHAR(45) NULL DEFAULT NULL,
  `calle_secundaria` VARCHAR(45) NULL DEFAULT NULL,
  `numero_edificacion` VARCHAR(20) NULL DEFAULT NULL,
  `detalle` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id_direccion`),
  INDEX `fk_direcciones_ciudades1_idx` (`ciudades_nombre_ciudad` ASC) VISIBLE,
  CONSTRAINT `fk_direcciones_ciudades1`
    FOREIGN KEY (`ciudades_nombre_ciudad`)
    REFERENCES `SchemaSpeeder`.`ciudades` (`nombre_ciudad`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SchemaSpeeder`.`usuarios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SchemaSpeeder`.`usuarios` (
  `cedula` VARCHAR(10) NOT NULL,
  `nombre` VARCHAR(45) NOT NULL,
  `apellidos` VARCHAR(45) NOT NULL,
  `correo` VARCHAR(60) NOT NULL,
  `contrasena` VARCHAR(255) NOT NULL,
  `hash` VARCHAR(45) NULL,
  `numero_telefono` VARCHAR(15) NULL DEFAULT NULL,
  `id_direccion_principal` INT NULL DEFAULT NULL,
  PRIMARY KEY (`cedula`),
  UNIQUE INDEX `correo_UNIQUE` (`correo` ASC) VISIBLE,
  INDEX `fk_usuarios_direcciones_idx` (`id_direccion_principal` ASC) VISIBLE,
  CONSTRAINT `fk_usuarios_direcciones`
    FOREIGN KEY (`id_direccion_principal`)
    REFERENCES `SchemaSpeeder`.`direcciones` (`id_direccion`)
    ON DELETE SET NULL
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SchemaSpeeder`.`empresarios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SchemaSpeeder`.`empresarios` (
  `usuario_cedula` VARCHAR(10) NOT NULL,
  `cargo_empresa` VARCHAR(45) NULL DEFAULT 'Empleado',
  `correo_empresarial` VARCHAR(60) NULL DEFAULT NULL,
  PRIMARY KEY (`usuario_cedula`),
  CONSTRAINT `fk_empresarios_usuarios`
    FOREIGN KEY (`usuario_cedula`)
    REFERENCES `SchemaSpeeder`.`usuarios` (`cedula`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SchemaSpeeder`.`metodos_pago`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SchemaSpeeder`.`metodos_pago` (
  `id_metodo_pago` INT NOT NULL AUTO_INCREMENT,
  `usuario_cedula` VARCHAR(10) NOT NULL,
  `tipo` ENUM('Tarjeta', 'Cuenta Bancaria') NULL DEFAULT 'Tarjeta',
  `datos` VARCHAR(250) NULL DEFAULT NULL,
  `predeterminado` TINYINT NULL DEFAULT 0,
  `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_metodo_pago`),
  INDEX `fk_metodos_pago_usuarios_idx` (`usuario_cedula` ASC) VISIBLE,
  CONSTRAINT `fk_metodos_pago_usuarios`
    FOREIGN KEY (`usuario_cedula`)
    REFERENCES `SchemaSpeeder`.`usuarios` (`cedula`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SchemaSpeeder`.`companias`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SchemaSpeeder`.`companias` (
  `RUC` VARCHAR(13) NOT NULL,
  `empresario_cedula` VARCHAR(10) NOT NULL,
  `nombre_compania` VARCHAR(45) NOT NULL,
  `tipo_compania` VARCHAR(45) NULL DEFAULT 'Privada',
  `descripcion` VARCHAR(100) NULL DEFAULT NULL,
  PRIMARY KEY (`RUC`),
  INDEX `fk_companias_empresarios_idx` (`empresario_cedula` ASC) VISIBLE,
  CONSTRAINT `fk_companias_empresarios`
    FOREIGN KEY (`empresario_cedula`)
    REFERENCES `SchemaSpeeder`.`empresarios` (`usuario_cedula`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SchemaSpeeder`.`sucursales`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SchemaSpeeder`.`sucursales` (
  `id_direccion` INT NOT NULL,
  `compania_RUC` VARCHAR(13) NOT NULL,
  `activa` TINYINT NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_direccion`, `compania_RUC`),
  INDEX `fk_sucursales_companias_idx` (`compania_RUC` ASC) VISIBLE,
  INDEX `fk_sucursales_direcciones_idx` (`id_direccion` ASC) VISIBLE,
  CONSTRAINT `fk_sucursales_direcciones`
    FOREIGN KEY (`id_direccion`)
    REFERENCES `SchemaSpeeder`.`direcciones` (`id_direccion`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_sucursales_companias`
    FOREIGN KEY (`compania_RUC`)
    REFERENCES `SchemaSpeeder`.`companias` (`RUC`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SchemaSpeeder`.`transportistas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SchemaSpeeder`.`transportistas` (
  `usuario_cedula` VARCHAR(10) NOT NULL,
  `numero_licencia` VARCHAR(45) NULL DEFAULT NULL,
  `tipo_licencia` VARCHAR(45) NULL DEFAULT NULL,
  `zona_cobertura` VARCHAR(45) NULL DEFAULT 'General',
  `disponibilidad` ENUM('Disponible', 'Ocupado', 'Fuera de servicio') NULL DEFAULT 'Disponible',
  `fondos` VARCHAR(45) NULL,
  PRIMARY KEY (`usuario_cedula`),
  CONSTRAINT `fk_transportistas_usuarios`
    FOREIGN KEY (`usuario_cedula`)
    REFERENCES `SchemaSpeeder`.`usuarios` (`cedula`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SchemaSpeeder`.`caracteristicas_paquete`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SchemaSpeeder`.`caracteristicas_paquete` (
  `tipo_paquete` VARCHAR(45) NOT NULL,
  `fragilidad` VARCHAR(45) NULL DEFAULT 'Media',
  `inflamabilidad` VARCHAR(45) NULL DEFAULT 'Baja',
  `reactividad` VARCHAR(45) NULL DEFAULT 'Nula',
  `riesgo_salud` VARCHAR(45) NULL DEFAULT 'Bajo',
  `riesgo_espacial` VARCHAR(45) NULL DEFAULT 'Ninguno',
  PRIMARY KEY (`tipo_paquete`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SchemaSpeeder`.`paquetes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SchemaSpeeder`.`paquetes` (
  `id_paquete` INT NOT NULL AUTO_INCREMENT,
  `descripcion` VARCHAR(100) NULL DEFAULT NULL,
  `peso` DECIMAL(10,2) NULL DEFAULT 0.00,
  `tipo` VARCHAR(45) NOT NULL,
  `dimension_x` DECIMAL(10,2) NULL DEFAULT 0.00,
  `dimension_y` DECIMAL(10,2) NULL DEFAULT 0.00,
  `dimension_z` DECIMAL(10,2) NULL DEFAULT 0.00,
  `requisitos` VARCHAR(45) NULL DEFAULT 'Ninguno',
  PRIMARY KEY (`id_paquete`),
  INDEX `fk_paquetes_caracteristicas_idx` (`tipo` ASC) VISIBLE,
  CONSTRAINT `fk_paquetes_caracteristicas`
    FOREIGN KEY (`tipo`)
    REFERENCES `SchemaSpeeder`.`caracteristicas_paquete` (`tipo_paquete`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SchemaSpeeder`.`envios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SchemaSpeeder`.`envios` (
  `id_envio` INT NOT NULL AUTO_INCREMENT,
  `sucursal_RUC` VARCHAR(13) NOT NULL,
  `sucursal_id_direccion` INT NOT NULL,
  `id_direccion_entrega` INT NOT NULL,
  `transportista_cedula` VARCHAR(10) NOT NULL,
  `id_paquete` INT NOT NULL,
  `fecha_hora_inicio` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_hora_final` DATETIME NULL DEFAULT NULL,
  `tarifa` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  `estado` ENUM('En recogida', 'En camino', 'Entregado', 'Cancelado') NULL DEFAULT 'En recogida',
  PRIMARY KEY (`id_envio`),
  INDEX `fk_envios_transportistas_idx` (`transportista_cedula` ASC) VISIBLE,
  INDEX `fk_envios_paquetes_idx` (`id_paquete` ASC) VISIBLE,
  INDEX `fk_envios_direcciones_idx` (`id_direccion_entrega` ASC) VISIBLE,
  INDEX `fk_envios_sucursales_idx` (`sucursal_id_direccion` ASC, `sucursal_RUC` ASC) VISIBLE,
  CONSTRAINT `fk_envios_transportistas`
    FOREIGN KEY (`transportista_cedula`)
    REFERENCES `SchemaSpeeder`.`transportistas` (`usuario_cedula`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE,
  CONSTRAINT `fk_envios_paquetes`
    FOREIGN KEY (`id_paquete`)
    REFERENCES `SchemaSpeeder`.`paquetes` (`id_paquete`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_envios_direcciones`
    FOREIGN KEY (`id_direccion_entrega`)
    REFERENCES `SchemaSpeeder`.`direcciones` (`id_direccion`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_envios_sucursales`
    FOREIGN KEY (`sucursal_id_direccion` , `sucursal_RUC`)
    REFERENCES `SchemaSpeeder`.`sucursales` (`id_direccion` , `compania_RUC`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SchemaSpeeder`.`modelos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SchemaSpeeder`.`modelos` (
  `nombre_modelo` VARCHAR(45) NOT NULL,
  `dimension_x` DECIMAL(10,2) NULL DEFAULT 0.00,
  `dimension_y` DECIMAL(10,2) NULL DEFAULT 0.00,
  `dimension_z` DECIMAL(10,2) NULL DEFAULT 0.00,
  `maximo_peso` DECIMAL(10,2) NULL DEFAULT 0.00,
  PRIMARY KEY (`nombre_modelo`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SchemaSpeeder`.`vehiculos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SchemaSpeeder`.`vehiculos` (
  `id_vehiculo` INT NOT NULL AUTO_INCREMENT,
  `transportista_cedula` VARCHAR(10) NOT NULL,
  `nombre_modelo` VARCHAR(45) NOT NULL,
  `color` VARCHAR(45) NULL DEFAULT 'Blanco',
  `placa` VARCHAR(8) NULL DEFAULT NULL,
  PRIMARY KEY (`id_vehiculo`),
  INDEX `fk_vehiculos_modelos_idx` (`nombre_modelo` ASC) VISIBLE,
  INDEX `fk_vehiculos_transportistas_idx` (`transportista_cedula` ASC) VISIBLE,
  CONSTRAINT `fk_vehiculos_modelos`
    FOREIGN KEY (`nombre_modelo`)
    REFERENCES `SchemaSpeeder`.`modelos` (`nombre_modelo`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE,
  CONSTRAINT `fk_vehiculos_transportistas`
    FOREIGN KEY (`transportista_cedula`)
    REFERENCES `SchemaSpeeder`.`transportistas` (`usuario_cedula`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SchemaSpeeder`.`modificadores_entrega`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SchemaSpeeder`.`modificadores_entrega` (
  `id_modificador` INT NOT NULL AUTO_INCREMENT,
  `urgencia` VARCHAR(45) NULL DEFAULT 'Normal',
  `tipo_de_distancia` VARCHAR(45) NULL DEFAULT NULL,
  `multiplicador_tarifa` DECIMAL(10,2) NULL DEFAULT 1.00,
  `reduccion_tiempo` INT NULL DEFAULT 0,
  `descripcion` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id_modificador`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SchemaSpeeder`.`modificadores_aplicados`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SchemaSpeeder`.`modificadores_aplicados` (
  `id_modificador` INT NOT NULL,
  `id_envio` INT NOT NULL,
  `valor_modificador_aplicado` DECIMAL(10,2) NULL DEFAULT NULL,
  `impacto_en_tarifa` DECIMAL(10,2) NULL DEFAULT 0.00,
  PRIMARY KEY (`id_modificador`, `id_envio`),
  INDEX `fk_mod_aplicados_envios_idx` (`id_envio` ASC) VISIBLE,
  INDEX `fk_mod_aplicados_modificadores_idx` (`id_modificador` ASC) VISIBLE,
  CONSTRAINT `fk_mod_aplicados_modificadores`
    FOREIGN KEY (`id_modificador`)
    REFERENCES `SchemaSpeeder`.`modificadores_entrega` (`id_modificador`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_mod_aplicados_envios`
    FOREIGN KEY (`id_envio`)
    REFERENCES `SchemaSpeeder`.`envios` (`id_envio`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SchemaSpeeder`.`administradores`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SchemaSpeeder`.`administradores` (
  `usuario_cedula` VARCHAR(10) NOT NULL,
  `codigo_empleado` VARCHAR(20) NULL DEFAULT NULL,
  `activo` TINYINT NULL DEFAULT 1,
  PRIMARY KEY (`usuario_cedula`),
  CONSTRAINT `fk_administradores_usuarios`
    FOREIGN KEY (`usuario_cedula`)
    REFERENCES `SchemaSpeeder`.`usuarios` (`cedula`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;



















USE SchemaSpeeder;

-- -----------------------------------------------------
-- CREACION DE USUARIOS
-- -----------------------------------------------------

DROP USER IF EXISTS 'emprendedor_user'@'localhost';
DROP USER IF EXISTS 'transportista_user'@'localhost';
DROP USER IF EXISTS 'admin_user'@'localhost';

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


USE SchemaSpeeder;





-- ------------------------------------------------------
-- CREACION DE FUNCIONES
-- ------------------------------------------------------





-- ------------------------------------------------------
-- CREACION DE PROCEDIMIENTOS
-- ------------------------------------------------------

























-- ------------------------------------------------------
-- CREACION DE TRIGGERS
-- ------------------------------------------------------

USE SchemaSpeeder;

-- ----------------------------------------------------------
-- gestión de métodos de pago (solo uno predeterminado)
-- ----------------------------------------------------------

DROP TRIGGER IF EXISTS trg_metodo_pago_unico_predeterminado_insert;

DELIMITER //
CREATE TRIGGER trg_metodo_pago_unico_predeterminado_insert
BEFORE INSERT ON metodos_pago
FOR EACH ROW
BEGIN
    -- Si el nuevo método es predeterminado, quitar el flag a los demás del mismo usuario
    IF NEW.predeterminado = 1 THEN
        UPDATE metodos_pago
        SET predeterminado = 0
        WHERE usuario_cedula = NEW.usuario_cedula;
    END IF;
END//
DELIMITER ;

DROP TRIGGER IF EXISTS trg_metodo_pago_unico_predeterminado_update;

DELIMITER //
CREATE TRIGGER trg_metodo_pago_unico_predeterminado_update
BEFORE UPDATE ON metodos_pago
FOR EACH ROW
BEGIN
    -- Si se actualiza a predeterminado, quitar el flag a los demás
    IF NEW.predeterminado = 1 AND OLD.predeterminado <> 1 THEN
        UPDATE metodos_pago
        SET predeterminado = 0
        WHERE usuario_cedula = NEW.usuario_cedula AND id_metodo_pago <> NEW.id_metodo_pago;
    END IF;
END//
DELIMITER ;


-- ----------------------------------------------------------
-- validación de capacidad (antes de crear envío)
-- ----------------------------------------------------------

DROP TRIGGER IF EXISTS trg_validar_capacidad_peso;

DELIMITER //
CREATE TRIGGER trg_validar_capacidad_peso
BEFORE INSERT ON envios
FOR EACH ROW
BEGIN
    DECLARE peso_paquete DECIMAL(10,2);
    DECLARE capacidad_maxima_transportista DECIMAL(10,2);

    -- Obtener el peso del paquete a enviar
    SELECT peso INTO peso_paquete 
    FROM paquetes 
    WHERE id_paquete = NEW.id_paquete;

    -- Obtener la capacidad máxima del mejor vehículo del transportista
    SELECT MAX(m.maximo_peso) INTO capacidad_maxima_transportista
    FROM vehiculos v
    JOIN modelos m ON v.nombre_modelo = m.nombre_modelo
    WHERE v.transportista_cedula = NEW.transportista_cedula;

    -- Validar: Si no tiene vehículo o el peso excede la capacidad
    IF capacidad_maxima_transportista IS NULL OR peso_paquete > capacidad_maxima_transportista THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Error: El transportista seleccionado no posee un vehículo con capacidad suficiente para el peso de este paquete.';
    END IF;
END//
DELIMITER ;


-- ----------------------------------------------------------
-- gestión de disponibilidad de transportista
-- ----------------------------------------------------------

DROP TRIGGER IF EXISTS trg_ocupar_transportista;

DELIMITER //
CREATE TRIGGER trg_ocupar_transportista
AFTER INSERT ON envios
FOR EACH ROW
BEGIN
    -- Al asignar un envío, el transportista pasa a Ocupado
    UPDATE transportistas 
    SET disponibilidad = 'Ocupado'
    WHERE usuario_cedula = NEW.transportista_cedula;
END//
DELIMITER ;

DROP TRIGGER IF EXISTS trg_liberar_transportista;

DELIMITER //
CREATE TRIGGER trg_liberar_transportista
AFTER UPDATE ON envios
FOR EACH ROW
BEGIN
    DECLARE envios_pendientes INT;
    
    -- Solo actuar si el envío finaliza (Entregado o Cancelado)
    IF NEW.estado IN ('Entregado', 'Cancelado') AND OLD.estado NOT IN ('Entregado', 'Cancelado') THEN
        
        -- Verificar si tiene otros envíos en curso
        SELECT COUNT(*) INTO envios_pendientes
        FROM envios
        WHERE transportista_cedula = NEW.transportista_cedula
        AND estado IN ('En recogida', 'En camino');
        
        -- Si no hay pendientes, liberarlo
        IF envios_pendientes = 0 THEN
            UPDATE transportistas 
            SET disponibilidad = 'Disponible'
            WHERE usuario_cedula = NEW.transportista_cedula;
        END IF;
    END IF;
END//
DELIMITER ;


-- ----------------------------------------------------------
-- actualización automática de fecha de entrega
-- ----------------------------------------------------------

DROP TRIGGER IF EXISTS trg_fecha_entrega_auto;

DELIMITER //
CREATE TRIGGER trg_fecha_entrega_auto
BEFORE UPDATE ON envios
FOR EACH ROW
BEGIN
    -- Si el estado cambia a Entregado, setear la fecha/hora actual
    IF NEW.estado = 'Entregado' AND OLD.estado <> 'Entregado' THEN
        SET NEW.fecha_hora_final = NOW();
    END IF;
END//
DELIMITER ;


-- ----------------------------------------------------------
-- cálculo dinámico de tarifa (por modificadores)
-- ----------------------------------------------------------

DROP TRIGGER IF EXISTS trg_actualizar_tarifa_envio;

DELIMITER //
CREATE TRIGGER trg_actualizar_tarifa_envio
AFTER INSERT ON modificadores_aplicados
FOR EACH ROW
BEGIN
    -- Sumar el costo extra del modificador a la tarifa base del envío
    UPDATE envios
    SET tarifa = tarifa + NEW.impacto_en_tarifa
    WHERE id_envio = NEW.id_envio;
END//
DELIMITER ;















-- ------------------------------------------------------
-- CREACION DE VISTAS
-- ------------------------------------------------------

-- -----------------------------------------------------
-- Informe de envíos mensuales
-- Muestra la cantidad total de envíos, cuántos se completaron exitosamente, los ingresos generados y el promedio de ingresos por envío
-- ----------------------------------------------------
CREATE OR REPLACE VIEW v_informe_envios_mensuales AS
SELECT 
    DATE_FORMAT(e.fecha_hora_inicio, '%Y-%m') AS mes_anio,
    COUNT(e.id_envio) AS total_envios,
    SUM(CASE WHEN e.estado = 'Entregado' THEN 1 ELSE 0 END) AS envios_entregados,
    SUM(CASE WHEN e.estado = 'Cancelado' THEN 1 ELSE 0 END) AS envios_cancelados,
    SUM(e.tarifa) AS ingresos_totales,
    AVG(e.tarifa) AS ticket_promedio
FROM 
    SchemaSpeeder.envios e
GROUP BY 
    DATE_FORMAT(e.fecha_hora_inicio, '%Y-%m')
ORDER BY 
    mes_anio DESC;

-- -----------------------------------------------------
-- Informe de empresarios de la plataforma
-- Lista a los empresarios registrados. Incluye un conteo de cuántas sucursales activas tiene cada compañía
-- ----------------------------------------------------
CREATE OR REPLACE VIEW v_informe_empresarios AS
SELECT 
    u.cedula,
    CONCAT(u.nombre, ' ', u.apellidos) AS nombre_completo,
    emp.correo_empresarial,
    c.nombre_compania,
    c.RUC,
    c.tipo_compania,
    (SELECT COUNT(*) 
     FROM SchemaSpeeder.sucursales s 
     WHERE s.compania_RUC = c.RUC AND s.activa = 1) AS sucursales_activas
FROM 
    SchemaSpeeder.usuarios u
JOIN 
    SchemaSpeeder.empresarios emp ON u.cedula = emp.usuario_cedula
JOIN 
    SchemaSpeeder.companias c ON emp.usuario_cedula = c.empresario_cedula;

-- -----------------------------------------------------
-- Informe de transportistas
-- Detalla los transportistas, su estado actual, el vehículo que conducen (si tienen uno asignado), su zona de cobertura y
--  cuántos envíos han realizado históricamente
-- ----------------------------------------------------
CREATE OR REPLACE VIEW v_informe_transportistas AS
SELECT 
    u.cedula,
    CONCAT(u.nombre, ' ', u.apellidos) AS nombre_transportista,
    t.disponibilidad,
    t.zona_cobertura,
    t.tipo_licencia,
    v.nombre_modelo AS vehiculo_modelo,
    v.placa AS vehiculo_placa,
    COUNT(e.id_envio) AS total_envios_historicos
FROM 
    SchemaSpeeder.usuarios u
JOIN 
    SchemaSpeeder.transportistas t ON u.cedula = t.usuario_cedula
LEFT JOIN 
    SchemaSpeeder.vehiculos v ON t.usuario_cedula = v.transportista_cedula
LEFT JOIN 
    SchemaSpeeder.envios e ON t.usuario_cedula = e.transportista_cedula
GROUP BY 
    u.cedula, t.disponibilidad, t.zona_cobertura, t.tipo_licencia, v.nombre_modelo, v.placa;

-- ----------------------------------------------------
-- Informe de flujo de envíos por empresa
-- Muestra qué compañías están generando más movimiento (flujo) a través de la plataforma
-- ----------------------------------------------------
CREATE OR REPLACE VIEW v_informe_flujo_empresa AS
SELECT 
    c.nombre_compania,
    c.RUC,
    COUNT(e.id_envio) AS cantidad_envios_generados,
    SUM(e.tarifa) AS monto_total_facturado,
    MAX(e.fecha_hora_inicio) AS ultimo_envio_realizado
FROM 
    SchemaSpeeder.companias c
JOIN 
    SchemaSpeeder.sucursales s ON c.RUC = s.compania_RUC
JOIN 
    SchemaSpeeder.envios e ON s.id_direccion = e.sucursal_id_direccion 
    AND s.compania_RUC = e.sucursal_RUC
GROUP BY 
    c.nombre_compania, c.RUC
ORDER BY 
    monto_total_facturado DESC;


-- -----------------------------------------------------
-- Informe de paquetes
-- Muestra todos los paquetes historicamente trabajados. Qué se envió, de dónde salió (Ciudad de Origen), a dónde llegó (Ciudad de Destino), dimensiones y tiempos de entrega.
-- ----------------------------------------------------
CREATE OR REPLACE VIEW v_informe_historico_paquetes AS
SELECT 
    e.id_envio,
    p.descripcion AS contenido_paquete,
    p.tipo AS tipo_paquete,
    p.peso,
    -- Información de Origen (Sucursal)
    c_origen.nombre_ciudad AS ciudad_origen,
    -- Información de Destino (Dirección de entrega)
    c_destino.nombre_ciudad AS ciudad_destino,
    e.fecha_hora_inicio,
    e.fecha_hora_final,
    TIMESTAMPDIFF(HOUR, e.fecha_hora_inicio, e.fecha_hora_final) AS horas_transcurridas,
    e.estado
FROM 
    SchemaSpeeder.envios e
JOIN 
    SchemaSpeeder.paquetes p ON e.id_paquete = p.id_paquete
-- Join para obtener ciudad de la sucursal (Origen)
JOIN 
    SchemaSpeeder.sucursales s ON e.sucursal_id_direccion = s.id_direccion 
    AND e.sucursal_RUC = s.compania_RUC
JOIN 
    SchemaSpeeder.direcciones d_sucursal ON s.id_direccion = d_sucursal.id_direccion
JOIN 
    SchemaSpeeder.ciudades c_origen ON d_sucursal.ciudades_nombre_ciudad = c_origen.nombre_ciudad
-- Join para obtener ciudad de entrega (Destino)
JOIN 
    SchemaSpeeder.direcciones d_entrega ON e.id_direccion_entrega = d_entrega.id_direccion
JOIN 
    SchemaSpeeder.ciudades c_destino ON d_entrega.ciudades_nombre_ciudad = c_destino.nombre_ciudad;












SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE ciudades;
TRUNCATE TABLE direcciones;
TRUNCATE TABLE usuarios;
TRUNCATE TABLE empresarios;
TRUNCATE TABLE metodos_pago;
TRUNCATE TABLE companias;
TRUNCATE TABLE sucursales;
TRUNCATE TABLE transportistas;
TRUNCATE TABLE caracteristicas_paquete;
TRUNCATE TABLE paquetes;
TRUNCATE TABLE envios;
TRUNCATE TABLE modelos;
TRUNCATE TABLE vehiculos;
TRUNCATE TABLE modificadores_entrega;
TRUNCATE TABLE modificadores_aplicados;
TRUNCATE TABLE administradores;

SET FOREIGN_KEY_CHECKS = 1;

