package Modelo;

public class Rol {

    private String idRol;
    private String idNegocio;
    private String nombreRol;

    public Rol() {
    }

    public Rol(String idNegocio, String nombreRol) {
        this.idNegocio = idNegocio;
        this.nombreRol = nombreRol;
    }

    public String getIdRol() {
        return idRol;
    }

    public void setIdRol(String idRol) {
        this.idRol = idRol;
    }

    public String getIdNegocio() {
        return idNegocio;
    }

    public void setIdNegocio(String idNegocio) {
        this.idNegocio = idNegocio;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    @Override
    public String toString() {
        return nombreRol == null ? "" : nombreRol;
    }
}
