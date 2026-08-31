# 🔑 Krypton (KTP)

### Sistema de gestión para microempresas — inventario, ventas y RRHH

**Stack real:** Java Swing (escritorio) + PostgreSQL · **Instituto:** Tec. Azuay

---

## 📌 Descripción

KTP es una aplicación de escritorio en Java que le permite a un negocio pequeño llevar su
inventario, sus ventas, sus compras a proveedores, su caja y su personal desde un solo
sistema, con roles distintos según quién esté usándolo (dueño, cajero, bodeguero, RRHH).

Cada usuario que se registra crea su propio negocio (multi-negocio: la misma base de datos
puede tener varias empresas usando el sistema, cada una viendo solo su propia información).

---

## 🧱 Stack y arquitectura reales

- **Interfaz:** Java Swing (NetBeans GUI Builder), no JavaFX.
- **Base de datos:** PostgreSQL, actualmente alojada en [Neon](https://neon.com) (en línea, no local).
- **Build:** Apache Ant / proyecto NetBeans estándar (`build.xml`).
- **Librerías:** driver JDBC de PostgreSQL, JFreeChart (gráficos de estadísticas), JavaMail
  (correo), OkHttp (cliente HTTP).
- **Contraseñas:** hash con PBKDF2 + salt (no texto plano, no BCrypt).
- **PDF y Excel:** el generador de facturas en PDF y el exportador de reportes a Excel están
  escritos a mano en Java (sin librerías como iText o Apache POI).

---

## ✅ Funcionalidades implementadas

**Cuentas y acceso**
- Registro de usuario + registro de negocio.
- Login con roles (Dueño, Cajero/Vendedor, Bodeguero, Recursos Humanos), cada rol ve solo los
  paneles para los que tiene permiso.
- Recuperación de contraseña por código enviado al correo.
- Un empleado puede solicitar unirse a un negocio; el dueño aprueba o rechaza la solicitud
  desde su panel.
- Selección de qué módulos tiene activos cada negocio (Catálogo, Ventas, Finanzas, RRHH,
  Configuración).

**Catálogo e inventario**
- CRUD de productos (categoría, IVA, precio, costo, stock mínimo/máximo, lote, ubicación,
  fecha de vencimiento).
- CRUD de proveedores y clientes.
- Ajustes y movimientos de inventario desde el rol Bodeguero.

**Ventas y compras**
- Facturación rápida, con historial de facturas y detalle por factura.
- Factura generada en PDF y envío de la factura por correo al cliente.
- Registro de compras a proveedores, con pagarés (crédito) y abonos parciales.
- Apertura y cierre de caja por turno (Cajero), con historial de cierres.

**Recursos Humanos**
- Gestión de empleados, roles y permisos por negocio.
- Generación de pagos a empleados y su historial.

**Otros**
- Notas personales del usuario.
- Notificaciones automáticas de stock bajo y pagarés por vencer (se generan al iniciar sesión).
- Estadísticas del negocio con gráficos (JFreeChart) y reportes exportables a Excel.
- **Asistente virtual con IA** (Groq, modelo `gpt-oss-120b`): responde preguntas sobre los
  datos reales del negocio (stock, compras a proveedores), entiende comandos de voz y texto, y
  puede navegar el sistema por ti (ej. "llévame a productos").

---

## ❌ Fuera de alcance (se descartó o nunca se llegó a programar)

Para que el README no prometa cosas que no existen:

- **Facturación electrónica real ante el SRI** (firma XML, web service SOAP): no está
  implementada. Solo existe un campo `estado_sri` y `clave_acceso` en la base de datos,
  pensados para una futura integración, pero hoy la factura solo se genera en PDF.
- **Motor de "plantillas maleables"** para adaptar el modelo de datos a cualquier tipo de
  negocio: se descartó por complejidad. Todos los negocios usan el mismo modelo fijo de
  tablas (productos, categorías, etc.).
- **Auditoría continua en segundo plano:** las notificaciones (stock bajo, pagarés por vencer)
  se revisan una vez al iniciar sesión, no con un proceso corriendo todo el tiempo.
- JavaFX, MySQL y BCrypt: mencionados en versiones anteriores de este README, nunca se usaron.

---

## 🚀 Cómo correr el proyecto

**Requisito:** Java 17.

1. Copia `config.properties.example` a `config.properties` y completa los datos de conexión a
   PostgreSQL (local o Neon) y, si quieres correo/IA, tu correo de aplicación de Gmail y tu
   API key de Groq.
2. Abre el proyecto en NetBeans y dale "Run", o compílalo desde consola:
   ```
   ant clean jar
   java -jar dist/ProyectoKTPV51.jar
   ```
3. El esquema de base de datos está en `sql/01_schema.sql` (créalo primero) y
   `sql/02_seed_tasa_iva.sql` (catálogo de IVA).

---

## 👥 Equipo

| Rol              | Responsable        | Responsabilidad principal                                  |
|------------------|---------------------|--------------------------------------------------------------|
| Scrum Master     | Pablo Alvarado      | Adopción del marco ágil, ceremonias, Asistente Virtual        |
| Product Owner    | Gerard Perez        | Backlog, priorización de historias, diseño visual             |
| Dev Team         | Domenica Crespo     | Estadísticas, plantillas, pruebas del sistema                 |
| Dev Team         | Karen Carabajo      | Lógica de negocio Java, facturación rápida, reportes PDF      |
| Dev Team         | Diana Tigre         | Base de datos relacional, modelado a objetos                  |

---

**Instituto Superior Universitario Tecnológico del Azuay** | Cuenca, Ecuador
**Carrera:** Tecnología Superior en Desarrollo de Software — Segundo M2A
**Docente:** Ing. Diana Romero
