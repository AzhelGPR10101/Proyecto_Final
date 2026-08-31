package Controladores;

import DAO.NegocioDAO;
import DAO.SolicitudAccesoDAO;
import DAO.UsuarioDAO;
import Modelo.Empleado;
import Modelo.Negocio;
import Modelo.Sesion;
import Modelo.UsuarioCuenta;
import javax.swing.JOptionPane;

public class ControladorLogin {

    public enum ResultadoLogin {
        DUENIO_CON_NEGOCIO,
        DUENIO_SIN_NEGOCIO,
        EMPLEADO_ACTIVO,
        EMPLEADO_PENDIENTE,
        EMPLEADO_INACTIVO,
        CREDENCIALES_INVALIDAS
    }

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final NegocioDAO negocioDAO = new NegocioDAO();
    private final DAO.EmpleadoDAO empleadoDAO = new DAO.EmpleadoDAO();
    private final SolicitudAccesoDAO solicitudDAO = new SolicitudAccesoDAO();

    public ResultadoLogin login(java.awt.Component parent, String correo, String password) {
        UsuarioCuenta usuario = usuarioDAO.buscarPorCorreoOUsuario(correo);

        if (usuario == null || !Seguridad.Hasher.verificar(password, usuario.getContrasena())) {
            JOptionPane.showMessageDialog(parent, "Correo o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
            return ResultadoLogin.CREDENCIALES_INVALIDAS;
        }

        String idUsuario = usuario.getIdUsuario();

        Negocio negocioPropio = negocioDAO.buscarPorIdUsuario(idUsuario);
        if (negocioPropio != null) {
            Sesion.iniciar(idUsuario, negocioPropio.getIdNegocio(), usuario.getNombres());
            Sesion.setRolUsuario(null);
            Sesion.guardarDatosUsuario(usuario.getCedula(), usuario.getApellidos(), usuario.getCorreo(),
                    usuario.getTelefono(), usuario.getFotoPerfil());
            return ResultadoLogin.DUENIO_CON_NEGOCIO;
        }

        Empleado empleado = empleadoDAO.buscarPorCedula(usuario.getCedula());
        if (empleado != null) {
            if (!empleado.isActivo()) {
                JOptionPane.showMessageDialog(parent,
                        "Este usuario fue desactivado y ya no tiene acceso al sistema. "
                        + "Contacta al administrador si crees que esto es un error.",
                        "Cuenta desactivada", JOptionPane.WARNING_MESSAGE);
                return ResultadoLogin.EMPLEADO_INACTIVO;
            }
            String idNegocioEmpleado = obtenerIdNegocioDeEmpleado(idUsuario);
            Sesion.iniciar(idUsuario, idNegocioEmpleado, usuario.getNombres());
            Sesion.setRolUsuario(empleado.getRol());
            Sesion.setIdRolUsuario(empleado.getIdRol());
            Sesion.guardarDatosUsuario(usuario.getCedula(), usuario.getApellidos(), usuario.getCorreo(),
                    usuario.getTelefono(), usuario.getFotoPerfil());
            return ResultadoLogin.EMPLEADO_ACTIVO;
        }

        if (solicitudDAO.tieneSolicitudPendiente(idUsuario)) {
            return ResultadoLogin.EMPLEADO_PENDIENTE;
        }

        Sesion.iniciar(idUsuario, null, usuario.getNombres());
        Sesion.setRolUsuario(null);
        Sesion.guardarDatosUsuario(usuario.getCedula(), usuario.getApellidos(), usuario.getCorreo(),
                usuario.getTelefono(), usuario.getFotoPerfil());
        return ResultadoLogin.DUENIO_SIN_NEGOCIO;
    }

    private String obtenerIdNegocioDeEmpleado(String idUsuario) {
        String sql = "SELECT id_negocio FROM empleado WHERE id_empleado = ?";
        try (java.sql.Connection con = Conexion.Conexion.getConnection(); java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idUsuario);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString(1) : null;
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
