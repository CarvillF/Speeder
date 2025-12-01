-------------------------------------------------------
--- Informe de envíos mensuales
--- Muestra la cantidad total de envíos, cuántos se completaron exitosamente, los ingresos generados y el promedio de ingresos por envío
------------------------------------------------------
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

-------------------------------------------------------
--- Informe de empresarios de la plataforma
--- Lista a los empresarios registrados. Incluye un conteo de cuántas sucursales activas tiene cada compañía
------------------------------------------------------
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


-------------------------------------------------------
--- Informe de transportistas
--- Detalla los transportistas, su estado actual, el vehículo que conducen (si tienen uno asignado), su zona de cobertura y
---  cuántos envíos han realizado históricamente
------------------------------------------------------
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




-------------------------------------------------------
--- Informe de flujo de envíos por empresa
--- Muestra qué compañías están generando más movimiento (flujo) a través de la plataforma
------------------------------------------------------
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


-------------------------------------------------------
--- Informe de paquetes
--- Muestra todos los paquetes historicamente trabajados. Qué se envió, de dónde salió (Ciudad de Origen), a dónde llegó (Ciudad de Destino), dimensiones y tiempos de entrega.
------------------------------------------------------
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