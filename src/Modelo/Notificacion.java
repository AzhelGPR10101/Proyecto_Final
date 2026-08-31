package Modelo;

public class Notificacion {

    private String idNotificacion;
    private String idUsuario;
    private String tipo;
    private String mensaje;
    private String fechaGeneracion;
    private boolean leido;

    public Notificacion() {
    }

    public Notificacion(String idNotificacion, String idUsuario, String tipo, String mensaje,
            String fechaGeneracion, boolean leido) {
        this.idNotificacion = idNotificacion;
        this.idUsuario = idUsuario;
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.fechaGeneracion = fechaGeneracion;
        this.leido = leido;
    }

    public String getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(String idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(String fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public boolean isLeido() {
        return leido;
    }

    public void setLeido(boolean leido) {
        this.leido = leido;
    }
}