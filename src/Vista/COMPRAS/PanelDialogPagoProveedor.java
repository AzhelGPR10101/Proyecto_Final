package Vista.COMPRAS;

public class PanelDialogPagoProveedor extends javax.swing.JPanel {

    public PanelDialogPagoProveedor() {
        initComponents();
    }

    public void cargarDatos(String nombreProveedor, double saldoPendiente) {
        lblTitulo.setText("PAGAR A " + nombreProveedor.toUpperCase());
        lblSaldoPendiente.setText(String.format("$%.2f", saldoPendiente));
        txtMonto.setText(String.format("%.2f", saldoPendiente));
    }

    public String getMetodoPago() {
        return (String) cbMetodoPago.getSelectedItem();
    }

    public String getMontoTexto() {
        return txtMonto.getText();
    }

    public javax.swing.JButton getBtnConfirmar() {
        return btnConfirmar;
    }

    public javax.swing.JButton getBtnCancelar() {
        return btnCancelar;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitulo = new javax.swing.JLabel();
        lblSaldoTexto = new javax.swing.JLabel();
        lblSaldoPendiente = new javax.swing.JLabel();
        sep1 = new javax.swing.JSeparator();
        lblMetodo = new javax.swing.JLabel();
        cbMetodoPago = new javax.swing.JComboBox<>();
        lblMonto = new javax.swing.JLabel();
        txtMonto = new javax.swing.JTextField();
        sep2 = new javax.swing.JSeparator();
        btnCancelar = new javax.swing.JButton();
        btnConfirmar = new javax.swing.JButton();

        setBackground(new java.awt.Color(31, 11, 43));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("PAGAR A PROVEEDOR");
        add(lblTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 15, 380, 35));

        lblSaldoTexto.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblSaldoTexto.setForeground(new java.awt.Color(255, 255, 255));
        lblSaldoTexto.setText("SALDO PENDIENTE:");
        add(lblSaldoTexto, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 65, 180, 30));

        lblSaldoPendiente.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        lblSaldoPendiente.setForeground(new java.awt.Color(255, 100, 100));
        lblSaldoPendiente.setText("$0.00");
        add(lblSaldoPendiente, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 60, 170, 40));

        add(sep1, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 105, 355, 10));

        lblMetodo.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblMetodo.setForeground(new java.awt.Color(255, 255, 255));
        lblMetodo.setText("Método de pago:");
        add(lblMetodo, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 135, 150, 25));

        cbMetodoPago.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Efectivo", "Transferencia" }));
        add(cbMetodoPago, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 135, 180, 25));

        lblMonto.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblMonto.setForeground(new java.awt.Color(255, 255, 255));
        lblMonto.setText("Monto a pagar:");
        add(lblMonto, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 175, 150, 25));

        add(txtMonto, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 175, 180, 25));

        add(sep2, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 215, 355, 10));

        btnCancelar.setBackground(new java.awt.Color(85, 0, 102));
        btnCancelar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnCancelar.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelar.setText("Cancelar");
        btnCancelar.setFocusPainted(false);
        add(btnCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(55, 250, 150, 35));

        btnConfirmar.setBackground(new java.awt.Color(85, 0, 102));
        btnConfirmar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnConfirmar.setForeground(new java.awt.Color(255, 255, 255));
        btnConfirmar.setText("Confirmar Pago");
        btnConfirmar.setFocusPainted(false);
        add(btnConfirmar, new org.netbeans.lib.awtextra.AbsoluteConstraints(215, 250, 165, 35));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnConfirmar;
    private javax.swing.JComboBox<String> cbMetodoPago;
    private javax.swing.JLabel lblMetodo;
    private javax.swing.JLabel lblMonto;
    private javax.swing.JLabel lblSaldoPendiente;
    private javax.swing.JLabel lblSaldoTexto;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JSeparator sep1;
    private javax.swing.JSeparator sep2;
    private javax.swing.JTextField txtMonto;
    // End of variables declaration//GEN-END:variables
}
