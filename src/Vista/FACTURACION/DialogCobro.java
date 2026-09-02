package Vista.FACTURACION;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import java.awt.Frame;

public class DialogCobro extends JDialog {

    private final PanelDialogCobro panel;
    private final double totalFactura;
    private boolean confirmado = false;
    private double efectivoIngresado = 0;

    public DialogCobro(Frame parent, double total) {
        super(parent, "Cobrar Factura", true);
        this.totalFactura = total;

        panel = new PanelDialogCobro();
        panel.cargarDatos(total);
        panel.getBtnConfirmarPago().addActionListener(e -> confirmarPago());
        panel.getBtnCancelar().addActionListener(e -> {
            confirmado = false;
            dispose();
        });

        add(panel);
        setResizable(false);
        pack();
        setLocationRelativeTo(parent);
    }

    private void confirmarPago() {
        try {
            efectivoIngresado = Double.parseDouble(panel.getEfectivoTexto().trim());
            if (efectivoIngresado < totalFactura) {
                JOptionPane.showMessageDialog(this, "El efectivo es menor al total a pagar.");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un valor de efectivo válido.");
            return;
        }

        confirmado = true;
        dispose();
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public String getMetodoPago() {
        return "Efectivo";
    }

    public double getEfectivoIngresado() {
        return efectivoIngresado;
    }
}
