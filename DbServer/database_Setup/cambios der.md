Datos mejor definirlo de otra forma (json)
Cambio nombre de la tabla cliente a usuario
Agregar parte de administrador, con atributos propios
Cambiado envios, envios ahora tiene por emisor de paquete una sucursal y por destinatario una dirección

Definir para cada tablas importantes defaults
- tMetodos_pago: predeterminado ahora es DEFAULT 0 y created_at es DEFAULT (CURRENT_DATE).
- tSucursales: activa ahora es DEFAULT 1 (Activa por defecto).
- tTransportista: disponibilidad ahora es DEFAULT 'Disponible'.
- tEnvios: estado inicia en DEFAULT 'Pendiente' y tarifa en '0.00'.
- Administrador: estado_cuenta se asigna por defecto al rol de menor privilegio (DEFAULT 'Soporte') por seguridad.
- tDirecciones: Coordenadas numéricas inicializadas en 0.00.

Se corrigieron problemas de tipos de datos en las tablas
- Cambiado Cedula de Int a varchar(10)
    tDirecciones:
        coordenada_y: Se cambió de VARCHAR(45) a DECIMAL(10,2).
        número_edificación: Se cambió de VARCHAR(45) a INT.
    tCompañias:
        RUC: Se cambió de INT a VARCHAR(10).
    tSucursales:
    compañias_RUC: Se cambió de INT a VARCHAR(10) para coincidir con la clave primaria de tCompañias.
    tPaquete:
        dimension_x, dimension_y, dimension_z: Se cambiaron de VARCHAR(45) a DECIMAL(10,2).
    tEnvios:
    tSucursales_compañias_RUC: Se cambió de INT a VARCHAR(10) para mantener la integridad de la clave foránea.
        fecha_hora_inicio y fecha_hora_final: Se cambiaron de VARCHAR(45) a DATETIME.
        tarifa: Se cambió de VARCHAR(45) a DECIMAL(10,2).
        estado: Se cambió de VARCHAR(45) a ENUM('En recogida', 'En camino', 'Cancelado').
    tModelos:
        dimension_x, dimension_y, dimension_z: Se cambiaron de VARCHAR(45) a DECIMAL(10,2).
        máximo_peso: Se cambió de VARCHAR(45) a DECIMAL(10,2).
    tVehículos:
        placa: Se cambió de VARCHAR(45) a VARCHAR(8).

    1. Tabla tPaquete
        Atributo: peso
        Problema: Está definido como VARCHAR(45).
        Incongruencia: El peso es una magnitud física numérica. Al guardarlo como texto, se impide realizar cálculos (sumas de peso total, validaciones de rangos, etc.). Debería ser DECIMAL o FLOAT.
    2. Tabla tModelos
        Atributo: nombre_modelo (PK)
        Problema: Está definido como INT.
        Incongruencia: El atributo se llama "nombre", lo que implica una cadena de texto (ej. "Ford Transit", "Boeing 747"), pero el tipo de dato es un entero. Si su función es ser un identificador numérico, debería llamarse id_modelo. Si realmente es el nombre, debe ser VARCHAR.
    3. Tabla tVehículos
        Atributo: nombre_modelo (FK)
        Problema: Está definido como INT.
        Incongruencia: Hereda el mismo problema semántico de la tabla tModelos.
    4. Tabla tModificadoresEntrega
        Atributo: multiplicador_tarifa
        Problema: Está definido como VARCHAR(45) con default '1.0'.
        Incongruencia: Un multiplicador es un factor matemático. Guardarlo como texto obliga a hacer conversiones (casting) en cada consulta que requiera calcular precios. Debería ser DECIMAL.
        Atributo: reducción tiempo (Nota: Este nombre tiene un espacio y causará error de sintaxis si no lleva comillas invertidas `).
        Problema: Está definido como VARCHAR(45) con default '0'.
        Incongruencia: La reducción de tiempo es una cantidad (minutos u horas). Debería ser INT o DECIMAL.
    5. Tabla Modificadores_aplicados
        Atributo: impacto_en_tarifa
        Problema: Está definido como VARCHAR(45) con default '0.00'.
        Incongruencia: Esto representa dinero (moneda). Usar VARCHAR para valores monetarios es una mala práctica que lleva a errores de redondeo y ordenamiento. Debería ser DECIMAL.
        Atributo: valor_modificador_aplicado
        Problema: Está definido como VARCHAR(45).
        Incongruencia: Dependiendo de la lógica, si este valor se usa para operaciones matemáticas, debería ser numérico.

Se corrigieron problemas de nombres
- Normalización de nombres: Se eliminaron todas las tildes (dirección -> direccion, vehículo -> vehiculo, compañía -> compania) y la "ñ".
- Espacios en blanco: tipo de distancia ahora es tipo_de_distancia.
- Seguridad: El campo contraseña pasó a llamarse contrasena y su longitud aumentó a VARCHAR(255).
- Limpieza: Se eliminó la columna basura Modificadores_aplicadoscol.
- Tablas renombradas: Las tablas con caracteres especiales en su nombre también fueron corregidas (ej. tVehículos -> tVehiculos).