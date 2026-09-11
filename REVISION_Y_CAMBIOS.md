# Matriz de cumplimiento de las historias de usuario

## Resultado de la integración

### Mejora UX y cierre de sesión

- Navegación inferior con iconos para catálogo, carrito, usuarios, historial y salida.
- Opciones visibles según el rol autenticado.
- Botón flotante para crear productos disponible únicamente al administrador.
- Confirmación antes de cerrar sesión.
- Archivo cifrado de sesión separado de versiones antiguas para evitar fallos al volver al login.
- Recuperación automática del almacenamiento cifrado si quedó dañado por una instalación anterior.
- Regreso explícito a `LoginActivity` después de limpiar sesión y carrito.
- Corrección comprobada de la asignación única del campo final `preferencias` en `GestorSesionLocal`.

| HU | Estado | Implementación principal |
|---|---|---|
| HU01 Iniciar sesión | Cumple | `LoginActivity`, `ControladorLogin`, almacenamiento cifrado y asignación de roles por ID. |
| HU02 Cerrar sesión | Cumple | Limpieza de credenciales, rol, usuario, carrito persistente y pila de actividades. |
| HU03 Consultar productos | Cumple | GET, RecyclerView, Glide, carga, error, reintento y navegación al detalle. |
| HU04 Filtrar categorías | Cumple | GET de categorías, chips, “Ver todos”, limpieza previa, carga y error. |
| HU05 Ver detalle | Cumple | GET por ID, información completa, imagen, error y controles dinámicos por rol. |
| HU06 Crear producto | Cumple | Restricción de administrador, POST, validación local, errores por campo, carga, ID y limpieza. |
| HU07 Editar producto | Cumple | Formulario precargado, validación, bloqueo por rol, botón deshabilitado y PUT por ID. |
| HU08 Eliminar producto | Cumple | Botón exclusivo, confirmación obligatoria, cancelación sin red, DELETE, error y retorno. |
| HU09 Agregar al carrito | Cumple | Producto real, cantidad positiva, usuario y fecha de sesión, POST, combinación de repetidos y reversión en error. |
| HU10 Administrar carrito | Cumple | Persistencia por usuario, imagen, cantidades, PUT/DELETE, total a dos decimales y estado vacío. |
| HU11 Consultar usuarios | Cumple | Restricción por rol, GET, modelos anidados, lista, carga, errores y reintento. |
| HU12 Auditar carritos | Cumple | Restricción por rol, GET, lista con ID/fecha/usuario y detalle de productos/cantidades en modo lectura. |

## Manejo de errores incorporado

- Validación antes de efectuar peticiones.
- Diferenciación entre respuesta HTTP inválida y fallo de conexión.
- Indicadores de carga y bloqueo de acciones repetidas.
- Mensajes comprensibles y opciones de reintento.
- Control de acceso tanto al navegar como al abrir directamente una actividad.
- Reversión del carrito local cuando falla la creación remota.
- Comprobaciones ante respuestas vacías y datos anidados opcionales.

## Nota sobre Fake Store API

Fake Store API simula las operaciones POST, PUT y DELETE y devuelve una respuesta correcta, pero no conserva permanentemente los cambios en su servidor. La aplicación mantiene el carrito localmente durante la sesión para que la interfaz sí conserve el estado solicitado.

## Verificación realizada

- Lectura y validación de todos los XML.
- Revisión de sintaxis Java y llaves.
- Comprobación de IDs de vistas y actividades del manifiesto.
- Búsqueda de prototipos activos, credenciales fijas, productos fijos y fechas fijas.
- Comprobación de endpoints GET, POST, PUT y DELETE.

La compilación completa requiere que Android Studio descargue Gradle y las dependencias de Android. Al abrir el proyecto se debe aceptar la sincronización de Gradle y después ejecutar **Build > Rebuild Project**.
