package Vista.COMPRAS;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import java.awt.Frame;

public class DialogPagoProveedor extends JDialog {

    private final PanelDialogPagoProveedor panel;
    private final double saldoPendiente;
    private boolean confirmado = false;
    private String metodoPagoSeleccionado = "Efectivo";
    private double montoIngresado = 0;

    public DialogPagoProveedor(Frame parent, String nombreProveedor, double saldoPendiente) {
        super(parent, "Pagar a Proveedor", true);
        this.saldoPendiente = saldoPendiente;

        panel = new PanelDialogPagoProveedor();
        panel.cargarDatos(nombreProveedor, saldoPendiente);
        panel.getBtnConfirmar().addActionListener(e -> confirmarPago());
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
            montoIngresado = Double.parseDouble(panel.getMontoTexto().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un monto válido.");
            return;
        }
        if (montoIngresado <= 0) {
            JOptionPane.showMessageDialog(this, "El monto debe ser mayor a cero.");
            return;
        }
        if (montoIngresado > saldoPendiente + 0.005) {
            JOptionPane.showMessageDialog(this, "El monto no puede superar el saldo pendiente.");
            return;
        }
        metodoPagoSeleccionado = panel.getMetodoPago();
        confirmado = true;
        dispose();
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public String getMetodoPago() {
        return metodoPagoSeleccionado;
    }

    public double getMontoIngresado() {
        return montoIngresado;
    }
}
