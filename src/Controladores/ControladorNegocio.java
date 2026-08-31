package Controladores;

import DAO.NegocioDAO;
import Modelo.Negocio;
import javax.swing.JOptionPane;

public class ControladorNegocio {

    private final NegocioDAO negocioDAO = new NegocioDAO();

    public String registrarNegocio(java.awt.Component parent, String idUsuario, String nombreNegocio, String ruc,
            String correoNegocio, String callePrincipal, String calleSecundaria, String ciudad) {

        if (Controladores.Validaciones.camposVacios(idUsuario, nombreNegocio, ruc, correoNegocio)) {
            JOptionPane.showMessageDialog(parent, "Por favor, complete todos los campos obligatorios (*).", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        if (!Controladores.Validaciones.validarRuc(ruc)) {
            JOptionPane.showMessageDialog(parent, "El RUC ingresado es inválido. Debe contener exactamente 13 dígitos numéricos.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        if (!Controladores.Validaciones.validarCorreo(correoNegocio)) {
            JOptionPane.showMessageDialog(parent, "El correo del negocio no es válido.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        if (negocioDAO.existeRuc(ruc)) {
            JOptionPane.showMessageDialog(parent, "Ya existe un negocio registrado con ese RUC.", "Negocio Duplicado", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        Negocio negocio = new Negocio();
        negocio.setIdUsuario(idUsuario);
        negocio.setNombreNegocio(nombreNegocio);
        negocio.setRucNegocio(ruc);
        negocio.setCorreoContacto(correoNegocio);
        negocio.setCallePrincipal(callePrincipal);
        negocio.setCalleSecundaria(calleSecundaria);
        negocio.setCiudad(ciudad);

        String idNegocio = negocioDAO.registrar(negocio);
        if (idNegocio == null) {
            JOptionPane.showMessageDialog(parent, "Ocurrió un error al registrar el negocio.", "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
        return idNegocio;
    }

    public Negocio buscarPorUsuario(String idUsuario) {
        return negocioDAO.buscarPorIdUsuario(idUsuario);
    }

    public boolean actualizarNegocio(java.awt.Component parent, String idNegocio, String nombreNegocio,
            String correoNegocio) {

        if (Controladores.Validaciones.camposVacios(idNegocio, nombreNegocio, correoNegocio)) {
            JOptionPane.showMessageDialog(parent, "Por favor, complete el nombre y el correo del negocio.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!Controladores.Validaciones.validarCorreo(correoNegocio)) {
            JOptionPane.showMessageDialog(parent, "El correo del negocio no es válido.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        Negocio negocio = new Negocio();
        negocio.setIdNegocio(idNegocio);
        negocio.setNombreNegocio(nombreNegocio);
        negocio.setCorreoContacto(correoNegocio);

        boolean actualizado = negocioDAO.actualizar(negocio);
        if (!actualizado) {
            JOptionPane.showMessageDialog(parent, "Ocurrió un error al actualizar el negocio.", "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
        return actualizado;
    }
}
