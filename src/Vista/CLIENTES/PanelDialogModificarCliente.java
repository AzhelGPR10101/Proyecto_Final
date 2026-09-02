package Vista.CLIENTES;

public class PanelDialogModificarCliente extends javax.swing.JPanel {

    public PanelDialogModificarCliente() {
        initComponents();
        componentes.FiltrosTexto.aplicarSoloNumeros(txtTelefono, 10);
        txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                if (Character.isDigit(evt.getKeyChar())) {
                    evt.consume();
                }
            }
        });
        txtApellido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                if (Character.isDigit(evt.getKeyChar())) {
                    evt.consume();
                }
            }
        });
    }

    public void cargarDatos(Modelo.Cliente cliente) {
        txtNombre.setText(cliente.getNombre());
        txtApellido.setText(cliente.getApellido());
        txtCedula.setText(cliente.getCedula());
        txtDireccion.setText(cliente.getDireccion());
        txtTelefono.setText(cliente.getTelefono());
        txtCorreo.setText(cliente.getCorreo());
    }

    public String getNombre() {
        return txtNombre.getText();
    }

    public String getApellido() {
        return txtApellido.getText();
    }

    public String getDireccion() {
        return txtDireccion.getText();
    }

    public String getTelefono() {
        return txtTelefono.getText();
    }

    public String getCorreo() {
        return txtCorreo.getText();
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
        lblNombre = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        lblApellido = new javax.swing.JLabel();
        txtApellido = new javax.swing.JTextField();
        lblCedula = new javax.swing.JLabel();
        txtCedula = new javax.swing.JTextField();
        lblDireccion = new javax.swing.JLabel();
        txtDireccion = new javax.swing.JTextField();
        lblTelefono = new javax.swing.JLabel();
        txtTelefono = new javax.swing.JTextField();
        lblCorreo = new javax.swing.JLabel();
        txtCorreo = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setBackground(new java.awt.Color(31, 11, 43));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("MODIFICAR CLIENTE");
        add(lblTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 15, 380, 35));

        lblNombre.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblNombre.setForeground(new java.awt.Color(255, 255, 255));
        lblNombre.setText("Nombre*:");
        add(lblNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 65, 110, 25));
        add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 65, 230, 25));

        lblApellido.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblApellido.setForeground(new java.awt.Color(255, 255, 255));
        lblApellido.setText("Apellido*:");
        add(lblApellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 100, 110, 25));
        add(txtApellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 100, 230, 25));

        lblCedula.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblCedula.setForeground(new java.awt.Color(255, 255, 255));
        lblCedula.setText("Cédula:");
        add(lblCedula, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 135, 110, 25));

        txtCedula.setBackground(new java.awt.Color(60, 60, 60));
        txtCedula.setForeground(new java.awt.Color(255, 255, 255));
        txtCedula.setEditable(false);
        add(txtCedula, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 135, 230, 25));

        lblDireccion.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblDireccion.setForeground(new java.awt.Color(255, 255, 255));
        lblDireccion.setText("Dirección:");
        add(lblDireccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 170, 110, 25));
        add(txtDireccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 170, 230, 25));

        lblTelefono.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblTelefono.setForeground(new java.awt.Color(255, 255, 255));
        lblTelefono.setText("Teléfono*:");
        add(lblTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 205, 110, 25));
        add(txtTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 205, 230, 25));

        lblCorreo.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblCorreo.setForeground(new java.awt.Color(255, 255, 255));
        lblCorreo.setText("Correo*:");
        add(lblCorreo, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 240, 110, 25));
        add(txtCorreo, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 240, 230, 25));

        btnGuardar.setBackground(new java.awt.Color(85, 0, 102));
        btnGuardar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnGuardar.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardar.setText("GUARDAR CAMBIOS");
        btnGuardar.setFocusPainted(false);
        add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(55, 280, 170, 35));

        btnCancelar.setBackground(new java.awt.Color(85, 0, 102));
        btnCancelar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnCancelar.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelar.setText("CANCELAR");
        btnCancelar.setFocusPainted(false);
        add(btnCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(235, 280, 120, 35));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JLabel lblApellido;
    private javax.swing.JLabel lblCedula;
    private javax.swing.JLabel lblCorreo;
    private javax.swing.JLabel lblDireccion;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblTelefono;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JTextField txtApellido;
    private javax.swing.JTextField txtCedula;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
