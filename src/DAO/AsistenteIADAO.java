package DAO;

import Conexion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AsistenteIADAO {

    public String obtenerResumenInventario(String idNegocio) {
        String sql = "SELECT "
                + "COUNT(*) AS productos, "
                + "COALESCE(SUM(stock_actual),0) AS unidades, "
                + "COALESCE(SUM(stock_actual * costo),0) AS valor_costo, "
                + "COALESCE(SUM(stock_actual * precio_venta),0) AS valor_venta "
                + "FROM producto "
                + "WHERE id_negocio = ? AND estado = 'activo'";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, idNegocio);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return "Productos activos: " + rs.getInt("productos")
                            + "\nUnidades totales en inventario: " + rs.getInt("unidades")
                            + "\nValor del inventario al costo: $" + String.format("%.2f", rs.getDouble("valor_costo"))
                            + "\nValor potencial de venta del inventario: $" + String.format("%.2f", rs.getDouble("valor_venta"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "No se pudo obtener el resumen del inventario.";
    }

    public List<String> listarProductosDetallados(String idNegocio) {
        List<String> lista = new ArrayList<>();

        String sql = "SELECT p.nombre_producto, p.codigo_barras, "
                + "cp.nombre_categoria, p.stock_actual, p.stock_minimo, "
                + "p.stock_maximo, p.costo, p.precio_venta, "
                + "p.fecha_elaboracion, p.fecha_vencimiento, "
                + "p.ubicacion_pasillo, p.lote "
                + "FROM producto p "
                + "JOIN categoria_producto cp ON cp.id_categoria = p.id_categoria "
                + "WHERE p.id_negocio = ? AND p.estado = 'activo' "
                + "ORDER BY p.nombre_producto";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, idNegocio);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    StringBuilder producto = new StringBuilder();

                    producto.append("Producto: ")
                            .append(rs.getString("nombre_producto"))
                            .append(" | codigo: ")
                            .append(rs.getString("codigo_barras"))
                            .append(" | categoria: ")
                            .append(rs.getString("nombre_categoria"))
                            .append(" | stock actual: ")
                            .append(rs.getInt("stock_actual"))
                            .append(" | stock minimo: ")
                            .append(rs.getInt("stock_minimo"))
                            .append(" | stock maximo: ")
                            .append(rs.getInt("stock_maximo"))
                            .append(" | costo: $")
                            .append(String.format("%.2f", rs.getDouble("costo")))
                            .append(" | precio venta: $")
                            .append(String.format("%.2f", rs.getDouble("precio_venta")));

                    if (rs.getDate("fecha_elaboracion") != null) {
                        producto.append(" | fecha elaboracion: ")
                                .append(rs.getDate("fecha_elaboracion"));
                    }

                    if (rs.getDate("fecha_vencimiento") != null) {
                        producto.append(" | fecha vencimiento: ")
                                .append(rs.getDate("fecha_vencimiento"));
                    }

                    if (rs.getString("ubicacion_pasillo") != null) {
                        producto.append(" | ubicacion: ")
                                .append(rs.getString("ubicacion_pasillo"));
                    }

                    if (rs.getString("lote") != null) {
                        producto.append(" | lote: ")
                                .append(rs.getString("lote"));
                    }

                    lista.add(producto.toString());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public String obtenerResumenCompras(String idNegocio) {
        String sql = "SELECT "
                + "COUNT(DISTINCT c.id_compra) AS compras, "
                + "COALESCE(SUM(cp.cantidad),0) AS unidades_compradas, "
                + "COALESCE(SUM(cp.subtotal),0) AS subtotal_productos, "
                + "COALESCE(SUM(c.total),0) AS total_compras "
                + "FROM compra c "
                + "JOIN compra_producto cp ON cp.id_compra = c.id_compra "
                + "WHERE c.id_negocio = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, idNegocio);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return "Numero de compras: " + rs.getInt("compras")
                            + "\nUnidades de productos compradas: " + rs.getInt("unidades_compradas")
                            + "\nSubtotal de productos comprados: $" + String.format("%.2f", rs.getDouble("subtotal_productos"))
                            + "\nTotal gastado en compras: $" + String.format("%.2f", rs.getDouble("total_compras"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "No se pudo obtener el resumen de compras.";
    }

    public List<String> listarComprasDetalladas(String idNegocio) {
        List<String> lista = new ArrayList<>();

        String sql = "SELECT c.fecha_compra, c.num_factura_proveedor, "
                + "TRIM(COALESCE(p.nombre_proveedor,'') || ' ' || COALESCE(p.apellido_proveedor,'')) AS proveedor, "
                + "pr.nombre_producto, cp.cantidad, cp.costo_unitario, cp.subtotal, c.total "
                + "FROM compra c "
                + "JOIN proveedor p ON p.id_proveedor = c.id_proveedor "
                + "JOIN compra_producto cp ON cp.id_compra = c.id_compra "
                + "JOIN producto pr ON pr.id_producto = cp.id_producto "
                + "WHERE c.id_negocio = ? "
                + "ORDER BY c.fecha_compra DESC, c.id_compra DESC";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, idNegocio);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(
                            "Fecha: " + rs.getDate("fecha_compra")
                            + " | proveedor: " + rs.getString("proveedor")
                            + " | factura proveedor: " + rs.getString("num_factura_proveedor")
                            + " | producto: " + rs.getString("nombre_producto")
                            + " | cantidad comprada: " + rs.getInt("cantidad")
                            + " | costo unitario: $" + String.format("%.2f", rs.getDouble("costo_unitario"))
                            + " | subtotal: $" + String.format("%.2f", rs.getDouble("subtotal"))
                            + " | total compra: $" + String.format("%.2f", rs.getDouble("total"))
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public String obtenerResumenVentas(String idNegocio) {
        String sql = "SELECT "
                + "COUNT(DISTINCT f.id_factura) AS facturas, "
                + "COALESCE(SUM(fp.cantidad),0) AS unidades_vendidas, "
                + "COALESCE(SUM(fp.subtotal),0) AS subtotal_ventas, "
                + "COALESCE(SUM(f.total),0) AS total_ventas "
                + "FROM factura f "
                + "JOIN factura_producto fp ON fp.id_factura = f.id_factura "
                + "WHERE f.id_negocio = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, idNegocio);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return "Facturas emitidas: " + rs.getInt("facturas")
                            + "\nUnidades vendidas: " + rs.getInt("unidades_vendidas")
                            + "\nSubtotal de ventas: $" + String.format("%.2f", rs.getDouble("subtotal_ventas"))
                            + "\nTotal vendido: $" + String.format("%.2f", rs.getDouble("total_ventas"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "No se pudo obtener el resumen de ventas.";
    }

    public List<String> listarVentasDetalladas(String idNegocio) {
        List<String> lista = new ArrayList<>();

        String sql = "SELECT f.fecha, f.num_factura, "
                + "p.nombre_producto, fp.cantidad, fp.precio_unitario, "
                + "fp.subtotal, f.total "
                + "FROM factura f "
                + "JOIN factura_producto fp ON fp.id_factura = f.id_factura "
                + "JOIN producto p ON p.id_producto = fp.id_producto "
                + "WHERE f.id_negocio = ? "
                + "ORDER BY f.fecha DESC, f.id_factura DESC";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, idNegocio);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(
                            "Fecha: " + rs.getDate("fecha")
                            + " | factura: " + rs.getString("num_factura")
                            + " | producto: " + rs.getString("nombre_producto")
                            + " | cantidad vendida: " + rs.getInt("cantidad")
                            + " | precio unitario: $" + String.format("%.2f", rs.getDouble("precio_unitario"))
                            + " | subtotal: $" + String.format("%.2f", rs.getDouble("subtotal"))
                            + " | total factura: $" + String.format("%.2f", rs.getDouble("total"))
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public String obtenerResumenFinanciero(String idNegocio) {
        String sql = "SELECT "
                + "(SELECT COALESCE(SUM(monto),0) FROM ingreso WHERE id_negocio = ?) AS ingresos, "
                + "(SELECT COALESCE(SUM(monto),0) FROM egreso WHERE id_negocio = ?) AS egresos";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, idNegocio);
            ps.setString(2, idNegocio);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double ingresos = rs.getDouble("ingresos");
                    double egresos = rs.getDouble("egresos");
                    double balance = ingresos - egresos;

                    return "Dinero ingresado por ventas: $" + String.format("%.2f", ingresos)
                            + "\nDinero salido por egresos: $" + String.format("%.2f", egresos)
                            + "\nBalance de ingresos menos egresos: $" + String.format("%.2f", balance);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "No se pudo obtener el resumen financiero.";
    }

    public List<String> listarMovimientosInventario(String idNegocio) {
        List<String> lista = new ArrayList<>();

        String sql = "SELECT mi.fecha, p.nombre_producto, "
                + "tm.nombre_tipo_movimiento, mi.cantidad, "
                + "mi.stock_anterior, mi.stock_nuevo, "
                + "c.num_factura_proveedor, f.num_factura "
                + "FROM movimiento_inventario mi "
                + "JOIN producto p ON p.id_producto = mi.id_producto "
                + "JOIN tipo_movimiento tm ON tm.id_tipo_movimiento = mi.id_tipo_movimiento "
                + "LEFT JOIN compra c ON c.id_compra = mi.id_compra "
                + "LEFT JOIN factura f ON f.id_factura = mi.id_factura "
                + "WHERE p.id_negocio = ? "
                + "ORDER BY mi.fecha DESC, mi.id_movimiento DESC";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, idNegocio);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String origen = rs.getString("num_factura_proveedor");

                    if (origen == null) {
                        origen = rs.getString("num_factura");
                    }

                    lista.add(
                            "Fecha: " + rs.getDate("fecha")
                            + " | producto: " + rs.getString("nombre_producto")
                            + " | movimiento: " + rs.getString("nombre_tipo_movimiento")
                            + " | cantidad: " + rs.getInt("cantidad")
                            + " | stock anterior: " + rs.getInt("stock_anterior")
                            + " | stock nuevo: " + rs.getInt("stock_nuevo")
                            + " | documento origen: " + origen
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public String obtenerProductosMasVendidos(String idNegocio) {
        String sql = "SELECT p.nombre_producto, SUM(fp.cantidad) AS cantidad "
                + "FROM factura_producto fp "
                + "JOIN factura f ON f.id_factura = fp.id_factura "
                + "JOIN producto p ON p.id_producto = fp.id_producto "
                + "WHERE f.id_negocio = ? "
                + "GROUP BY p.nombre_producto "
                + "ORDER BY cantidad DESC "
                + "LIMIT 10";

        StringBuilder sb = new StringBuilder();

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, idNegocio);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    sb.append(rs.getString("nombre_producto"))
                            .append(": ")
                            .append(rs.getInt("cantidad"))
                            .append(" unidades vendidas\n");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sb.length() == 0 ? "No hay productos vendidos todavía." : sb.toString();
    }
}
