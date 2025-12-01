# Credenciales de Base de Datos - Speeder

Este directorio contiene las credenciales para diferentes tipos de usuarios de la base de datos MySQL.

## Archivos de Credenciales

### 1. `emprendedor.properties`
- **Usuario**: `emprendedor_user`
- **Uso**: Empresarios que gestionan compañías y sucursales
- **Permisos**: Lectura/escritura en tablas de negocios (empresarios, companias, sucursales, envios, paquetes)

### 2. `transportista.properties`
- **Usuario**: `transportista_user`
- **Uso**: Transportistas que gestionan envíos y vehículos
- **Permisos**: Lectura/escritura en tablas de transporte (transportistas, vehiculos, envios, direcciones)

### 3. `administrador.properties`
- **Usuario**: `admin_user`
- **Uso**: Administradores del sistema
- **Permisos**: Acceso completo a todas las tablas

## Configuración en MySQL

Para crear estos usuarios en MySQL, ejecuta los siguientes comandos:

```sql
-- Usuario Emprendedor
CREATE USER 'emprendedor_user'@'localhost' IDENTIFIED BY 'emprendedor_pass_2024';
GRANT SELECT, INSERT, UPDATE ON mydb.usuarios TO 'emprendedor_user'@'localhost';
GRANT SELECT, INSERT, UPDATE ON mydb.empresarios TO 'emprendedor_user'@'localhost';
GRANT SELECT, INSERT, UPDATE ON mydb.companias TO 'emprendedor_user'@'localhost';
GRANT SELECT, INSERT, UPDATE ON mydb.sucursales TO 'emprendedor_user'@'localhost';
GRANT SELECT, INSERT, UPDATE ON mydb.envios TO 'emprendedor_user'@'localhost';
GRANT SELECT, INSERT, UPDATE ON mydb.paquetes TO 'emprendedor_user'@'localhost';
GRANT SELECT ON mydb.direcciones TO 'emprendedor_user'@'localhost';
GRANT SELECT ON mydb.transportistas TO 'emprendedor_user'@'localhost';
FLUSH PRIVILEGES;

-- Usuario Transportista
CREATE USER 'transportista_user'@'localhost' IDENTIFIED BY 'transportista_pass_2024';
GRANT SELECT, INSERT, UPDATE ON mydb.usuarios TO 'transportista_user'@'localhost';
GRANT SELECT, INSERT, UPDATE ON mydb.transportistas TO 'transportista_user'@'localhost';
GRANT SELECT, INSERT, UPDATE ON mydb.vehiculos TO 'transportista_user'@'localhost';
GRANT SELECT, INSERT, UPDATE ON mydb.envios TO 'transportista_user'@'localhost';
GRANT SELECT ON mydb.direcciones TO 'transportista_user'@'localhost';
GRANT SELECT ON mydb.paquetes TO 'transportista_user'@'localhost';
GRANT SELECT ON mydb.sucursales TO 'transportista_user'@'localhost';
FLUSH PRIVILEGES;

-- Usuario Administrador
CREATE USER 'admin_user'@'localhost' IDENTIFIED BY 'admin_pass_2024';
GRANT ALL PRIVILEGES ON mydb.* TO 'admin_user'@'localhost';
FLUSH PRIVILEGES;
```

## Seguridad

⚠️ **IMPORTANTE**: 
- Estas credenciales son para desarrollo/pruebas
- En producción, usa contraseñas fuertes y únicas
- No subas estos archivos a repositorios públicos
- Considera usar variables de entorno o gestores de secretos en producción
