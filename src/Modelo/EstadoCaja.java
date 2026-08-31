
package Modelo;

public enum EstadoCaja {
    ABIERTA(" CAJA ABIERTA"),
    CERRADA(" CAJA CERRADA");

    private final String etiqueta;

    EstadoCaja(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }
}
