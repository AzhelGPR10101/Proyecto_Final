package Vista.PRODUCTOS;

import javax.swing.JDialog;
import java.awt.Frame;

public class DialogModificarProducto extends JDialog {

    private final PanelDialogModificarProducto panel;
    private final String codigoOriginal;
    private boolean guardado = false;

    public DialogModificarProducto(Modelo.Producto producto) {
        super((Frame) null, "Modificar Producto", true);
        this.codigoOriginal = producto.getCodigo();

        panel = new PanelDialogModificarProducto();
        panel.cargarDatos(producto);
        panel.getBtnGuardar().addActionListener(e -> guardarCambios());
        panel.getBtnCancelar().addActionListener(e -> dispose());

        add(panel);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void guardarCambios() {
        boolean exito = Controladores.ControladorProducto.actualizarProducto(
                this,
                codigoOriginal,
                panel.getNombre(),
                panel.getCategoria(),
                panel.getCantidad(),
                panel.getPrecio(),
                panel.isTieneIva(),
                panel.getStockMinimo()
        );

        if (exito) {
            guardado = true;
            dispose();
        }
    }

    public boolean isGuardado() {
        return guardado;
    }
}
