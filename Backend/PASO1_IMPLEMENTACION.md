# Paso 1: Capa de Acceso a Datos (DAO) - Implementación Completa

## Resumen

Se ha implementado exitosamente el **Paso 1** del plan de implementación para el backend del sistema Speeder. Este paso establece la base de datos con soporte para múltiples tipos de usuarios y un sistema de acceso a datos robusto.

## Estructura Creada

### 📁 Archivos de Credenciales (`DbServer/credentials/`)

Se crearon archivos de propiedades para tres tipos de usuarios:

1. **`emprendedor.properties`**
   - Usuario: `emprendedor_user`
   - Permisos: Lectura/escritura en empresarios, compañías, sucursales, envíos, paquetes

2. **`transportista.properties`**
   - Usuario: `transportista_user`
   - Permisos: Lectura/escritura en transportistas, vehículos, envíos, direcciones

3. **`administrador.properties`**
   - Usuario: `admin_user`
   - Permisos: Acceso completo a todas las tablas

4. **`README.md`**
   - Documentación con comandos SQL para crear usuarios
   - Instrucciones de seguridad

### 📦 Backend Java (`Backend/src/`)

#### `database/UserType.java` (NUEVO)
Enum que define los tres tipos de usuarios del sistema:
- `EMPRENDEDOR`
- `TRANSPORTISTA`
- `ADMINISTRADOR`

Cada valor del enum está mapeado a su archivo de credenciales correspondiente.

#### `database/DatabaseConfig.java` (MEJORADO)
- Soporte para múltiples perfiles de usuario
- Cache de propiedades para optimizar el rendimiento
- Métodos con parámetro `UserType` para cargar credenciales específicas
- Mantiene compatibilidad hacia atrás con código existente (métodos deprecated)
- Carga archivos desde `DbServer/credentials/`

#### `database/DBConnection.java` (MEJORADO)
- Nuevo método `connect(UserType)` para conexiones específicas por tipo de usuario
- Manejo mejorado de errores con mensajes descriptivos
- Logging para debug de conexiones
- Mantiene método `connect()` deprecated para compatibilidad

#### `dao/UserDAO.java` (NUEVO)
Clase DAO con métodos de autenticación:
- `login(correo, contrasena)`: Valida credenciales básicas
- `loginAndGetUserType(correo, contrasena)`: Valida y retorna el tipo de usuario
- `correoExiste(correo)`: Verifica existencia de email
- `cedulaExiste(cedula)`: Verifica existencia de cédula

#### `protocol/Request.java` (NUEVO)
Clase para peticiones JSON del cliente:
```java
{
  "action": "LOGIN",
  "payload": { ... }
}
```

#### `protocol/Response.java` (NUEVO)
Clase para respuestas JSON del servidor:
```java
{
  "status": "SUCCESS",
  "message": "Login correcto",
  "data": { ... }
}
```

## Características Implementadas

### ✅ Gestión de Credenciales
- Archivos de credenciales separados por tipo de usuario
- Permisos diferenciados según el rol
- Documentación SQL para crear usuarios en MySQL

### ✅ Conexión Multi-Usuario
- Soporte para tres tipos de usuarios diferentes
- Cache de credenciales para mejorar rendimiento
- Fallback a configuración por defecto si falla la carga

### ✅ Capa DAO Robusta
- Métodos de autenticación seguros con PreparedStatement
- Detección automática del tipo de usuario mediante JOINs
- Manejo de errores con logging descriptivo
- Validaciones de existencia para registro

### ✅ Protocolo de Comunicación
- Clases POJO para Request y Response
- Estructura lista para serialización JSON con Gson
- Campos flexibles con `Object payload/data`

## Uso del Código

### Ejemplo: Autenticación con Detección de Tipo

```java
import dao.UserDAO;
import database.UserType;

// Login y obtener tipo de usuario
UserType userType = UserDAO.loginAndGetUserType("usuario@ejemplo.com", "password123");

if (userType != null) {
    System.out.println("Login exitoso como " + userType);
    // Usar conexión específica para este tipo de usuario
} else {
    System.out.println("Credenciales inválidas");
}
```

### Ejemplo: Conexión con Tipo de Usuario Específico

```java
import database.DBConnection;
import database.UserType;
import java.sql.Connection;

// Conectar como transportista
try (Connection conn = DBConnection.connect(UserType.TRANSPORTISTA)) {
    // Realizar operaciones de transportista
    // Este usuario solo tiene permisos específicos
} catch (SQLException e) {
    e.printStackTrace();
}
```

### Ejemplo: Protocolo de Comunicación

```java
import protocol.Request;
import protocol.Response;
import com.google.gson.Gson;

Gson gson = new Gson();

// Crear petición
Request request = new Request("LOGIN", 
    Map.of("username", "user@mail.com", "password", "pass123"));

// Serializar a JSON
String jsonRequest = gson.toJson(request);

// Deserializar respuesta
String jsonResponse = "{\"status\":\"SUCCESS\",\"message\":\"Login correcto\"}";
Response response = gson.fromJson(jsonResponse, Response.class);
```

## Próximos Pasos

Con el Paso 1 completado, el backend está listo para:

- **Paso 2**: Ampliar el protocolo JSON para más acciones (CREATE_ORDER, UPDATE_STATUS, etc.)
- **Paso 3**: Implementar `SpeederServer.java` con ServerSocket para aceptar conexiones
- **Paso 4**: Implementar `ClientHandler.java` para manejar clientes en hilos separados

## Notas Importantes

### ⚠️ Configuración Requerida en MySQL

Antes de usar el sistema, debes ejecutar los comandos SQL del archivo `DbServer/credentials/README.md` para crear los usuarios en MySQL:

```bash
mysql -u root -p < create_users.sql
```

O copiar los comandos manualmente en MySQL Workbench.

### 🔒 Seguridad

- Las contraseñas en los archivos `.properties` son para desarrollo/pruebas
- En producción, usar variables de entorno o gestores de secretos
- Nunca subir archivos de credenciales a repositorios públicos
- Implementar hashing de contraseñas (BCrypt/Argon2) en lugar de texto plano

### 📝 Compatibilidad

El código mantiene compatibilidad hacia atrás:
- Los métodos antiguos de `DBConnection.connect()` y `DatabaseConfig` siguen funcionando
- Están marcados como `@Deprecated` para indicar que se debe migrar al nuevo sistema
- El código existente en `consultas/Buscar.java` y `consultas/Agregar.java` puede seguir usando los métodos antiguos
