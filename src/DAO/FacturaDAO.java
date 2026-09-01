package DAO;

import Conexion.Conexion;
import Modelo.Cliente;
import Modelo.DetalleFactura;
import Modelo.Factura;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FacturaDAO {

    private static final String DOCUMENTO_CONSUMIDOR_FINAL = "CONSFINAL";

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final ProductoDAO productoDAO = new ProductoDAO();

    public String obtenerOCrearClienteConsumidorFinal() {
        String idExistente = clienteDAO.obtenerIdPorDocumento(DOCUMENTO_CONSUMIDOR_FINAL);
        if (idExistente != null) {
            return idExistente;
        }
        Cliente cf = new Cliente("Cedula", DOCUMENTO_CONSUMIDOR_FINAL, "Consumidor Final", "", "");
        clienteDAO.registrar(cf);
        return clienteDAO.obtenerIdPorDocumento(DOCUMENTO_CONSUMIDOR_FINAL);
    }

    public String obtenerIdEmpleadoCajero(String idNegocio) {
        String sql = "SELECT id_empleado FROM empleado WHERE id_negocio = ? ORDER BY id_empleado LIMIT 1";
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idNegocio);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString(1) : null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

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
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        }
        return null;
    }

    public String registrar(Factura factura) {

        String sqlFactura = "INSERT INTO factura (id_negocio, id_cliente, id_empleado, id_metodo_pago, "
                + "num_factura, subtotal, valor_iva, descuento, total, estado_sri) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?) RETURNING id_factura";
        String sqlDetalle = "INSERT INTO factura_producto (id_factura, id_producto, cantidad, precio_unitario, valor_iva, subtotal) "
                + "VALUES (?,?,?,?,?,?)";

        try (Connection con = Conexion.getConnection()) {
            con.setAutoCommit(false);
            try {
                String idMetodoPago = obtenerOCrearMetodoPago(con, factura.getMetodoPago());
                if (idMetodoPago == null) {
                    con.rollback();
                    return null;
                }

                String idFactura;
                try (PreparedStatement ps = con.prepareStatement(sqlFactura)) {
                    ps.setString(1, factura.getIdNegocio());
                    ps.setString(2, factura.getCliente().getIdCliente());
                    ps.setString(3, factura.getIdEmpleado());
                    ps.setString(4, idMetodoPago);
                    ps.setString(5, factura.getNumFactura());
                    ps.setDouble(6, factura.getSubtotal());
                    ps.setDouble(7, factura.getValorIva());
                    ps.setDouble(8, factura.getDescuento());
                    ps.setDouble(9, factura.getTotal());
                    ps.setString(10, "Pendiente");
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            con.rollback();
                            return null;
                        }
                        idFactura = rs.getString(1);
                    }
                }
                String sqlIngreso = "INSERT INTO ingreso (id_negocio, id_factura, fecha, monto, concepto) "
                        + "VALUES (?, ?, CURRENT_DATE, ?, ?)";
                try (PreparedStatement ps = con.prepareStatement(sqlIngreso)) {
                    ps.setString(1, factura.getIdNegocio());
                    ps.setString(2, idFactura);
                    ps.setDouble(3, factura.getTotal());
                    ps.setString(4, "Venta factura " + factura.getNumFactura());
                    ps.executeUpdate();
                }
                for (DetalleFactura d : factura.getDetalles()) {
                    String idProducto = productoDAO.obtenerIdProductoPorCodigoBarras(d.getIdProducto());
                    if (idProducto == null || !productoDAO.descontarStock(con, idProducto, d.getCantidad())) {
                        con.rollback();
                        return null;
                    }
                    try (PreparedStatement ps = con.prepareStatement(sqlDetalle)) {
                        ps.setString(1, idFactura);
                        ps.setString(2, idProducto);
                        ps.setInt(3, d.getCantidad());
                        ps.setDouble(4, d.getPrecioUnitario());
                        ps.setDouble(5, d.getValorIva());
                        ps.setDouble(6, d.getSubtotal());
                        ps.executeUpdate();
                    }
                }

                con.commit();
                return idFactura;
            } catch (SQLException e) {
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
public List<DetalleFactura> obtenerDetallePorFactura(String idFactura) {
    List<DetalleFactura> lista = new ArrayList<>();
    String sql = "SELECT p.nombre_producto, fp.cantidad, fp.precio_unitario, fp.valor_iva, fp.subtotal "
            + "FROM factura_producto fp JOIN producto p ON p.id_producto = fp.id_producto "
            + "WHERE fp.id_factura = ?";
    try (Connection con = Conexion.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, idFactura);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new DetalleFactura(null, rs.getString("nombre_producto"),
                        rs.getInt("cantidad"), rs.getDouble("precio_unitario"),
                        rs.getDouble("subtotal"), rs.getDouble("valor_iva")));
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return lista;
}
    public List<Factura> listarPorEmpleadoEnTurno(String idEmpleado, String idNegocio) {
        List<Factura> lista = new ArrayList<>();
        String sql = "SELECT f.id_factura, f.num_factura, f.fecha, f.subtotal, f.valor_iva, f.descuento, f.total, "
                + "f.estado_sri, mp.nombre_metodo_pago, "
                + "c.id_cliente, c.numero_documento, c.nombre_cliente, c.telefono, c.correo, "
                + "u.nombres, u.apellidos "
                + "FROM factura f "
                + "JOIN metodo_pago mp ON mp.id_metodo_pago = f.id_metodo_pago "
                + "JOIN cliente c ON c.id_cliente = f.id_cliente "
                + "JOIN usuario u ON u.id_usuario = f.id_empleado "
                + "JOIN cierre_caja cc ON cc.id_cierre = ("
                + "SELECT cc2.id_cierre FROM cierre_caja cc2 "
                + "WHERE cc2.id_empleado = ? AND cc2.id_negocio = ? "
                + "ORDER BY cc2.fecha_inicio DESC LIMIT 1) "
                + "WHERE f.id_negocio = ? AND f.id_empleado = ? "
                + "AND (f.fecha + f.hora) >= cc.fecha_inicio "
                + "AND (cc.fecha_fin IS NULL OR (f.fecha + f.hora) <= cc.fecha_fin) "
                + "ORDER BY f.fecha DESC, f.hora DESC, f.id_factura DESC";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idEmpleado);
            ps.setString(2, idNegocio);
            ps.setString(3, idNegocio);
            ps.setString(4, idEmpleado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cliente cliente = new Cliente("Cedula", rs.getString("numero_documento"),
                            rs.getString("nombre_cliente"), rs.getString("telefono"), rs.getString("correo"));
                    cliente.setIdCliente(rs.getString("id_cliente"));
                    Factura f = new Factura(
                            rs.getString("id_factura"),
                            rs.getString("num_factura"),
                            rs.getDate("fecha") == null ? "" : rs.getDate("fecha").toString(),
                            cliente,
                            rs.getString("nombre_metodo_pago"),
                            new ArrayList<>(),
                            rs.getDouble("subtotal"),
                            rs.getDouble("valor_iva"),
                            rs.getDouble("descuento"),
                            rs.getDouble("total"),
                            rs.getString("estado_sri"));
                    f.setNombreEmpleado(rs.getString("nombres") + " " + rs.getString("apellidos"));
                    lista.add(f);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Factura> listarPorNegocio(String idNegocio) {
        List<Factura> lista = new ArrayList<>();
       String sql = "SELECT f.id_factura, f.num_factura, f.fecha, f.subtotal, f.valor_iva, f.descuento, f.total, "
        + "f.estado_sri, mp.nombre_metodo_pago, "
        + "c.id_cliente, c.numero_documento, c.nombre_cliente, c.telefono, c.correo, "
        + "u.nombres, u.apellidos "
        + "FROM factura f "
        + "JOIN metodo_pago mp ON mp.id_metodo_pago = f.id_metodo_pago "
        + "JOIN cliente c ON c.id_cliente = f.id_cliente "
        + "JOIN usuario u ON u.id_usuario = f.id_empleado "
        + "WHERE f.id_negocio = ? ORDER BY f.fecha DESC, f.id_factura DESC";
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idNegocio);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cliente cliente = new Cliente("Cedula", rs.getString("numero_documento"),
                            rs.getString("nombre_cliente"), rs.getString("telefono"), rs.getString("correo"));
                    cliente.setIdCliente(rs.getString("id_cliente"));

                    Factura f = new Factura(
                            rs.getString("id_factura"),
                            rs.getString("num_factura"),
                            rs.getDate("fecha") == null ? "" : rs.getDate("fecha").toString(),
                            cliente,
                            rs.getString("nombre_metodo_pago"),
                            new ArrayList<>(),
                            rs.getDouble("subtotal"),
                            rs.getDouble("valor_iva"),
                            rs.getDouble("descuento"),
                            rs.getDouble("total"),
                            rs.getString("estado_sri")
                    );
                    f.setNombreEmpleado(rs.getString("nombres") + " " + rs.getString("apellidos"));
                    lista.add(f);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean eliminar(String idNegocio, String idFactura) {
        String sqlVerificarNegocio = "SELECT 1 FROM factura WHERE id_factura = ? AND id_negocio = ?";
        String sqlDetalle = "SELECT id_producto, cantidad FROM factura_producto WHERE id_factura = ?";
        String sqlReponerStock = "UPDATE producto SET stock_actual = stock_actual + ? WHERE id_producto = ?";
        String sqlBorrarIngresos = "DELETE FROM ingreso WHERE id_factura = ?";
        String sqlBorrarMovimientos = "DELETE FROM movimiento_inventario WHERE id_factura = ?";
        String sqlBorrarDetalle = "DELETE FROM factura_producto WHERE id_factura = ?";
        String sqlBorrarFactura = "DELETE FROM factura WHERE id_factura = ?";

        try (Connection con = Conexion.getConnection()) {
            con.setAutoCommit(false);
            try {
                try (PreparedStatement ps = con.prepareStatement(sqlVerificarNegocio)) {
                    ps.setString(1, idFactura);
                    ps.setString(2, idNegocio);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            con.rollback();
                            return false;
                        }
                    }
                }

                try (PreparedStatement ps = con.prepareStatement(sqlDetalle)) {
                    ps.setString(1, idFactura);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            String idProducto = rs.getString("id_producto");
                            int cantidad = rs.getInt("cantidad");
                            try (PreparedStatement psStock = con.prepareStatement(sqlReponerStock)) {
                                psStock.setInt(1, cantidad);
                                psStock.setString(2, idProducto);
                                psStock.executeUpdate();
                            }
                        }
                    }
                }

                try (PreparedStatement ps = con.prepareStatement(sqlBorrarIngresos)) {
                    ps.setString(1, idFactura);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = con.prepareStatement(sqlBorrarMovimientos)) {
                    ps.setString(1, idFactura);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = con.prepareStatement(sqlBorrarDetalle)) {
                    ps.setString(1, idFactura);
                    ps.executeUpdate();
                }
                int filas;
                try (PreparedStatement ps = con.prepareStatement(sqlBorrarFactura)) {
                    ps.setString(1, idFactura);
                    filas = ps.executeUpdate();
                }
                con.commit();
                return filas > 0;
            } catch (SQLException e) {
                con.rollback();
                e.printStackTrace();
                return false;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Object[]> listarSriPendientesParaNotificar(String idNegocio) {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT id_factura, num_factura, fecha, total FROM factura "
                + "WHERE id_negocio = ? AND estado_sri = 'Pendiente' "
                + "AND fecha <= CURRENT_DATE - INTERVAL '1 day'";
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idNegocio);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Object[]{rs.getString("id_factura"), rs.getString("num_factura"),
                        rs.getDate("fecha").toString(), rs.getDouble("total")});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
