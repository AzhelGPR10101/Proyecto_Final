package Vista.FACTURACION;

import javax.swing.*;
import java.awt.*;

public class DialogCobro extends JDialog {

    private JLabel lblTotalPagar;
    private JTextField txtEfectivo;
    private JTextField txtCambio;
    private JButton btnConfirmarPago;
    private JButton btnCancelar;

    private final double totalFactura;
    private boolean confirmado = false;
    private double efectivoIngresado = 0;

    private static final Color COLOR_FONDO = new Color(31, 11, 43);
    private static final Color COLOR_BOTON = new Color(85, 0, 102);
    private static final Color COLOR_TEXTO = Color.WHITE;
    private static final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 15);
    private static final Font FONT_TITULO = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font FONT_TOTAL = new Font("Segoe UI", Font.BOLD, 26);
    private static final Font FONT_BOTON = new Font("Segoe UI", Font.BOLD, 14);

    public DialogCobro(Frame parent, double total) {
        super(parent, "Cobrar Factura", true);
        this.totalFactura = total;
        initComponents();
        configurarListeners();
        setResizable(false);
        pack();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        getContentPane().setBackground(COLOR_FONDO);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        JLabel titulo = new JLabel("COBRAR FACTURA");
        titulo.setFont(FONT_TITULO);
        titulo.setForeground(COLOR_TEXTO);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        add(titulo, gbc);
        gbc.gridwidth = 1;
        row++;

        JLabel lblTotalTexto = new JLabel("TOTAL A PAGAR:");
        lblTotalTexto.setFont(FONT_LABEL);
        lblTotalTexto.setForeground(COLOR_TEXTO);
        gbc.gridx = 0;
        gbc.gridy = row;
        add(lblTotalTexto, gbc);

        lblTotalPagar = new JLabel(String.format("$%.2f", totalFactura));
        lblTotalPagar.setFont(FONT_TOTAL);
        lblTotalPagar.setForeground(new Color(0, 230, 150));
        gbc.gridx = 1;
        gbc.gridy = row;
        add(lblTotalPagar, gbc);
        row++;

        JSeparator sep = new JSeparator();
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        add(sep, gbc);
        gbc.gridwidth = 1;
        row++;

        JLabel lblEfectivo = new JLabel("Efectivo:");
        lblEfectivo.setFont(FONT_LABEL);
        lblEfectivo.setForeground(COLOR_TEXTO);
        gbc.gridx = 0;
        gbc.gridy = row;
        add(lblEfectivo, gbc);

        txtEfectivo = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = row;
        add(txtEfectivo, gbc);
        row++;

        JLabel lblCambio = new JLabel("Cambio:");
        lblCambio.setFont(FONT_LABEL);
        lblCambio.setForeground(COLOR_TEXTO);
        gbc.gridx = 0;
        gbc.gridy = row;
        add(lblCambio, gbc);

        txtCambio = new JTextField(10);
        txtCambio.setEditable(false);
        gbc.gridx = 1;
        gbc.gridy = row;
        add(txtCambio, gbc);
        row++;

        JSeparator sep2 = new JSeparator();
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        add(sep2, gbc);
        gbc.gridwidth = 1;
        row++;

        btnConfirmarPago = new JButton("Confirmar Pago");
        btnCancelar = new JButton("Cancelar");
        estilizarBoton(btnConfirmarPago);
        estilizarBoton(btnCancelar);

        gbc.gridx = 0;
        gbc.gridy = row;
        add(btnCancelar, gbc);
        gbc.gridx = 1;
        gbc.gridy = row;
        add(btnConfirmarPago, gbc);
    }

    private void estilizarBoton(JButton btn) {
        btn.setFont(FONT_BOTON);
        btn.setForeground(COLOR_TEXTO);
        btn.setBackground(COLOR_BOTON);
        btn.setFocusPainted(false);
    }

    private void actualizarCambio() {
        String texto = txtEfectivo.getText().trim();
        if (texto.isEmpty()) {
            txtCambio.setText("");
            return;
        }
        try {
            double efectivo = Double.parseDouble(texto);
            double cambio = efectivo - totalFactura;
            txtCambio.setText(cambio < 0 ? "0.00" : String.format("%.2f", cambio));
        } catch (NumberFormatException ex) {
            txtCambio.setText("");
        }
    }

    private void configurarListeners() {
        txtEfectivo.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { actualizarCambio(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { actualizarCambio(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { actualizarCambio(); }
        });

        btnConfirmarPago.addActionListener(e -> {
            try {
                efectivoIngresado = Double.parseDouble(txtEfectivo.getText().trim());
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
        return "Efectivo";
    }

    public double getEfectivoIngresado() {
        return efectivoIngresado;
    }
}