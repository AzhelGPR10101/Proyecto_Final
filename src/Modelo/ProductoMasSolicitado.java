package Modelo;

public class ProductoMasSolicitado {

    private String nombreProducto;
    private int totalDespachado;

    public ProductoMasSolicitado() {
    }

    public ProductoMasSolicitado(String nombreProducto, int totalDespachado) {
        this.nombreProducto = nombreProducto;
        this.totalDespachado = totalDespachado;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public int getTotalDespachado() {
        return totalDespachado;
    }

    public void setTotalDespachado(int totalDespachado) {
        this.totalDespachado = totalDespachado;
    }
}
