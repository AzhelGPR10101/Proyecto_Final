package Vista.FACTURACION;

import Modelo.DetalleFactura;
import Modelo.MovimientoFinanciero;

import javax.swing.JDialog;
import java.awt.Frame;
import java.util.List;

public class DialogDetalleFactura extends JDialog {

    public DialogDetalleFactura(Frame parent, MovimientoFinanciero movimiento, List<DetalleFactura> detalles) {
        super(parent, "Detalle factura " + safe(movimiento.getReferencia()), true);

        PanelDialogDetalleFactura panel = new PanelDialogDetalleFactura();
        panel.cargarDatos(movimiento, detalles);
        panel.getBtnCerrar().addActionListener(e -> dispose());

        add(panel);
        setResizable(false);
        pack();
        setLocationRelativeTo(parent);
    }

    private static String safe(String texto) {
        return texto != null ? texto : "";
    }
}
