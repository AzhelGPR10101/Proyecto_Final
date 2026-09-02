package Vista.PRODUCTOS;

public class PanelDialogModificarProducto extends javax.swing.JPanel {

    public PanelDialogModificarProducto() {
        initComponents();
        cboCategoria.setModel(new javax.swing.DefaultComboBoxModel<>(
                Controladores.ControladorProducto.obtenerNombresCategorias()));
        componentes.FiltrosTexto.aplicarLetrasYNumeros(txtNombre, 150);
        componentes.FiltrosTexto.aplicarSoloNumeros(txtCantidad, 8);
        componentes.FiltrosTexto.aplicarSoloDecimal(txtPrecio, 8);
        componentes.FiltrosTexto.aplicarSoloNumeros(txtStockMinimo, 8);
    }

    public void cargarDatos(Modelo.Producto producto) {
        txtCodigo.setText(producto.getCodigo());
        txtNombre.setText(producto.getNombre());
        cboCategoria.setSelectedItem(producto.getCategoria());
        txtCantidad.setText(String.valueOf(producto.getCantidad()));
        txtPrecio.setText(String.valueOf(producto.getPrecioUnitario()));
        txtStockMinimo.setText(String.valueOf(producto.getStockMinimo()));
        chkTieneIva.setSelected(producto.isTieneIva());
    }

    public String getNombre() {
        return txtNombre.getText();
    }

    public String getCategoria() {
        return (cboCategoria.getSelectedItem() == null) ? "" : cboCategoria.getSelectedItem().toString();
    }

    public String getCantidad() {
        return txtCantidad.getText();
    }

    public String getPrecio() {
        return txtPrecio.getText();
    }

    public boolean isTieneIva() {
        return chkTieneIva.isSelected();
    }

    public String getStockMinimo() {
        return txtStockMinimo.getText();
    }

    public javax.swing.JButton getBtnGuardar() {
        return btnGuardar;
    }

    public javax.swing.JButton getBtnCancelar() {
        return btnCancelar;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitulo = new javax.swing.JLabel();
        lblCodigo = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        lblNombre = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        lblCategoria = new javax.swing.JLabel();
        cboCategoria = new javax.swing.JComboBox<>();
        lblCantidad = new javax.swing.JLabel();
        txtCantidad = new javax.swing.JTextField();
        lblPrecio = new javax.swing.JLabel();
        txtPrecio = new javax.swing.JTextField();
        lblStockMinimo = new javax.swing.JLabel();
        txtStockMinimo = new javax.swing.JTextField();
        chkTieneIva = new javax.swing.JCheckBox();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setBackground(new java.awt.Color(31, 11, 43));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("MODIFICAR PRODUCTO");
        add(lblTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 15, 380, 35));

        lblCodigo.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblCodigo.setForeground(new java.awt.Color(255, 255, 255));
        lblCodigo.setText("Código:");
        add(lblCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 65, 110, 25));

        txtCodigo.setBackground(new java.awt.Color(60, 60, 60));
        txtCodigo.setForeground(new java.awt.Color(255, 255, 255));
        txtCodigo.setEditable(false);
        add(txtCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 65, 230, 25));

        lblNombre.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblNombre.setForeground(new java.awt.Color(255, 255, 255));
        lblNombre.setText("Nombre*:");
        add(lblNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 100, 110, 25));

        add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 100, 230, 25));

        lblCategoria.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblCategoria.setForeground(new java.awt.Color(255, 255, 255));
        lblCategoria.setText("Categoría*:");
        add(lblCategoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 135, 110, 25));

        add(cboCategoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 135, 230, 25));

        lblCantidad.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblCantidad.setForeground(new java.awt.Color(255, 255, 255));
        lblCantidad.setText("Cantidad*:");
        add(lblCantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 170, 110, 25));

        add(txtCantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 170, 230, 25));

        lblPrecio.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblPrecio.setForeground(new java.awt.Color(255, 255, 255));
        lblPrecio.setText("Precio Unit.*:");
        add(lblPrecio, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 205, 110, 25));

        add(txtPrecio, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 205, 230, 25));

        lblStockMinimo.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblStockMinimo.setForeground(new java.awt.Color(255, 255, 255));
        lblStockMinimo.setText("Stock mínimo:");
        add(lblStockMinimo, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 240, 110, 25));

        add(txtStockMinimo, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 240, 230, 25));

        chkTieneIva.setBackground(new java.awt.Color(31, 11, 43));
        chkTieneIva.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        chkTieneIva.setForeground(new java.awt.Color(255, 255, 255));
        chkTieneIva.setText("Tiene IVA");
        add(chkTieneIva, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 275, 150, 25));

        btnGuardar.setBackground(new java.awt.Color(85, 0, 102));
        btnGuardar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnGuardar.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardar.setText("GUARDAR CAMBIOS");
        btnGuardar.setFocusPainted(false);
        add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(55, 320, 170, 35));

        btnCancelar.setBackground(new java.awt.Color(85, 0, 102));
        btnCancelar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnCancelar.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelar.setText("CANCELAR");
        btnCancelar.setFocusPainted(false);
        add(btnCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(235, 320, 120, 35));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JComboBox<String> cboCategoria;
    private javax.swing.JCheckBox chkTieneIva;
    private javax.swing.JLabel lblCantidad;
    private javax.swing.JLabel lblCategoria;
    private javax.swing.JLabel lblCodigo;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblPrecio;
    private javax.swing.JLabel lblStockMinimo;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtPrecio;
    private javax.swing.JTextField txtStockMinimo;
    // End of variables declaration//GEN-END:variables
}
