package Modelo;

public class Permiso {

    private String idPermiso;
    private String nombrePermiso;

    public Permiso() {
    }

    public Permiso(String nombrePermiso) {
        this.nombrePermiso = nombrePermiso;
    }

    public String getIdPermiso() {
        return idPermiso;
    }

    public void setIdPermiso(String idPermiso) {
        this.idPermiso = idPermiso;
    }

    public String getNombrePermiso() {
        return nombrePermiso;
    }

    public void setNombrePermiso(String nombrePermiso) {
        this.nombrePermiso = nombrePermiso;
    }

    @Override
    public String toString() {
        return nombrePermiso == null ? "" : nombrePermiso;
    }
}
