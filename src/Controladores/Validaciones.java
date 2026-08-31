package Controladores;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validaciones {

    public static boolean validarCedula(String cedula) {
        if (cedula == null || !cedula.matches("\\d{10}")) {
            return false;
        }

        int provincia = Integer.parseInt(cedula.substring(0, 2));
        if (provincia < 1 || provincia > 24) {
            return false;
        }

        int digitoVerificador = Integer.parseInt(cedula.substring(9, 10));
        int suma = 0;
        int[] coeficientes = {2, 1, 2, 1, 2, 1, 2, 1, 2};

        for (int i = 0; i < 9; i++) {
            int valor = Integer.parseInt(cedula.substring(i, i + 1)) * coeficientes[i];
            if (valor >= 10) {
                valor -= 9;
            }
            suma += valor;
        }

        int resultado = (suma % 10 == 0) ? 0 : 10 - (suma % 10);
        if (resultado != digitoVerificador) {
            return false;
        }

        return true;
    }

    public static boolean soloLetras(String texto, String nombreCampo) {
        if (texto == null || !texto.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) {
            return false;
        }
        return true;
    }

    public static boolean validarSueldo(String sueldoStr) {
        try {
            double sueldo = Double.parseDouble(sueldoStr);
            return sueldo >= 0 && sueldo <= 20000;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean validarFecha(String fechaStr) {
        if (fechaStr == null || fechaStr.trim().isEmpty()) {
            return false;
        }
        java.text.SimpleDateFormat formato = new java.text.SimpleDateFormat("dd/MM/yyyy");
        formato.setLenient(false);
        try {
            formato.parse(fechaStr.trim());
            return true;
        } catch (java.text.ParseException e) {
            return false;
        }
    }

    public static boolean camposVacios(String... campos) {
        for (String campo : campos) {
            if (campo == null || campo.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public static boolean validarRuc(String ruc) {
        return ruc != null && ruc.matches("^\\d{13}$");
    }

    public static boolean validarTelefono(String telefono) {
        return telefono != null && telefono.matches("^\\d{10}$");
    }

    public static boolean validarCorreo(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            return false;
        }
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(correo.trim());
        return matcher.matches();
    }

    public static String aMayusculas(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.trim().toUpperCase();
    }

    public static boolean validarCodigoProducto(String codigo) {
        return codigo != null && codigo.trim().matches("\\d{8,13}");
    }

    public static boolean validarNombreProducto(String nombre) {
        if (nombre == null) {
            return false;
        }
        String limpio = nombre.trim();
        if (limpio.isEmpty() || limpio.length() > 15) {
            return false;
        }
        return limpio.matches("[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ ]+");
    }

    public static boolean validarCantidadStock(String cantidadStr) {
        if (cantidadStr == null) {
            return false;
        }
        String limpio = cantidadStr.trim();
        if (!limpio.matches("\\d{1,8}")) {
            return false;
        }
        try {
            return Integer.parseInt(limpio) >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean validarPrecioUnitario(String precioStr) {
        if (precioStr == null) {
            return false;
        }
        String limpio = precioStr.trim();
        if (!limpio.matches("\\d{1,8}(\\.\\d+)?")) {
            return false;
        }
        try {
            return Double.parseDouble(limpio) >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static java.awt.Component ventanaPadre(java.awt.Component parent) {
        java.awt.Window ventana = javax.swing.SwingUtilities.getWindowAncestor(parent);
        return ventana != null ? ventana : parent;
    }

    private static final Pattern PATRON_CONTRASENA = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{8,16}$");

    public static boolean validarContrasena(String contrasena) {
        return contrasena != null && PATRON_CONTRASENA.matcher(contrasena).matches();
    }

    public static String generarContrasenaTemporal() {
        String mayus = "ABCDEFGHJKLMNPQRSTUVWXYZ";
        String minus = "abcdefghijkmnpqrstuvwxyz";
        String digitos = "23456789";
        String especiales = "!@#$%*?";
        java.security.SecureRandom rnd = new java.security.SecureRandom();

        java.util.List<Character> chars = new java.util.ArrayList<>();
        chars.add(mayus.charAt(rnd.nextInt(mayus.length())));
        chars.add(minus.charAt(rnd.nextInt(minus.length())));
        chars.add(digitos.charAt(rnd.nextInt(digitos.length())));
        chars.add(especiales.charAt(rnd.nextInt(especiales.length())));

        String todos = mayus + minus + digitos + especiales;
        for (int i = 0; i < 6; i++) {
            chars.add(todos.charAt(rnd.nextInt(todos.length())));
        }
        java.util.Collections.shuffle(chars, rnd);

        StringBuilder sb = new StringBuilder();
        for (char c : chars) {
            sb.append(c);
        }
        return sb.toString();
    }
}
