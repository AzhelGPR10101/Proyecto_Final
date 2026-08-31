package Vista.COMPRAS;

import javax.swing.*;
import java.awt.*;

public class DialogPagoProveedor extends JDialog {

    private JLabel lblSaldoPendiente;
    private JComboBox<String> cbMetodoPago;
    private JTextField txtMonto;
    private JButton btnConfirmar;
    private JButton btnCancelar;

    private final double saldoPendiente;
    private boolean confirmado = false;
    private String metodoPagoSeleccionado = "Efectivo";
    private double montoIngresado = 0;

    private static final Color COLOR_FONDO = new Color(31, 11, 43);
    private static final Color COLOR_BOTON = new Color(85, 0, 102);
    private static final Color COLOR_TEXTO = Color.WHITE;
    private static final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 15);
    private static final Font FONT_TITULO = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font FONT_TOTAL = new Font("Segoe UI", Font.BOLD, 26);
    private static final Font FONT_BOTON = new Font("Segoe UI", Font.BOLD, 14);

    public DialogPagoProveedor(Frame parent, String nombreProveedor, double saldoPendiente) {
        super(parent, "Pagar a Proveedor", true);
        this.saldoPendiente = saldoPendiente;
        initComponents(nombreProveedor);
        configurarListeners();
        setResizable(false);
        pack();
        setLocationRelativeTo(parent);
    }

    private void initComponents(String nombreProveedor) {
        getContentPane().setBackground(COLOR_FONDO);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        JLabel titulo = new JLabel("PAGAR A " + nombreProveedor.toUpperCase());
        titulo.setFont(FONT_TITULO);
        titulo.setForeground(COLOR_TEXTO);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        add(titulo, gbc);
        gbc.gridwidth = 1;
        row++;

        JLabel lblSaldoTexto = new JLabel("SALDO PENDIENTE:");
        lblSaldoTexto.setFont(FONT_LABEL);
        lblSaldoTexto.setForeground(COLOR_TEXTO);
        gbc.gridx = 0;
        gbc.gridy = row;
        add(lblSaldoTexto, gbc);

        lblSaldoPendiente = new JLabel(String.format("$%.2f", saldoPendiente));
        lblSaldoPendiente.setFont(FONT_TOTAL);
        lblSaldoPendiente.setForeground(new Color(255, 100, 100));
        gbc.gridx = 1;
        gbc.gridy = row;
        add(lblSaldoPendiente, gbc);
        row++;

        JSeparator sep = new JSeparator();
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        add(sep, gbc);
        gbc.gridwidth = 1;
        row++;

        JLabel lblMetodo = new JLabel("Método de pago:");
        lblMetodo.setFont(FONT_LABEL);
        lblMetodo.setForeground(COLOR_TEXTO);
        gbc.gridx = 0;
        gbc.gridy = row;
        add(lblMetodo, gbc);

        cbMetodoPago = new JComboBox<>(new String[]{"Efectivo", "Transferencia"});
        gbc.gridx = 1;
        gbc.gridy = row;
        add(cbMetodoPago, gbc);
        row++;

        JLabel lblMonto = new JLabel("Monto a pagar:");
        lblMonto.setFont(FONT_LABEL);
        lblMonto.setForeground(COLOR_TEXTO);
        gbc.gridx = 0;
        gbc.gridy = row;
        add(lblMonto, gbc);

        txtMonto = new JTextField(String.format("%.2f", saldoPendiente), 10);
        gbc.gridx = 1;
        gbc.gridy = row;
        add(txtMonto, gbc);
        row++;

        JSeparator sep2 = new JSeparator();
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        add(sep2, gbc);
        gbc.gridwidth = 1;
        row++;

        btnConfirmar = new JButton("Confirmar Pago");
        btnCancelar = new JButton("Cancelar");
        estilizarBoton(btnConfirmar);
        estilizarBoton(btnCancelar);

        gbc.gridx = 0;
        gbc.gridy = row;
        add(btnCancelar, gbc);
        gbc.gridx = 1;
        gbc.gridy = row;
        add(btnConfirmar, gbc);
    }

    private void estilizarBoton(JButton btn) {
        btn.setFont(FONT_BOTON);
        btn.setForeground(COLOR_TEXTO);
        btn.setBackground(COLOR_BOTON);
        btn.setFocusPainted(false);
    }

    private void configurarListeners() {
        btnConfirmar.addActionListener(e -> {
            try {
                montoIngresado = Double.parseDouble(txtMonto.getText().trim());
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
            metodoPagoSeleccionado = (String) cbMetodoPago.getSelectedItem();
            confirmado = true;
            dispose();
        });

        btnCancelar.addActionListener(e -> {
            confirmado = false;
            dispose();
        });
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
