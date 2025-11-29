-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`tDirecciones`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`tDirecciones` (
  `id_direccion` INT NOT NULL AUTO_INCREMENT,
  `nombre_ciudad` VARCHAR(45) NULL DEFAULT 'Sin Ciudad',
  `coordenada_x` DECIMAL(10,2) NULL DEFAULT 0.00,
  `coordenada_y` DECIMAL(10,2) NULL DEFAULT 0.00,
  `calle_principal` VARCHAR(45) NULL DEFAULT NULL,
  `calle_secundaria` VARCHAR(45) NULL DEFAULT NULL,
  `numero_edificacion` INT NULL DEFAULT NULL,
  `detalle` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id_direccion`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Usuario` (
  `cedula` VARCHAR(10) NOT NULL,
  `nombre` VARCHAR(45) NULL DEFAULT NULL,
  `apellidos` VARCHAR(45) NULL DEFAULT NULL,
  `correo` VARCHAR(45) NULL DEFAULT NULL,
  `contrasena` VARCHAR(255) NULL DEFAULT NULL,
  `numero` VARCHAR(45) NULL DEFAULT NULL,
  `tDirecciones_id_direccion` INT NOT NULL,
  PRIMARY KEY (`cedula`),
  INDEX `fk_Cliente_tDirecciones1_idx` (`tDirecciones_id_direccion` ASC) VISIBLE,
  CONSTRAINT `fk_Cliente_tDirecciones1`
    FOREIGN KEY (`tDirecciones_id_direccion`)
    REFERENCES `mydb`.`tDirecciones` (`id_direccion`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`tEmpresarios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`tEmpresarios` (
  `tUsuario_cedula` VARCHAR(10) NOT NULL,
  `cargo_empresa` VARCHAR(45) NULL DEFAULT 'Empleado',
  `correo_empresarial` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`tUsuario_cedula`),
  INDEX `fk_Clientes_table11_idx` (`tUsuario_cedula` ASC) VISIBLE,
  CONSTRAINT `fk_Clientes_table11`
    FOREIGN KEY (`tUsuario_cedula`)
    REFERENCES `mydb`.`Usuario` (`cedula`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`tMetodos_pago`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`tMetodos_pago` (
  `id_metodo_pago` INT NOT NULL,
  `cedula` VARCHAR(10) NOT NULL,
  `tipo` ENUM('tarjeta', 'Cuenta Bancaria') NULL DEFAULT 'tarjeta',
  `datos` JSON NULL DEFAULT NULL,
  `predeterminado` TINYINT NULL DEFAULT 0,
  `created_at` DATE NULL DEFAULT CURRENT_DATE,
  PRIMARY KEY (`id_metodo_pago`),
  INDEX `fk_tMetodos_pago_tEmpresarios1_idx` (`cedula` ASC) VISIBLE,
  CONSTRAINT `fk_tMetodos_pago_tEmpresarios1`
    FOREIGN KEY (`cedula`)
    REFERENCES `mydb`.`tEmpresarios` (`tUsuario_cedula`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`tCompanias`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`tCompanias` (
  `RUC` VARCHAR(10) NOT NULL,
  `cedula` VARCHAR(10) NOT NULL,
  `nombre_compania` VARCHAR(45) NULL DEFAULT NULL,
  `tipo_compania` VARCHAR(45) NULL DEFAULT 'Privada',
  `descripcion` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`RUC`),
  INDEX `fk_tCompanias_tEmpresarios1_idx` (`cedula` ASC) VISIBLE,
  CONSTRAINT `fk_tCompanias_tEmpresarios1`
    FOREIGN KEY (`cedula`)
    REFERENCES `mydb`.`tEmpresarios` (`tUsuario_cedula`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`tSucursales`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`tSucursales` (
  `tDirecciones_id_direccion` INT NOT NULL,
  `companias_RUC` VARCHAR(10) NOT NULL,
  `activa` TINYINT NOT NULL DEFAULT 1,
  PRIMARY KEY (`tDirecciones_id_direccion`, `companias_RUC`),
  INDEX `fk_tDirecciones_has_companias_companias1_idx` (`companias_RUC` ASC) VISIBLE,
  INDEX `fk_tDirecciones_has_companias_tDirecciones1_idx` (`tDirecciones_id_direccion` ASC) VISIBLE,
  CONSTRAINT `fk_tDirecciones_has_companias_tDirecciones1`
    FOREIGN KEY (`tDirecciones_id_direccion`)
    REFERENCES `mydb`.`tDirecciones` (`id_direccion`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_tDirecciones_has_companias_companias1`
    FOREIGN KEY (`companias_RUC`)
    REFERENCES `mydb`.`tCompanias` (`RUC`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`tTransportista`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`tTransportista` (
  `tUsuario_cedula` VARCHAR(10) NOT NULL,
  `numero_licencia` VARCHAR(45) NULL DEFAULT NULL,
  `tipo_licencia` VARCHAR(45) NULL DEFAULT NULL,
  `zona_cobertura` VARCHAR(45) NULL DEFAULT 'General',
  `disponibilidad` VARCHAR(45) NULL DEFAULT 'Disponible',
  PRIMARY KEY (`tUsuario_cedula`),
  CONSTRAINT `fk_tTransportista_Cliente1`
    FOREIGN KEY (`tUsuario_cedula`)
    REFERENCES `mydb`.`Usuario` (`cedula`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`tCaracteristicasPaquete`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`tCaracteristicasPaquete` (
  `tipo_paquete` VARCHAR(45) NOT NULL,
  `fragilidad` VARCHAR(45) NULL DEFAULT 'Media',
  `inflamabilidad` VARCHAR(45) NULL DEFAULT 'Baja',
  `reactividad` VARCHAR(45) NULL DEFAULT 'Nula',
  `riesgo_salud` VARCHAR(45) NULL DEFAULT 'Bajo',
  `riesgo_espacial` VARCHAR(45) NULL DEFAULT 'Ninguno',
  PRIMARY KEY (`tipo_paquete`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`tPaquete`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`tPaquete` (
  `id_paquete` INT NOT NULL,
  `descripcion` VARCHAR(45) NULL DEFAULT NULL,
  `peso` DECIMAL(10,2) NULL DEFAULT 0.00,
  `tipo` VARCHAR(45) NOT NULL,
  `dimension_x` DECIMAL(10,2) NULL DEFAULT 0.00,
  `dimension_y` DECIMAL(10,2) NULL DEFAULT 0.00,
  `dimension_z` DECIMAL(10,2) NULL DEFAULT 0.00,
  `requisitos` VARCHAR(45) NULL DEFAULT 'Ninguno',
  PRIMARY KEY (`id_paquete`),
  INDEX `fk_tPaquete_tCaracteristicasPaquete1_idx` (`tipo` ASC) VISIBLE,
  CONSTRAINT `fk_tPaquete_tCaracteristicasPaquete1`
    FOREIGN KEY (`tipo`)
    REFERENCES `mydb`.`tCaracteristicasPaquete` (`tipo_paquete`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`tEnvios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`tEnvios` (
  `id_envios` INT NOT NULL,
  `tSucursales_companias_RUC` VARCHAR(10) NOT NULL,
  `tDirecciones_direccion_entrega` INT NOT NULL,
  `tTransportista_cedula` VARCHAR(10) NOT NULL,
  `tPaquete_id_paquete` INT NOT NULL,
  `fecha_hora_inicio` DATETIME NULL DEFAULT NULL,
  `fecha_hora_final` DATETIME NULL DEFAULT NULL,
  `tarifa` DECIMAL(10,2) NULL DEFAULT 0.00,
  `estado` ENUM('En recogida', 'En camino', 'Cancelado') NULL DEFAULT 'En recogida',
  `tSucursales_id_direccion` INT NOT NULL,
  PRIMARY KEY (`id_envios`),
  INDEX `fk_tEnvios_tTransportista1_idx` (`tTransportista_cedula` ASC) VISIBLE,
  INDEX `fk_tEnvios_tPaquete1_idx` (`tPaquete_id_paquete` ASC) VISIBLE,
  INDEX `fk_tEnvios_tDirecciones1_idx` (`tDirecciones_direccion_entrega` ASC) VISIBLE,
  INDEX `fk_tEnvios_tSucursales1_idx` (`tSucursales_id_direccion` ASC, `tSucursales_companias_RUC` ASC) VISIBLE,
  CONSTRAINT `fk_tEnvios_tTransportista1`
    FOREIGN KEY (`tTransportista_cedula`)
    REFERENCES `mydb`.`tTransportista` (`tUsuario_cedula`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_tEnvios_tPaquete1`
    FOREIGN KEY (`tPaquete_id_paquete`)
    REFERENCES `mydb`.`tPaquete` (`id_paquete`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_tEnvios_tDirecciones1`
    FOREIGN KEY (`tDirecciones_direccion_entrega`)
    REFERENCES `mydb`.`tDirecciones` (`id_direccion`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_tEnvios_tSucursales1`
    FOREIGN KEY (`tSucursales_id_direccion` , `tSucursales_companias_RUC`)
    REFERENCES `mydb`.`tSucursales` (`tDirecciones_id_direccion` , `companias_RUC`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`tModelos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`tModelos` (
  `nombre_modelo` VARCHAR(45) NOT NULL,
  `dimension_x` DECIMAL(10,2) NULL DEFAULT 0.00,
  `dimension_y` DECIMAL(10,2) NULL DEFAULT 0.00,
  `dimension_z` DECIMAL(10,2) NULL DEFAULT 0.00,
  `maximo_peso` DECIMAL(10,2) NULL DEFAULT 0.00,
  PRIMARY KEY (`nombre_modelo`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`tVehiculos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`tVehiculos` (
  `id_vehiculo` INT NOT NULL,
  `tTransportista_cedula` VARCHAR(10) NOT NULL,
  `nombre_modelo` VARCHAR(45) NOT NULL,
  `color` VARCHAR(45) NULL DEFAULT 'Blanco',
  `placa` VARCHAR(8) NULL DEFAULT NULL,
  PRIMARY KEY (`id_vehiculo`),
  INDEX `fk_tVehiculos_tModelos1_idx` (`nombre_modelo` ASC) VISIBLE,
  INDEX `fk_tVehiculos_tTransportista1_idx` (`tTransportista_cedula` ASC) VISIBLE,
  CONSTRAINT `fk_tVehiculos_tModelos1`
    FOREIGN KEY (`nombre_modelo`)
    REFERENCES `mydb`.`tModelos` (`nombre_modelo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_tVehiculos_tTransportista1`
    FOREIGN KEY (`tTransportista_cedula`)
    REFERENCES `mydb`.`tTransportista` (`tUsuario_cedula`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`tModificadoresEntrega`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`tModificadoresEntrega` (
  `id_modificador` INT NOT NULL,
  `urgencia` VARCHAR(45) NULL DEFAULT 'Normal',
  `tipo_de_distancia` VARCHAR(45) NULL DEFAULT NULL,
  `multiplicador_tarifa` DECIMAL(10,2) NULL DEFAULT 1.00,
  `reduccion_tiempo` INT NULL DEFAULT 0,
  `descripcion` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id_modificador`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Modificadores_aplicados`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Modificadores_aplicados` (
  `tModificadoresEntrega_id_modificador` INT NOT NULL,
  `tEnvios_id_envios` INT NOT NULL,
  `valor_modificador_aplicado` DECIMAL(10,2) NULL DEFAULT NULL,
  `impacto_en_tarifa` DECIMAL(10,2) NULL DEFAULT 0.00,
  PRIMARY KEY (`tModificadoresEntrega_id_modificador`, `tEnvios_id_envios`),
  INDEX `fk_tModificadoresEntrega_has_tEnvios_tEnvios1_idx` (`tEnvios_id_envios` ASC) VISIBLE,
  INDEX `fk_tModificadoresEntrega_has_tEnvios_tModificadoresEntrega1_idx` (`tModificadoresEntrega_id_modificador` ASC) VISIBLE,
  CONSTRAINT `fk_tModificadoresEntrega_has_tEnvios_tModificadoresEntrega1`
    FOREIGN KEY (`tModificadoresEntrega_id_modificador`)
    REFERENCES `mydb`.`tModificadoresEntrega` (`id_modificador`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_tModificadoresEntrega_has_tEnvios_tEnvios1`
    FOREIGN KEY (`tEnvios_id_envios`)
    REFERENCES `mydb`.`tEnvios` (`id_envios`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Administrador`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Administrador` (
  `tUsuario_cedula` VARCHAR(10) NOT NULL,
  `codigo_empleado` VARCHAR(10) NULL DEFAULT NULL,
  `estado_cuenta` ENUM('SuperAdmin', 'Gerente', 'Soporte', 'Auditor') NULL DEFAULT 'Soporte',
  PRIMARY KEY (`tUsuario_cedula`),
  CONSTRAINT `fk_Administrador_Usuario1`
    FOREIGN KEY (`tUsuario_cedula`)
    REFERENCES `mydb`.`Usuario` (`cedula`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
