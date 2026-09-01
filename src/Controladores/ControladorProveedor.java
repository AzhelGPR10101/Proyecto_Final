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
