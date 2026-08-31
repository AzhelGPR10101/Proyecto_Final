package Modelo;

public class Producto {

    private String codigo;
    private String nombre;
    private String categoria;
    private int cantidad;
    private double precioUnitario;
    private boolean tieneIva;
    private String fechaElaboracion;
    private String fechaVencimiento;
    private int stockMinimo;
    private String ubicacionPasillo;
    private String lote;
    private int stockMaximo;

    private String idNegocio;
    private String idCategoria;
    private String idTasaIva;

    public String getIdNegocio() {
        return idNegocio;
    }

    public void setIdNegocio(String idNegocio) {
        this.idNegocio = idNegocio;
    }

    public String getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(String idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getIdTasaIva() {
        return idTasaIva;
    }

    public void setIdTasaIva(String idTasaIva) {
        this.idTasaIva = idTasaIva;
    }

    public Producto() {
    }

    public Producto(String codigo, String nombre, String categoria, int cantidad,
            double precioUnitario, boolean tieneIva,
            String fechaElaboracion, String fechaVencimiento) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.tieneIva = tieneIva;
        this.fechaElaboracion = fechaElaboracion;
        this.fechaVencimiento = fechaVencimiento;
    }

    public double getPrecioFinal() {
        return tieneIva ? precioUnitario * 1.15 : precioUnitario;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public boolean isTieneIva() {
        return tieneIva;
    }

    public void setTieneIva(boolean tieneIva) {
        this.tieneIva = tieneIva;
    }

    public String getFechaElaboracion() {
        return fechaElaboracion;
    }

    public void setFechaElaboracion(String fechaElaboracion) {
        this.fechaElaboracion = fechaElaboracion;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
      public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public String getUbicacionPasillo() {
        return ubicacionPasillo;
    }

    public void setUbicacionPasillo(String ubicacionPasillo) {
        this.ubicacionPasillo = ubicacionPasillo;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public int getStockMaximo() {
        return stockMaximo;
    }

    public void setStockMaximo(int stockMaximo) {
        this.stockMaximo = stockMaximo;
    }

    @Override
    public String toString() {
        return nombre + " - $" + String.format("%.2f", getPrecioFinal());
    }
}
