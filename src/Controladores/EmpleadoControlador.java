
package Controladores;

import DAO.EmpleadoDAO;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoControlador {

    private static final EmpleadoDAO empleadoDAO = new EmpleadoDAO();

    public static Modelo.Empleado buscarEmpleadoPorCedula(String cedula) {
        return empleadoDAO.buscarPorCedula(cedula);
    }

    public static boolean actualizarEmpleado(java.awt.Component parent,
            String cedulaOriginal, String nombres, String apellidos,
            String sueldoStr, String telefono, String usuario, String password) {

        if (Controladores.Validaciones.camposVacios(nombres, apellidos, sueldoStr, telefono, usuario)) {
            JOptionPane.showMessageDialog(parent, "Por favor, complete todos los campos obligatorios (*).", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (!Controladores.Validaciones.validarSueldoBase(sueldoStr)) {
            JOptionPane.showMessageDialog(parent,
                    String.format("El sueldo debe estar entre $%.2f (Salario Básico Unificado) y $%.2f.",
                            Controladores.Validaciones.SUELDO_BASICO_UNIFICADO_ECUADOR,
                            Controladores.Validaciones.SUELDO_MAXIMO_PERMITIDO),
                    "Sueldo Inválido", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        telefono = telefono.trim();

        if (telefono.length() != 10 || !telefono.matches("\\d+")) {
            JOptionPane.showMessageDialog(parent, "El teléfono debe contener exactamente 10 dígitos numéricos. (Largo actual: " + telefono.length() + ")", "Teléfono Inválido", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (password != null && !password.trim().isEmpty()
                && !Controladores.Validaciones.validarContrasena(password.trim())) {
            JOptionPane.showMessageDialog(parent,
                    "La contraseña debe tener entre 8 y 16 caracteres, con mayúscula, minúscula, número y carácter especial.",
                    "Contraseña Inválida", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        double sueldo = Double.parseDouble(sueldoStr);

        Modelo.Empleado empleadoActualizado = new Modelo.Cajero();
        empleadoActualizado.setIdNegocio(Modelo.Sesion.getIdNegocio());
        empleadoActualizado.setCedula(cedulaOriginal);
        empleadoActualizado.setNombres(nombres);
        empleadoActualizado.setApellidos(apellidos);
        empleadoActualizado.setSueldo(sueldo);
        empleadoActualizado.setTelefono(telefono);
        empleadoActualizado.setCorreo(usuario);
        empleadoActualizado.setPassword(password);

        boolean actualizado = empleadoDAO.actualizar(empleadoActualizado);

        if (actualizado) {
            JOptionPane.showMessageDialog(parent, "¡Empleado actualizado exitosamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(parent, "No se encontró el empleado a modificar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return actualizado;
    }

    public static List<Modelo.Empleado> listarEmpleados() {
        String idNegocio = Modelo.Sesion.getIdNegocio();
        if (idNegocio == null) {
            return new ArrayList<>();
        }
        return empleadoDAO.listarPorNegocio(idNegocio);
    }

    public static List<Modelo.Empleado> filtrarEmpleados(String texto, String opcion) {
        List<Modelo.Empleado> lista = listarEmpleados();
        List<Modelo.Empleado> filtrada = new ArrayList<>();

        boolean esOrden = "Nombre A-Z".equals(opcion) || "Nombre Z-A".equals(opcion)
                || "Fecha_Contratacion".equals(opcion) || "Todos".equals(opcion) || opcion == null;

        for (Modelo.Empleado emp : lista) {
            boolean coincideTexto = texto == null || texto.trim().isEmpty()
                    || emp.getNombres().toLowerCase().contains(texto.toLowerCase())
                    || emp.getApellidos().toLowerCase().contains(texto.toLowerCase())
                    || emp.getCedula().toLowerCase().contains(texto.toLowerCase());

            boolean coincideRol = esOrden || opcion.equalsIgnoreCase(emp.getRol());

            if (coincideTexto && coincideRol) {
                filtrada.add(emp);
            }
        }

        java.text.SimpleDateFormat formato = new java.text.SimpleDateFormat("dd/MM/yyyy");

        if ("Nombre Z-A".equals(opcion)) {
            filtrada.sort((a, b) -> b.getNombres().compareToIgnoreCase(a.getNombres()));
        } else if ("Fecha_Contratacion".equals(opcion)) {
            filtrada.sort((a, b) -> {
                try {
                    return formato.parse(a.getFechaContratacion()).compareTo(formato.parse(b.getFechaContratacion()));
                } catch (java.text.ParseException e) {
                    return 0;
                }
            });
        } else {
            filtrada.sort((a, b) -> a.getNombres().compareToIgnoreCase(b.getNombres()));
        }

        return filtrada;
    }

    public static List<Modelo.Empleado> filtrarEmpleados(String texto, String rolFiltro, String ordenNombre) {
        List<Modelo.Empleado> lista = listarEmpleados();
        List<Modelo.Empleado> filtrada = new ArrayList<>();

        for (Modelo.Empleado emp : lista) {
            boolean coincideTexto = texto == null || texto.trim().isEmpty()
                    || emp.getNombres().toLowerCase().contains(texto.toLowerCase())
                    || emp.getApellidos().toLowerCase().contains(texto.toLowerCase())
                    || emp.getCedula().toLowerCase().contains(texto.toLowerCase());

            boolean coincideRol = rolFiltro == null || "Todos".equals(rolFiltro)
                    || rolFiltro.equalsIgnoreCase(emp.getRol());

            if (coincideTexto && coincideRol) {
                filtrada.add(emp);
            }
        }

        if ("Nombre Z-A".equals(ordenNombre)) {
            filtrada.sort((a, b) -> b.getNombres().compareToIgnoreCase(a.getNombres()));
        } else {
            filtrada.sort((a, b) -> a.getNombres().compareToIgnoreCase(b.getNombres()));
        }

        return filtrada;
    }

    public static boolean eliminarEmpleado(java.awt.Component parent, String cedula) {
        boolean eliminado = empleadoDAO.eliminar(Modelo.Sesion.getIdNegocio(), cedula);
        if (!eliminado) {
            JOptionPane.showMessageDialog(parent, "No se encontró el empleado con cédula: " + cedula, "Empleado no encontrado", JOptionPane.WARNING_MESSAGE);
        }
        return eliminado;
    }
        public static List<Modelo.Empleado> listarEmpleadosInactivos() {
        String idNegocio = Modelo.Sesion.getIdNegocio();
        if (idNegocio == null) {
            return new ArrayList<>();
        }
        return empleadoDAO.listarInactivosPorNegocio(idNegocio);
    }

    public static boolean reactivarEmpleado(java.awt.Component parent, String cedula) {
        boolean reactivado = empleadoDAO.reactivar(Modelo.Sesion.getIdNegocio(), cedula);
        if (!reactivado) {
            JOptionPane.showMessageDialog(parent, "No se encontró el empleado con cédula: " + cedula, "Empleado no encontrado", JOptionPane.WARNING_MESSAGE);
        }
        return reactivado;
    }

    public static class EmpleadoActual {
        public final String idEmpleado;
        public final String nombreCompleto;

        public EmpleadoActual(String idEmpleado, String nombreCompleto) {
            this.idEmpleado = idEmpleado;
            this.nombreCompleto = nombreCompleto;
        }
    }

    public static EmpleadoActual resolverEmpleadoDeSesion() {
        String nombreSesion = Modelo.Sesion.getNombreUsuario() + " " + Modelo.Sesion.getApellidosUsuario();
        if (Modelo.Sesion.esDueno()) {
            String idEmpleado = empleadoDAO.asegurarEmpleadoDueno(
                    Modelo.Sesion.getIdUsuario(), Modelo.Sesion.getIdNegocio());
            return new EmpleadoActual(idEmpleado, nombreSesion);
        }
        Modelo.Empleado empleadoSesion = empleadoDAO.buscarPorCedula(Modelo.Sesion.getCedulaUsuario());
        if (empleadoSesion != null) {
            return new EmpleadoActual(empleadoSesion.getIdEmpleado(),
                    empleadoSesion.getNombres() + " " + empleadoSesion.getApellidos());
        }
        return new EmpleadoActual(null, nombreSesion);
    }
}
