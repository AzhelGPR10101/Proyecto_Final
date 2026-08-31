
package Modelo;

public class Bodeguero extends Empleado{
    private String areaBodegaAsignada;
    private boolean permisoMovimientoStock;

    public Bodeguero(String areaBodegaAsignada, boolean permisoMovimientoStock, String cedula, String nombres, String apellidos, String correo, String telefono, double sueldo) {
        super(cedula, nombres, apellidos, correo, telefono, sueldo);
        this.areaBodegaAsignada = areaBodegaAsignada;
        this.permisoMovimientoStock = permisoMovimientoStock;
    }

    public Bodeguero() {
        super();
    }

    public String getAreaBodegaAsignada() {
        return areaBodegaAsignada;
    }

    public void setAreaBodegaAsignada(String areaBodegaAsignada) {
        this.areaBodegaAsignada = areaBodegaAsignada;
    }

    public boolean isPermisoMovimientoStock() {
        return permisoMovimientoStock;
    }

    public void setPermisoMovimientoStock(boolean permisoMovimientoStock) {
        this.permisoMovimientoStock = permisoMovimientoStock;
    }

}
