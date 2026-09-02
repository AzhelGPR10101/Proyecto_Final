package Controladores;

import DAO.ModuloDAO;
import Modelo.Modulo;
import java.util.List;

public class ControladorModulo {

    private final ModuloDAO moduloDAO = new ModuloDAO();

    public List<Modulo> obtenerModulosDeNegocio(String idNegocio) {
        return moduloDAO.obtenerModulosDeNegocio(idNegocio);
    }

    public List<Modulo> obtenerCatalogoCompleto() {
        return moduloDAO.obtenerModulos();
    }

    public List<String> obtenerNombresModulosActivos(String idNegocio) {
        return moduloDAO.obtenerNombresModulosActivos(idNegocio);
    }

    public boolean activarModulo(String idNegocio, String nombreModulo) {
        String idModulo = moduloDAO.buscarIdPorNombre(nombreModulo);
        if (idModulo == null) {
            return false;
        }
        return moduloDAO.activarModulo(idNegocio, idModulo);
    }

    public boolean desactivarModulo(String idNegocio, String nombreModulo) {
        if ("Configuración".equals(nombreModulo)) {
            return true;
        }
        String idModulo = moduloDAO.buscarIdPorNombre(nombreModulo);
        if (idModulo == null) {
            return false;
        }
        return moduloDAO.desactivarModulo(idNegocio, idModulo);
    }
}
