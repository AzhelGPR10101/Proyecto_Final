
package Vista.PROVEEDORES;

import javax.swing.JDialog;
import java.awt.Frame;

public class DialogModificarProveedor extends JDialog {

    private final PanelDialogModificarProveedor panel;
    private final String rucOriginal;

    public DialogModificarProveedor(Modelo.Proveedores proveedor) {
        super((Frame) null, "Modificar Proveedor", true);
        this.rucOriginal = proveedor.getRuc();

        panel = new PanelDialogModificarProveedor();
        panel.cargarDatos(proveedor);
        panel.getBtnGuardar().addActionListener(e -> guardarCambios());
        panel.getBtnCancelar().addActionListener(e -> dispose());

        add(panel);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void guardarCambios() {
        Modelo.Proveedores proveedor = new Modelo.Proveedores(
                rucOriginal,
                panel.getNombreEmpresa().trim(),
                panel.getNombreContacto().trim(),
                panel.getTelefono().trim(),
                panel.getCorreo().trim(),
                panel.getDireccion().trim()
        );

        boolean exito = new Controladores.ControladorProveedor().modificar(proveedor);

        if (exito) {
            dispose();
        }
    }
}
