package Modelo;

public enum MetodoPago {
    EFECTIVO("Efectivo"),
    TARJETA("Tarjeta"),
    TRANSFERENCIA("Transferencia");

    private final String nombreEnBD;

    MetodoPago(String nombreEnBD) {
        this.nombreEnBD = nombreEnBD;
    }

    public String getNombreEnBD() {
        return nombreEnBD;
    }

    public static MetodoPago desdeNombreEnBD(String nombre) {
        for (MetodoPago metodo : values()) {
            if (metodo.nombreEnBD.equalsIgnoreCase(nombre)) {
                return metodo;
            }
        }
        return null;
    }
}
