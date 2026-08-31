
package Modelo;

public class Recursos_Humanos extends Empleado {
    private String departamento;
    private boolean nivelAccesoTotal;

    public Recursos_Humanos(String departamento, boolean nivelAccesoTotal, String cedula, String nombres, String apellidos, String correo, String telefono, double sueldo) {
        super(cedula, nombres, apellidos, correo, telefono, sueldo);
        this.departamento = departamento;
        this.nivelAccesoTotal = nivelAccesoTotal;
    }

    public Recursos_Humanos() {
        super();
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public boolean isNivelAccesoTotal() {
        return nivelAccesoTotal;
    }

    public void setNivelAccesoTotal(boolean nivelAccesoTotal) {
        this.nivelAccesoTotal = nivelAccesoTotal;
    }

}
