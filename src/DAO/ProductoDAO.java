package DAO;

import Conexion.Conexion;
import Modelo.Producto;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Modelo.ProductoMasSolicitado;
import Modelo.MovimientoInventario;

public class ProductoDAO {

    public boolean existeCodigoBarras(String codigoBarras) {
        String sql = "SELECT 1 FROM producto WHERE codigo_barras = ?";
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, codigoBarras);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String obtenerIdTasaIva(boolean conIva) {
        double porcentajeBuscado = conIva ? 15.00 : 0.00;
        String sql = "SELECT id_tasa_iva FROM tasa_iva WHERE porcentaje = ? LIMIT 1";
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, porcentajeBuscado);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public double obtenerPorcentajeIvaVigente() {
        String sql = "SELECT porcentaje FROM tasa_iva WHERE porcentaje > 0 ORDER BY porcentaje DESC LIMIT 1";
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1) / 100.0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.15;
    }

    public List<Object[]> listarStockBajoParaNotificar(String idNegocio) {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT id_producto, nombre_producto, stock_actual, stock_minimo FROM producto "
                + "WHERE id_negocio = ? AND estado = 'activo' AND stock_actual <= stock_minimo";
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idNegocio);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Object[]{rs.getString("id_producto"), rs.getString("nombre_producto"),
                        rs.getInt("stock_actual"), rs.getInt("stock_minimo")});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean registrar(Producto p) {
        String sql = "INSERT INTO producto "
                + "(id_negocio, id_categoria, id_tasa_iva, nombre_producto, codigo_barras, "
                + "precio_venta, costo, stock_actual, stock_minimo, fecha_vencimiento, ubicacion_pasillo, "
                + "fecha_elaboracion, lote) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getIdNegocio());
            ps.setString(2, p.getIdCategoria());
            ps.setString(3, p.getIdTasaIva());
            ps.setString(4, p.getNombre());
            ps.setString(5, p.getCodigo());
            ps.setDouble(6, p.getPrecioUnitario());
            ps.setDouble(7, p.getPrecioUnitario());
            ps.setInt(8, p.getCantidad());
            ps.setInt(9, p.getStockMinimo());
            String fechaVencimiento = p.getFechaVencimiento();
            if (fechaVencimiento == null || fechaVencimiento.trim().isEmpty()) {
                ps.setNull(10, java.sql.Types.DATE);
            } else {
                ps.setDate(10, Date.valueOf(fechaVencimiento.trim()));
            }
            ps.setString(11, p.getUbicacionPasillo());
            String fechaElaboracion = p.getFechaElaboracion();
            if (fechaElaboracion == null || fechaElaboracion.trim().isEmpty()) {
                ps.setNull(12, java.sql.Types.DATE);
            } else {
                ps.setDate(12, Date.valueOf(fechaElaboracion.trim()));
            }
            ps.setString(13, p.getLote());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Producto> listarPorNegocio(String idNegocio) {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT p.codigo_barras, p.nombre_producto, cp.nombre_categoria, "
                + "p.stock_actual, p.stock_minimo, p.precio_venta, ti.porcentaje, p.fecha_vencimiento, "
                + "p.fecha_elaboracion, p.ubicacion_pasillo, p.lote "
                + "FROM producto p "
                + "JOIN categoria_producto cp ON cp.id_categoria = p.id_categoria "
                + "JOIN tasa_iva ti ON ti.id_tasa_iva = p.id_tasa_iva "
                + "WHERE p.id_negocio = ? AND p.estado = 'activo' "
                + "ORDER BY p.nombre_producto";
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idNegocio);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Producto p = new Producto();
                    p.setCodigo(rs.getString("codigo_barras"));
                    p.setNombre(rs.getString("nombre_producto"));
                    p.setCategoria(rs.getString("nombre_categoria"));
                    p.setCantidad(rs.getInt("stock_actual"));
                    p.setStockMinimo(rs.getInt("stock_minimo"));
                    p.setPrecioUnitario(rs.getDouble("precio_venta"));
                    p.setTieneIva(rs.getDouble("porcentaje") > 0);
                    Date elaboracion = rs.getDate("fecha_elaboracion");
                    p.setFechaElaboracion(elaboracion == null ? "" : elaboracion.toString());
                    Date vencimiento = rs.getDate("fecha_vencimiento");
                    p.setFechaVencimiento(vencimiento == null ? "" : vencimiento.toString());
                    p.setUbicacionPasillo(rs.getString("ubicacion_pasillo"));
                    p.setLote(rs.getString("lote"));
                    lista.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean actualizar(Producto p) {
        String sql = "UPDATE producto SET nombre_producto = ?, id_categoria = ?, "
                + "stock_actual = ?, precio_venta = ?, costo = ?, id_tasa_iva = ?, stock_minimo = ? "
                + "WHERE codigo_barras = ? AND id_negocio = ?";
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getIdCategoria());
            ps.setInt(3, p.getCantidad());
            ps.setDouble(4, p.getPrecioUnitario());
            ps.setDouble(5, p.getPrecioUnitario());
            ps.setString(6, p.getIdTasaIva());
            ps.setInt(7, p.getStockMinimo());
            ps.setString(8, p.getCodigo());
            ps.setString(9, p.getIdNegocio());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String obtenerIdProductoPorCodigoBarras(String codigoBarras) {
        String sql = "SELECT id_producto FROM producto WHERE codigo_barras = ?";
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, codigoBarras);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString(1) : null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean descontarStock(Connection con, String idProducto, int cantidad) throws SQLException {
        String sql = "UPDATE producto SET stock_actual = stock_actual - ? WHERE id_producto = ? AND stock_actual >= ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cantidad);
            ps.setString(2, idProducto);
            ps.setInt(3, cantidad);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(String idNegocio, String codigoBarras) {
    String sql = "UPDATE producto SET estado = 'inactivo' WHERE codigo_barras = ? AND id_negocio = ?";
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, codigoBarras);
            ps.setString(2, idNegocio);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            if ("23503".equals(e.getSQLState())) {
                return false;
            }
            e.printStackTrace();
            return false;
        }
    }

    public int obtenerStockActual(Connection con, String idProducto) throws SQLException {
        String sql = "SELECT stock_actual FROM producto WHERE id_producto = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idProducto);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public boolean aumentarStock(Connection con, String idProducto, int cantidad) throws SQLException {
        String sql = "UPDATE producto SET stock_actual = stock_actual + ? WHERE id_producto = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cantidad);
            ps.setString(2, idProducto);
            return ps.executeUpdate() > 0;
        }
    }

    private Producto mapearProductoBodega(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setCodigo(rs.getString("codigo_barras"));
        p.setNombre(rs.getString("nombre_producto"));
        p.setCategoria(rs.getString("nombre_categoria"));
        p.setCantidad(rs.getInt("stock_actual"));
        p.setStockMinimo(rs.getInt("stock_minimo"));
        p.setPrecioUnitario(rs.getDouble("precio_venta"));
        p.setTieneIva(rs.getDouble("porcentaje") > 0);
        Date elaboracion = rs.getDate("fecha_elaboracion");
        p.setFechaElaboracion(elaboracion == null ? "" : elaboracion.toString());
        Date vencimiento = rs.getDate("fecha_vencimiento");
        p.setFechaVencimiento(vencimiento == null ? "" : vencimiento.toString());
        p.setUbicacionPasillo(rs.getString("ubicacion_pasillo"));
        p.setLote(rs.getString("lote"));
        p.setStockMaximo(rs.getInt("stock_maximo"));
        return p;
    }

    public Producto obtenerPorCodigo(String idNegocio, String codigoBarras) {
        String sql = "SELECT p.codigo_barras, p.nombre_producto, cp.nombre_categoria, "
                + "p.stock_actual, p.stock_minimo, p.precio_venta, ti.porcentaje, p.fecha_vencimiento, "
                + "p.fecha_elaboracion, p.ubicacion_pasillo, p.lote, p.stock_maximo "
                + "FROM producto p "
                + "JOIN categoria_producto cp ON cp.id_categoria = p.id_categoria "
                + "JOIN tasa_iva ti ON ti.id_tasa_iva = p.id_tasa_iva "
                + "WHERE p.id_negocio = ? AND p.codigo_barras = ? AND p.estado = 'activo'";
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idNegocio);
            ps.setString(2, codigoBarras);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapearProductoBodega(rs) : null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Producto> buscarParaBodega(String idNegocio, String texto, int limite) {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT p.codigo_barras, p.nombre_producto, cp.nombre_categoria, "
                + "p.stock_actual, p.stock_minimo, p.precio_venta, ti.porcentaje, p.fecha_vencimiento, "
                + "p.fecha_elaboracion, p.ubicacion_pasillo, p.lote, p.stock_maximo "
                + "FROM producto p "
                + "JOIN categoria_producto cp ON cp.id_categoria = p.id_categoria "
                + "JOIN tasa_iva ti ON ti.id_tasa_iva = p.id_tasa_iva "
                + "WHERE p.id_negocio = ? AND p.estado = 'activo' "
                + "AND (p.codigo_barras ILIKE ? OR p.nombre_producto ILIKE ?) "
                + "ORDER BY p.nombre_producto LIMIT ?";
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            String comodin = "%" + texto.trim() + "%";
            ps.setString(1, idNegocio);
            ps.setString(2, comodin);
            ps.setString(3, comodin);
            ps.setInt(4, limite);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearProductoBodega(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Producto> listarBajoStock(String idNegocio, int limite) {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT p.codigo_barras, p.nombre_producto, cp.nombre_categoria, "
                + "p.stock_actual, p.stock_minimo, p.precio_venta, ti.porcentaje, p.fecha_vencimiento, "
                + "p.fecha_elaboracion, p.ubicacion_pasillo, p.lote, p.stock_maximo "
                + "FROM producto p "
                + "JOIN categoria_producto cp ON cp.id_categoria = p.id_categoria "
                + "JOIN tasa_iva ti ON ti.id_tasa_iva = p.id_tasa_iva "
                + "WHERE p.id_negocio = ? AND p.estado = 'activo' AND p.stock_actual <= p.stock_minimo "
                + "ORDER BY p.stock_actual ASC LIMIT ?";
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idNegocio);
            ps.setInt(2, limite);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearProductoBodega(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<ProductoMasSolicitado> listarMasSolicitados(String idNegocio, int limite) {
        List<ProductoMasSolicitado> lista = new ArrayList<>();
        String sql = "SELECT p.nombre_producto, SUM(fp.cantidad) AS total_despachado "
                + "FROM factura_producto fp "
                + "JOIN factura f ON f.id_factura = fp.id_factura "
                + "JOIN producto p ON p.id_producto = fp.id_producto "
                + "WHERE f.id_negocio = ? "
                + "GROUP BY p.nombre_producto "
                + "ORDER BY total_despachado DESC LIMIT ?";
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idNegocio);
            ps.setInt(2, limite);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ProductoMasSolicitado p = new ProductoMasSolicitado(
                            rs.getString("nombre_producto"),
                            rs.getInt("total_despachado")
                    );
                    lista.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean sumarStock(String idNegocio, String codigoBarras, int cantidad) {
        String sql = "UPDATE producto SET stock_actual = stock_actual + ? "
                + "WHERE codigo_barras = ? AND id_negocio = ? AND estado = 'activo'";
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cantidad);
            ps.setString(2, codigoBarras);
            ps.setString(3, idNegocio);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean descontarStockPorCodigo(String idNegocio, String codigoBarras, int cantidad) {
        String sql = "UPDATE producto SET stock_actual = stock_actual - ? "
                + "WHERE codigo_barras = ? AND id_negocio = ? AND estado = 'activo' AND stock_actual >= ?";
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cantidad);
            ps.setString(2, codigoBarras);
            ps.setString(3, idNegocio);
            ps.setInt(4, cantidad);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarInventarioBodega(String idNegocio, String codigoBarras,
            String ubicacionPasillo, String lote, int stockMinimo, int stockMaximo, int stockActual) {
        String sql = "UPDATE producto SET ubicacion_pasillo = ?, lote = ?, "
                + "stock_minimo = ?, stock_maximo = ?, stock_actual = ? "
                + "WHERE codigo_barras = ? AND id_negocio = ? AND estado = 'activo'";
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ubicacionPasillo);
            ps.setString(2, lote);
            ps.setInt(3, stockMinimo);
            ps.setInt(4, stockMaximo);
            ps.setInt(5, stockActual);
            ps.setString(6, codigoBarras);
            ps.setString(7, idNegocio);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<MovimientoInventario> listarMovimientosRecientes(String idNegocio, int limite) {
        List<MovimientoInventario> lista = new ArrayList<>();
        String sql = "SELECT tipo, nombre_producto, cantidad, fecha, hora FROM ("
                + "SELECT 'Entrada' AS tipo, p.nombre_producto, cp.cantidad, "
                + "c.fecha_compra AS fecha, NULL::time AS hora, c.id_compra AS orden "
                + "FROM compra_producto cp "
                + "JOIN compra c ON c.id_compra = cp.id_compra "
                + "JOIN producto p ON p.id_producto = cp.id_producto "
                + "WHERE c.id_negocio = ? "
                + "UNION ALL "
                + "SELECT 'Salida' AS tipo, p.nombre_producto, fp.cantidad, "
                + "f.fecha AS fecha, f.hora AS hora, f.id_factura AS orden "
                + "FROM factura_producto fp "
                + "JOIN factura f ON f.id_factura = fp.id_factura "
                + "JOIN producto p ON p.id_producto = fp.id_producto "
                + "WHERE f.id_negocio = ?"
                + ") movimientos ORDER BY fecha DESC, hora DESC NULLS LAST, orden DESC LIMIT ?";
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idNegocio);
            ps.setString(2, idNegocio);
            ps.setInt(3, limite);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new MovimientoInventario(
                            rs.getString("tipo"),
                            rs.getString("nombre_producto"),
                            rs.getInt("cantidad"),
                            rs.getDate("fecha"),
                            rs.getTime("hora")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

}
