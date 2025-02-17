# Documentación de la Aplicación Meteorológica

## 1. Introducción

Este documento proporciona una documentación completa de la Aplicación Meteorológica ("Clima Galego"), un sistema basado en Java que recupera, almacena y muestra información meteorológica para ciudades de Galicia, España. La aplicación utiliza la API de MeteoGalicia para obtener datos meteorológicos en tiempo real y los presenta a través de una interfaz gráfica de usuario.

## 2. Arquitectura del Sistema

La aplicación sigue un patrón de arquitectura en capas, que consta de:

1. **Capa de Acceso a Datos**: Maneja la comunicación con la base de datos MySQL (clase `ConnectMysql`)
2. **Capa de Repositorio**: Gestiona las llamadas a la API y el procesamiento de datos (clase `TiempoRepository`)
3. **Capa de Modelo**: Representa la estructura de datos (clase `Tiempo`)
4. **Capa de Presentación**: Proporciona la interfaz de usuario (clase `PantallaPrincipal`)

### 2.1 Diagrama de Arquitectura

```
+-------------------+        +-------------------+        +----------------+
|  Interfaz Usuario |        |   Repositorio     |        |     API        |
|  (PantallaPrinc.) | -----> | (TiempoRepository)| -----> | (MeteoGalicia) |
+-------------------+        +-------------------+        +----------------+
         |                            |
         |                            |
         v                            v
+-------------------+        +-------------------+
|   Modelo Datos    |        |   Base de Datos   |
|    (Tiempo)       | <----> |  (ConnectMysql)   |
+-------------------+        +-------------------+
```

## 3. Descripción de Clases

### 3.1 TiempoRepository

Esta clase es responsable de recuperar datos meteorológicos de la API de MeteoGalicia y gestionar la transferencia de datos entre la API y la base de datos.

#### Métodos Principales:
- `getTiempo(String location)`: Recupera información meteorológica para una ubicación específica desde la API
- `getTiempoSql(String tiempoName)`: Recupera información meteorológica desde la base de datos
- `updateAllTiempoSql()`: Actualiza la base de datos con datos frescos para todas las ciudades configuradas
- `getFirstValue(JsonNode jsonNode, String nombreVariable)`: Método auxiliar para extraer valores de la respuesta JSON

#### Detalles de la API:
- URL Base: `https://servizos.meteogalicia.gal/apiv4/`
- Endpoints:
  - Buscar Lugares: `/findPlaces?location=`
  - Información Meteorológica: `/getNumericForecastInfo`

### 3.2 ConnectMysql

Esta clase maneja todas las operaciones de base de datos, proporcionando métodos para conectar, consultar y actualizar la base de datos MySQL.

#### Métodos Principales:
- `conectar()`: Establece una conexión con la base de datos
- `createDatabase()`: Crea la base de datos y las tablas requeridas si no existen
- `insertTiempo(Tiempo tiempo)`: Inserta datos meteorológicos en la base de datos
- `deleteDatosTiempo()`: Elimina todos los datos meteorológicos de la base de datos
- `getTiempo(String ciudad)`: Recupera datos meteorológicos para una ciudad específica desde la base de datos

#### Configuración de la Base de Datos:
- URL: `jdbc:mysql://localhost:3307/tiempojson`
- Usuario: `root`
- Contraseña: [Vacía]
- Estructura de la Tabla:
  ```sql
  CREATE TABLE IF NOT EXISTS tiempo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    localidad VARCHAR(255),
    estado_cielo VARCHAR(255),
    temperatura VARCHAR(255),
    viento VARCHAR(255),
    humedad VARCHAR(255),
    cobertura_nubosa VARCHAR(255)
  )
  ```

### 3.3 PantallaPrincipal

Esta clase implementa la interfaz gráfica de usuario utilizando Java Swing. Muestra información meteorológica y proporciona capacidades de interacción del usuario.

#### Componentes Principales:
- Menú desplegable (`jComboBox1`) para selección de ciudad
- Campos de texto para mostrar parámetros meteorológicos:
  - `estadoCielo`: Condición del cielo
  - `temperatura`: Temperatura
  - `viento`: Velocidad del viento
  - `humedad`: Humedad
  - `cobertura`: Cobertura de nubes
- Botón de descarga (`btnDescargar`) para exportar datos a CSV

#### Manejadores de Eventos:
- `jComboBox1ActionPerformed()`: Recupera y muestra datos meteorológicos para la ciudad seleccionada
- `btnDescargarActionPerformed()`: Exporta los datos meteorológicos recopilados a un archivo CSV

## 4. Flujo de Datos

1. El usuario selecciona una ciudad del menú desplegable
2. La aplicación intenta recuperar datos meteorológicos primero de la base de datos
3. Si no están disponibles, se conecta a la API de MeteoGalicia:
   - Primero encuentra las coordenadas de la ubicación utilizando el endpoint de Buscar Lugares
   - Luego recupera los datos meteorológicos utilizando estas coordenadas
4. Los datos son procesados y mostrados en la UI
5. El usuario puede recopilar datos para múltiples ciudades
6. Cuando se hace clic en el botón "Descargar", todos los datos recopilados se exportan a un archivo CSV

## 5. Manejo de Errores

La aplicación implementa manejo de errores en múltiples niveles:

1. Errores a nivel de API:
   - Los errores de conexión son capturados y mostrados al usuario
   - Las respuestas inválidas de la API son manejadas adecuadamente

2. Errores a nivel de base de datos:
   - Las excepciones SQL son capturadas y registradas
   - Los problemas de conexión son reportados al usuario

3. Errores a nivel de UI:
   - Validación de entrada para selección de ciudad
   - Mensajes de error mostrados a través de cuadros de diálogo

## 6. Modelo de Datos

La clase `Tiempo` (no mostrada en el código proporcionado) representa los datos meteorológicos con los siguientes atributos:

- `localidad`: Nombre de la ciudad
- `estadoCielo`: Condición del cielo
- `temperatura`: Temperatura
- `viento`: Velocidad del viento
- `humedad`: Humedad
- `coberturaNubosa`: Cobertura de nubes

## 7. Instalación y Configuración

### 7.1 Prerrequisitos
- Kit de Desarrollo de Java (JDK) 8 o superior
- Servidor MySQL
- Conector JDBC MySQL

### 7.2 Configuración de Base de Datos
1. Crear una base de datos MySQL llamada `tiempojson`
2. Configurar la conexión de base de datos en `ConnectMysql.java` si es necesario:
   ```java
   private static final String URL = "jdbc:mysql://localhost:3307/tiempojson";
   private static final String USER = "root";
   private static final String PASSWORD = "";
   ```

### 7.3 Configuración de API
1. Asegúrate de tener una clave API válida para MeteoGalicia
2. Actualiza la constante API_KEY en `TiempoRepository.java` si es necesario:
   ```java
   private static final String API_KEY = "MWXPF8V59rh5BX8G983Bj4aWCm08aalDegD98fIKni4C2lK5jJnMIhA11lbde1MF";
   ```

## 8. Guía de Uso

1. Iniciar la aplicación
2. Seleccionar una ciudad del menú desplegable
3. Ver la información meteorológica mostrada en los campos de texto
4. Repetir pasos 2-3 para recopilar datos para múltiples ciudades
5. Hacer clic en el botón "Descargar" para exportar todos los datos recopilados a un archivo CSV

## 9. Localización

La interfaz de usuario de la aplicación está en gallego. Las traducciones clave incluyen:

- "Elixe unha cidade para mostrar as principais caracteríticas meteorolóxicas": Elige una ciudad para mostrar las principales características meteorológicas
- "Estado do ceo": Estado del cielo
- "Temperatura": Temperatura
- "Vento": Viento
- "Humidade": Humedad
- "Cobertura nubosa": Cobertura nubosa
- "Descargar": Descargar

## 10. Mejoras Futuras

Posibles mejoras para versiones futuras:

1. Implementar mecanismo de caché para reducir llamadas a la API
2. Añadir componentes de visualización (gráficos, iconos meteorológicos)
3. Expandir la selección de ciudades más allá de Galicia
4. Implementar funcionalidad de pronóstico
5. Añadir preferencias de usuario para unidades (métrico/imperial)
6. Mejorar el manejo de errores y retroalimentación del usuario

## 11. Mantenimiento

### 11.1 Mantenimiento de Base de Datos
- Respaldos regulares de la base de datos meteorológica
- Limpieza periódica de datos antiguos

### 11.2 Consideraciones de API
- Monitorear el uso de API y respetar los límites de velocidad
- Mantener segura la clave API
- Verificar actualizaciones o cambios en la API

## 12. Conclusión

La Aplicación Meteorológica proporciona una forma simple pero efectiva de recuperar, mostrar y exportar información meteorológica para ciudades en Galicia. Su arquitectura modular permite un fácil mantenimiento y mejoras futuras.
