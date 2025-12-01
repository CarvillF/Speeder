-- -----------------------------------------------------
-- script de triggers para schemaspeeder
-- unificación de lógica de negocio y automatización
-- -----------------------------------------------------

USE `SchemaSpeeder`;

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