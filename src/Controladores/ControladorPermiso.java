package Controladores;

public class ControladorPermiso {
    private static final DAO.PermisoDAO permisoDAO = new DAO.PermisoDAO();

    public static boolean tienePermiso(String idRol, String nombrePermiso) {
        return permisoDAO.tienePermiso(idRol, nombrePermiso);
    }
}
