package DAO;

import Conexion.Conexion;
import Modelo.Bodeguero;
import Modelo.Cajero;
import Modelo.Empleado;
import Modelo.Recursos_Humanos;
import Modelo.Rol;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {

    private static final SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("dd/MM/yyyy");

    private static final String SELECT_BASE =
            "SELECT e.id_empleado, e.id_rol, e.salario, e.fecha_ingreso, e.estado, r.nombre_rol, "
            + "u.cedula, u.nombres, u.apellidos, u.correo, u.contrasena, u.telefono "
            + "FROM empleado e "
            + "JOIN usuario u ON u.id_usuario = e.id_empleado "
            + "JOIN rol r ON r.id_rol = e.id_rol ";

    public boolean existeCedula(String cedula) {
        String sql = "SELECT 1 FROM usuario WHERE cedula = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cedula);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizar(Empleado empleado) {

        String password = empleado.getPassword();
        String sqlUsuario = "UPDATE usuario SET nombres=?, apellidos=?, correo=?, telefono=?"
                + (password != null && !password.trim().isEmpty() ? ", contrasena=?" : "")
                + " WHERE cedula=?";
        String sqlEmpleado = "UPDATE empleado SET salario=? "
                + "WHERE id_empleado = (SELECT id_usuario FROM usuario WHERE cedula=?) AND id_negocio=?";

        try (Connection con = Conexion.getConnection()) {
            con.setAutoCommit(false);
            try {
                try (PreparedStatement ps = con.prepareStatement(sqlUsuario)) {
                    int i = 1;
                    ps.setString(i++, empleado.getNombres());
                    ps.setString(i++, empleado.getApellidos());
                    ps.setString(i++, empleado.getCorreo());
                    ps.setString(i++, empleado.getTelefono());
                    if (password != null && !password.trim().isEmpty()) {
                        ps.setString(i++, Seguridad.Hasher.hashear(password.trim()));
                    }
                    ps.setString(i, empleado.getCedula());
                    ps.executeUpdate();
                }
                int filas;
                try (PreparedStatement ps = con.prepareStatement(sqlEmpleado)) {
                    ps.setDouble(1, empleado.getSueldo());
                    ps.setString(2, empleado.getCedula());
                    ps.setString(3, empleado.getIdNegocio());
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

    public Empleado buscarPorCedula(String cedula) {
        String sql = SELECT_BASE + "WHERE u.cedula = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cedula);
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
    public String asegurarEmpleadoDueno(String idUsuario, String idNegocio) {
    String sqlExiste = "SELECT 1 FROM empleado WHERE id_empleado = ?";
    try (Connection con = Conexion.getConnection()) {

        try (PreparedStatement ps = con.prepareStatement(sqlExiste)) {
            ps.setString(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return idUsuario;
                }
            }
        }

        String idRol = new RolDAO().obtenerOCrearIdRol(con, new Rol(idNegocio, "Administrador"));
        if (idRol == null) {
            return null;
        }

        String sqlInsertar = "INSERT INTO empleado (id_empleado, id_negocio, id_rol, fecha_ingreso, estado) "
                + "VALUES (?,?,?,?,'activo')";
        try (PreparedStatement ps = con.prepareStatement(sqlInsertar)) {
            ps.setString(1, idUsuario);
            ps.setString(2, idNegocio);
            ps.setString(3, idRol);
            ps.setDate(4, Date.valueOf(java.time.LocalDate.now()));
            ps.executeUpdate();
        }
        return idUsuario;

    } catch (SQLException e) {
        e.printStackTrace();
        return null;
    }
}

    public List<Empleado> listarPorNegocio(String idNegocio) {
        List<Empleado> lista = new ArrayList<>();
        String sql = SELECT_BASE + "WHERE e.id_negocio = ? AND (e.estado IS NULL OR e.estado = 'activo')";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idNegocio);
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

    public List<Empleado> listarInactivosPorNegocio(String idNegocio) {
        List<Empleado> lista = new ArrayList<>();
        String sql = SELECT_BASE + "WHERE e.id_negocio = ? AND e.estado = 'inactivo'";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idNegocio);
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

    public boolean eliminar(String idNegocio, String cedula) {
        String sql = "UPDATE empleado SET estado = 'inactivo' WHERE id_empleado = "
                + "(SELECT id_usuario FROM usuario WHERE cedula = ?) AND id_negocio = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cedula);
            ps.setString(2, idNegocio);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean reactivar(String idNegocio, String cedula) {
        String sql = "UPDATE empleado SET estado = 'activo' WHERE id_empleado = "
                + "(SELECT id_usuario FROM usuario WHERE cedula = ?) AND id_negocio = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cedula);
            ps.setString(2, idNegocio);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Empleado mapear(ResultSet rs) throws SQLException {
        String nombreRol = rs.getString("nombre_rol");
        Empleado emp;
        if ("Bodegero".equalsIgnoreCase(nombreRol) || "Bodeguero".equalsIgnoreCase(nombreRol)) {
            emp = new Bodeguero();
        } else if ("Recursos Humanos".equalsIgnoreCase(nombreRol)
                || "RRHH".equalsIgnoreCase(nombreRol)
                || "Talento Humano".equalsIgnoreCase(nombreRol)) {
            emp = new Recursos_Humanos();
        } else {
            emp = new Cajero();
        }

        emp.setCedula(rs.getString("cedula"));
        emp.setIdEmpleado(rs.getString("id_empleado"));
        emp.setIdRol(rs.getString("id_rol"));
        emp.setNombres(rs.getString("nombres"));
        emp.setApellidos(rs.getString("apellidos"));
        emp.setCorreo(rs.getString("correo"));
        emp.setSueldo(rs.getDouble("salario"));
        Date fechaIngreso = rs.getDate("fecha_ingreso");
        if (fechaIngreso != null) {
            emp.setFechaContratacion(FORMATO_FECHA.format(fechaIngreso));
        }
        emp.setTelefono(rs.getString("telefono"));
        emp.setUsername(rs.getString("correo"));
        emp.setRol(nombreRol);
        String estado = rs.getString("estado");
        emp.setEstado(estado == null ? "activo" : estado);
        return emp;
    }

    public Empleado buscarPorId(String idEmpleado) {
        String sql = SELECT_BASE + "WHERE e.id_empleado = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idEmpleado);
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

    public String obtenerIdNegocioDeEmpleado(String idEmpleado) {
        String sql = "SELECT id_negocio FROM empleado WHERE id_empleado = ?";
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idEmpleado);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString(1) : null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}