package Controladores;

import DAO.NegocioDAO;
import DAO.PermisoDAO;
import DAO.RolDAO;
import DAO.SolicitudAccesoDAO;
import Modelo.Permiso;
import Modelo.PermisoSistema;
import Modelo.RolPredefinido;
import Modelo.Sesion;
import Modelo.SolicitudAcceso;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ControladorSolicitud {

    private final SolicitudAccesoDAO solicitudDAO = new SolicitudAccesoDAO();
    private final NegocioDAO negocioDAO = new NegocioDAO();
    private final RolDAO rolDAO = new RolDAO();
    private final PermisoDAO permisoDAO = new PermisoDAO();

    public boolean solicitarAcceso(java.awt.Component parent, String idUsuario, String codigoNegocio, String rolSolicitado) {
        if (idUsuario == null) {
            JOptionPane.showMessageDialog(parent, "Debes completar tu registro antes de solicitar acceso.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (codigoNegocio == null || codigoNegocio.trim().isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Ingresa el código del negocio.", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        String idNegocio = codigoNegocio.trim().toUpperCase();
        if (!negocioDAO.existeIdNegocio(idNegocio)) {
            JOptionPane.showMessageDialog(parent, "No existe ningún negocio registrado con ese código.", "Código Inválido", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String rol = (rolSolicitado == null || rolSolicitado.trim().isEmpty()) ? "Cajero" : rolSolicitado.trim();

        String idRol;
        try (Connection con = Conexion.Conexion.getConnection()) {
            idRol = rolDAO.obtenerOCrearIdRol(con, new Modelo.Rol(idNegocio, rol));
            if (idRol != null && permisoDAO.listarPermisosDeRol(idRol).isEmpty()) {

                asignarPermisosPorDefecto(con, idRol, rol);
            }
        } catch (Exception e) {
            e.printStackTrace();
            idRol = null;
        }
        if (idRol == null) {
            JOptionPane.showMessageDialog(parent, "No se pudo procesar el rol solicitado.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        SolicitudAcceso nuevaSolicitud = new SolicitudAcceso();
        nuevaSolicitud.setIdUsuario(idUsuario);
        nuevaSolicitud.setIdNegocio(idNegocio);
        nuevaSolicitud.setIdRol(idRol);
        String idSolicitud = solicitudDAO.crear(nuevaSolicitud);
        if (idSolicitud == null) {
            JOptionPane.showMessageDialog(parent, "Ocurrió un error al enviar tu solicitud.", "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        Modelo.UsuarioCuenta solicitante = new Controladores.ControladorUsuario().obtenerUsuario(idUsuario);
        String nombreSolicitante = solicitante != null
                ? (solicitante.getNombres() + " " + solicitante.getApellidos()).trim()
                : "Un usuario";
        String detalleNotif = nombreSolicitante + " solicito acceso al negocio con el rol de " + rol + ".";
        new ControladorNotificacion().notificarEvento(idNegocio, "SOLICITUD_" + idSolicitud, detalleNotif);

        return true;
    }

    private void asignarPermisosPorDefecto(Connection con, String idRol, String nombreRol) throws java.sql.SQLException {
        RolPredefinido predefinido = RolPredefinido.desdeNombre(nombreRol);
        if (predefinido == null) {
            return;
        }
        for (PermisoSistema permisoSistema : predefinido.permisosPorDefecto()) {
            Permiso permiso = new Permiso(permisoSistema.name());
            String idPermiso = permisoDAO.obtenerOCrearIdPermiso(con, permiso);
            if (idPermiso != null) {
                permisoDAO.asignarPermiso(idRol, idPermiso);
            }
        }
    }

    public List<SolicitudAcceso> listarPendientes() {
        String idNegocio = Sesion.getIdNegocio();
        if (idNegocio == null) {
            return new ArrayList<>();
        }
        return solicitudDAO.listarPendientesPorNegocio(idNegocio);
    }

    public boolean aprobar(java.awt.Component parent, String idSolicitud, String sueldoStr) {
        if (!Validaciones.validarSueldo(sueldoStr)) {
            JOptionPane.showMessageDialog(parent, "Ingresa un sueldo válido antes de aceptar.", "Sueldo Inválido", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        double sueldo = Double.parseDouble(sueldoStr.trim());
        boolean exito = solicitudDAO.aprobar(idSolicitud, sueldo);
        if (!exito) {
            JOptionPane.showMessageDialog(parent, "No se pudo aceptar la solicitud.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return exito;
    }

    public boolean tieneSolicitudPendiente(String idUsuario) {
        return solicitudDAO.tieneSolicitudPendiente(idUsuario);
    }
}
