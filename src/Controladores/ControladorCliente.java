package Controladores;

import DAO.ClienteDAO;
import Modelo.Cliente;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;

public class ControladorCliente {

    private final ClienteDAO clienteDAO = new ClienteDAO();

    public boolean registrarCliente(java.awt.Component parent, String tipoDocumento, String numeroDocumento,
            String nombreCliente, String telefono, String correo) {

        if (Controladores.Validaciones.camposVacios(numeroDocumento, nombreCliente)) {
            JOptionPane.showMessageDialog(parent, "El documento y el nombre del cliente son obligatorios.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        Cliente cliente = new Cliente(tipoDocumento, numeroDocumento, nombreCliente, telefono, correo);
        boolean guardado = clienteDAO.registrar(cliente);
        if (!guardado) {
            JOptionPane.showMessageDialog(parent, "El documento ya está registrado o no hay un negocio activo en la sesión.", "No se pudo registrar", JOptionPane.WARNING_MESSAGE);
        }
        return guardado;
    }

    public String registrarCliente(String tipoDocumento, String cedula, String nombre,
            String apellido, String telefono, String correo, String direccion) {

        if (Controladores.Validaciones.camposVacios(cedula, nombre, apellido)) {
            return "La cedula, el nombre y el apellido son obligatorios.";
        }
        if (!Controladores.Validaciones.validarTelefono(telefono)) {
            return "El telefono debe tener exactamente 10 digitos.";
        }
        if (!Controladores.Validaciones.validarCorreo(correo)) {
            return "Ingrese un correo electronico valido.";
        }

        Cliente cliente = new Cliente(cedula, nombre, apellido, telefono, correo, direccion);
        cliente.setTipoDocumento(tipoDocumento);
        boolean guardado = clienteDAO.registrar(cliente);
        return guardado ? "OK" : "El documento ya esta registrado o no hay un negocio activo en la sesion.";
    }

    public boolean modificar(Cliente cliente) {
        return clienteDAO.modificar(cliente);
    }

    public boolean eliminar(String numeroDocumento) {
        return clienteDAO.eliminar(numeroDocumento);
    }

    public Cliente buscarPorDocumento(String numeroDocumento) {
        return clienteDAO.buscarPorDocumento(numeroDocumento);
    }

    public List<Cliente> listarTodos() {
        return clienteDAO.listarTodos();
    }

    public List<Cliente> filtrarClientes(String texto) {
        List<Cliente> lista = listarTodos();
        List<Cliente> filtrada = new ArrayList<>();
        for (Cliente c : lista) {
            boolean coincide = texto == null || texto.trim().isEmpty()
                    || c.getNombreCliente().toLowerCase().contains(texto.toLowerCase())
                    || c.getNumeroDocumento().toLowerCase().contains(texto.toLowerCase());
            if (coincide) {
                filtrada.add(c);
            }
        }
        filtrada.sort((a, b) -> a.getNombreCliente().compareToIgnoreCase(b.getNombreCliente()));
        return filtrada;
    }

    public List<Cliente> filtrarClientes(String texto, String orden) {
        List<Cliente> filtrada = filtrarClientes(texto);
        if ("Apellido A-Z".equals(orden)) {
            filtrada.sort((a, b) -> a.getApellido().compareToIgnoreCase(b.getApellido()));
        } else {
            filtrada.sort((a, b) -> a.getNombre().compareToIgnoreCase(b.getNombre()));
        }
        return filtrada;
    }

    public Cliente buscarPorCedula(String cedula) {
        return buscarPorDocumento(cedula);
    }
}
