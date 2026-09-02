package Vista.FACTURACION;

public class PanelDialogCobro extends javax.swing.JPanel {

    private double totalFactura;

    public PanelDialogCobro() {
        initComponents();
        txtEfectivo.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { actualizarCambio(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { actualizarCambio(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { actualizarCambio(); }
        });
    }

    public void cargarDatos(double total) {
        this.totalFactura = total;
        lblTotalPagar.setText(String.format("$%.2f", total));
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

    public String getEfectivoTexto() {
        return txtEfectivo.getText();
    }

    public javax.swing.JButton getBtnConfirmarPago() {
        return btnConfirmarPago;
    }

    public javax.swing.JButton getBtnCancelar() {
        return btnCancelar;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitulo = new javax.swing.JLabel();
        lblTotalTexto = new javax.swing.JLabel();
        lblTotalPagar = new javax.swing.JLabel();
        sep1 = new javax.swing.JSeparator();
        lblEfectivo = new javax.swing.JLabel();
        txtEfectivo = new javax.swing.JTextField();
        lblCambio = new javax.swing.JLabel();
        txtCambio = new javax.swing.JTextField();
        sep2 = new javax.swing.JSeparator();
        btnCancelar = new javax.swing.JButton();
        btnConfirmarPago = new javax.swing.JButton();

        setBackground(new java.awt.Color(31, 11, 43));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("COBRAR FACTURA");
        add(lblTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 15, 380, 35));

        lblTotalTexto.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblTotalTexto.setForeground(new java.awt.Color(255, 255, 255));
        lblTotalTexto.setText("TOTAL A PAGAR:");
        add(lblTotalTexto, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 65, 170, 30));

        lblTotalPagar.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        lblTotalPagar.setForeground(new java.awt.Color(0, 230, 150));
        lblTotalPagar.setText("$0.00");
        add(lblTotalPagar, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 60, 180, 40));

        add(sep1, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 105, 355, 10));

        lblEfectivo.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblEfectivo.setForeground(new java.awt.Color(255, 255, 255));
        lblEfectivo.setText("Efectivo:");
        add(lblEfectivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 135, 150, 25));

        add(txtEfectivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 135, 180, 25));

        lblCambio.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblCambio.setForeground(new java.awt.Color(255, 255, 255));
        lblCambio.setText("Cambio:");
        add(lblCambio, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 170, 150, 25));

        txtCambio.setEditable(false);
        add(txtCambio, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 170, 180, 25));

        add(sep2, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 210, 355, 10));

        btnCancelar.setBackground(new java.awt.Color(85, 0, 102));
        btnCancelar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnCancelar.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelar.setText("Cancelar");
        btnCancelar.setFocusPainted(false);
        add(btnCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(55, 245, 150, 35));

        btnConfirmarPago.setBackground(new java.awt.Color(85, 0, 102));
        btnConfirmarPago.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnConfirmarPago.setForeground(new java.awt.Color(255, 255, 255));
        btnConfirmarPago.setText("Confirmar Pago");
        btnConfirmarPago.setFocusPainted(false);
        add(btnConfirmarPago, new org.netbeans.lib.awtextra.AbsoluteConstraints(215, 245, 165, 35));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnConfirmarPago;
    private javax.swing.JLabel lblCambio;
    private javax.swing.JLabel lblEfectivo;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTotalPagar;
    private javax.swing.JLabel lblTotalTexto;
    private javax.swing.JSeparator sep1;
    private javax.swing.JSeparator sep2;
    private javax.swing.JTextField txtCambio;
    private javax.swing.JTextField txtEfectivo;
    // End of variables declaration//GEN-END:variables
}
