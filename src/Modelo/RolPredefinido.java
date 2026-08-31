package Modelo;

import java.util.Arrays;
import java.util.List;

public enum RolPredefinido {

    VENDEDOR("Vendedor", Arrays.asList(
            PermisoSistema.VER_PRODUCTOS, PermisoSistema.VER_CLIENTES, PermisoSistema.VER_VENTAS,
            PermisoSistema.VER_EGRESOS)),

    BODEGUERO("Bodeguero", Arrays.asList(
            PermisoSistema.VER_BODEGA)),

    RECURSOS_HUMANOS("Recursos Humanos", Arrays.asList(
            PermisoSistema.VER_EMPLEADOS, PermisoSistema.VER_NOMINA));

    private final String nombreVisible;
    private final List<PermisoSistema> permisosPorDefecto;

    RolPredefinido(String nombreVisible, List<PermisoSistema> permisosPorDefecto) {
        this.nombreVisible = nombreVisible;
        this.permisosPorDefecto = permisosPorDefecto;
    }

    public List<PermisoSistema> permisosPorDefecto() {
        return permisosPorDefecto;
    }

    public String getNombreVisible() {
        return nombreVisible;
    }

    public static RolPredefinido desdeNombre(String nombreRol) {
        if (nombreRol == null) {
            return null;
        }
        String n = nombreRol.trim();
        if (n.equalsIgnoreCase("Vendedor") || n.equalsIgnoreCase("Cajero")) {
            return VENDEDOR;
        }
        if (n.equalsIgnoreCase("Bodeguero") || n.equalsIgnoreCase("Bodegero")) {
            return BODEGUERO;
        }
        if (n.equalsIgnoreCase("Recursos Humanos") || n.equalsIgnoreCase("RRHH") || n.equalsIgnoreCase("Talento Humano")) {
            return RECURSOS_HUMANOS;
        }
        return null;
    }
}
