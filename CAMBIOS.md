# Integración PostgreSQL - Resumen

## Hecho
- Conexion.java: agregado getConnection() (JDBC PostgreSQL, jdbc:postgresql://localhost:5432/KryptonBase, usuario postgres). Se mantiene abrir() (db4o) SOLO porque EmpleadoControlador.java y PanelProveedoresModificar.java todavía la usan; no se tocó por instrucción de no modificar vistas.
- libs/postgresql-42.7.13.jar agregado y enlazado en nbproject/project.properties.
- sql/01_schema.sql: tu script de KryptonBase tal cual (usuario, negocio, direccion, moneda, modulo, negocio_modulo, cliente, tipo_documento, proveedor, etc). Ejecútalo primero en psql.
- sql/02_seed.sql: inserta los 5 módulos de tu pantalla de Modulos, moneda Dolar y tipos de documento. Ejecútalo después del schema.
- Modelo nuevos: Sesion, UsuarioCuenta, Negocio, Modulo, Cliente.
- DAO nuevos: UsuarioDAO, NegocioDAO, ModuloDAO, ClienteDAO, ProveedorDAO (reemplaza al db4o).
- Controladores nuevos: ControladorLogin, ControladorUsuario, ControladorNegocio, ControladorModulo, ControladorCliente.
- ControladorProveedor.java: reescrito para usar ProveedorDAO (PostgreSQL) en vez de db4o. Mantiene los mismos métodos públicos que ya llaman tus 5 vistas de PROVEEDORES, así que ESAS VISTAS NO SE TOCARON y deberían seguir compilando igual.

## Pendiente (vista) - lo vemos juntos después
Ninguna vista fue modificada. AhMismo, los botones de estas pantallas no leen sus propios campos ni pasan datos entre paneles, así que aunque el backend ya está listo, falta conectar:
- Login.java: BtnIngresarActionPerformed tiene el "Admin/Admin" hardcodeado -> debe llamar a ControladorLogin.login(...)
- PanelRegistroUsuario.java: btncontinuaActionPerformed no lee los campos ni los pasa al siguiente panel -> falta leer txtcedula/txtnombre/etc y llamar a ControladorUsuario.registrarUsuario(...)
- PanelRegistroNegocio.java: BtnAvanzarActionPerformed no lee campos ni recibe el idUsuario del paso anterior -> falta leer campos y llamar a ControladorNegocio.registrarNegocio(...)
- PanelModulos.java: BtnSiguiente no tiene actionListener ni lee los ModuloRol -> falta conectar con ControladorModulo
- Vista/CLIENTES/*: no existe controlador ni conexión aún -> falta conectar con ControladorCliente (ya creado)
- PanelProveedoresModificar.java: es el único que sigue usando db4o directo -> falta migrarlo a ProveedorDAO

## Nota importante
Cambia la contraseña real de postgres en Conexion.java (línea PASSWORD = "postgres").
