
USE `SchemaSpeeder`;

-- ----------------------------------------------------------
-- Procedimiento: sp_login
-- Descripción: Autentica un usuario mediante correo y contraseña
--              y retorna información del usuario si las credenciales son válidas
-- ----------------------------------------------------------

DROP PROCEDURE IF EXISTS sp_login;

DELIMITER //
CREATE PROCEDURE sp_login(
    IN p_correo VARCHAR(60),
    IN p_contrasena VARCHAR(255),
    OUT p_exists TINYINT,
    OUT p_cedula VARCHAR(10),
    OUT p_nombre VARCHAR(45),
    OUT p_user_type VARCHAR(20)
)
BEGIN
    DECLARE v_apellidos VARCHAR(45);
    
    -- Inicializar variables de salida
    SET p_exists = 0;
    SET p_cedula = NULL;
    SET p_nombre = NULL;
    SET p_user_type = NULL;
    
    -- Buscar usuario con credenciales correctas
    SELECT u.cedula, u.nombre, u.apellidos
    INTO p_cedula, p_nombre, v_apellidos
    FROM usuarios u
    WHERE u.correo = p_correo 
      AND u.contrasena = p_contrasena
    LIMIT 1;
    
    -- Si se encontró el usuario
    IF p_cedula IS NOT NULL THEN
        SET p_exists = 1;
        
        -- Determinar tipo de usuario
        IF EXISTS (SELECT 1 FROM empresarios WHERE usuario_cedula = p_cedula) THEN
            SET p_user_type = 'BUSINESS';
        ELSEIF EXISTS (SELECT 1 FROM transportistas WHERE usuario_cedula = p_cedula) THEN
            SET p_user_type = 'DRIVER';
        ELSEIF EXISTS (SELECT 1 FROM administradores WHERE usuario_cedula = p_cedula) THEN
            SET p_user_type = 'ADMIN';
        END IF;
        
        -- Concatenar nombre completo
        SET p_nombre = CONCAT(p_nombre, ' ', v_apellidos);
    END IF;
END//
DELIMITER ;


-- ----------------------------------------------------------
-- Procedimiento: sp_register_business
-- Descripción: Registra un nuevo usuario empresario en el sistema
--              con manejo de transacciones
-- ----------------------------------------------------------

DROP PROCEDURE IF EXISTS sp_register_business;

DELIMITER //
CREATE PROCEDURE sp_register_business(
    IN p_cedula VARCHAR(10),
    IN p_nombre VARCHAR(45),
    IN p_apellidos VARCHAR(45),
    IN p_correo VARCHAR(60),
    IN p_contrasena VARCHAR(255),
    IN p_telefono VARCHAR(15),
    IN p_cargo_empresa VARCHAR(45),
    IN p_correo_empresarial VARCHAR(60),
    OUT p_success TINYINT,
    OUT p_error_msg VARCHAR(255)
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        -- En caso de error, hacer rollback
        ROLLBACK;
        SET p_success = 0;
        SET p_error_msg = 'Error al registrar el empresario. Por favor intente nuevamente.';
    END;
    
    -- Inicializar variables de salida
    SET p_success = 0;
    SET p_error_msg = NULL;
    
    -- Validar que la cédula no exista
    IF EXISTS (SELECT 1 FROM usuarios WHERE cedula = p_cedula) THEN
        SET p_error_msg = 'La cédula ya está registrada en el sistema.';
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Cédula duplicada';
    END IF;
    
    -- Validar que el correo no exista
    IF EXISTS (SELECT 1 FROM usuarios WHERE correo = p_correo) THEN
        SET p_error_msg = 'El correo electrónico ya está registrado en el sistema.';
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Correo duplicado';
    END IF;
    
    -- Iniciar transacción
    START TRANSACTION;
    
    -- Insertar en tabla usuarios
    INSERT INTO usuarios (
        cedula, 
        nombre, 
        apellidos, 
        correo, 
        contrasena, 
        numero_telefono
    ) VALUES (
        p_cedula,
        p_nombre,
        p_apellidos,
        p_correo,
        p_contrasena,
        p_telefono
    );
    
    -- Insertar en tabla empresarios
    INSERT INTO empresarios (
        usuario_cedula,
        cargo_empresa,
        correo_empresarial
    ) VALUES (
        p_cedula,
        IFNULL(p_cargo_empresa, 'Empleado'),
        p_correo_empresarial
    );
    
    -- Si todo salió bien, hacer commit
    COMMIT;
    SET p_success = 1;
    SET p_error_msg = 'Registro exitoso';
END//
DELIMITER ;


-- ==============================================================
-- PAIR 1: COMPANY MANAGEMENT PROCEDURES
-- ==============================================================

-- ----------------------------------------------------------
-- Procedimiento: sp_get_companies
-- Descripción: Obtiene todas las compañías asociadas a un empresario
-- ----------------------------------------------------------

DROP PROCEDURE IF EXISTS sp_get_companies;

DELIMITER //
CREATE PROCEDURE sp_get_companies(
    IN p_usuario_cedula VARCHAR(10)
)
BEGIN
    SELECT 
        c.RUC,
        c.nombre_compania,
        c.tipo_compania,
        c.descripcion
    FROM companias c
    WHERE c.empresario_cedula = p_usuario_cedula
    ORDER BY c.nombre_compania;
END//
DELIMITER ;


-- ----------------------------------------------------------
-- Procedimiento: sp_create_company
-- Descripción: Crea una nueva compañía para un empresario
-- ----------------------------------------------------------

DROP PROCEDURE IF EXISTS sp_create_company;

DELIMITER //
CREATE PROCEDURE sp_create_company(
    IN p_ruc VARCHAR(13),
    IN p_empresario_cedula VARCHAR(10),
    IN p_nombre VARCHAR(45),
    IN p_tipo VARCHAR(45),
    IN p_descripcion VARCHAR(100),
    OUT p_success TINYINT,
    OUT p_error_msg VARCHAR(255)
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET p_success = 0;
        SET p_error_msg = 'Error al crear la compañía. Por favor intente nuevamente.';
    END;
    
    SET p_success = 0;
    SET p_error_msg = NULL;
    
    -- Validar que el RUC no exista
    IF EXISTS (SELECT 1 FROM companias WHERE RUC = p_ruc) THEN
        SET p_error_msg = 'El RUC ya está registrado en el sistema.';
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'RUC duplicado';
    END IF;
    
    -- Validar que el empresario exista
    IF NOT EXISTS (SELECT 1 FROM empresarios WHERE usuario_cedula = p_empresario_cedula) THEN
        SET p_error_msg = 'El empresario no existe en el sistema.';
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Empresario no encontrado';
    END IF;
    
    START TRANSACTION;
    
    INSERT INTO companias (
        RUC,
        empresario_cedula,
        nombre_compania,
        tipo_compania,
        descripcion
    ) VALUES (
        p_ruc,
        p_empresario_cedula,
        p_nombre,
        IFNULL(p_tipo, 'Privada'),
        p_descripcion
    );
    
    COMMIT;
    SET p_success = 1;
    SET p_error_msg = 'Compañía creada exitosamente';
END//
DELIMITER ;


-- ----------------------------------------------------------
-- Procedimiento: sp_update_company
-- Descripción: Actualiza los datos de una compañía existente
-- ----------------------------------------------------------

DROP PROCEDURE IF EXISTS sp_update_company;

DELIMITER //
CREATE PROCEDURE sp_update_company(
    IN p_ruc VARCHAR(13),
    IN p_nombre VARCHAR(45),
    IN p_tipo VARCHAR(45),
    IN p_descripcion VARCHAR(100),
    OUT p_success TINYINT,
    OUT p_error_msg VARCHAR(255)
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET p_success = 0;
        SET p_error_msg = 'Error al actualizar la compañía.';
    END;
    
    SET p_success = 0;
    SET p_error_msg = NULL;
    
    -- Validar que la compañía exista
    IF NOT EXISTS (SELECT 1 FROM companias WHERE RUC = p_ruc) THEN
        SET p_error_msg = 'La compañía no existe en el sistema.';
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Compañía no encontrada';
    END IF;
    
    START TRANSACTION;
    
    UPDATE companias
    SET nombre_compania = p_nombre,
        tipo_compania = p_tipo,
        descripcion = p_descripcion
    WHERE RUC = p_ruc;
    
    COMMIT;
    SET p_success = 1;
    SET p_error_msg = 'Compañía actualizada exitosamente';
END//
DELIMITER ;


-- ----------------------------------------------------------
-- Procedimiento: sp_delete_company
-- Descripción: Elimina una compañía (cascada a sucursales por FK)
-- ----------------------------------------------------------

DROP PROCEDURE IF EXISTS sp_delete_company;

DELIMITER //
CREATE PROCEDURE sp_delete_company(
    IN p_ruc VARCHAR(13),
    OUT p_success TINYINT,
    OUT p_error_msg VARCHAR(255)
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET p_success = 0;
        SET p_error_msg = 'Error al eliminar la compañía.';
    END;
    
    SET p_success = 0;
    SET p_error_msg = NULL;
    
    -- Validar que la compañía exista
    IF NOT EXISTS (SELECT 1 FROM companias WHERE RUC = p_ruc) THEN
        SET p_error_msg = 'La compañía no existe en el sistema.';
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Compañía no encontrada';
    END IF;
    
    START TRANSACTION;
    
    -- El DELETE CASCADE en la FK eliminará las sucursales automáticamente
    DELETE FROM companias WHERE RUC = p_ruc;
    
    COMMIT;
    SET p_success = 1;
    SET p_error_msg = 'Compañía eliminada exitosamente';
END//
DELIMITER ;


-- ==============================================================
-- PAIR 2: BRANCH MANAGEMENT PROCEDURES
-- ==============================================================

-- ----------------------------------------------------------
-- Procedimiento: sp_get_branches
-- Descripción: Obtiene todas las sucursales de las compañías de un empresario
-- ----------------------------------------------------------

DROP PROCEDURE IF EXISTS sp_get_branches;

DELIMITER //
CREATE PROCEDURE sp_get_branches(
    IN p_usuario_cedula VARCHAR(10)
)
BEGIN
    SELECT 
        s.id_direccion,
        s.compania_RUC,
        s.activa
    FROM sucursales s
    INNER JOIN companias c ON s.compania_RUC = c.RUC
    WHERE c.empresario_cedula = p_usuario_cedula
    ORDER BY s.compania_RUC, s.id_direccion;
END//
DELIMITER ;


-- ----------------------------------------------------------
-- Procedimiento: sp_create_branch
-- Descripción: Crea una nueva sucursal para una compañía
-- ----------------------------------------------------------

DROP PROCEDURE IF EXISTS sp_create_branch;

DELIMITER //
CREATE PROCEDURE sp_create_branch(
    IN p_id_direccion INT,
    IN p_compania_ruc VARCHAR(13),
    IN p_activa TINYINT,
    OUT p_success TINYINT,
    OUT p_error_msg VARCHAR(255)
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET p_success = 0;
        SET p_error_msg = 'Error al crear la sucursal.';
    END;
    
    SET p_success = 0;
    SET p_error_msg = NULL;
    
    -- Validar que la compañía exista
    IF NOT EXISTS (SELECT 1 FROM companias WHERE RUC = p_compania_ruc) THEN
        SET p_error_msg = 'La compañía no existe en el sistema.';
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Compañía no encontrada';
    END IF;
    
    -- Validar que la dirección exista
    IF NOT EXISTS (SELECT 1 FROM direcciones WHERE id_direccion = p_id_direccion) THEN
        SET p_error_msg = 'La dirección no existe en el sistema.';
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Dirección no encontrada';
    END IF;
    
    -- Validar que no exista ya esta sucursal
    IF EXISTS (SELECT 1 FROM sucursales WHERE id_direccion = p_id_direccion AND compania_RUC = p_compania_ruc) THEN
        SET p_error_msg = 'Esta sucursal ya existe.';
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Sucursal duplicada';
    END IF;
    
    START TRANSACTION;
    
    INSERT INTO sucursales (
        id_direccion,
        compania_RUC,
        activa
    ) VALUES (
        p_id_direccion,
        p_compania_ruc,
        IFNULL(p_activa, 1)
    );
    
    COMMIT;
    SET p_success = 1;
    SET p_error_msg = 'Sucursal creada exitosamente';
END//
DELIMITER ;


-- ----------------------------------------------------------
-- Procedimiento: sp_update_branch
-- Descripción: Actualiza el estado de una sucursal
-- ----------------------------------------------------------

DROP PROCEDURE IF EXISTS sp_update_branch;

DELIMITER //
CREATE PROCEDURE sp_update_branch(
    IN p_id_direccion INT,
    IN p_compania_ruc VARCHAR(13),
    IN p_activa TINYINT,
    OUT p_success TINYINT,
    OUT p_error_msg VARCHAR(255)
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET p_success = 0;
        SET p_error_msg = 'Error al actualizar la sucursal.';
    END;
    
    SET p_success = 0;
    SET p_error_msg = NULL;
    
    -- Validar que la sucursal exista
    IF NOT EXISTS (SELECT 1 FROM sucursales WHERE id_direccion = p_id_direccion AND compania_RUC = p_compania_ruc) THEN
        SET p_error_msg = 'La sucursal no existe en el sistema.';
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Sucursal no encontrada';
    END IF;
    
    START TRANSACTION;
    
    UPDATE sucursales
    SET activa = p_activa
    WHERE id_direccion = p_id_direccion AND compania_RUC = p_compania_ruc;
    
    COMMIT;
    SET p_success = 1;
    SET p_error_msg = 'Sucursal actualizada exitosamente';
END//
DELIMITER ;


-- ----------------------------------------------------------
-- Procedimiento: sp_delete_branch
-- Descripción: Elimina una sucursal
-- ----------------------------------------------------------

DROP PROCEDURE IF EXISTS sp_delete_branch;

DELIMITER //
CREATE PROCEDURE sp_delete_branch(
    IN p_id_direccion INT,
    OUT p_success TINYINT,
    OUT p_error_msg VARCHAR(255)
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET p_success = 0;
        SET p_error_msg = 'Error al eliminar la sucursal.';
    END;
    
    SET p_success = 0;
    SET p_error_msg = NULL;
    
    -- Validar que la sucursal exista
    IF NOT EXISTS (SELECT 1 FROM sucursales WHERE id_direccion = p_id_direccion) THEN
        SET p_error_msg = 'La sucursal no existe en el sistema.';
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Sucursal no encontrada';
    END IF;
    
    START TRANSACTION;
    
    DELETE FROM sucursales WHERE id_direccion = p_id_direccion;
    
    COMMIT;
    SET p_success = 1;
    SET p_error_msg = 'Sucursal eliminada exitosamente';
END//
DELIMITER ;


-- ==============================================================
-- PAIR 3: PAYMENT METHOD MANAGEMENT PROCEDURES
-- ==============================================================

-- ----------------------------------------------------------
-- Procedimiento: sp_get_payment_methods
-- Descripción: Obtiene todos los métodos de pago de un usuario
-- ----------------------------------------------------------

DROP PROCEDURE IF EXISTS sp_get_payment_methods;

DELIMITER //
CREATE PROCEDURE sp_get_payment_methods(
    IN p_usuario_cedula VARCHAR(10)
)
BEGIN
    SELECT 
        id_metodo_pago,
        tipo,
        datos,
        predeterminado,
        created_at
    FROM metodos_pago
    WHERE usuario_cedula = p_usuario_cedula
    ORDER BY predeterminado DESC, created_at DESC;
END//
DELIMITER ;


-- ----------------------------------------------------------
-- Procedimiento: sp_create_payment_method
-- Descripción: Crea un nuevo método de pago para un usuario
-- ----------------------------------------------------------

DROP PROCEDURE IF EXISTS sp_create_payment_method;

DELIMITER //
CREATE PROCEDURE sp_create_payment_method(
    IN p_usuario_cedula VARCHAR(10),
    IN p_tipo VARCHAR(50),
    IN p_datos VARCHAR(250),
    IN p_predeterminado TINYINT,
    OUT p_success TINYINT,
    OUT p_id_metodo INT,
    OUT p_error_msg VARCHAR(255)
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET p_success = 0;
        SET p_error_msg = 'Error al crear el método de pago.';
    END;
    
    SET p_success = 0;
    SET p_id_metodo = NULL;
    SET p_error_msg = NULL;
    
    -- Validar que el usuario exista
    IF NOT EXISTS (SELECT 1 FROM usuarios WHERE cedula = p_usuario_cedula) THEN
        SET p_error_msg = 'El usuario no existe en el sistema.';
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Usuario no encontrado';
    END IF;
    
    START TRANSACTION;
    
    -- El trigger trg_metodo_pago_unico_predeterminado_insert manejará el cambio de predeterminado
    INSERT INTO metodos_pago (
        usuario_cedula,
        tipo,
        datos,
        predeterminado
    ) VALUES (
        p_usuario_cedula,
        p_tipo,
        p_datos,
        IFNULL(p_predeterminado, 0)
    );
    
    SET p_id_metodo = LAST_INSERT_ID();
    
    COMMIT;
    SET p_success = 1;
    SET p_error_msg = 'Método de pago creado exitosamente';
END//
DELIMITER ;


-- ----------------------------------------------------------
-- Procedimiento: sp_delete_payment_method
-- Descripción: Elimina un método de pago
-- ----------------------------------------------------------

DROP PROCEDURE IF EXISTS sp_delete_payment_method;

DELIMITER //
CREATE PROCEDURE sp_delete_payment_method(
    IN p_id_metodo_pago INT,
    OUT p_success TINYINT,
    OUT p_error_msg VARCHAR(255)
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET p_success = 0;
        SET p_error_msg = 'Error al eliminar el método de pago.';
    END;
    
    SET p_success = 0;
    SET p_error_msg = NULL;
    
    -- Validar que el método de pago exista
    IF NOT EXISTS (SELECT 1 FROM metodos_pago WHERE id_metodo_pago = p_id_metodo_pago) THEN
        SET p_error_msg = 'El método de pago no existe.';
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Método de pago no encontrado';
    END IF;
    
    START TRANSACTION;
    
    DELETE FROM metodos_pago WHERE id_metodo_pago = p_id_metodo_pago;
    
    COMMIT;
    SET p_success = 1;
    SET p_error_msg = 'Método de pago eliminado exitosamente';
END//
DELIMITER ;


-- ==============================================================
-- PAIR 4: USER PROFILE & SHIPMENT MANAGEMENT PROCEDURES
-- ==============================================================

-- ----------------------------------------------------------
-- Procedimiento: sp_update_user_profile
-- Descripción: Actualiza el perfil de un usuario (campos opcionales)
-- ----------------------------------------------------------

DROP PROCEDURE IF EXISTS sp_update_user_profile;

DELIMITER //
CREATE PROCEDURE sp_update_user_profile(
    IN p_cedula VARCHAR(10),
    IN p_nombre VARCHAR(45),
    IN p_apellidos VARCHAR(45),
    IN p_correo VARCHAR(60),
    IN p_telefono VARCHAR(15),
    IN p_contrasena VARCHAR(255),
    OUT p_success TINYINT,
    OUT p_error_msg VARCHAR(255)
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET p_success = 0;
        SET p_error_msg = 'Error al actualizar el perfil.';
    END;
    
    SET p_success = 0;
    SET p_error_msg = NULL;
    
    -- Validar que el usuario exista
    IF NOT EXISTS (SELECT 1 FROM usuarios WHERE cedula = p_cedula) THEN
        SET p_error_msg = 'El usuario no existe en el sistema.';
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Usuario no encontrado';
    END IF;
    
    -- Validar que el correo no esté en uso por otro usuario
    IF p_correo IS NOT NULL AND EXISTS (SELECT 1 FROM usuarios WHERE correo = p_correo AND cedula != p_cedula) THEN
        SET p_error_msg = 'El correo ya está en uso por otro usuario.';
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Correo duplicado';
    END IF;
    
    START TRANSACTION;
    
    -- Actualizar solo los campos no nulos
    UPDATE usuarios
    SET nombre = IFNULL(p_nombre, nombre),
        apellidos = IFNULL(p_apellidos, apellidos),
        correo = IFNULL(p_correo, correo),
        numero_telefono = IFNULL(p_telefono, numero_telefono),
        contrasena = IFNULL(p_contrasena, contrasena)
    WHERE cedula = p_cedula;
    
    COMMIT;
    SET p_success = 1;
    SET p_error_msg = 'Perfil actualizado exitosamente';
END//
DELIMITER ;


-- ----------------------------------------------------------
-- Procedimiento: sp_get_user_shipments
-- Descripción: Obtiene todos los envíos de un usuario empresario
-- ----------------------------------------------------------

DROP PROCEDURE IF EXISTS sp_get_user_shipments;

DELIMITER //
CREATE PROCEDURE sp_get_user_shipments(
    IN p_usuario_cedula VARCHAR(10)
)
BEGIN
    SELECT 
        e.id_envio,
        e.sucursal_RUC,
        e.sucursal_id_direccion,
        e.id_direccion_entrega,
        e.transportista_cedula,
        e.id_paquete,
        e.fecha_hora_inicio,
        e.fecha_hora_final,
        e.tarifa,
        e.estado,
        -- Información de dirección de envío
        d_envio.ciudades_nombre_ciudad AS ciudad_envio,
        d_envio.calle_principal AS calle_envio,
        d_envio.calle_secundaria AS calle_secundaria_envio,
        -- Información de dirección de entrega
        d_entrega.ciudades_nombre_ciudad AS ciudad_entrega,
        d_entrega.calle_principal AS calle_entrega,
        d_entrega.calle_secundaria AS calle_secundaria_entrega,
        -- Información del paquete
        p.descripcion AS paquete_descripcion,
        p.peso AS paquete_peso
    FROM envios e
    INNER JOIN sucursales s ON e.sucursal_id_direccion = s.id_direccion AND e.sucursal_RUC = s.compania_RUC
    INNER JOIN companias c ON s.compania_RUC = c.RUC
    LEFT JOIN direcciones d_envio ON s.id_direccion = d_envio.id_direccion
    LEFT JOIN direcciones d_entrega ON e.id_direccion_entrega = d_entrega.id_direccion
    LEFT JOIN paquetes p ON e.id_paquete = p.id_paquete
    WHERE c.empresario_cedula = p_usuario_cedula
    ORDER BY e.fecha_hora_inicio DESC;
END//
DELIMITER ;


-- ----------------------------------------------------------
-- Procedimiento: sp_cancel_shipment
-- Descripción: Cancela un envío si pertenece al usuario y no está entregado
-- ----------------------------------------------------------

DROP PROCEDURE IF EXISTS sp_cancel_shipment;

DELIMITER //
CREATE PROCEDURE sp_cancel_shipment(
    IN p_id_envio INT,
    IN p_usuario_cedula VARCHAR(10),
    OUT p_success TINYINT,
    OUT p_error_msg VARCHAR(255)
)
BEGIN
    DECLARE v_estado VARCHAR(50);
    DECLARE v_empresario_cedula VARCHAR(10);
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET p_success = 0;
        SET p_error_msg = 'Error al cancelar el envío.';
    END;
    
    SET p_success = 0;
    SET p_error_msg = NULL;
    
    -- Obtener el estado del envío y verificar que pertenece al usuario
    SELECT e.estado, c.empresario_cedula
    INTO v_estado, v_empresario_cedula
    FROM envios e
    INNER JOIN sucursales s ON e.sucursal_id_direccion = s.id_direccion AND e.sucursal_RUC = s.compania_RUC
    INNER JOIN companias c ON s.compania_RUC = c.RUC
    WHERE e.id_envio = p_id_envio
    LIMIT 1;
    
    -- Validar que el envío exista
    IF v_estado IS NULL THEN
        SET p_error_msg = 'El envío no existe.';
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Envío no encontrado';
    END IF;
    
    -- Validar que el envío pertenece al usuario
    IF v_empresario_cedula != p_usuario_cedula THEN
        SET p_error_msg = 'No tiene permisos para cancelar este envío.';
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Permiso denegado';
    END IF;
    
    -- Validar que el envío no esté ya entregado o cancelado
    IF v_estado IN ('Entregado', 'Cancelado') THEN
        SET p_error_msg = 'No se puede cancelar un envío que ya está entregado o cancelado.';
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Estado inválido';
    END IF;
    
    START TRANSACTION;
    
    -- Actualizar el estado a Cancelado
    -- El trigger trg_liberar_transportista se encargará de liberar al transportista
    UPDATE envios
    SET estado = 'Cancelado'
    WHERE id_envio = p_id_envio;
    
    COMMIT;
    SET p_success = 1;
    SET p_error_msg = 'Envío cancelado exitosamente';
END//
DELIMITER ;

