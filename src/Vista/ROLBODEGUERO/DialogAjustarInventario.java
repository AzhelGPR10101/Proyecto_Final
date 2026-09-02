
package Vista.ROLBODEGUERO;

import Controladores.ControladorInventarioBodega;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import java.awt.Frame;

public class DialogAjustarInventario extends JDialog {

    private final PanelDialogAjustarInventario panel;
    private boolean guardado = false;

    public DialogAjustarInventario(Frame parent) {
        super(parent, "Ajustar Inventario", true);

        panel = new PanelDialogAjustarInventario();
        panel.getBtnGuardar().addActionListener(e -> guardar());
        panel.getBtnCancelar().addActionListener(e -> dispose());

        add(panel);
        pack();
        setLocationRelativeTo(parent);
        setResizable(false);
        componentes.escalado.KryptonPanelScrollable.envolverJDialog(this);
    }

    public boolean isGuardado() {
        return guardado;
    }

    private void guardar() {
        if (!panel.hayProductoSeleccionado()) {
            JOptionPane.showMessageDialog(this, "Busca y selecciona un producto antes de continuar.",
                    "Producto no seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean exito = ControladorInventarioBodega.ajustarInventario(this,
                panel.getCodigoSeleccionado(), panel.getUbicacion(), panel.getLote(),
                panel.getStockMinimoTexto(), panel.getStockMaximoTexto(), panel.getStockActualTexto());
        if (exito) {
            guardado = true;
            dispose();
        }
    }
}
