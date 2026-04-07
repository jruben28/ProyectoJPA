# 📚 Documentación Completa — ProyectoJPA

> **Propósito:** Este documento sirve como guía de referencia rápida ("desempolvado") para todos los integrantes del equipo. Aquí encontrarás la arquitectura, las capas, las entidades, los flujos y el estado actual de cada módulo.

---

## 📋 Tabla de Contenidos

1. [Visión General](#-visión-general)
2. [Arquitectura](#️-arquitectura)
3. [Estructura del Proyecto](#-estructura-del-proyecto)
4. [Tecnologías](#️-tecnologías)
5. [Capa de Dominio — Entidades](#-capa-de-dominio--entidades)
6. [Capa de DTOs](#-capa-de-dtos)
7. [Capa de Persistencia — DAOs](#-capa-de-persistencia--daos)
8. [Capa de Negocio — BOs](#️-capa-de-negocio--bos)
9. [Adaptadores](#-adaptadores)
10. [Utilidades](#-utilidades)
11. [Manejo de Excepciones](#-manejo-de-excepciones)
12. [Capa de Presentación — UI](#️-capa-de-presentación--ui)
13. [Configuración JPA](#-configuración-jpa)
14. [Tests Unitarios](#-tests-unitarios)
15. [Flujos de Datos Completos](#-flujos-de-datos-completos)
16. [Contribuciones por Integrante](#-contribuciones-por-integrante)
17. [Módulo Combos — Guía para Kevin](#-módulo-combos--guía-para-kevin)
18. [Próximos Pasos por Módulo](#-próximos-pasos-por-módulo)
19. [Puntos Clave a Recordar](#-puntos-clave-a-recordar)

---

## 🎯 Visión General

**ProyectoJPA** es un sistema de gestión de clientes y comandas para un negocio (restaurante/establecimiento). Permite:

- Registrar y administrar **clientes frecuentes** (con sistema de puntos) y **clientes generales** (anónimos).
- Crear y gestionar **comandas** (órdenes de compra).
- Calcular **puntos de lealtad** automáticamente según el consumo (1 punto por cada $20 gastados).
- Buscar clientes por nombre, teléfono o correo con filtros dinámicos.
- Editar la información de clientes existentes.

---

## 🏗️ Arquitectura

El proyecto usa una **arquitectura en capas** (N-Tier), donde cada capa solo conoce a la inmediata inferior. Esto garantiza bajo acoplamiento y facilita el mantenimiento.

```
┌──────────────────────────────────┐
│   PRESENTACIÓN (JavaFX)          │  ← Interfaz gráfica, pantallas, controladores
├──────────────────────────────────┤
│   NEGOCIO (Business Objects)     │  ← Lógica de negocio, validaciones, cálculos
├──────────────────────────────────┤
│   PERSISTENCIA (DAOs)            │  ← Acceso a la base de datos, JPQL, Criteria API
├──────────────────────────────────┤
│   DOMINIO (Entidades JPA)        │  ← Clases mapeadas a tablas de MySQL
├──────────────────────────────────┤
│   DTOs + ADAPTADORES             │  ← Transferencia de datos entre capas
└──────────────────────────────────┘
```

### Regla de Oro
> La UI **nunca** habla directamente con el DAO. Todo pasa por el BO.
> Las entidades **nunca** salen de la capa de negocio hacia la UI. Se usan DTOs.

---

## 📁 Estructura del Proyecto

Es un **proyecto Maven multi-módulo**. Cada módulo es un JAR independiente con sus dependencias declaradas.

```
ProyectoJPA/                         ← Raíz del proyecto (pom.xml padre)
│
├── dominio/                         ← Entidades JPA (@Entity)
│   └── src/main/java/
│       ├── Entidades/
│       │   ├── Cliente.java
│       │   ├── ClienteFrecuente.java
│       │   ├── ClienteGeneral.java
│       │   └── Comanda.java
│       └── enums/
│           └── EstadoComanda.java
│
├── dtos/                            ← Data Transfer Objects
│   └── src/main/java/com/dtos/
│       ├── ClienteDTO.java
│       ├── ClienteFrecuenteDTO.java
│       └── ComandaDTO.java
│
├── persistencia/                    ← DAOs + Conexión a BD
│   └── src/main/java/
│       ├── Conexion/
│       │   └── ConexionBD.java
│       ├── DAOs/
│       │   ├── IClienteDAO.java
│       │   ├── ClienteDAO.java
│       │   ├── IComandaDAO.java
│       │   └── ComandaDAO.java
│       └── excepciones/
│           └── PersistenciaException.java
│   └── src/main/resources/META-INF/
│       └── persistence.xml
│
├── negocio/                         ← BOs + Adaptadores + Utilidades
│   └── src/main/java/
│       ├── BOs/
│       │   ├── IClienteBO.java
│       │   ├── ClienteBO.java
│       │   ├── IComandaBO.java
│       │   └── ComandaBO.java
│       ├── adaptadores/
│       │   ├── ClienteFrecuenteAdapter.java
│       │   └── ComandaAdapter.java
│       ├── utilidades/
│       │   └── Encriptador.java
│       ├── excepciones/
│       │   └── NegocioException.java
│       └── pruebas/
│           └── InsertDatos.java     ← Script para poblar la BD con datos de prueba
│
└── presentacion/                    ← UI con JavaFX
    └── src/main/java/com/presentacion/
        ├── Presentacion.java        ← Main, punto de entrada
        ├── ControllerClienteFrecuente.java
        ├── BuscadorClientesFrm.java
        ├── RegistroClienteFrm.java
        └── SistemaPuntosFrm.java
```

---

## 🛠️ Tecnologías

| Tecnología | Versión / Detalle |
|---|---|
| Java | 22 (maven.compiler.release=22) |
| JPA (API) | Jakarta Persistence 2.2 |
| Proveedor JPA | **EclipseLink** (org.eclipse.persistence) |
| Base de datos | MySQL 8 (`comandas` schema) |
| Driver JDBC | `com.mysql.cj.jdbc.Driver` |
| UI | **JavaFX** |
| Build | Maven multi-módulo |
| Tests | JUnit 5 (Jupiter) |
| Encriptación | Base64 (java.util.Base64) |

---

## 🗃️ Capa de Dominio — Entidades

Las entidades son clases Java anotadas con `@Entity` que JPA mapea directamente a tablas en MySQL.

### Jerarquía de Herencia

```
           Cliente  (tabla: clientes)
          @Inheritance(JOINED)
               │
       ┌───────┴────────┐
       │                │
ClienteFrecuente   ClienteGeneral
(tabla: clientes_  (tabla: clientes_
        frecuentes)       generales)
```

Se usa la estrategia `JOINED`: existe una tabla `clientes` con las columnas comunes (id, nombre), y tablas separadas para cada subtipo con sus columnas específicas.

---

### `Cliente.java` — Clase base

```java
@Entity
@Table(name = "clientes")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_cliente", discriminatorType = DiscriminatorType.STRING)
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    // Constructores, getters y setters
}
```

| Campo | Tipo | Columna BD | Notas |
|---|---|---|---|
| `id` | Long | `id` | PK, auto-generado |
| `nombre` | String | `nombre` | NOT NULL |

---

### `ClienteFrecuente.java` — Cliente registrado con puntos

```java
@Entity
@Table(name = "clientes_frecuentes")
public class ClienteFrecuente extends Cliente implements Serializable {

    @Column(nullable = false)
    private String telefono;       // Almacenado ENCRIPTADO en Base64

    @Column(nullable = true)
    private String correo;         // Opcional

    @Temporal(TemporalType.DATE)
    private Date fechaRegistro;    // Auto-asignada en el constructor
}
```

| Campo | Tipo | Notas |
|---|---|---|
| `telefono` | String | **Encriptado con Base64** en BD, 10 dígitos original |
| `correo` | String | Opcional (nullable) |
| `fechaRegistro` | Date | Se asigna `new Date()` al crear |

> ⚠️ **Importante:** El teléfono se encripta con `Encriptador.encriptar()` en el BO antes de llegar al DAO. La búsqueda usa `FROM_BASE64()` de MySQL (Criteria API) para poder buscar aunque esté cifrado.

---

### `ClienteGeneral.java` — Cliente anónimo

```java
@Entity
@Table(name = "clientes_generales")
public class ClienteGeneral extends Cliente implements Serializable {
    // Sin campos adicionales
    // Solo hereda id y nombre
}
```

Existe **un único registro "Cliente General"** en la base de datos, compartido para todas las comandas de clientes no registrados. El BO se encarga de crearlo si no existe (`obtenerOCrearClienteGeneral()`).

---

### `Comanda.java` — Orden de compra

```java
@Entity
@Table(name = "comandas")
public class Comanda implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double total;

    @Column(nullable = false)
    private String estado;           // "ABIERTA", "ENTREGADA", "CANCELADA"

    @Column(name = "id_cliente", nullable = true)
    private Long idCliente;          // FK al cliente (puede ser null = ClienteGeneral)
}
```

> 📝 **Nota de diseño:** El campo `estado` es un `String` simple (no usa el enum `EstadoComanda`). Esto fue un cambio de última hora para simplificar la base de datos.

| Estado | Descripción |
|---|---|
| `ABIERTA` | Comanda activa, el cliente todavía está en el establecimiento |
| `ENTREGADA` | Comanda completada, cuenta para los puntos del cliente |
| `CANCELADA` | Comanda cancelada, no cuenta para puntos |

---

### `EstadoComanda.java` — Enum (referencia)

```java
public enum EstadoComanda {
    ABIERTA, ENTREGADA, CANCELADA
}
```

> Este enum existe como referencia pero actualmente no se usa en la entidad `Comanda`. Se puede usar para validar valores en el BO si se desea.

---

## 📦 Capa de DTOs

Los DTOs (Data Transfer Objects) son objetos "tontos" que solo transportan datos entre capas. No tienen lógica, solo campos, constructores, getters y setters.

### `ClienteDTO.java` — Base

```java
public class ClienteDTO implements Serializable {
    private Long id;
    private String nombre;
    private String tipoCliente;  // "FRECUENTE" o "GENERAL"
}
```

### `ClienteFrecuenteDTO.java` — Extiende ClienteDTO

```java
public class ClienteFrecuenteDTO extends ClienteDTO {
    private String telefono;           // Desencriptado (texto plano para la UI)
    private String correo;
    private Date fechaRegistro;
    private Integer puntosAcumulados;  // Calculado por el BO (total/20)
    private Double totalGastado;       // Suma de todas las comandas ENTREGADAS
    private Integer numVisitas;        // Número de comandas ENTREGADAS
}
```

> 🎯 `puntosAcumulados`, `totalGastado` y `numVisitas` **no existen en la BD**. Son calculados dinámicamente por el `ClienteBO` cada vez que se hace una búsqueda.

### `ComandaDTO.java`

```java
public class ComandaDTO {
    private Long id;
    private String estado;
    private Double total;
    private Long idCliente;
}
```

---

## 🔌 Capa de Persistencia — DAOs

### `ConexionBD.java` — Singleton de conexión

```java
public class ConexionBD {
    private static final EntityManagerFactory entityManagerFactory =
        Persistence.createEntityManagerFactory("ConexionPU");

    private ConexionBD() {}  // Constructor privado = no se puede instanciar

    public static EntityManager crearConexion() {
        return entityManagerFactory.createEntityManager();
    }
}
```

- El `EntityManagerFactory` se crea **una sola vez** al cargar la clase (es costoso).
- Cada operación crea su propio `EntityManager` (es liviano y debe cerrarse con `em.close()`).

---

### `IClienteDAO.java` — Interfaz del DAO de clientes

```java
public interface IClienteDAO {
    Cliente agregar(Cliente cliente);
    Cliente actualizar(Cliente cliente);
    ClienteFrecuente agregarClienteFrecuente(ClienteFrecuente cf);
    ClienteFrecuente actualizarClienteFrecuente(ClienteFrecuente cf);
    Cliente buscarPorId(Long id);
    List<ClienteFrecuente> buscarFrecuentesPorFiltro(String filtro);
    ClienteGeneral obtenerClienteGeneral();
    List<Comanda> buscarComandasPorCliente(Long idCliente);
    List<ClienteFrecuente> buscarFrecuentesPorCampo(String filtro, String campo);
}
```

### `ClienteDAO.java` — Implementación

| Método | Qué hace |
|---|---|
| `agregar(cliente)` | `em.persist()` → INSERT |
| `actualizar(cliente)` | `em.merge()` → UPDATE |
| `agregarClienteFrecuente(cf)` | `em.persist()` específico para ClienteFrecuente |
| `actualizarClienteFrecuente(cf)` | `em.merge()` específico para ClienteFrecuente |
| `buscarPorId(id)` | `em.find(Cliente.class, id)` → SELECT por PK |
| `buscarFrecuentesPorFiltro(filtro)` | JPQL con `LIKE` en nombre, teléfono y correo |
| `obtenerClienteGeneral()` | JPQL: `WHERE c.nombre = 'Cliente General'`, retorna `null` si no existe |
| `buscarComandasPorCliente(idCliente)` | JPQL: comandas con `estado = 'ENTREGADA'` de un cliente |
| `buscarFrecuentesPorCampo(filtro, campo)` | **Criteria API** con `FROM_BASE64()` para teléfonos cifrados |

#### Detalle: búsqueda con Criteria API (teléfonos encriptados)

```java
// Si el campo de búsqueda incluye "telefono":
Expression<String> telDecodificado =
    cb.function("FROM_BASE64", String.class, root.<String>get("telefono"));
predicados.add(cb.like(telDecodificado, patron));
```

La BD decodifica el Base64 en tiempo de consulta usando la función nativa `FROM_BASE64()` de MySQL. Esto evita traer todos los registros a Java para desencriptarlos.

---

### `IComandaDAO.java` / `ComandaDAO.java`

```java
public interface IComandaDAO {
    Comanda agregarComanda(Comanda comanda) throws PersistenciaException;
}
```

El DAO de Comanda actualmente solo tiene el método de agregar. Los métodos de búsqueda y actualización son parte de los módulos en desarrollo.

---

## ⚙️ Capa de Negocio — BOs

Los BOs contienen toda la lógica de negocio: validaciones, cálculos, y orquestación entre DAOs.

### `IClienteBO.java` — Interfaz

```java
public interface IClienteBO {
    Integer calcularPuntos(Long idCliente) throws NegocioException;
    Double calcularTotalGastado(Long idCliente) throws NegocioException;
    void agregarClienteFrecuente(ClienteFrecuenteDTO dto) throws NegocioException;
    void actualizarClienteFrecuente(ClienteFrecuenteDTO dto) throws NegocioException;
    List<Comanda> buscarComandasPorCliente(Long idCliente) throws NegocioException;
    List<ClienteFrecuenteDTO> buscarFrecuentesPorFiltro(String filtro, String campo) throws NegocioException;
    String obtenerOCrearClienteGeneral() throws NegocioException;
}
```

### `ClienteBO.java` — Métodos principales

#### `agregarClienteFrecuente(dto)`
1. Llama a `validarClienteFrecuenteDTO(dto)` (nombre, correo regex, teléfono 10 dígitos, fecha no futura).
2. Encripta el teléfono: `Encriptador.encriptar(dto.getTelefono())`.
3. Convierte: `ClienteFrecuente entidad = ClienteFrecuenteAdapter.dtoAEntidad(dto)`.
4. Persiste: `clienteDAO.agregarClienteFrecuente(entidad)`.

#### `actualizarClienteFrecuente(dto)`
1. Valida los datos (mismas reglas).
2. Verifica que `dto.getId() != null` (no se puede actualizar sin ID).
3. Encripta el teléfono.
4. Convierte y persiste con `em.merge()`.

#### `buscarFrecuentesPorFiltro(filtro, campoBusqueda)`
1. Valida que el filtro no esté vacío.
2. Llama a `clienteDAO.buscarFrecuentesPorCampo(filtro, campoBusqueda)`.
3. Para cada cliente encontrado:
   - Calcula `totalGastado` sumando sus comandas entregadas.
   - Calcula `puntos = (int)(totalGastado / 20)`.
   - Calcula `numVisitas` = cantidad de comandas entregadas.
4. Crea el DTO con `ClienteFrecuenteAdapter.entidadADTO(c, puntos, totalGastado, numVisitas)`.
5. Desencripta el teléfono antes de retornar al DTO.

#### `calcularPuntos(idCliente)` y `calcularTotalGastado(idCliente)`
```
Regla de puntos: 1 punto por cada $20 gastados en comandas ENTREGADAS
puntos = (int)(totalGastado / 20)
```

#### `obtenerOCrearClienteGeneral()`
Busca el registro "Cliente General". Si no existe, lo crea automáticamente. Esto garantiza que siempre haya un cliente anónimo disponible.

---

### `ComandaBO.java`

```java
public class ComandaBO implements IComandaBO {
    public void agregarComanda(ComandaDTO comandaDTO) throws NegocioException {
        Comanda comanda = ComandaAdapter.dtoAEntidad(comandaDTO);
        comandaDAO.agregarComanda(comanda);
    }
}
```

> 📝 Las validaciones de comanda están pendientes de implementar (módulo Rubén).

---

## 🔄 Adaptadores

Los adaptadores convierten entre DTO y Entidad. Se encuentran en `negocio/adaptadores/`.

### `ClienteFrecuenteAdapter.java`

```java
// DTO → Entidad (para guardar/actualizar en BD)
public static ClienteFrecuente dtoAEntidad(ClienteFrecuenteDTO dto) {
    ClienteFrecuente cf = new ClienteFrecuente();
    cf.setId(dto.getId());
    cf.setNombre(dto.getNombre());
    cf.setCorreo(dto.getCorreo());
    cf.setFechaRegistro(dto.getFechaRegistro());
    cf.setTelefono(dto.getTelefono());  // Ya viene encriptado desde el BO
    return cf;
}

// Entidad → DTO (para mostrar en la UI)
// puntos, totalGastado y numVisitas son calculados por el BO
public static ClienteFrecuenteDTO entidadADTO(ClienteFrecuente e, Integer puntos,
                                               Double totalGastado, Integer numVisitas) {
    ClienteFrecuenteDTO dto = new ClienteFrecuenteDTO();
    dto.setId(e.getId());
    dto.setNombre(e.getNombre());
    dto.setTipoCliente("FRECUENTE");
    dto.setCorreo(e.getCorreo());
    dto.setFechaRegistro(e.getFechaRegistro());
    dto.setPuntosAcumulados(puntos);
    dto.setTotalGastado(totalGastado);
    dto.setNumVisitas(numVisitas);
    dto.setTelefono(Encriptador.desencriptar(e.getTelefono()));  // Desencripta aquí
    return dto;
}
```

### `ComandaAdapter.java`

```java
// DTO → Entidad
public static Comanda dtoAEntidad(ComandaDTO dto) {
    Comanda comanda = new Comanda();
    if (dto.getId() != null) comanda.setId(dto.getId());
    comanda.setIdCliente(dto.getIdCliente());
    comanda.setEstado(dto.getEstado());
    comanda.setTotal(dto.getTotal());
    return comanda;
}
```

> 📝 El adaptador `entidadADTO` para Comanda está pendiente de implementar.

---

## 🔧 Utilidades

### `Encriptador.java`

Localización: `negocio/utilidades/Encriptador.java`

```java
// Encripta un teléfono (10 dígitos → Base64)
public static String encriptar(String nTelefono)
    → Base64.getEncoder().encodeToString(nTelefono.getBytes())

// Desencripta un teléfono (Base64 → 10 dígitos)
public static String desencriptar(String nTelefonoCifrado)
    → new String(Base64.getDecoder().decode(nTelefonoCifrado))

// Enmascara el teléfono para mostrar en UI: "6441234567" → "******4567"
public static String mostrarUltimosNumeros(String telefonoDesencriptado)
```

**Ejemplo:**
```
"6441234567"  →  encriptar()  →  "NjQ0MTIzNDU2Nw=="  (en BD)
"NjQ0MTIzNDU2Nw=="  →  desencriptar()  →  "6441234567"  (en UI)
"6441234567"  →  mostrarUltimosNumeros()  →  "******4567"  (en pantalla de puntos)
```

---

## 🚨 Manejo de Excepciones

El proyecto usa **excepciones personalizadas** por capa:

| Excepción | Capa | Tipo | Cuándo se lanza |
|---|---|---|---|
| `PersistenciaException` | persistencia | `RuntimeException` | Errores en BD (persist, merge, find) |
| `NegocioException` | negocio | `RuntimeException` | Validaciones fallidas, errores de lógica |

```java
// PersistenciaException — se lanza en los catch de los DAOs
throw new PersistenciaException("Error al agregar un cliente");

// NegocioException — se lanza en las validaciones del BO
throw new NegocioException("El teléfono del cliente no es válido");
throw new NegocioException("No se puede actualizar el cliente porque no tiene un ID asignado.");
```

Ambas son `RuntimeException`, por lo que no es obligatorio declararlas con `throws`, aunque en las interfaces sí se declaran explícitamente para mayor claridad.

---

## 🖥️ Capa de Presentación — UI

La UI usa **JavaFX** con un patrón **MVC simplificado**:
- Las **vistas** (Forms) no se comunican entre sí.
- Todo pasa por el **controlador** (`ControllerClienteFrecuente`).

### `ControllerClienteFrecuente.java` — Controlador central

```java
public class ControllerClienteFrecuente {
    private final IClienteBO clienteBO;   // Solo conoce la interfaz, no la implementación
    private final Stage primaryStage;
    private ClienteFrecuenteDTO clienteVinculado;  // Cliente seleccionado actualmente

    // Navegación entre pantallas:
    mostrarBuscador()          → BuscadorClientesFrm
    mostrarSistemaPuntos(dto)  → SistemaPuntosFrm
    mostrarRegistro()          → RegistroClienteFrm (modo nuevo)
    mostrarRegistroEdicion(dto)→ RegistroClienteFrm (modo edición)

    // Operaciones de negocio:
    buscarClientes(filtro, campo) → clienteBO.buscarFrecuentesPorFiltro()
    registrarCliente(dto)         → clienteBO.agregarClienteFrecuente()
    actualizarCliente(dto)        → clienteBO.actualizarClienteFrecuente()
    crearClienteGeneral()         → clienteBO.obtenerOCrearClienteGeneral()
    vincularCliente(dto)          → guarda el cliente seleccionado
}
```

### Pantallas disponibles

| Clase | Descripción |
|---|---|
| `Presentacion.java` | Main de JavaFX, punto de entrada de la app |
| `BuscadorClientesFrm.java` | Tabla de búsqueda con filtros por nombre, teléfono, correo |
| `RegistroClienteFrm.java` | Formulario para registrar o editar un cliente frecuente |
| `SistemaPuntosFrm.java` | Perfil del cliente + historial de transacciones + puntos |

### Flujo de navegación

```
Presentacion (Main)
      │
      └── ControllerClienteFrecuente
                   │
          ┌────────┴────────┐
          │                 │
    BuscadorClientesFrm   RegistroClienteFrm
    (pantalla principal)  (modal nuevo/editar)
          │
    [doble clic en fila]
          │
    SistemaPuntosFrm (modal con puntos)
```

---

## ⚙️ Configuración JPA

Archivo: `persistencia/src/main/resources/META-INF/persistence.xml`

```xml
<persistence-unit name="ConexionPU" transaction-type="RESOURCE_LOCAL">
    <provider>org.eclipse.persistence.jpa.PersistenceProvider</provider>

    <!-- Entidades registradas -->
    <class>Entidades.Cliente</class>
    <class>Entidades.ClienteFrecuente</class>
    <class>Entidades.ClienteGeneral</class>
    <class>Entidades.Comanda</class>

    <properties>
        <!-- Conexión a MySQL -->
        <property name="javax.persistence.jdbc.url"
                  value="jdbc:mysql://localhost:3306/comandas?zeroDateTimeBehavior=CONVERT_TO_NULL"/>
        <property name="javax.persistence.jdbc.user" value="root"/>
        <property name="javax.persistence.jdbc.driver" value="com.mysql.cj.jdbc.Driver"/>
        <property name="javax.persistence.jdbc.password" value="..."/>

        <!-- Solo crea tablas que falten, NO borra datos existentes -->
        <property name="javax.persistence.schema-generation.database.action" value="create"/>
    </properties>
</persistence-unit>
```

### 🔑 Puntos importantes de la configuración

- **Schema**: La BD se llama `comandas` (no `proyectojpa`).
- **`schema-generation.database.action = create`**: Solo crea tablas que no existen. **No borra ni recrea** las tablas existentes (corrección del 28 de marzo: antes era `drop-and-create`, que borraba todo al reiniciar).
- **Proveedor**: EclipseLink (no Hibernate). Algunas anotaciones/comportamientos pueden diferir.
- Agrega la nueva entidad `Combo` aquí cuando la implementes.

---

## 🧪 Tests Unitarios

### `ClienteDAOTest.java` — Prueba la capa de persistencia

Ubicación: `persistencia/src/test/java/ClienteDAOTest.java`

| Test | Tipo | Qué prueba |
|---|---|---|
| `testAgregarYBuscarPorId_FlujoBase` | Flujo base | Agregar un ClienteFrecuente y recuperarlo por ID |
| `testBuscarPorId_FlujoAlternativo` | Flujo alternativo | Buscar un ID que no existe → retorna `null` |
| `testActualizar_FlujoBase` | Flujo base | Modificar nombre y teléfono de un cliente existente |
| `testBuscarFrecuentesPorFiltro_FlujoBase` | Flujo base | Buscar por filtro parcial y verificar resultados |
| `testAgregarComanda_FlujoBase` | Flujo base | Agregar una Comanda y verificar su ID |
| `testObtenerClienteGeneral_FlujoBase` | Flujo base | Buscar el registro "Cliente General" |

### `ClienteBOTest.java` — Prueba la capa de negocio

Ubicación: `negocio/src/test/java/ClienteBOTest.java`

| Test | Tipo | Qué prueba |
|---|---|---|
| `testActualizarClienteSinId_DebeLanzarExcepcion` | Flujo alternativo | Actualizar sin ID lanza `NegocioException` |
| `testActualizarCliente_FlujoBase` | Flujo base | Actualizar un cliente existente correctamente |

---

## 🔄 Flujos de Datos Completos

### Flujo 1: Registrar un Cliente Frecuente

```
1. UI (RegistroClienteFrm) → Usuario llena: nombre, teléfono (10 dígitos), correo, fecha

2. Crear DTO:
   ClienteFrecuenteDTO dto = new ClienteFrecuenteDTO();
   dto.setNombre("Juan Pérez");
   dto.setTelefono("6441234567");  // Texto plano
   dto.setCorreo("juan@test.com");
   dto.setFechaRegistro(new Date());

3. Controller → clienteBO.agregarClienteFrecuente(dto)

4. ClienteBO:
   a. validarClienteFrecuenteDTO(dto)
      - nombre: no vacío, max 200 chars
      - correo: regex "^[A-Za-z0-9+_.-]+@(.+)$" (opcional)
      - teléfono: regex "^\d{10}$" (obligatorio)
      - fechaRegistro: no nula, no futura
   b. dto.setTelefono(Encriptador.encriptar("6441234567"))
      → dto.telefono = "NjQ0MTIzNDU2Nw=="
   c. ClienteFrecuente entidad = ClienteFrecuenteAdapter.dtoAEntidad(dto)
   d. clienteDAO.agregarClienteFrecuente(entidad)

5. ClienteDAO:
   em.getTransaction().begin()
   em.persist(clienteFrecuente)
   em.getTransaction().commit()
   → SQL: INSERT INTO clientes (nombre) VALUES ('Juan Pérez')
          INSERT INTO clientes_frecuentes (id, telefono, correo, fecha_registro)
                      VALUES (1, 'NjQ0MTIzNDU2Nw==', 'juan@test.com', '2024-03-28')

6. Retorna a la UI con éxito
```

---

### Flujo 2: Buscar Clientes Frecuentes

```
1. UI (BuscadorClientesFrm) → Usuario escribe "Juan" y selecciona filtro "nombre"

2. Controller → clienteBO.buscarFrecuentesPorFiltro("Juan", "nombre")

3. ClienteBO:
   a. Valida que filtro no esté vacío
   b. clienteDAO.buscarFrecuentesPorCampo("Juan", "nombre")

4. ClienteDAO (Criteria API):
   → SQL: SELECT * FROM clientes c JOIN clientes_frecuentes cf ON c.id = cf.id
          WHERE LOWER(c.nombre) LIKE '%juan%'

5. ClienteBO (por cada cliente encontrado):
   a. clienteDAO.buscarComandasPorCliente(c.getId())
      → SQL: SELECT * FROM comandas WHERE id_cliente = 1 AND UPPER(estado) = 'ENTREGADA'
   b. totalGastado = suma de todos los totales
   c. puntos = (int)(totalGastado / 20)
   d. numVisitas = cantidad de comandas
   e. dto = ClienteFrecuenteAdapter.entidadADTO(c, puntos, totalGastado, numVisitas)
   f. dto.setTelefono(Encriptador.desencriptar(c.getTelefono()))
      → "NjQ0MTIzNDU2Nw==" → "6441234567"

6. Retorna List<ClienteFrecuenteDTO> a la UI → se muestra en la tabla
```

---

### Flujo 3: Ver Sistema de Puntos

```
1. UI → Usuario hace doble clic en un cliente de la tabla

2. Controller:
   a. cargarTransacciones(cliente):
      - clienteBO.buscarComandasPorCliente(cliente.getId())
      - Por cada comanda: puntosGanados = (int)(total / 20), folio = "OB-000001"
   b. Abre SistemaPuntosFrm(cliente, transacciones)

3. SistemaPuntosFrm muestra:
   - Avatar con iniciales del nombre
   - Teléfono enmascarado: "******4567"
   - Correo (si tiene)
   - Fecha de registro
   - Total de puntos acumulados
   - Tabla de historial: fecha, folio, monto, puntos ganados, puntos acumulados
```

---

### Flujo 4: Crear Comanda (Cliente General)

```
1. UI → Usuario presiona "Cliente General" (sin seleccionar un cliente registrado)

2. Controller → crearClienteGeneral()
   → clienteBO.obtenerOCrearClienteGeneral()

3. ClienteBO:
   a. clienteDAO.obtenerClienteGeneral()
      → SELECT c FROM ClienteGeneral c WHERE c.nombre = 'Cliente General'
   b. Si retorna null (primera vez):
      - new ClienteGeneral("Cliente General")
      - clienteDAO.agregar(clienteGeneral)
   c. Retorna el nombre "Cliente General"

4. UI → ComandaDTO dto = new ComandaDTO("ABIERTA", 500.50, null)
   // idCliente null = cliente general

5. ComandaBO → agregarComanda(dto)
   a. ComandaAdapter.dtoAEntidad(dto)
   b. ComandaDAO.agregarComanda(comanda)
   → SQL: INSERT INTO comandas (total, estado, id_cliente) VALUES (500.50, 'ABIERTA', null)
```

---

## 👥 Contribuciones por Integrante

### 🟢 Keppl3r — Kevin

**Módulo completado:** Cliente Frecuente  
**Módulo en desarrollo:** Combos (módulo extra)

**Lo que implementó en Cliente Frecuente:**
- Entidad `ClienteFrecuente` con encriptación de teléfono.
- `ClienteBO` completo: validaciones, cálculo de puntos, búsqueda con filtros.
- `ClienteDAO` con búsqueda Criteria API (incluyendo `FROM_BASE64` para teléfonos).
- `Encriptador.java` (Base64).
- `PersistenciaException` y `NegocioException`.
- Fix en `persistence.xml`: cambio de `drop-and-create` a `create`.
- `SistemaPuntosFrm` (UI del sistema de puntos/perfil del cliente).
- Corrección del bug donde el puntaje no se actualizaba en la UI.
- Entidad `Comanda` (simplificada con String para estado).
- Método `buscarComandasPorCliente()` en el DAO.

---

### 🔵 jruben28 — Rubén

**Rol:** Coordinación del proyecto + Módulo Comanda (en desarrollo)

**Lo que implementó:**
- `ComandaDTO` con campos id, estado, total, idCliente.
- `ComandaBO` con método `agregarComanda()`.
- `ComandaDAO` con método `agregarComanda()`.
- `ComandaAdapter` (dtoAEntidad).
- `IComandaDAO` e `IComandaBO`.
- `InsertDatos.java`: script para poblar la BD con 10+ clientes y comandas de prueba.
- Coordinación general del proyecto y revisión de código.

**Pendiente:**
- Módulo Comanda completo: búsqueda, actualización, cancelación de comandas.
- UI para gestión de comandas.
- Validaciones en `ComandaBO`.

---

### 🟡 isa1ascm — Isaías

**Rol:** QA y Tests Unitarios

**Lo que implementó:**
- `ClienteDAOTest.java`: 6 pruebas unitarias para la capa de persistencia.
  - Prueba de agregar y buscar por ID.
  - Prueba de buscar ID inexistente.
  - Prueba de actualizar cliente.
  - Prueba de búsqueda por filtro.
  - Prueba de agregar comanda.
  - Prueba de obtener cliente general.
- `ClienteBOTest.java`: 2 pruebas unitarias para la capa de negocio.
  - Prueba de actualizar sin ID (debe lanzar excepción).
  - Prueba de actualizar con flujo correcto.
- Entidad `Cliente.java` (clase base).
- Entidad `ClienteGeneral.java`.
- `ClienteDTO.java` y `ClienteFrecuenteDTO.java`.

**Pendiente:**
- Tests para `ComandaBO` y `ComandaDAO`.
- Tests para el módulo Combos cuando esté listo.

---

### ⚪ Cris

**Rol:** Módulo pendiente de asignar

**Estado actual:** En el repositorio existe la rama `Cris` pero su módulo específico aún está por definirse.

**Posibles módulos a asignar:**
- UI de Comandas (formulario para crear/editar comandas).
- Módulo de Reportes.
- Módulo de Menú/Productos.

---

## 🍱 Módulo Combos — Guía para Kevin

El módulo Combos es el módulo extra asignado a Kevin. Aquí está todo lo que necesitas saber para implementarlo siguiendo los patrones existentes del proyecto.

### ¿Qué es un Combo?

Un Combo es un paquete de productos con un precio fijo (ej: "Combo Familiar", "Combo Personal"). Puede estar disponible para incluir en las comandas.

### Estructura sugerida a implementar

#### 1. Entidad `Combo.java`

Crear en `dominio/src/main/java/Entidades/Combo.java`:

```java
@Entity
@Table(name = "combos")
public class Combo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;         // "Combo Familiar", "Combo Personal"

    @Column(nullable = true)
    private String descripcion;    // Descripción del combo

    @Column(nullable = false)
    private Double precio;         // Precio del combo

    @Column(nullable = false)
    private Boolean disponible;    // Si está activo para venta

    // Constructor vacío (requerido por JPA)
    public Combo() {}

    // Constructor principal
    public Combo(String nombre, String descripcion, Double precio, Boolean disponible) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.disponible = disponible;
    }

    // Getters y setters...
}
```

#### 2. `ComboDTO.java`

Crear en `dtos/src/main/java/com/dtos/ComboDTO.java`:

```java
public class ComboDTO implements Serializable {
    private Long id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private Boolean disponible;

    // Constructores, getters y setters...
}
```

#### 3. `IComboDAO.java` y `ComboDAO.java`

Crear en `persistencia/src/main/java/DAOs/`:

```java
public interface IComboDAO {
    Combo agregar(Combo combo) throws PersistenciaException;
    Combo actualizar(Combo combo) throws PersistenciaException;
    Combo buscarPorId(Long id) throws PersistenciaException;
    List<Combo> buscarTodos() throws PersistenciaException;
    List<Combo> buscarDisponibles() throws PersistenciaException;
    void eliminar(Long id) throws PersistenciaException;
}
```

#### 4. `IComboBO.java` y `ComboBO.java`

Crear en `negocio/src/main/java/BOs/`:

```java
public interface IComboBO {
    void agregarCombo(ComboDTO comboDTO) throws NegocioException;
    void actualizarCombo(ComboDTO comboDTO) throws NegocioException;
    List<ComboDTO> obtenerCombosDisponibles() throws NegocioException;
    void eliminarCombo(Long id) throws NegocioException;
}
```

#### 5. `ComboAdapter.java`

Crear en `negocio/src/main/java/adaptadores/ComboAdapter.java`:

```java
public class ComboAdapter {
    public static Combo dtoAEntidad(ComboDTO dto) { ... }
    public static ComboDTO entidadADTO(Combo combo) { ... }
}
```

#### 6. Registrar en `persistence.xml`

Agregar en el bloque de clases:
```xml
<class>Entidades.Combo</class>
```

### Checklist de implementación del Módulo Combos

- [ ] Entidad `Combo.java` en `dominio`
- [ ] `ComboDTO.java` en `dtos`
- [ ] `IComboDAO.java` + `ComboDAO.java` en `persistencia`
- [ ] `IComboBO.java` + `ComboBO.java` en `negocio`
- [ ] `ComboAdapter.java` en `negocio/adaptadores`
- [ ] Registrar `Entidades.Combo` en `persistence.xml`
- [ ] UI para listar, agregar y editar combos (JavaFX)
- [ ] Integración de combos con las comandas (agregar combo a una comanda)
- [ ] Tests unitarios para `ComboDAO` y `ComboBO`

---

## 📌 Próximos Pasos por Módulo

### Kevin (Combos)
- [ ] Implementar entidad `Combo` con campos: nombre, descripción, precio, disponible.
- [ ] DAO de combos: CRUD completo.
- [ ] BO de combos: validaciones (precio > 0, nombre no vacío, etc.).
- [ ] UI para gestionar combos.
- [ ] Integrar combos en el flujo de comandas.

### Rubén (Comanda)
- [ ] Completar UI para crear comandas (asociar cliente + combos/productos).
- [ ] Implementar `actualizarComanda()` en DAO y BO.
- [ ] Implementar cancelación de comandas.
- [ ] Validaciones en `ComandaBO` (total > 0, estado válido, cliente existe).
- [ ] Implementar `entidadADTO()` en `ComandaAdapter`.
- [ ] Búsqueda de comandas por fecha, estado, cliente.

### Isaías (QA)
- [ ] Tests para `ComandaBO` y `ComandaDAO`.
- [ ] Tests para `ComboBO` y `ComboDAO` (cuando estén listos).
- [ ] Pruebas de integración entre módulos.
- [ ] Tests para el `Encriptador` (casos edge: null, cadena vacía, caracteres especiales).

### Cris
- [ ] Módulo por asignar (UI de Comandas, Reportes, o Menú).

---

## 💡 Puntos Clave a Recordar

1. **Jerarquía de clientes:** `Cliente` (abstracta) ← `ClienteFrecuente` + `ClienteGeneral`. Estrategia JOINED (tablas separadas).

2. **Teléfonos encriptados:** Se cifran con Base64 en el BO antes de guardar. Al buscar en BD, MySQL usa `FROM_BASE64()`. Al mostrar en UI, se desencriptan.

3. **Puntos calculados en tiempo real:** `puntosAcumulados` no se almacena en la BD. Se calcula cada vez: suma de `total` de todas las comandas `ENTREGADA` del cliente, dividido entre 20.

4. **DTOs como escudo:** Las entidades nunca llegan a la UI. Los adaptadores hacen la conversión en la capa de negocio.

5. **`obtenerOCrearClienteGeneral()`:** Patrón para garantizar que siempre exista el cliente anónimo. Se crea la primera vez y se reutiliza.

6. **`persistence.xml`:** Usa `create` (no `drop-and-create`). Esto significa que las tablas se crean si no existen pero **no se borran** al reiniciar. Fundamental para no perder datos.

7. **Proveedor JPA:** EclipseLink (no Hibernate). Si buscas soluciones en internet, asegúrate de que apliquen a EclipseLink.

8. **Combos — nueva entidad:** Seguir exactamente el mismo patrón que ClienteFrecuente: Entidad → DTO → DAO → BO → Adapter → UI.

9. **`InsertDatos.java`:** Script para poblar la BD con datos de prueba. Útil para development. Está en `negocio/pruebas/`.

10. **Regla del controlador:** Las vistas (Forms) nunca hablan entre sí ni conocen el BO. Todo pasa por `ControllerClienteFrecuente`. Al crear el módulo Combos, crear un `ControllerCombo` similar.

---

*Documento generado el 7 de abril de 2025 • ProyectoJPA — Equipo: Keppl3r, jruben28, isa1ascm, Cris*
