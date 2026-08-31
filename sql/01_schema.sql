-- ============================================================
-- Proyecto ERP - Usuario / Negocio / Modulos
-- Script de creacion COMPLETO y LIMPIO en PostgreSQL.
--
-- Incluye: las 31 tablas originales + pago_empleado + las columnas
-- nuevas que fue trayendo el backup del equipo (producto, cierre_caja,
-- factura, ingreso, egreso, cliente.activo, empleado.estado).
--
-- COMO CORRERLO (borra tu base actual y la crea de cero):
-- 1) Conectate a la base "postgres" (NO a KryptonBase), porque
--    Postgres no te deja borrar la base a la que estas conectado.
--    En pgAdmin: clic en el servidor -> Query Tool sobre "postgres".
-- 2) Corre SOLO estas 2 lineas primero, una por una:
--       DROP DATABASE IF EXISTS "KryptonBase";
--       CREATE DATABASE "KryptonBase";
-- 3) Conectate ya a "KryptonBase" (clic en ella, Query Tool nueva)
--    y corre el resto de este archivo completo, de una sola vez.
--
-- Nota rapida sobre los IDs: en vez de dejar que Postgres numere
-- solo (1, 2, 3...) decidimos usar codigos con prefijo, tipo
-- PO01 para productos, US01 para usuarios, etc. Para no tener
-- que escribirlos a mano cada vez, cada tabla tiene un trigger
-- que le pone el codigo automatico apenas haces el INSERT
-- (si no le mandas el id, el solito calcula el siguiente).
-- ============================================================

-- USUARIO
-- Es el dueño que se registra primero, antes de crear su negocio.
CREATE TABLE usuario (
    id_usuario      VARCHAR(10) PRIMARY KEY,
    cedula          VARCHAR(10)  UNIQUE NOT NULL,
    nombres         VARCHAR(100) NOT NULL,
    apellidos       VARCHAR(100) NOT NULL,
    correo          VARCHAR(150) UNIQUE NOT NULL,
    contrasena      VARCHAR(255) NOT NULL,
    telefono        VARCHAR(15),
    foto_perfil     VARCHAR(255)
);

-- secuencia + funcion + trigger para que id_usuario se autogenere como US01, US02...
CREATE SEQUENCE seq_usuario START 1;
CREATE OR REPLACE FUNCTION generar_id_usuario() RETURNS TRIGGER AS $$
BEGIN
    IF NEW.id_usuario IS NULL THEN
        NEW.id_usuario := 'US' || LPAD(nextval('seq_usuario')::TEXT, 2, '0');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER trg_usuario BEFORE INSERT ON usuario
FOR EACH ROW EXECUTE FUNCTION generar_id_usuario();


-- DIRECCION
-- Tabla aparte para no repetir calle/referencia como texto suelto.
-- La usan tanto Negocio como Proveedor.
CREATE TABLE direccion (
    id_direccion    VARCHAR(10) PRIMARY KEY,
    calle_principal VARCHAR(100),
    calle_secundaria VARCHAR(100),
    referencia      VARCHAR(200),
    ciudad          VARCHAR(100)
);

CREATE SEQUENCE seq_direccion START 1;
CREATE OR REPLACE FUNCTION generar_id_direccion() RETURNS TRIGGER AS $$
BEGIN
    IF NEW.id_direccion IS NULL THEN
        NEW.id_direccion := 'DI' || LPAD(nextval('seq_direccion')::TEXT, 2, '0');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER trg_direccion BEFORE INSERT ON direccion
FOR EACH ROW EXECUTE FUNCTION generar_id_direccion();


-- NEGOCIO
-- Cada usuario registra un solo negocio (por eso id_usuario es UNIQUE,
-- no solo FK -- asi Postgres no deja que un mismo usuario tenga dos negocios).
CREATE TABLE negocio (
    id_negocio      VARCHAR(10) PRIMARY KEY,
    id_usuario      VARCHAR(10) NOT NULL UNIQUE REFERENCES usuario(id_usuario),
    id_direccion    VARCHAR(10) REFERENCES direccion(id_direccion),
    nombre_negocio  VARCHAR(150) NOT NULL,
    ruc_negocio     VARCHAR(13) UNIQUE NOT NULL,
    correo_contacto VARCHAR(150),
    fecha_registro  DATE DEFAULT CURRENT_DATE
);

CREATE SEQUENCE seq_negocio START 1;
CREATE OR REPLACE FUNCTION generar_id_negocio() RETURNS TRIGGER AS $$
BEGIN
    IF NEW.id_negocio IS NULL THEN
        NEW.id_negocio := 'NE' || LPAD(nextval('seq_negocio')::TEXT, 2, '0');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER trg_negocio BEFORE INSERT ON negocio
FOR EACH ROW EXECUTE FUNCTION generar_id_negocio();


-- MODULO
-- Los modulos que puede activar un negocio: Catalogo, Ventas, RRHH, etc.
-- Esta tabla es fija del sistema, no depende de cada negocio.
CREATE TABLE modulo (
    id_modulo       VARCHAR(10) PRIMARY KEY,
    nombre_modulo   VARCHAR(100) NOT NULL,
    descripcion     VARCHAR(255)
);

CREATE SEQUENCE seq_modulo START 1;
CREATE OR REPLACE FUNCTION generar_id_modulo() RETURNS TRIGGER AS $$
BEGIN
    IF NEW.id_modulo IS NULL THEN
        NEW.id_modulo := 'MD' || LPAD(nextval('seq_modulo')::TEXT, 2, '0');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER trg_modulo BEFORE INSERT ON modulo
FOR EACH ROW EXECUTE FUNCTION generar_id_modulo();


-- ROL
-- Cada negocio arma sus propios roles (Cajero, Bodeguero, Gerente...).
-- OJO: aqui NO va un campo "cargo" repetido en Empleado, porque el
-- desplegable de cargo ES este mismo catalogo -- ver EMPLEADO abajo.
CREATE TABLE rol (
    id_rol          VARCHAR(10) PRIMARY KEY,
    id_negocio      VARCHAR(10) NOT NULL REFERENCES negocio(id_negocio),
    nombre_rol      VARCHAR(50) NOT NULL
);

CREATE SEQUENCE seq_rol START 1;
CREATE OR REPLACE FUNCTION generar_id_rol() RETURNS TRIGGER AS $$
BEGIN
    IF NEW.id_rol IS NULL THEN
        NEW.id_rol := 'RO' || LPAD(nextval('seq_rol')::TEXT, 2, '0');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER trg_rol BEFORE INSERT ON rol
FOR EACH ROW EXECUTE FUNCTION generar_id_rol();


-- PERMISO
-- Los permisos sueltos (crear_venta, ver_reportes...) que despues
-- se combinan con los roles en la tabla puente rol_permiso.
CREATE TABLE permiso (
    id_permiso      VARCHAR(10) PRIMARY KEY,
    nombre_permiso  VARCHAR(100) NOT NULL
);

CREATE SEQUENCE seq_permiso START 1;
CREATE OR REPLACE FUNCTION generar_id_permiso() RETURNS TRIGGER AS $$
BEGIN
    IF NEW.id_permiso IS NULL THEN
        NEW.id_permiso := 'PM' || LPAD(nextval('seq_permiso')::TEXT, 2, '0');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER trg_permiso BEFORE INSERT ON permiso
FOR EACH ROW EXECUTE FUNCTION generar_id_permiso();


-- EMPLEADO
-- No lleva campo "cargo" a proposito: el cargo que se ve en el
-- formulario (Cajero/Bodeguero/Gerente) es literalmente el id_rol,
-- asi que ponerlo aparte hubiera sido guardar el mismo dato dos veces.
-- Ademas EMPLEADO hereda de USUARIO (antes eran 2 tablas duplicadas con
-- los mismos campos de persona: cedula, nombres, apellidos, correo,
-- telefono, foto, contrasena). Ahora la identidad y el login
-- (usuario/contrasena/correo) SOLO viven en "usuario". "empleado" es la
-- extension: mismo id que su usuario, y solo los datos propios de ser
-- empleado de un negocio.
CREATE TABLE empleado (
    id_empleado     VARCHAR(10) PRIMARY KEY REFERENCES usuario(id_usuario),
    id_negocio      VARCHAR(10) NOT NULL REFERENCES negocio(id_negocio),
    id_rol          VARCHAR(10) NOT NULL REFERENCES rol(id_rol),
    salario         NUMERIC(10,2),
    fecha_ingreso   DATE DEFAULT CURRENT_DATE,
    estado          VARCHAR(20) DEFAULT 'activo'
);

-- PAGO_EMPLEADO: pago de sueldo a un empleado (agregada por el equipo).
CREATE TABLE pago_empleado (
    id_pago         VARCHAR(10) PRIMARY KEY,
    id_empleado     VARCHAR(10) NOT NULL REFERENCES empleado(id_empleado),
    fecha_pago      DATE DEFAULT CURRENT_DATE,
    periodo         VARCHAR(50) NOT NULL,
    monto           NUMERIC(10,2) NOT NULL,
    observaciones   VARCHAR(255)
);

CREATE SEQUENCE seq_pago_empleado START 1;
CREATE OR REPLACE FUNCTION generar_id_pago_empleado() RETURNS TRIGGER AS $$
BEGIN
    IF NEW.id_pago IS NULL THEN
        NEW.id_pago := 'PE' || LPAD(nextval('seq_pago_empleado')::TEXT, 2, '0');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER trg_pago_empleado BEFORE INSERT ON pago_empleado
FOR EACH ROW EXECUTE FUNCTION generar_id_pago_empleado();

-- SOLICITUD_ACCESO
-- Cuando alguien se registra marcando "soy empleado", primero se crea su
-- USUARIO (identidad + login), y la solicitud queda aqui en estado
-- 'pendiente' hasta que el dueño del negocio la acepte desde
-- PanelAceptarEmpleados. Al aceptar: se crea la fila en "empleado" (con el
-- salario que asigne el dueño) y esta solicitud pasa a 'aprobada'.
CREATE TABLE solicitud_acceso (
    id_solicitud     VARCHAR(10) PRIMARY KEY,
    id_usuario       VARCHAR(10) NOT NULL REFERENCES usuario(id_usuario),
    id_negocio       VARCHAR(10) NOT NULL REFERENCES negocio(id_negocio),
    id_rol           VARCHAR(10) NOT NULL REFERENCES rol(id_rol),
    fecha_solicitud  DATE DEFAULT CURRENT_DATE,
    estado           VARCHAR(20) NOT NULL DEFAULT 'pendiente'  -- pendiente / aprobada / rechazada
);

CREATE SEQUENCE seq_solicitud START 1;
CREATE OR REPLACE FUNCTION generar_id_solicitud() RETURNS TRIGGER AS $$
BEGIN
    IF NEW.id_solicitud IS NULL THEN
        NEW.id_solicitud := 'SO' || LPAD(nextval('seq_solicitud')::TEXT, 2, '0');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER trg_solicitud BEFORE INSERT ON solicitud_acceso
FOR EACH ROW EXECUTE FUNCTION generar_id_solicitud();


-- TASA_IVA
-- Aqui es donde va el IVA, NO en Negocio -- porque dentro del mismo
-- negocio puede haber productos con 15% y productos con 0% (ej. lacteos).
CREATE TABLE tasa_iva (
    id_tasa_iva     VARCHAR(10) PRIMARY KEY,
    porcentaje      NUMERIC(5,2) NOT NULL,
    descripcion     VARCHAR(150)
);

CREATE SEQUENCE seq_tasa_iva START 1;
CREATE OR REPLACE FUNCTION generar_id_tasa_iva() RETURNS TRIGGER AS $$
BEGIN
    IF NEW.id_tasa_iva IS NULL THEN
        NEW.id_tasa_iva := 'IV' || LPAD(nextval('seq_tasa_iva')::TEXT, 2, '0');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER trg_tasa_iva BEFORE INSERT ON tasa_iva
FOR EACH ROW EXECUTE FUNCTION generar_id_tasa_iva();


-- CATEGORIA_PRODUCTO
-- Para poder filtrar el catalogo por categoria (Lacteos, Tecnologia...).
CREATE TABLE categoria_producto (
    id_categoria    VARCHAR(10) PRIMARY KEY,
    id_negocio      VARCHAR(10) NOT NULL REFERENCES negocio(id_negocio),
    nombre_categoria VARCHAR(100) NOT NULL
);

CREATE SEQUENCE seq_categoria_producto START 1;
CREATE OR REPLACE FUNCTION generar_id_categoria_producto() RETURNS TRIGGER AS $$
BEGIN
    IF NEW.id_categoria IS NULL THEN
        NEW.id_categoria := 'CA' || LPAD(nextval('seq_categoria_producto')::TEXT, 2, '0');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER trg_categoria_producto BEFORE INSERT ON categoria_producto
FOR EACH ROW EXECUTE FUNCTION generar_id_categoria_producto();


-- PRODUCTO
-- El stock_actual arranca en 0 y solo se mueve a traves de la tabla
-- movimiento_inventario (nunca se edita a mano) -- asi queda auditable.
CREATE TABLE producto (
    id_producto     VARCHAR(10) PRIMARY KEY,
    id_negocio      VARCHAR(10) NOT NULL REFERENCES negocio(id_negocio),
    id_categoria    VARCHAR(10) NOT NULL REFERENCES categoria_producto(id_categoria),
    id_tasa_iva     VARCHAR(10) NOT NULL REFERENCES tasa_iva(id_tasa_iva),
    nombre_producto VARCHAR(150) NOT NULL,
    codigo_barras   VARCHAR(50) UNIQUE,
    precio_venta    NUMERIC(10,2) NOT NULL,
    costo           NUMERIC(10,2) NOT NULL,
    stock_actual    INT DEFAULT 0,
    stock_minimo    INT DEFAULT 0,
    stock_maximo    INT DEFAULT 0,
    ubicacion_pasillo VARCHAR(50),
    lote            VARCHAR(50),
    fecha_vencimiento DATE,
    estado          VARCHAR(20) DEFAULT 'activo'
);

CREATE SEQUENCE seq_producto START 1;
CREATE OR REPLACE FUNCTION generar_id_producto() RETURNS TRIGGER AS $$
BEGIN
    IF NEW.id_producto IS NULL THEN
        NEW.id_producto := 'PO' || LPAD(nextval('seq_producto')::TEXT, 2, '0');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER trg_producto BEFORE INSERT ON producto
FOR EACH ROW EXECUTE FUNCTION generar_id_producto();


-- PROVEEDOR
-- Se asume que el proveedor siempre es empresa (RUC fijo), a diferencia
-- de Cliente que puede ser persona natural o empresa.
CREATE TABLE proveedor (
    id_proveedor    VARCHAR(10) PRIMARY KEY,
    id_negocio      VARCHAR(10) NOT NULL REFERENCES negocio(id_negocio),
    id_direccion    VARCHAR(10) REFERENCES direccion(id_direccion),
    ruc             VARCHAR(13) UNIQUE NOT NULL,
    nombre_proveedor VARCHAR(150) NOT NULL,
    apellido_proveedor VARCHAR(150),
    correo          VARCHAR(150),
    telefono        VARCHAR(15)
);

CREATE SEQUENCE seq_proveedor START 1;
CREATE OR REPLACE FUNCTION generar_id_proveedor() RETURNS TRIGGER AS $$
BEGIN
    IF NEW.id_proveedor IS NULL THEN
        NEW.id_proveedor := 'PV' || LPAD(nextval('seq_proveedor')::TEXT, 2, '0');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER trg_proveedor BEFORE INSERT ON proveedor
FOR EACH ROW EXECUTE FUNCTION generar_id_proveedor();


-- TIPO_DOCUMENTO
-- Solo lo usa Cliente (cedula, RUC o pasaporte), porque el cliente
-- si puede ser persona natural o empresa.
CREATE TABLE tipo_documento (
    id_tipo_documento VARCHAR(10) PRIMARY KEY,
    nombre_tipo_documento VARCHAR(50) NOT NULL
);

CREATE SEQUENCE seq_tipo_documento START 1;
CREATE OR REPLACE FUNCTION generar_id_tipo_documento() RETURNS TRIGGER AS $$
BEGIN
    IF NEW.id_tipo_documento IS NULL THEN
        NEW.id_tipo_documento := 'TD' || LPAD(nextval('seq_tipo_documento')::TEXT, 2, '0');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER trg_tipo_documento BEFORE INSERT ON tipo_documento
FOR EACH ROW EXECUTE FUNCTION generar_id_tipo_documento();


-- CLIENTE
CREATE TABLE cliente (
    id_cliente      VARCHAR(10) PRIMARY KEY,
    id_negocio      VARCHAR(10) NOT NULL REFERENCES negocio(id_negocio),
    id_tipo_documento VARCHAR(10) NOT NULL REFERENCES tipo_documento(id_tipo_documento),
    numero_documento VARCHAR(20) NOT NULL,
    nombre_cliente  VARCHAR(150) NOT NULL,
    telefono        VARCHAR(15),
    correo          VARCHAR(150),
    activo          BOOLEAN NOT NULL DEFAULT TRUE,
    UNIQUE (id_negocio, numero_documento)
);

CREATE SEQUENCE seq_cliente START 1;
CREATE OR REPLACE FUNCTION generar_id_cliente() RETURNS TRIGGER AS $$
BEGIN
    IF NEW.id_cliente IS NULL THEN
        NEW.id_cliente := 'CL' || LPAD(nextval('seq_cliente')::TEXT, 2, '0');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER trg_cliente BEFORE INSERT ON cliente
FOR EACH ROW EXECUTE FUNCTION generar_id_cliente();


-- COMPRA
-- Cuando el negocio le compra a un proveedor. El detalle de que
-- productos trajo esa compra va en la tabla puente compra_producto.
CREATE TABLE compra (
    id_compra       VARCHAR(10) PRIMARY KEY,
    id_negocio      VARCHAR(10) NOT NULL REFERENCES negocio(id_negocio),
    id_proveedor    VARCHAR(10) NOT NULL REFERENCES proveedor(id_proveedor),
    num_factura_proveedor VARCHAR(50),
    fecha_compra    DATE DEFAULT CURRENT_DATE,
    subtotal        NUMERIC(10,2) NOT NULL,
    valor_iva       NUMERIC(10,2) DEFAULT 0,
    descuento       NUMERIC(10,2) DEFAULT 0,
    total           NUMERIC(10,2) NOT NULL
);

CREATE SEQUENCE seq_compra START 1;
CREATE OR REPLACE FUNCTION generar_id_compra() RETURNS TRIGGER AS $$
BEGIN
    IF NEW.id_compra IS NULL THEN
        NEW.id_compra := 'CO' || LPAD(nextval('seq_compra')::TEXT, 2, '0');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER trg_compra BEFORE INSERT ON compra
FOR EACH ROW EXECUTE FUNCTION generar_id_compra();


-- PAGARE
-- No toda compra genera pagare (si fue al contado, no hay fila aca) --
-- por eso id_compra es UNIQUE pero puede quedar sin usar en algunas compras.
CREATE TABLE pagare (
    id_pagare       VARCHAR(10) PRIMARY KEY,
    id_compra       VARCHAR(10) UNIQUE REFERENCES compra(id_compra),
    monto_total     NUMERIC(10,2) NOT NULL,
    saldo_pendiente NUMERIC(10,2) NOT NULL,
    fecha_emision   DATE DEFAULT CURRENT_DATE,
    fecha_vencimiento DATE,
    estado          VARCHAR(20) DEFAULT 'pendiente'
);

CREATE SEQUENCE seq_pagare START 1;
CREATE OR REPLACE FUNCTION generar_id_pagare() RETURNS TRIGGER AS $$
BEGIN
    IF NEW.id_pagare IS NULL THEN
        NEW.id_pagare := 'PG' || LPAD(nextval('seq_pagare')::TEXT, 2, '0');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER trg_pagare BEFORE INSERT ON pagare
FOR EACH ROW EXECUTE FUNCTION generar_id_pagare();


-- METODO_PAGO
-- Catalogo (Efectivo, Transferencia, Tarjeta) que usan tanto
-- Pago_Proveedor como Factura, para no repetir texto libre en ninguna de las dos.
CREATE TABLE metodo_pago (
    id_metodo_pago  VARCHAR(10) PRIMARY KEY,
    nombre_metodo_pago VARCHAR(50) NOT NULL
);

CREATE SEQUENCE seq_metodo_pago START 1;
CREATE OR REPLACE FUNCTION generar_id_metodo_pago() RETURNS TRIGGER AS $$
BEGIN
    IF NEW.id_metodo_pago IS NULL THEN
        NEW.id_metodo_pago := 'MP' || LPAD(nextval('seq_metodo_pago')::TEXT, 2, '0');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER trg_metodo_pago BEFORE INSERT ON metodo_pago
FOR EACH ROW EXECUTE FUNCTION generar_id_metodo_pago();


-- PAGO_PROVEEDOR
-- Los abonos que se le van haciendo a un pagare (pueden ser varios
-- pagos parciales hasta dejar el saldo en 0).
CREATE TABLE pago_proveedor (
    id_pago         VARCHAR(10) PRIMARY KEY,
    id_pagare       VARCHAR(10) NOT NULL REFERENCES pagare(id_pagare),
    id_metodo_pago  VARCHAR(10) NOT NULL REFERENCES metodo_pago(id_metodo_pago),
    monto           NUMERIC(10,2) NOT NULL,
    fecha_pago      DATE DEFAULT CURRENT_DATE
);

CREATE SEQUENCE seq_pago_proveedor START 1;
CREATE OR REPLACE FUNCTION generar_id_pago_proveedor() RETURNS TRIGGER AS $$
BEGIN
    IF NEW.id_pago IS NULL THEN
        NEW.id_pago := 'PP' || LPAD(nextval('seq_pago_proveedor')::TEXT, 2, '0');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER trg_pago_proveedor BEFORE INSERT ON pago_proveedor
FOR EACH ROW EXECUTE FUNCTION generar_id_pago_proveedor();


-- FACTURA
-- La venta al cliente. Igual que en Compra, el detalle de productos
-- vendidos va en la tabla puente factura_producto.
CREATE TABLE factura (
    id_factura      VARCHAR(10) PRIMARY KEY,
    id_negocio      VARCHAR(10) NOT NULL REFERENCES negocio(id_negocio),
    id_cliente      VARCHAR(10) NOT NULL REFERENCES cliente(id_cliente),
    id_empleado     VARCHAR(10) NOT NULL REFERENCES empleado(id_empleado),
    id_metodo_pago  VARCHAR(10) NOT NULL REFERENCES metodo_pago(id_metodo_pago),
    num_factura     VARCHAR(50) UNIQUE NOT NULL,
    clave_acceso    VARCHAR(60),
    fecha           DATE DEFAULT CURRENT_DATE,
    hora            TIME DEFAULT CURRENT_TIME,
    subtotal        NUMERIC(10,2) NOT NULL,
    valor_iva       NUMERIC(10,2) DEFAULT 0,
    descuento       NUMERIC(10,2) DEFAULT 0,
    total           NUMERIC(10,2) NOT NULL,
    estado_sri      VARCHAR(30)
);

CREATE SEQUENCE seq_factura START 1;
CREATE OR REPLACE FUNCTION generar_id_factura() RETURNS TRIGGER AS $$
BEGIN
    IF NEW.id_factura IS NULL THEN
        NEW.id_factura := 'FA' || LPAD(nextval('seq_factura')::TEXT, 2, '0');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER trg_factura BEFORE INSERT ON factura
FOR EACH ROW EXECUTE FUNCTION generar_id_factura();


-- TIPO_MOVIMIENTO
-- Solo tiene 2 valores de verdad (Entrada / Salida), no le busques
-- una tercera opcion, es un catalogo cerrado a proposito.
CREATE TABLE tipo_movimiento (
    id_tipo_movimiento VARCHAR(10) PRIMARY KEY,
    nombre_tipo_movimiento VARCHAR(20) NOT NULL
);

CREATE SEQUENCE seq_tipo_movimiento START 1;
CREATE OR REPLACE FUNCTION generar_id_tipo_movimiento() RETURNS TRIGGER AS $$
BEGIN
    IF NEW.id_tipo_movimiento IS NULL THEN
        NEW.id_tipo_movimiento := 'TM' || LPAD(nextval('seq_tipo_movimiento')::TEXT, 2, '0');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER trg_tipo_movimiento BEFORE INSERT ON tipo_movimiento
FOR EACH ROW EXECUTE FUNCTION generar_id_tipo_movimiento();


-- MOVIMIENTO_INVENTARIO
-- Es el "libro mayor" del stock: cada compra suma, cada venta resta.
-- El CHECK de abajo obliga a que cada fila venga SOLO de una compra
-- o SOLO de una factura, nunca de las dos a la vez.
CREATE TABLE movimiento_inventario (
    id_movimiento   VARCHAR(10) PRIMARY KEY,
    id_producto     VARCHAR(10) NOT NULL REFERENCES producto(id_producto),
    id_tipo_movimiento VARCHAR(10) NOT NULL REFERENCES tipo_movimiento(id_tipo_movimiento),
    id_compra       VARCHAR(10) REFERENCES compra(id_compra),
    id_factura      VARCHAR(10) REFERENCES factura(id_factura),
    cantidad        INT NOT NULL,
    stock_anterior  INT NOT NULL,
    stock_nuevo     INT NOT NULL,
    fecha           DATE DEFAULT CURRENT_DATE,
    CONSTRAINT chk_origen_unico CHECK (
        (id_compra IS NOT NULL AND id_factura IS NULL) OR
        (id_compra IS NULL AND id_factura IS NOT NULL)
    )
);

CREATE SEQUENCE seq_movimiento_inventario START 1;
CREATE OR REPLACE FUNCTION generar_id_movimiento_inventario() RETURNS TRIGGER AS $$
BEGIN
    IF NEW.id_movimiento IS NULL THEN
        NEW.id_movimiento := 'MI' || LPAD(nextval('seq_movimiento_inventario')::TEXT, 2, '0');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER trg_movimiento_inventario BEFORE INSERT ON movimiento_inventario
FOR EACH ROW EXECUTE FUNCTION generar_id_movimiento_inventario();


-- INGRESO
-- Cada factura pagada genera un ingreso automatico (no se digita a mano).
CREATE TABLE ingreso (
    id_ingreso      VARCHAR(10) PRIMARY KEY,
    id_negocio      VARCHAR(10) NOT NULL REFERENCES negocio(id_negocio),
    id_factura      VARCHAR(10) REFERENCES factura(id_factura),
    fecha           DATE DEFAULT CURRENT_DATE,
    hora            TIME DEFAULT CURRENT_TIME,
    monto           NUMERIC(10,2) NOT NULL,
    concepto        VARCHAR(200)
);

CREATE SEQUENCE seq_ingreso START 1;
CREATE OR REPLACE FUNCTION generar_id_ingreso() RETURNS TRIGGER AS $$
BEGIN
    IF NEW.id_ingreso IS NULL THEN
        NEW.id_ingreso := 'IN' || LPAD(nextval('seq_ingreso')::TEXT, 2, '0');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER trg_ingreso BEFORE INSERT ON ingreso
FOR EACH ROW EXECUTE FUNCTION generar_id_ingreso();


-- EGRESO
-- Mismo concepto que Ingreso pero al reves: cada pago a proveedor
-- genera un egreso automatico.
CREATE TABLE egreso (
    id_egreso       VARCHAR(10) PRIMARY KEY,
    id_negocio      VARCHAR(10) NOT NULL REFERENCES negocio(id_negocio),
    id_pago         VARCHAR(10) REFERENCES pago_proveedor(id_pago),
    fecha           DATE DEFAULT CURRENT_DATE,
    hora            TIME DEFAULT CURRENT_TIME,
    monto           NUMERIC(10,2) NOT NULL,
    concepto        VARCHAR(200)
);

CREATE SEQUENCE seq_egreso START 1;
CREATE OR REPLACE FUNCTION generar_id_egreso() RETURNS TRIGGER AS $$
BEGIN
    IF NEW.id_egreso IS NULL THEN
        NEW.id_egreso := 'EG' || LPAD(nextval('seq_egreso')::TEXT, 2, '0');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER trg_egreso BEFORE INSERT ON egreso
FOR EACH ROW EXECUTE FUNCTION generar_id_egreso();


-- CIERRE_CAJA
-- Ojo: monto_esperado no se llena a mano, se calcula sumando las
-- facturas del turno por metodo de pago (eso lo hace la app, no la tabla).
CREATE TABLE cierre_caja (
    id_cierre       VARCHAR(10) PRIMARY KEY,
    id_negocio      VARCHAR(10) NOT NULL REFERENCES negocio(id_negocio),
    id_empleado     VARCHAR(10) NOT NULL REFERENCES empleado(id_empleado),
    fecha_inicio    TIMESTAMP,
    fecha_fin       TIMESTAMP,
    monto_inicial   NUMERIC(10,2) DEFAULT 0,
    notas_apertura  VARCHAR(255),
    total_efectivo  NUMERIC(10,2) DEFAULT 0,
    total_tarjeta   NUMERIC(10,2) DEFAULT 0,
    total_transferencia NUMERIC(10,2) DEFAULT 0,
    monto_esperado  NUMERIC(10,2) DEFAULT 0,
    monto_real      NUMERIC(10,2) DEFAULT 0,
    diferencia      NUMERIC(10,2) DEFAULT 0
);

CREATE SEQUENCE seq_cierre_caja START 1;
CREATE OR REPLACE FUNCTION generar_id_cierre_caja() RETURNS TRIGGER AS $$
BEGIN
    IF NEW.id_cierre IS NULL THEN
        NEW.id_cierre := 'CC' || LPAD(nextval('seq_cierre_caja')::TEXT, 2, '0');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER trg_cierre_caja BEFORE INSERT ON cierre_caja
FOR EACH ROW EXECUTE FUNCTION generar_id_cierre_caja();


-- NOTIFICACION
-- Las alertas automaticas (stock bajo, pagare por vencer, etc.)
-- que le llegan al usuario dueño.
-- NOTA: notas libres de un usuario (recordatorios personales, no ligadas a un
-- negocio). Un usuario puede tener una o varias.
CREATE TABLE nota (
    id_nota             VARCHAR(10) PRIMARY KEY,
    id_usuario          VARCHAR(10) NOT NULL REFERENCES usuario(id_usuario),
    titulo              VARCHAR(150) NOT NULL,
    cuerpo              TEXT,
    fecha_creacion      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE SEQUENCE seq_nota START 1;
CREATE OR REPLACE FUNCTION generar_id_nota() RETURNS TRIGGER AS $$
BEGIN
    IF NEW.id_nota IS NULL THEN
        NEW.id_nota := 'NT' || LPAD(nextval('seq_nota')::TEXT, 2, '0');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER trg_nota BEFORE INSERT ON nota
FOR EACH ROW EXECUTE FUNCTION generar_id_nota();

CREATE TABLE notificacion (
    id_notificacion VARCHAR(10) PRIMARY KEY,
    id_usuario      VARCHAR(10) NOT NULL REFERENCES usuario(id_usuario),
    tipo            VARCHAR(50),
    mensaje         VARCHAR(255),
    fecha_generacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    leido           BOOLEAN DEFAULT FALSE
);

CREATE SEQUENCE seq_notificacion START 1;
CREATE OR REPLACE FUNCTION generar_id_notificacion() RETURNS TRIGGER AS $$
BEGIN
    IF NEW.id_notificacion IS NULL THEN
        NEW.id_notificacion := 'NO' || LPAD(nextval('seq_notificacion')::TEXT, 2, '0');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER trg_notificacion BEFORE INSERT ON notificacion
FOR EACH ROW EXECUTE FUNCTION generar_id_notificacion();


-- =========================================================
-- De aca para abajo van las tablas puente (N:M). Estas no
-- llevan su propio id ni trigger -- su llave primaria es la
-- combinacion de las dos FK que conectan.
-- =========================================================

-- NEGOCIO_MODULO: que modulos tiene activos cada negocio
CREATE TABLE negocio_modulo (
    id_negocio      VARCHAR(10) REFERENCES negocio(id_negocio),
    id_modulo       VARCHAR(10) REFERENCES modulo(id_modulo),
    activo          BOOLEAN DEFAULT TRUE,
    fecha_activacion DATE DEFAULT CURRENT_DATE,
    PRIMARY KEY (id_negocio, id_modulo)
);

-- ROL_PERMISO: que permisos tiene cada rol
CREATE TABLE rol_permiso (
    id_rol          VARCHAR(10) REFERENCES rol(id_rol),
    id_permiso      VARCHAR(10) REFERENCES permiso(id_permiso),
    PRIMARY KEY (id_rol, id_permiso)
);

-- COMPRA_PRODUCTO: el detalle de que productos trajo cada compra
CREATE TABLE compra_producto (
    id_compra       VARCHAR(10) REFERENCES compra(id_compra),
    id_producto     VARCHAR(10) REFERENCES producto(id_producto),
    cantidad        INT NOT NULL,
    costo_unitario  NUMERIC(10,2) NOT NULL,
    subtotal        NUMERIC(10,2) NOT NULL,
    PRIMARY KEY (id_compra, id_producto)
);

-- FACTURA_PRODUCTO: el detalle de que productos se vendieron en cada factura
CREATE TABLE factura_producto (
    id_factura      VARCHAR(10) REFERENCES factura(id_factura),
    id_producto     VARCHAR(10) REFERENCES producto(id_producto),
    cantidad        INT NOT NULL,
    precio_unitario NUMERIC(10,2) NOT NULL,
    valor_iva       NUMERIC(10,2) DEFAULT 0,
    subtotal        NUMERIC(10,2) NOT NULL,
    PRIMARY KEY (id_factura, id_producto)
);
INSERT INTO modulo (nombre_modulo, descripcion) VALUES
('Catálogo', 'Producto, plantilla de negocio y proveedores.'),
('Ventas y Facturación', 'Ventas rápidas, facturación SRI y tabla de ventas.'),
('Finanzas', 'Estadística del negocio y arqueo/cierre de caja.'),
('Recursos Humanos', 'Gestión de empleados y roles / permisos.'),
('Configuración', 'Perfil, notificaciones y módulos activos.');


INSERT INTO tipo_documento (nombre_tipo_documento) VALUES
('Cedula'), ('RUC'), ('Pasaporte');
