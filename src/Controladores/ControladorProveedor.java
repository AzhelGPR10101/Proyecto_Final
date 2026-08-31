package Controladores;

import DAO.ProveedorDAO;
import Modelo.Proveedores;
import java.util.ArrayList;
import java.util.List;

public class ControladorProveedor {

    private final ProveedorDAO proveedorDAO = new ProveedorDAO();

    public boolean guardar(Proveedores nuevoProveedor) {
        return proveedorDAO.guardar(nuevoProveedor);
    }

    public boolean registrarProveedor(java.awt.Component parent, String ruc, String empresa,
            String contacto, String telefono, String correo, String direccion) {

        if (empresa.isEmpty() || contacto.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(parent,
                    "Los campos Nombre Empresa y Nombre Contacto son obligatorios.",
                    "Campos Incompletos", javax.swing.JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!Controladores.Validaciones.validarRuc(ruc)) {
            javax.swing.JOptionPane.showMessageDialog(parent,
                    "El RUC ingresado es inválido. Debe contener exactamente 13 dígitos numéricos.",
                    "Error de Validación", javax.swing.JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!Controladores.Validaciones.validarTelefono(telefono)) {
            javax.swing.JOptionPane.showMessageDialog(parent,
                    "El número de teléfono debe tener exactamente 10 dígitos numéricos.",
                    "Error de Validación", javax.swing.JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!Controladores.Validaciones.validarCorreo(correo)) {
            javax.swing.JOptionPane.showMessageDialog(parent,
                    "Por favor, ingrese un correo electrónico válido (ej: usuario@ejemplo.com).",
                    "Error de Validación", javax.swing.JOptionPane.WARNING_MESSAGE);
            return false;
        }

        Modelo.Proveedores nuevo = new Modelo.Proveedores(ruc, empresa, contacto, telefono, correo, direccion);
        boolean guardado = guardar(nuevo);
        if (!guardado) {
            javax.swing.JOptionPane.showMessageDialog(parent,
                    "El RUC ya se encuentra registrado o no hay un negocio activo en la sesión.",
                    "No se pudo registrar", javax.swing.JOptionPane.WARNING_MESSAGE);
        }
        return guardado;
    }

    public Proveedores buscarPorRuc(String ruc) {
        return proveedorDAO.buscarPorRuc(ruc);
    }

    public boolean modificar(Proveedores proveedorModificado) {
        proveedorModificado.setIdNegocio(Modelo.Sesion.getIdNegocio());
        return proveedorDAO.modificar(proveedorModificado);
    }

    public List<Proveedores> listarTodos() {
        return proveedorDAO.listarTodos();
    }

    public List<Proveedores> filtrarProveedores(String texto, String orden) {
        List<Proveedores> lista = listarTodos();
        List<Proveedores> filtrada = new ArrayList<>();

        for (Proveedores prov : lista) {
            boolean coincideTexto = texto == null || texto.trim().isEmpty()
                    || prov.getNombreEmpresa().toLowerCase().contains(texto.toLowerCase())
                    || prov.getNombreContacto().toLowerCase().contains(texto.toLowerCase())
                    || prov.getRuc().toLowerCase().contains(texto.toLowerCase());

            if (coincideTexto) {
                filtrada.add(prov);
            }
        }

        if ("Nombre del dueño A-Z".equals(orden)) {
            filtrada.sort((a, b) -> a.getNombreContacto().compareToIgnoreCase(b.getNombreContacto()));
        } else {
            filtrada.sort((a, b) -> a.getNombreEmpresa().compareToIgnoreCase(b.getNombreEmpresa()));
        }

        return filtrada;
    }

    public boolean eliminar(String ruc) {
        return proveedorDAO.eliminar(Modelo.Sesion.getIdNegocio(), ruc);
    }
}
