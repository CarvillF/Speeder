# Revisión del Plan de Implementación - Paso 4

## Resumen
Se ha contrastado el **Paso 4: El Servidor (ServerSocket + Multi-hilo)** del plan de implementación con los archivos existentes en el repositorio.

**Veredicto General:** ❌ **Insuficiente**
La implementación actual del servidor (`SpeederServer`, `ClientHandler`) es funcional en su estructura básica, pero **no cumple** con todos los requisitos del plan ni se integra correctamente con las clases de consulta proporcionadas (`consultas/Agregar.java`, `consultas/Buscar.java`).

## Detalles del Contraste

### 1. Puerto del Servidor (Ignorar)
*   **Requisito (Plan):** Puerto `8080`.
*   **Implementación (`SpeederServer.java`):** Puerto `5000`.
*   **Impacto:** Menor, pero debe alinearse con el cliente JavaFX.

### 2. Integración DAO (Login)
*   **Requisito (Plan):** `ClientHandler` debe llamar a `UserDAO.login()` que retorna `boolean`.
*   **Implementación (`ClientHandler.java`):** Llama a `dao.UserDAO.loginAndGetUserType()` que retorna un `UserType`.
*   **Archivos Proporcionados (`consultas/Buscar.java`):** Contiene `login()` que retorna `boolean`.
*   **Discrepancia:** `ClientHandler` está usando una clase `dao.UserDAO` (que existe en el proyecto pero no fue la indicada para revisar) en lugar de `consultas.Buscar`. Además, la lógica de respuesta espera un `UserType` para devolverlo en el JSON, lo cual `consultas.Buscar.login` no provee.

### 3. Funcionalidad Crear Orden
*   **Requisito (Plan):** Manejar la acción `CREATE_ORDER`.
*   **Implementación (`ClientHandler.java`):** **No implementado**. El `switch` solo tiene el caso `LOGIN`.
*   **Archivos Proporcionados (`consultas/Agregar.java`):** Contiene métodos para agregar usuarios (`agregarUsuario`, `agregarTransportista`), pero **no tiene métodos para crear órdenes**.
*   **Impacto:** Crítico. Falta la lógica de negocio principal mencionada en el ejemplo.

### 4. Protocolo JSON
*   **Requisito (Plan):** Estructura `Request` y `Response`.
*   **Implementación:** Correcta. Se usan las clases `protocol.Request` y `protocol.Response` con `Gson`.

## Pruebas Generadas

Se ha generado un archivo de prueba en:
`Backend/src/test/Step4Test.java`

Este test:
1.  Levanta el servidor en un hilo independiente.
2.  Envía una petición de `LOGIN` (Verifica conexión y autenticación).
3.  Envía una petición de `CREATE_ORDER` (Verifica la funcionalidad faltante).

**Resultado de la ejecución preliminar:**
*   **LOGIN:** Se ejecuta (resultado depende de la DB).
*   **CREATE_ORDER:** Falla con mensaje `Acción no reconocida`, confirmando que falta la implementación.

## Recomendaciones
1.  **Actualizar `ClientHandler`** para manejar el caso `CREATE_ORDER`.
2.  **Implementar `agregarPedido`** en una clase DAO (ej: `consultas/Agregar.java` o nueva `consultas/Pedidos.java`).
3.  **Unificar DAO:** Decidir si usar `dao.UserDAO` o `consultas.Buscar`. Si se usa `consultas.Buscar`, modificar `ClientHandler` para adaptarse a su firma (`boolean`) o mejorar `Buscar` para devolver más datos del usuario.
