package DAO;

import Conexion.Conexion;
import Modelo.MovimientoDiario;
import Modelo.MovimientoHorario;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class EstadisticaDAO {

    public List<MovimientoHorario> listarPorHora(String idNegocio, LocalDate dia) {
        Map<Integer, double[]> montos = new TreeMap<>();
        Map<Integer, Integer> vendidos = new TreeMap<>();
        Date fecha = Date.valueOf(dia);

        String sqlIngresos = "SELECT EXTRACT(HOUR FROM hora)::int AS hr, SUM(monto) AS total "
                + "FROM ingreso WHERE id_negocio = ? AND fecha = ? GROUP BY hr";
        String sqlEgresos = "SELECT EXTRACT(HOUR FROM hora)::int AS hr, SUM(monto) AS total "
                + "FROM egreso WHERE id_negocio = ? AND fecha = ? GROUP BY hr";
        String sqlVendidos = "SELECT EXTRACT(HOUR FROM f.hora)::int AS hr, SUM(fp.cantidad) AS total "
                + "FROM factura f JOIN factura_producto fp ON fp.id_factura = f.id_factura "
                + "WHERE f.id_negocio = ? AND f.fecha = ? GROUP BY hr";

        try (Connection con = Conexion.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(sqlIngresos)) {
                ps.setString(1, idNegocio);
                ps.setDate(2, fecha);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        montos.computeIfAbsent(rs.getInt("hr"), k -> new double[2])[0] = rs.getDouble("total");
                    }
                }
            }
            try (PreparedStatement ps = con.prepareStatement(sqlEgresos)) {
                ps.setString(1, idNegocio);
                ps.setDate(2, fecha);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        montos.computeIfAbsent(rs.getInt("hr"), k -> new double[2])[1] = rs.getDouble("total");
                    }
                }
            }
            try (PreparedStatement ps = con.prepareStatement(sqlVendidos)) {
                ps.setString(1, idNegocio);
                ps.setDate(2, fecha);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        vendidos.put(rs.getInt("hr"), rs.getInt("total"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Map<Integer, double[]> todas = new TreeMap<>(montos);
        for (Integer hr : vendidos.keySet()) {
            todas.putIfAbsent(hr, new double[2]);
        }

        List<MovimientoHorario> lista = new ArrayList<>();
        for (Map.Entry<Integer, double[]> e : todas.entrySet()) {
            int hr = e.getKey();
            lista.add(new MovimientoHorario(hr, e.getValue()[0], e.getValue()[1], vendidos.getOrDefault(hr, 0)));
        }
        return lista;
    }

    public List<MovimientoDiario> listarPorDia(String idNegocio, LocalDate desde, LocalDate hasta) {
        Map<LocalDate, double[]> montos = new TreeMap<>();
        Map<LocalDate, Integer> vendidos = new LinkedHashMap<>();
        Date sqlDesde = Date.valueOf(desde);
        Date sqlHasta = Date.valueOf(hasta);

        String sqlIngresos = "SELECT fecha, SUM(monto) AS total FROM ingreso "
                + "WHERE id_negocio = ? AND fecha BETWEEN ? AND ? GROUP BY fecha";
        String sqlEgresos = "SELECT fecha, SUM(monto) AS total FROM egreso "
                + "WHERE id_negocio = ? AND fecha BETWEEN ? AND ? GROUP BY fecha";
        String sqlVendidos = "SELECT f.fecha, SUM(fp.cantidad) AS total "
                + "FROM factura f JOIN factura_producto fp ON fp.id_factura = f.id_factura "
                + "WHERE f.id_negocio = ? AND f.fecha BETWEEN ? AND ? GROUP BY f.fecha";

        try (Connection con = Conexion.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(sqlIngresos)) {
                ps.setString(1, idNegocio);
                ps.setDate(2, sqlDesde);
                ps.setDate(3, sqlHasta);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        LocalDate f = rs.getDate("fecha").toLocalDate();
                        montos.computeIfAbsent(f, k -> new double[2])[0] = rs.getDouble("total");
                    }
                }
            }
            try (PreparedStatement ps = con.prepareStatement(sqlEgresos)) {
                ps.setString(1, idNegocio);
                ps.setDate(2, sqlDesde);
                ps.setDate(3, sqlHasta);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        LocalDate f = rs.getDate("fecha").toLocalDate();
                        montos.computeIfAbsent(f, k -> new double[2])[1] = rs.getDouble("total");
                    }
                }
            }
            try (PreparedStatement ps = con.prepareStatement(sqlVendidos)) {
                ps.setString(1, idNegocio);
                ps.setDate(2, sqlDesde);
                ps.setDate(3, sqlHasta);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        vendidos.put(rs.getDate("fecha").toLocalDate(), rs.getInt("total"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Map<LocalDate, double[]> todas = new TreeMap<>(montos);
        for (LocalDate f : vendidos.keySet()) {
            todas.putIfAbsent(f, new double[2]);
        }

        List<MovimientoDiario> lista = new ArrayList<>();
        for (Map.Entry<LocalDate, double[]> e : todas.entrySet()) {
            LocalDate f = e.getKey();
            lista.add(new MovimientoDiario(f, e.getValue()[0], e.getValue()[1], vendidos.getOrDefault(f, 0)));
        }
        return lista;
    }

    public String obtenerCategoriaMasVendida(String idNegocio, LocalDate desde, LocalDate hasta) {
        String sql = "SELECT cp.nombre_categoria, SUM(fp.cantidad) AS total "
                + "FROM factura_producto fp "
                + "JOIN factura f ON f.id_factura = fp.id_factura "
                + "JOIN producto p ON p.id_producto = fp.id_producto "
                + "JOIN categoria_producto cp ON cp.id_categoria = p.id_categoria "
                + "WHERE f.id_negocio = ? AND f.fecha BETWEEN ? AND ? "
                + "GROUP BY cp.nombre_categoria ORDER BY total DESC LIMIT 1";
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idNegocio);
            ps.setDate(2, Date.valueOf(desde));
            ps.setDate(3, Date.valueOf(hasta));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString(1) : null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int contarProductosActivos(String idNegocio) {
        String sql = "SELECT COUNT(*) FROM producto WHERE id_negocio = ? AND estado = 'activo'";
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idNegocio);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<Modelo.ProductoVendido> listarMasVendidos(String idNegocio, LocalDate desde, LocalDate hasta, int limite) {
        List<Modelo.ProductoVendido> lista = new ArrayList<>();
        String sql = "SELECT p.nombre_producto, SUM(fp.cantidad) AS total "
                + "FROM factura_producto fp "
                + "JOIN factura f ON f.id_factura = fp.id_factura "
                + "JOIN producto p ON p.id_producto = fp.id_producto "
                + "WHERE f.id_negocio = ? AND f.fecha BETWEEN ? AND ? "
                + "GROUP BY p.nombre_producto ORDER BY total DESC LIMIT ?";
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idNegocio);
            ps.setDate(2, Date.valueOf(desde));
            ps.setDate(3, Date.valueOf(hasta));
            ps.setInt(4, limite);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Modelo.ProductoVendido(rs.getString("nombre_producto"), rs.getInt("total")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Modelo.Producto> listarStockBajo(String idNegocio, int limite) {
        List<Modelo.Producto> lista = new ArrayList<>();
        String sql = "SELECT nombre_producto, stock_actual FROM producto "
                + "WHERE id_negocio = ? AND estado = 'activo' ORDER BY stock_actual ASC LIMIT ?";
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idNegocio);
            ps.setInt(2, limite);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Modelo.Producto p = new Modelo.Producto();
                    p.setNombre(rs.getString("nombre_producto"));
                    p.setCantidad(rs.getInt("stock_actual"));
                    lista.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Modelo.ActividadReciente> listarActividadReciente(String idNegocio, int limite) {
        List<Modelo.ActividadReciente> lista = new ArrayList<>();
        String sql = "SELECT descripcion, monto, fecha, hora FROM ("
                + "SELECT ('Venta ' || f.num_factura) AS descripcion, f.total AS monto, "
                + "f.fecha AS fecha, f.hora AS hora, f.id_factura AS orden "
                + "FROM factura f WHERE f.id_negocio = ? "
                + "UNION ALL "
                + "SELECT ('Egreso: ' || COALESCE(e.concepto, 'Sin concepto')) AS descripcion, "
                + "-e.monto AS monto, e.fecha AS fecha, e.hora AS hora, e.id_egreso AS orden "
                + "FROM egreso e WHERE e.id_negocio = ? "
                + "UNION ALL "
                + "SELECT ('Compra a ' || COALESCE(pr.nombre_proveedor, 'proveedor')) AS descripcion, "
                + "-c.total AS monto, c.fecha_compra AS fecha, CAST('00:00:00' AS time) AS hora, c.id_compra AS orden "
                + "FROM compra c LEFT JOIN proveedor pr ON pr.id_proveedor = c.id_proveedor WHERE c.id_negocio = ? "
                + "UNION ALL "
                + "SELECT 'Nuevo empleado registrado' AS descripcion, 0 AS monto, "
                + "emp.fecha_ingreso AS fecha, CAST('00:00:00' AS time) AS hora, emp.id_empleado AS orden "
                + "FROM empleado emp WHERE emp.id_negocio = ?"
                + ") movimientos ORDER BY fecha DESC, hora DESC, orden DESC LIMIT ?";
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idNegocio);
            ps.setString(2, idNegocio);
            ps.setString(3, idNegocio);
            ps.setString(4, idNegocio);
            ps.setInt(5, limite);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    java.sql.Date fecha = rs.getDate("fecha");
                    java.sql.Time hora = rs.getTime("hora");
                    LocalDateTime fechaHora;
                    if (fecha == null) {
                        fechaHora = LocalDateTime.now();
                    } else {
                        fechaHora = fecha.toLocalDate().atStartOfDay();
                        if (hora != null) {
                            fechaHora = fechaHora.with(hora.toLocalTime());
                        }
                    }
                    lista.add(new Modelo.ActividadReciente(
                            rs.getString("descripcion"),
                            rs.getDouble("monto"),
                            fechaHora));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
