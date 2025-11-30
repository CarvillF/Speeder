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
-- Table `mydb`.`direcciones`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`direcciones` (
  `id_direccion` INT NOT NULL AUTO_INCREMENT,
  `nombre_ciudad` VARCHAR(45) NOT NULL,
  `coordenada_x` DECIMAL(11,8) NULL DEFAULT 0.00000000,
  `coordenada_y` DECIMAL(11,8) NULL DEFAULT 0.00000000,
  `calle_principal` VARCHAR(45) NULL DEFAULT NULL,
  `calle_secundaria` VARCHAR(45) NULL DEFAULT NULL,
  `numero_edificacion` VARCHAR(20) NULL DEFAULT NULL,
  `detalle` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id_direccion`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`usuarios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`usuarios` (
  `cedula` VARCHAR(10) NOT NULL,
  `nombre` VARCHAR(45) NOT NULL,
  `apellidos` VARCHAR(45) NOT NULL,
  `correo` VARCHAR(60) NOT NULL,
  `contrasena` VARCHAR(255) NOT NULL,
  `numero_telefono` VARCHAR(15) NULL DEFAULT NULL,
  `id_direccion_principal` INT NULL DEFAULT NULL,
  PRIMARY KEY (`cedula`),
  UNIQUE INDEX `correo_UNIQUE` (`correo` ASC) VISIBLE,
  INDEX `fk_usuarios_direcciones_idx` (`id_direccion_principal` ASC) VISIBLE,
  CONSTRAINT `fk_usuarios_direcciones`
    FOREIGN KEY (`id_direccion_principal`)
    REFERENCES `mydb`.`direcciones` (`id_direccion`)
    ON DELETE SET NULL
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`empresarios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`empresarios` (
  `usuario_cedula` VARCHAR(10) NOT NULL,
  `cargo_empresa` VARCHAR(45) NULL DEFAULT 'Empleado',
  `correo_empresarial` VARCHAR(60) NULL DEFAULT NULL,
  PRIMARY KEY (`usuario_cedula`),
  CONSTRAINT `fk_empresarios_usuarios`
    FOREIGN KEY (`usuario_cedula`)
    REFERENCES `mydb`.`usuarios` (`cedula`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`metodos_pago`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`metodos_pago` (
  `id_metodo_pago` INT NOT NULL AUTO_INCREMENT,
  `usuario_cedula` VARCHAR(10) NOT NULL,
  `tipo` ENUM('Tarjeta', 'Cuenta Bancaria', 'Efectivo') NULL DEFAULT 'Tarjeta',
  `datos` JSON NULL DEFAULT NULL,
  `predeterminado` TINYINT NULL DEFAULT 0,
  `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_metodo_pago`),
  INDEX `fk_metodos_pago_usuarios_idx` (`usuario_cedula` ASC) VISIBLE,
  CONSTRAINT `fk_metodos_pago_usuarios`
    FOREIGN KEY (`usuario_cedula`)
    REFERENCES `mydb`.`usuarios` (`cedula`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`companias`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`companias` (
  `RUC` VARCHAR(13) NOT NULL,
  `empresario_cedula` VARCHAR(10) NOT NULL,
  `nombre_compania` VARCHAR(45) NOT NULL,
  `tipo_compania` VARCHAR(45) NULL DEFAULT 'Privada',
  `descripcion` VARCHAR(100) NULL DEFAULT NULL,
  PRIMARY KEY (`RUC`),
  INDEX `fk_companias_empresarios_idx` (`empresario_cedula` ASC) VISIBLE,
  CONSTRAINT `fk_companias_empresarios`
    FOREIGN KEY (`empresario_cedula`)
    REFERENCES `mydb`.`empresarios` (`usuario_cedula`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`sucursales`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`sucursales` (
  `id_direccion` INT NOT NULL,
  `compania_RUC` VARCHAR(13) NOT NULL,
  `activa` TINYINT NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_direccion`, `compania_RUC`),
  INDEX `fk_sucursales_companias_idx` (`compania_RUC` ASC) VISIBLE,
  INDEX `fk_sucursales_direcciones_idx` (`id_direccion` ASC) VISIBLE,
  CONSTRAINT `fk_sucursales_direcciones`
    FOREIGN KEY (`id_direccion`)
    REFERENCES `mydb`.`direcciones` (`id_direccion`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_sucursales_companias`
    FOREIGN KEY (`compania_RUC`)
    REFERENCES `mydb`.`companias` (`RUC`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`transportistas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`transportistas` (
  `usuario_cedula` VARCHAR(10) NOT NULL,
  `numero_licencia` VARCHAR(45) NULL DEFAULT NULL,
  `tipo_licencia` VARCHAR(45) NULL DEFAULT NULL,
  `zona_cobertura` VARCHAR(45) NULL DEFAULT 'General',
  `disponibilidad` ENUM('Disponible', 'Ocupado', 'Fuera de servicio') NULL DEFAULT 'Disponible',
  PRIMARY KEY (`usuario_cedula`),
  CONSTRAINT `fk_transportistas_usuarios`
    FOREIGN KEY (`usuario_cedula`)
    REFERENCES `mydb`.`usuarios` (`cedula`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`caracteristicas_paquete`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`caracteristicas_paquete` (
  `tipo_paquete` VARCHAR(45) NOT NULL,
  `fragilidad` VARCHAR(45) NULL DEFAULT 'Media',
  `inflamabilidad` VARCHAR(45) NULL DEFAULT 'Baja',
  `reactividad` VARCHAR(45) NULL DEFAULT 'Nula',
  `riesgo_salud` VARCHAR(45) NULL DEFAULT 'Bajo',
  `riesgo_espacial` VARCHAR(45) NULL DEFAULT 'Ninguno',
  PRIMARY KEY (`tipo_paquete`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`paquetes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`paquetes` (
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
    REFERENCES `mydb`.`caracteristicas_paquete` (`tipo_paquete`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`envios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`envios` (
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
    REFERENCES `mydb`.`transportistas` (`usuario_cedula`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE,
  CONSTRAINT `fk_envios_paquetes`
    FOREIGN KEY (`id_paquete`)
    REFERENCES `mydb`.`paquetes` (`id_paquete`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_envios_direcciones`
    FOREIGN KEY (`id_direccion_entrega`)
    REFERENCES `mydb`.`direcciones` (`id_direccion`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_envios_sucursales`
    FOREIGN KEY (`sucursal_id_direccion` , `sucursal_RUC`)
    REFERENCES `mydb`.`sucursales` (`id_direccion` , `compania_RUC`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`modelos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`modelos` (
  `nombre_modelo` VARCHAR(45) NOT NULL,
  `dimension_x` DECIMAL(10,2) NULL DEFAULT 0.00,
  `dimension_y` DECIMAL(10,2) NULL DEFAULT 0.00,
  `dimension_z` DECIMAL(10,2) NULL DEFAULT 0.00,
  `maximo_peso` DECIMAL(10,2) NULL DEFAULT 0.00,
  PRIMARY KEY (`nombre_modelo`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`vehiculos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`vehiculos` (
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
    REFERENCES `mydb`.`modelos` (`nombre_modelo`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE,
  CONSTRAINT `fk_vehiculos_transportistas`
    FOREIGN KEY (`transportista_cedula`)
    REFERENCES `mydb`.`transportistas` (`usuario_cedula`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`modificadores_entrega`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`modificadores_entrega` (
  `id_modificador` INT NOT NULL AUTO_INCREMENT,
  `urgencia` VARCHAR(45) NULL DEFAULT 'Normal',
  `tipo_de_distancia` VARCHAR(45) NULL DEFAULT NULL,
  `multiplicador_tarifa` DECIMAL(10,2) NULL DEFAULT 1.00,
  `reduccion_tiempo` INT NULL DEFAULT 0,
  `descripcion` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id_modificador`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`modificadores_aplicados`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`modificadores_aplicados` (
  `id_modificador` INT NOT NULL,
  `id_envio` INT NOT NULL,
  `valor_modificador_aplicado` DECIMAL(10,2) NULL DEFAULT NULL,
  `impacto_en_tarifa` DECIMAL(10,2) NULL DEFAULT 0.00,
  PRIMARY KEY (`id_modificador`, `id_envio`),
  INDEX `fk_mod_aplicados_envios_idx` (`id_envio` ASC) VISIBLE,
  INDEX `fk_mod_aplicados_modificadores_idx` (`id_modificador` ASC) VISIBLE,
  CONSTRAINT `fk_mod_aplicados_modificadores`
    FOREIGN KEY (`id_modificador`)
    REFERENCES `mydb`.`modificadores_entrega` (`id_modificador`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_mod_aplicados_envios`
    FOREIGN KEY (`id_envio`)
    REFERENCES `mydb`.`envios` (`id_envio`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`administradores`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`administradores` (
  `usuario_cedula` VARCHAR(10) NOT NULL,
  `codigo_empleado` VARCHAR(20) NULL DEFAULT NULL,
  `rol` ENUM('SuperAdmin', 'Gerente', 'Soporte', 'Auditor') NULL DEFAULT 'Soporte',
  PRIMARY KEY (`usuario_cedula`),
  CONSTRAINT `fk_administradores_usuarios`
    FOREIGN KEY (`usuario_cedula`)
    REFERENCES `mydb`.`usuarios` (`cedula`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
