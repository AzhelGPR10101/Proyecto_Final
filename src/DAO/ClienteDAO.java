package DAO;

import Conexion.Conexion;
import Modelo.Cliente;
import Modelo.Sesion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    private String obtenerOCrearTipoDocumento(Connection con, String nombreTipo) throws SQLException {
        if (nombreTipo == null || nombreTipo.trim().isEmpty()) {
            nombreTipo = "Cedula";
        }
        String buscar = "SELECT id_tipo_documento FROM tipo_documento WHERE nombre_tipo_documento = ?";
        try (PreparedStatement ps = con.prepareStatement(buscar)) {
            ps.setString(1, nombreTipo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        }
        String insertar = "INSERT INTO tipo_documento (nombre_tipo_documento) VALUES (?) RETURNING id_tipo_documento";
        try (PreparedStatement ps = con.prepareStatement(insertar)) {
            ps.setString(1, nombreTipo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        }
        return null;
    }

    public boolean existeDocumento(String numeroDocumento) {
        String idNegocio = Sesion.getIdNegocio();
        String sql = "SELECT 1 FROM cliente WHERE numero_documento = ? AND id_negocio = ?";
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, numeroDocumento);
            ps.setString(2, idNegocio);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

        public boolean registrar(Cliente cliente) {
        String idNegocio = Sesion.getIdNegocio();
        if (idNegocio == null) {
            System.out.println("No hay negocio activo en la sesion.");
            return false;
        }

        // Si ya existe (activo o inactivo) ese documento en este negocio, revisamos su estado.
        String sqlBuscar = "SELECT activo FROM cliente WHERE numero_documento = ? AND id_negocio = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sqlBuscar)) {
            ps.setString(1, cliente.getNumeroDocumento());
            ps.setString(2, idNegocio);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    boolean estaActivo = rs.getBoolean("activo");
                    if (estaActivo) {
                        return false; // ya existe y esta activo: no se duplica
                    }
                    // Estaba eliminado (inactivo): lo reactivamos con los datos nuevos.
                    String sqlReactivar = "UPDATE cliente SET nombre_cliente = ?, telefono = ?, correo = ?, direccion = ?, activo = TRUE "
                            + "WHERE numero_documento = ? AND id_negocio = ?";
                    try (PreparedStatement psReact = con.prepareStatement(sqlReactivar)) {
                        psReact.setString(1, cliente.getNombreCliente());
                        psReact.setString(2, cliente.getTelefono());
                        psReact.setString(3, cliente.getCorreo());
                        psReact.setString(4, cliente.getDireccion());
                        psReact.setString(5, cliente.getNumeroDocumento());
                        psReact.setString(6, idNegocio);
                        return psReact.executeUpdate() > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        String sql = "INSERT INTO cliente (id_negocio, id_tipo_documento, numero_documento, nombre_cliente, telefono, correo, direccion) VALUES (?,?,?,?,?,?,?)";
        try (Connection con = Conexion.getConnection()) {
            String idTipoDocumento = obtenerOCrearTipoDocumento(con, cliente.getTipoDocumento());
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, idNegocio);
                ps.setString(2, idTipoDocumento);
                ps.setString(3, cliente.getNumeroDocumento());
                ps.setString(4, cliente.getNombreCliente());
                ps.setString(5, cliente.getTelefono());
                ps.setString(6, cliente.getCorreo());
                ps.setString(7, cliente.getDireccion());
                ps.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean modificar(Cliente cliente) {
        String sql = "UPDATE cliente SET nombre_cliente = ?, telefono = ?, correo = ?, direccion = ? WHERE numero_documento = ? AND id_negocio = ?";
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cliente.getNombreCliente());
            ps.setString(2, cliente.getTelefono());
            ps.setString(3, cliente.getCorreo());
            ps.setString(4, cliente.getDireccion());
            ps.setString(5, cliente.getNumeroDocumento());
            ps.setString(6, Sesion.getIdNegocio());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminar(String numeroDocumento) {
        String idNegocio = Sesion.getIdNegocio();
        String sqlDelete = "DELETE FROM cliente WHERE numero_documento = ? AND id_negocio = ?";
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sqlDelete)) {
            ps.setString(1, numeroDocumento);
            ps.setString(2, idNegocio);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            if (!"23503".equals(e.getSQLState())) {
                e.printStackTrace();
                return false;
            }
            String sqlSoftDelete = "UPDATE cliente SET activo = FALSE WHERE numero_documento = ? AND id_negocio = ?";
            try (Connection con2 = Conexion.getConnection(); PreparedStatement ps2 = con2.prepareStatement(sqlSoftDelete)) {
                ps2.setString(1, numeroDocumento);
                ps2.setString(2, idNegocio);
                return ps2.executeUpdate() > 0;
            } catch (SQLException e2) {
                e2.printStackTrace();
                return false;
            }
        }
    }

    public Cliente buscarPorDocumento(String numeroDocumento) {
        String sql = "SELECT c.id_cliente, c.numero_documento, c.nombre_cliente, c.telefono, c.correo, c.direccion, td.nombre_tipo_documento "
                + "FROM cliente c JOIN tipo_documento td ON c.id_tipo_documento = td.id_tipo_documento "
                + "WHERE c.numero_documento = ? AND c.id_negocio = ? AND c.activo = TRUE";
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, numeroDocumento);
            ps.setString(2, Sesion.getIdNegocio());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Cliente> listarTodos() {
        List<Cliente> lista = new ArrayList<>();
        String idNegocio = Sesion.getIdNegocio();
        String sql = "SELECT c.id_cliente, c.numero_documento, c.nombre_cliente, c.telefono, c.correo, c.direccion, td.nombre_tipo_documento "
                + "FROM cliente c JOIN tipo_documento td ON c.id_tipo_documento = td.id_tipo_documento "
                + "WHERE c.numero_documento <> 'CONSFINAL' AND c.activo = TRUE"
                + (idNegocio != null ? " AND c.id_negocio = ?" : "");
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            if (idNegocio != null) {
                ps.setString(1, idNegocio);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private Cliente mapear(ResultSet rs) throws SQLException {
        Cliente c = new Cliente(rs.getString("nombre_tipo_documento"), rs.getString("numero_documento"),
                rs.getString("nombre_cliente"), rs.getString("telefono"), rs.getString("correo"));
        c.setIdCliente(rs.getString("id_cliente"));
        c.setIdNegocio(Sesion.getIdNegocio());
        c.setDireccion(rs.getString("direccion"));
        return c;
    }

    public String obtenerIdPorDocumento(String numeroDocumento) {
        Cliente c = buscarPorDocumento(numeroDocumento);
        return c == null ? null : c.getIdCliente();
    }
}
