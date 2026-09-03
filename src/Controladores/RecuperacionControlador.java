package Controladores;

import DAO.UsuarioDAO;
import Modelo.UsuarioCuenta;
import java.awt.Component;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;
import javax.swing.JOptionPane;

public class RecuperacionControlador {

    private static final long VIGENCIA_CODIGO_MS = TimeUnit.MINUTES.toMillis(10);
    private static final int INTENTOS_MAXIMOS = 5;
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final UsuarioDAO usuarioDAO = new UsuarioDAO();

    private static String codigoGenerado = null;
    private static String correoEnRecuperacion = null;
    private static String idUsuarioEnRecuperacion = null;
    private static long momentoGeneracion = 0L;
    private static int intentosFallidos = 0;

    public static UsuarioCuenta buscarPorCorreo(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            return null;
        }
        return usuarioDAO.buscarPorCorreoOUsuario(correo.trim());
    }

    public static boolean solicitarCodigo(Component parent, String correo) {

        if (correo == null || correo.trim().isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Ingrese un correo electrónico.", "Campo vacío", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (!Controladores.Validaciones.validarCorreo(correo.trim())) {
            JOptionPane.showMessageDialog(parent, "El correo ingresado no tiene un formato válido.", "Correo inválido", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        UsuarioCuenta usuario = buscarPorCorreo(correo.trim());

        if (usuario == null) {
            JOptionPane.showMessageDialog(parent, "No existe ninguna cuenta registrada con ese correo.",
                    "Correo no encontrado", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        String codigo = String.valueOf(100000 + RANDOM.nextInt(900000));

        if (Correo.EmailService.hayCorreoConfigurado()) {
            try {
                Correo.EmailService.enviarCodigoRecuperacion(usuario.getCorreo(), usuario.getNombres(), codigo);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(parent,
                        "No se pudo enviar el correo de verificación.\nRevisa tu conexión a internet e inténtalo de nuevo.",
                        "Error de envío", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            codigoGenerado = codigo;
            correoEnRecuperacion = usuario.getCorreo();
            idUsuarioEnRecuperacion = usuario.getIdUsuario();
            momentoGeneracion = System.currentTimeMillis();
            intentosFallidos = 0;

            JOptionPane.showMessageDialog(parent,
                    "Hemos enviado un código de verificación a:\n" + usuario.getCorreo(),
                    "Código enviado", JOptionPane.INFORMATION_MESSAGE);
            return true;
        }

        codigoGenerado = codigo;
        correoEnRecuperacion = usuario.getCorreo();
        idUsuarioEnRecuperacion = usuario.getIdUsuario();
        momentoGeneracion = System.currentTimeMillis();

        JOptionPane.showMessageDialog(parent,
                "Tu código de verificación es: " + codigo + "\n(válido por 10 minutos)\n\n"
                + "Nota: todavía no hay una cuenta de correo configurada para enviarlo de verdad.",
                "Código de verificación", JOptionPane.INFORMATION_MESSAGE);
        return true;
    }

    public static boolean validarCodigo(String correo, String codigoIngresado) {
        if (codigoGenerado == null || correoEnRecuperacion == null || correo == null) {
            return false;
        }

        if (!correoEnRecuperacion.equalsIgnoreCase(correo.trim())) {
            return false;
        }

        boolean vigente = (System.currentTimeMillis() - momentoGeneracion) <= VIGENCIA_CODIGO_MS;
        if (!vigente || intentosFallidos >= INTENTOS_MAXIMOS) {
            limpiarSesionRecuperacion();
            return false;
        }

        String ingresado = codigoIngresado == null ? "" : codigoIngresado.trim();
        boolean coincide = codigoGenerado.equals(ingresado);
        if (!coincide) {
            intentosFallidos++;
            if (intentosFallidos >= INTENTOS_MAXIMOS) {
                limpiarSesionRecuperacion();
            }
        }
        return coincide;
    }

    public static boolean actualizarPassword(Component parent, String correo, String nuevaPassword) {
        if (idUsuarioEnRecuperacion == null || correoEnRecuperacion == null
                || !correoEnRecuperacion.equalsIgnoreCase(correo == null ? "" : correo.trim())) {
            JOptionPane.showMessageDialog(parent, "No se encontró la cuenta a actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        boolean actualizado = usuarioDAO.actualizarContrasena(idUsuarioEnRecuperacion, nuevaPassword);

        if (actualizado) {
            JOptionPane.showMessageDialog(parent, "¡Contraseña actualizada exitosamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarSesionRecuperacion();
            return true;
        }

        JOptionPane.showMessageDialog(parent, "Error al actualizar la contraseña.", "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        return false;
    }

    public static boolean validarNuevaPassword(Component parent, String password, String confirmacion) {
        if (password == null || password.isEmpty() || confirmacion == null || confirmacion.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Debe ingresar y confirmar la nueva contraseña.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (!Controladores.Validaciones.validarContrasena(password)) {
            JOptionPane.showMessageDialog(parent,
                    "La contraseña debe tener entre 8 y 16 caracteres, con al menos una mayúscula, una minúscula, un número y un carácter especial.",
                    "Contraseña Inválida", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (!password.equals(confirmacion)) {
            JOptionPane.showMessageDialog(parent, "Las contraseñas no coinciden.", "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    public static void limpiarSesionRecuperacion() {
        codigoGenerado = null;
        correoEnRecuperacion = null;
        idUsuarioEnRecuperacion = null;
        momentoGeneracion = 0L;
        intentosFallidos = 0;
    }
}
