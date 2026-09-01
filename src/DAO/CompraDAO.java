package DAO;

import Conexion.Conexion;
import Modelo.Compra;
import Modelo.DetalleCompra;
import Modelo.Proveedores;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompraDAO {

    private final ProductoDAO productoDAO = new ProductoDAO();

    private String obtenerOCrearMetodoPago(Connection con, String nombre) throws SQLException {
        String buscar = "SELECT id_metodo_pago FROM metodo_pago WHERE nombre_metodo_pago = ?";
        try (PreparedStatement ps = con.prepareStatement(buscar)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        }
        String insertar = "INSERT INTO metodo_pago (nombre_metodo_pago) VALUES (?) RETURNING id_metodo_pago";
        try (PreparedStatement ps = con.prepareStatement(insertar)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString(1) : null;
            }
        }
    }

    private String obtenerOCrearTipoMovimiento(Connection con, String nombre) throws SQLException {
        String buscar = "SELECT id_tipo_movimiento FROM tipo_movimiento WHERE nombre_tipo_movimiento = ?";
        try (PreparedStatement ps = con.prepareStatement(buscar)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        }
        String insertar = "INSERT INTO tipo_movimiento (nombre_tipo_movimiento) VALUES (?) RETURNING id_tipo_movimiento";
        try (PreparedStatement ps = con.prepareStatement(insertar)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString(1) : null;
            }
        }
    }

    public String registrar(String idNegocio, String idProveedor, String numFacturaProveedor,
            double subtotal, double valorIva, double descuento, double total,
            List<DetalleCompra> detalles, boolean pagoContado, String metodoPagoNombre,
            String fechaVencimientoPagare, String nombreProveedorConcepto) {

        String sqlCompra = "INSERT INTO compra (id_negocio, id_proveedor, num_factura_proveedor, "
                + "subtotal, valor_iva, descuento, total) VALUES (?,?,?,?,?,?,?) RETURNING id_compra";
        String sqlDetalle = "INSERT INTO compra_producto (id_compra, id_producto, cantidad, costo_unitario, subtotal) "
                + "VALUES (?,?,?,?,?)";
        String sqlMovimiento = "INSERT INTO movimiento_inventario "
                + "(id_producto, id_tipo_movimiento, id_compra, cantidad, stock_anterior, stock_nuevo) "
                + "VALUES (?,?,?,?,?,?)";
        String sqlPagare = "INSERT INTO pagare (id_compra, monto_total, saldo_pendiente, fecha_vencimiento, estado) "
                + "VALUES (?,?,?,?,'pendiente')";
        String sqlPagareContado = "INSERT INTO pagare (id_compra, monto_total, saldo_pendiente, fecha_vencimiento, estado) "
                + "VALUES (?,?,0,NULL,'pagado') RETURNING id_pagare";
        String sqlPagoProveedor = "INSERT INTO pago_proveedor (id_pagare, id_metodo_pago, monto, fecha_pago) "
                + "VALUES (?,?,?,CURRENT_DATE) RETURNING id_pago";
        String sqlEgreso = "INSERT INTO egreso (id_negocio, id_pago, fecha, monto, concepto) "
                + "VALUES (?,?,CURRENT_DATE,?,?)";

        try (Connection con = Conexion.getConnection()) {
            con.setAutoCommit(false);
            try {
                String idCompra;
                try (PreparedStatement ps = con.prepareStatement(sqlCompra)) {
                    ps.setString(1, idNegocio);
                    ps.setString(2, idProveedor);
                    ps.setString(3, numFacturaProveedor);
                    ps.setDouble(4, subtotal);
                    ps.setDouble(5, valorIva);
                    ps.setDouble(6, descuento);
                    ps.setDouble(7, total);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            con.rollback();
                            return null;
                        }
                        idCompra = rs.getString(1);
                    }
                }

                String idTipoMovEntrada = obtenerOCrearTipoMovimiento(con, "Entrada");
                if (idTipoMovEntrada == null) {
                    con.rollback();
                    return null;
                }

                for (DetalleCompra d : detalles) {
                    String idProducto = productoDAO.obtenerIdProductoPorCodigoBarras(d.getIdProducto());
                    if (idProducto == null) {
                        con.rollback();
                        return null;
                    }
                    int stockAnterior = productoDAO.obtenerStockActual(con, idProducto);
                    if (!productoDAO.aumentarStock(con, idProducto, d.getCantidad())) {
                        con.rollback();
                        return null;
                    }
                    int stockNuevo = stockAnterior + d.getCantidad();

                    try (PreparedStatement ps = con.prepareStatement(sqlDetalle)) {
                        ps.setString(1, idCompra);
                        ps.setString(2, idProducto);
                        ps.setInt(3, d.getCantidad());
                        ps.setDouble(4, d.getCostoUnitario());
                        ps.setDouble(5, d.getSubtotal());
                        ps.executeUpdate();
                    }

                    try (PreparedStatement ps = con.prepareStatement(sqlMovimiento)) {
                        ps.setString(1, idProducto);
                        ps.setString(2, idTipoMovEntrada);
                        ps.setString(3, idCompra);
                        ps.setInt(4, d.getCantidad());
                        ps.setInt(5, stockAnterior);
                        ps.setInt(6, stockNuevo);
                        ps.executeUpdate();
                    }
                }

                if (pagoContado) {
                    String idMetodoPago = obtenerOCrearMetodoPago(con, metodoPagoNombre);
                    if (idMetodoPago == null) {
                        con.rollback();
                        return null;
                    }
                    String idPagare;
                    try (PreparedStatement ps = con.prepareStatement(sqlPagareContado)) {
                        ps.setString(1, idCompra);
                        ps.setDouble(2, total);
                        try (ResultSet rs = ps.executeQuery()) {
                            rs.next();
                            idPagare = rs.getString(1);
                        }
                    }
                    String idPago;
                    try (PreparedStatement ps = con.prepareStatement(sqlPagoProveedor)) {
                        ps.setString(1, idPagare);
                        ps.setString(2, idMetodoPago);
                        ps.setDouble(3, total);
                        try (ResultSet rs = ps.executeQuery()) {
                            rs.next();
                            idPago = rs.getString(1);
                        }
                    }
                    String concepto = "Compra de contado - Factura Prov. " + numFacturaProveedor
                            + " - " + nombreProveedorConcepto + " (" + metodoPagoNombre + ")";
                    try (PreparedStatement ps = con.prepareStatement(sqlEgreso)) {
                        ps.setString(1, idNegocio);
                        ps.setString(2, idPago);
                        ps.setDouble(3, total);
                        ps.setString(4, concepto);
                        ps.executeUpdate();
                    }
                } else {
                    try (PreparedStatement ps = con.prepareStatement(sqlPagare)) {
                        ps.setString(1, idCompra);
                        ps.setDouble(2, total);
                        ps.setDouble(3, total);
                        if (fechaVencimientoPagare == null || fechaVencimientoPagare.trim().isEmpty()) {
                            ps.setNull(4, java.sql.Types.DATE);
                        } else {
                            ps.setDate(4, java.sql.Date.valueOf(fechaVencimientoPagare));
                        }
                        ps.executeUpdate();
                    }
                }

                con.commit();
                return idCompra;
            } catch (SQLException | IllegalArgumentException e) {
                con.rollback();
                e.printStackTrace();
                return null;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Compra> listarPorNegocio(String idNegocio) {
        List<Compra> lista = new ArrayList<>();
        String sql = "SELECT c.id_compra, c.num_factura_proveedor, c.fecha_compra, c.subtotal, c.valor_iva, "
                + "c.descuento, c.total, p.ruc, p.nombre_proveedor, p.apellido_proveedor, p.telefono, p.correo, "
                + "pg.estado AS estado_pagare "
                + "FROM compra c "
                + "JOIN proveedor p ON p.id_proveedor = c.id_proveedor "
                + "LEFT JOIN pagare pg ON pg.id_compra = c.id_compra "
                + "WHERE c.id_negocio = ? ORDER BY c.fecha_compra DESC, c.id_compra DESC";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idNegocio);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Proveedores prov = new Proveedores(
                            rs.getString("ruc"), rs.getString("nombre_proveedor"),
                            rs.getString("apellido_proveedor"), rs.getString("telefono"),
                            rs.getString("correo"), null);
                    String estadoPagare = rs.getString("estado_pagare");
                    Compra c = new Compra(
                            rs.getString("id_compra"),
                            rs.getString("num_factura_proveedor"),
                            rs.getDate("fecha_compra") == null ? "" : rs.getDate("fecha_compra").toString(),
                            prov, new ArrayList<>(),
                            rs.getDouble("subtotal"), rs.getDouble("valor_iva"),
                            rs.getDouble("descuento"), rs.getDouble("total"),
                            "pendiente".equals(estadoPagare) ? "Crédito" : "Contado",
                            estadoPagare
                    );
                    lista.add(c);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}