package Modelo;

public class CategoriaProducto {
    private String idCategoria;
    private String idNegocio;
    private String nombreCategoria;

    public CategoriaProducto() {
    }

    public CategoriaProducto(String idNegocio, String nombreCategoria) {
        this.idNegocio = idNegocio;
        this.nombreCategoria = nombreCategoria;
    }

    public String getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(String idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getIdNegocio() {
        return idNegocio;
    }

    public void setIdNegocio(String idNegocio) {
        this.idNegocio = idNegocio;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }
}
