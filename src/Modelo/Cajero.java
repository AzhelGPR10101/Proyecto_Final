
package Modelo;

public class Cajero extends Empleado{
    private int numeroCaja;
    private String turno;

    public Cajero(int numeroCaja, String turno, String cedula, String nombres, String apellidos, String correo, String telefono, double sueldo) {
        super(cedula, nombres, apellidos, correo, telefono, sueldo);
        this.numeroCaja = numeroCaja;
        this.turno = turno;
    }

    public Cajero() {
        super();
    }

    public int getNumeroCaja() {
        return numeroCaja;
    }

    public void setNumeroCaja(int numeroCaja) {
        this.numeroCaja = numeroCaja;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

}
