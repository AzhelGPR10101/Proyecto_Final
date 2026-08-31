package Modelo;

public class Sesion {
    private static String idUsuario;
    private static String idNegocio;
    private static String nombreUsuario;
    private static String cedulaUsuario;
    private static String apellidosUsuario;
    private static String correoUsuario;
    private static String telefonoUsuario;
    private static String fotoPerfilUsuario;
    private static String rolUsuario;
    private static String idRolUsuario;

    public static String getIdRolUsuario() {
        return idRolUsuario;
    }

    public static void setIdRolUsuario(String idRolUsuario) {
        Sesion.idRolUsuario = idRolUsuario;
    }

    public static void iniciar(String idUsuario, String idNegocio, String nombreUsuario) {
        Sesion.idUsuario = idUsuario;
        Sesion.idNegocio = idNegocio;
        Sesion.nombreUsuario = nombreUsuario;
    }

    public static void setRolUsuario(String rol) {
        Sesion.rolUsuario = rol;
    }

    public static String getRolUsuario() {
        return rolUsuario;
    }

    public static boolean esDueno() {
        return rolUsuario == null;
    }

    public static void guardarDatosUsuario(String cedula, String apellidos, String correo, String telefono, String fotoPerfil) {
        Sesion.cedulaUsuario = cedula;
        Sesion.apellidosUsuario = apellidos;
        Sesion.correoUsuario = correo;
        Sesion.telefonoUsuario = telefono;
        Sesion.fotoPerfilUsuario = fotoPerfil;
    }

    public static void cerrar() {
        idUsuario = null;
        idNegocio = null;
        nombreUsuario = null;
        cedulaUsuario = null;
        apellidosUsuario = null;
        correoUsuario = null;
        telefonoUsuario = null;
        fotoPerfilUsuario = null;
        rolUsuario = null;
        idRolUsuario = null;
    }

    public static String getIdUsuario() {
        return idUsuario;
    }

    public static String getIdNegocio() {
        return idNegocio;
    }

    public static String getNombreUsuario() {
        return nombreUsuario;
    }

    public static boolean haySesionActiva() {
        return idUsuario != null;
    }

    public static String getCedulaUsuario() {
        return cedulaUsuario;
    }

    public static String getApellidosUsuario() {
        return apellidosUsuario;
    }

    public static String getCorreoUsuario() {
        return correoUsuario;
    }

    public static String getTelefonoUsuario() {
        return telefonoUsuario;
    }

    public static String getFotoPerfilUsuario() {
        return fotoPerfilUsuario;
    }
}
