package Modelo;

public class Modulo {
    private String idModulo;
    private String nombreModulo;
    private String descripcion;
    private boolean activo;

    public Modulo() {
    }

    public Modulo(String idModulo, String nombreModulo, String descripcion, boolean activo) {
        this.idModulo = idModulo;
        this.nombreModulo = nombreModulo;
        this.descripcion = descripcion;
        this.activo = activo;
    }

    public String getIdModulo() {
        return idModulo;
    }

    public void setIdModulo(String idModulo) {
        this.idModulo = idModulo;
    }

    public String getNombreModulo() {
        return nombreModulo;
    }

    public void setNombreModulo(String nombreModulo) {
        this.nombreModulo = nombreModulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
