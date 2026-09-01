package Controladores;

import DAO.UsuarioDAO;
import Modelo.UsuarioCuenta;
import javax.swing.JOptionPane;

public class ControladorUsuario {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public String registrarUsuario(java.awt.Component parent, String cedula, String nombres, String apellidos,
            String correo, String password, String telefono) {
        return registrarUsuario(parent, cedula, nombres, apellidos, correo, password, telefono, null);
    }

    public String registrarUsuario(java.awt.Component parent, String cedula, String nombres, String apellidos,
            String correo, String password, String telefono, String rutaFotoPerfil) {

        if (Controladores.Validaciones.camposVacios(cedula, nombres, apellidos, correo, password, telefono)) {
            JOptionPane.showMessageDialog(parent, "Por favor, complete todos los campos obligatorios.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        if (!Controladores.Validaciones.validarCedula(cedula)) {
            JOptionPane.showMessageDialog(parent, "La cédula ingresada no es válida.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return null;
        }
                if (!Controladores.Validaciones.validarCorreo(correo)) {
            JOptionPane.showMessageDialog(parent, "El correo ingresado no es válido.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        if (!Controladores.Validaciones.validarContrasena(password)) {
            JOptionPane.showMessageDialog(parent,
                    "La contraseña debe tener entre 8 y 16 caracteres, con al menos una mayúscula, una minúscula, un número y un carácter especial.",
                    "Contraseña Inválida", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        if (!Controladores.Validaciones.validarTelefono(telefono)) {
            JOptionPane.showMessageDialog(parent, "El teléfono debe ser un número celular ecuatoriano válido (09XXXXXXXX).", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return null;
        }
                if (usuarioDAO.existeCorreoOCedula(correo, cedula)) {
            JOptionPane.showMessageDialog(parent, "Ya existe un usuario registrado con esa cédula o ese correo.", "Registro Duplicado", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        UsuarioCuenta nuevo = new UsuarioCuenta(cedula, nombres, apellidos, correo, password, telefono);
        nuevo.setFotoPerfil(rutaFotoPerfil);
        String idUsuario = usuarioDAO.registrar(nuevo);
        if (idUsuario == null) {
            JOptionPane.showMessageDialog(parent, "Ocurrió un error al registrar el usuario.", "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
        return idUsuario;
    }

    public UsuarioCuenta obtenerUsuario(String idUsuario) {
        return usuarioDAO.buscarPorId(idUsuario);
    }

    public boolean actualizarDatos(java.awt.Component parent, String idUsuario, String nombres, String apellidos,
            String correo, String rutaFotoPerfil) {

        if (Controladores.Validaciones.camposVacios(idUsuario, nombres, apellidos, correo)) {
            JOptionPane.showMessageDialog(parent, "Por favor, complete el nombre, apellido y correo.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!Controladores.Validaciones.validarCorreo(correo)) {
            JOptionPane.showMessageDialog(parent, "El correo ingresado no es válido.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (usuarioDAO.existeCorreoExcluyendo(correo, idUsuario)) {
            JOptionPane.showMessageDialog(parent, "Ese correo ya está en uso por otro usuario.", "Correo Duplicado", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        Modelo.UsuarioCuenta datosActualizados = new Modelo.UsuarioCuenta();
        datosActualizados.setIdUsuario(idUsuario);
        datosActualizados.setNombres(nombres);
        datosActualizados.setApellidos(apellidos);
        datosActualizados.setCorreo(correo);
        datosActualizados.setFotoPerfil(rutaFotoPerfil);
        boolean actualizado = usuarioDAO.actualizarDatos(datosActualizados);
        if (!actualizado) {
            JOptionPane.showMessageDialog(parent, "Ocurrió un error al actualizar tus datos.", "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
        return actualizado;
    }

    public boolean cambiarContrasena(java.awt.Component parent, String idUsuario, String nuevaContrasena, String confirmacion) {
        if (Controladores.Validaciones.camposVacios(idUsuario, nuevaContrasena, confirmacion)) {
            JOptionPane.showMessageDialog(parent, "Por favor, escribe y confirma la nueva contraseña.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            return false;
        }
               if (!Controladores.Validaciones.validarContrasena(nuevaContrasena)) {
            JOptionPane.showMessageDialog(parent,
                    "La contraseña debe tener entre 8 y 16 caracteres, con al menos una mayúscula, una minúscula, un número y un carácter especial.",
                    "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!nuevaContrasena.equals(confirmacion)) {
            JOptionPane.showMessageDialog(parent, "Las contraseñas no coinciden.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        boolean actualizado = usuarioDAO.actualizarContrasena(idUsuario, nuevaContrasena);
        if (actualizado) {
            JOptionPane.showMessageDialog(parent, "Contraseña actualizada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(parent, "Ocurrió un error al cambiar la contraseña.", "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
        return actualizado;
    }

    public boolean eliminarCuenta(java.awt.Component parent, String idUsuario) {
        boolean eliminado = usuarioDAO.eliminar(idUsuario);
        if (!eliminado) {
            JOptionPane.showMessageDialog(parent, "No se pudo eliminar la cuenta. Es posible que tenga un negocio u otros registros asociados.", "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
        return eliminado;
    }
}
