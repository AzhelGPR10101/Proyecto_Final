package Modelo;

public class Cliente {
    private String idCliente;
    private String idNegocio;
    private String tipoDocumento;
    private String numeroDocumento;
    private String nombreCliente;
    private String telefono;
    private String correo;
    private String direccion;

    public Cliente() {
    }

    public Cliente(String tipoDocumento, String numeroDocumento, String nombreCliente, String telefono, String correo) {
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.nombreCliente = nombreCliente;
        this.telefono = telefono;
        this.correo = correo;
    }

    public Cliente(String cedula, String nombre, String apellido, String telefonoCliente, String correoCliente, String direccionCliente) {
        this.tipoDocumento = "Cedula";
        this.numeroDocumento = cedula;
        this.nombreCliente = (apellido == null || apellido.trim().isEmpty()) ? nombre : nombre + " " + apellido;
        this.telefono = telefonoCliente;
        this.correo = correoCliente;
        this.direccion = direccionCliente;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getIdNegocio() {
        return idNegocio;
    }

    public void setIdNegocio(String idNegocio) {
        this.idNegocio = idNegocio;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCedula() {
        return numeroDocumento;
    }

    public String getNombre() {
        if (nombreCliente == null) return "";
        int espacio = nombreCliente.indexOf(' ');
        return espacio == -1 ? nombreCliente : nombreCliente.substring(0, espacio);
    }

    public String getApellido() {
        if (nombreCliente == null) return "";
        int espacio = nombreCliente.indexOf(' ');
        return espacio == -1 ? "" : nombreCliente.substring(espacio + 1);
    }

    public String getDireccion() {
        return direccion == null ? "" : direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return nombreCliente == null ? "" : nombreCliente;
    }
}
