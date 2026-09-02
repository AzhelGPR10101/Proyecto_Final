package DAO;

import Conexion.Conexion;
import Modelo.UsuarioCuenta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public boolean existeCorreoOCedula(String correo, String cedula) {
        String sql = "SELECT 1 FROM usuario WHERE correo = ? OR cedula = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo);
            ps.setString(2, cedula);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String registrar(UsuarioCuenta usuario) {
        String sql = "INSERT INTO usuario (cedula, nombres, apellidos, correo, contrasena, telefono, foto_perfil) VALUES (?,?,?,?,?,?,?) RETURNING id_usuario";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario.getCedula());
            ps.setString(2, usuario.getNombres());
            ps.setString(3, usuario.getApellidos());
            ps.setString(4, usuario.getCorreo());
            ps.setString(5, Seguridad.Hasher.hashear(usuario.getContrasena()));
            ps.setString(6, usuario.getTelefono());
            ps.setString(7, usuario.getFotoPerfil());
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

    public UsuarioCuenta buscarPorCorreoOUsuario(String correoOUsuario) {
        String sql = "SELECT id_usuario, cedula, nombres, apellidos, correo, contrasena, telefono, foto_perfil FROM usuario WHERE correo = ? OR cedula = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correoOUsuario);
            ps.setString(2, correoOUsuario);
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

    public UsuarioCuenta buscarPorId(String idUsuario) {
        String sql = "SELECT id_usuario, cedula, nombres, apellidos, correo, contrasena, telefono, foto_perfil FROM usuario WHERE id_usuario = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idUsuario);
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

    public boolean existeCorreoExcluyendo(String correo, String idUsuario) {
        String sql = "SELECT 1 FROM usuario WHERE correo = ? AND id_usuario <> ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo);
            ps.setString(2, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarDatos(UsuarioCuenta usuario) {
        String sql = "UPDATE usuario SET nombres = ?, apellidos = ?, correo = ?, foto_perfil = ? WHERE id_usuario = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario.getNombres());
            ps.setString(2, usuario.getApellidos());
            ps.setString(3, usuario.getCorreo());
            ps.setString(4, usuario.getFotoPerfil());
            ps.setString(5, usuario.getIdUsuario());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

        public boolean actualizarContrasena(String idUsuario, String nuevaContrasena) {
        String sql = "UPDATE usuario SET contrasena = ? WHERE id_usuario = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, Seguridad.Hasher.hashear(nuevaContrasena));
            ps.setString(2, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean intentarIniciarSesion(String idUsuario) {
        String sql = "UPDATE usuario SET sesion_activa = TRUE WHERE id_usuario = ? AND sesion_activa = FALSE";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean cerrarSesion(String idUsuario) {
        String sql = "UPDATE usuario SET sesion_activa = FALSE WHERE id_usuario = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminar(String idUsuario) {
        String sql = "DELETE FROM usuario WHERE id_usuario = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private UsuarioCuenta mapear(ResultSet rs) throws SQLException {
        UsuarioCuenta u = new UsuarioCuenta();
        u.setIdUsuario(rs.getString("id_usuario"));
        u.setCedula(rs.getString("cedula"));
        u.setNombres(rs.getString("nombres"));
        u.setApellidos(rs.getString("apellidos"));
        u.setCorreo(rs.getString("correo"));
        u.setContrasena(rs.getString("contrasena"));
        u.setTelefono(rs.getString("telefono"));
        u.setFotoPerfil(rs.getString("foto_perfil"));
        return u;
    }
}
