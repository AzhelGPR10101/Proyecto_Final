package Vista.EMPLEADOS;

public class PanelDialogModificarEmpleado extends javax.swing.JPanel {

    private String cedulaOriginal;
    private javax.swing.JDialog ventana;

    public PanelDialogModificarEmpleado() {
        initComponents();
        btnGuardar.addActionListener(e -> guardarCambios());
        btnCancelar.addActionListener(e -> ventana.dispose());
    }

    public static void mostrar(Modelo.Empleado empleado) {
        PanelDialogModificarEmpleado panel = new PanelDialogModificarEmpleado();
        panel.cedulaOriginal = empleado.getCedula();
        panel.cargarDatos(empleado);

        javax.swing.JDialog ventana = new javax.swing.JDialog((java.awt.Frame) null, "Modificar Empleado", true);
        panel.ventana = ventana;
        ventana.add(panel);
        ventana.pack();
        ventana.setLocationRelativeTo(null);
        ventana.setResizable(false);
        ventana.setVisible(true);
    }

    private void cargarDatos(Modelo.Empleado empleado) {
        txtNombres.setText(empleado.getNombres());
        txtApellidos.setText(empleado.getApellidos());
        txtCedula.setText(empleado.getCedula());
        txtSueldo.setText(String.valueOf(empleado.getSueldo()));
        txtTelefono.setText(empleado.getTelefono());
        txtUsuario.setText(empleado.getUsername());
    }

    private void guardarCambios() {
        boolean exito = Controladores.EmpleadoControlador.actualizarEmpleado(
                ventana,
                cedulaOriginal,
                txtNombres.getText().trim(),
                txtApellidos.getText().trim(),
                txtSueldo.getText().trim(),
                txtTelefono.getText().trim(),
                txtUsuario.getText().trim(),
                new String(txtPassword.getPassword())
        );

        if (exito) {
            ventana.dispose();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitulo = new javax.swing.JLabel();
        lblNombres = new javax.swing.JLabel();
        txtNombres = new javax.swing.JTextField();
        lblApellidos = new javax.swing.JLabel();
        txtApellidos = new javax.swing.JTextField();
        lblCedula = new javax.swing.JLabel();
        txtCedula = new javax.swing.JTextField();
        lblSueldo = new javax.swing.JLabel();
        txtSueldo = new javax.swing.JTextField();
        lblTelefono = new javax.swing.JLabel();
        txtTelefono = new javax.swing.JTextField();
        lblUsuario = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        lblPassword = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        lblNotaPassword = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setBackground(new java.awt.Color(31, 11, 43));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("MODIFICAR EMPLEADO");
        add(lblTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 15, 380, 35));

        lblNombres.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblNombres.setForeground(new java.awt.Color(255, 255, 255));
        lblNombres.setText("Nombres*:");
        add(lblNombres, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 65, 120, 25));
        add(txtNombres, new org.netbeans.lib.awtextra.AbsoluteConstraints(155, 65, 225, 25));

        lblApellidos.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblApellidos.setForeground(new java.awt.Color(255, 255, 255));
        lblApellidos.setText("Apellidos*:");
        add(lblApellidos, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 100, 120, 25));
        add(txtApellidos, new org.netbeans.lib.awtextra.AbsoluteConstraints(155, 100, 225, 25));

        lblCedula.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblCedula.setForeground(new java.awt.Color(255, 255, 255));
        lblCedula.setText("Cédula:");
        add(lblCedula, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 135, 120, 25));

        txtCedula.setBackground(new java.awt.Color(60, 60, 60));
        txtCedula.setForeground(new java.awt.Color(255, 255, 255));
        txtCedula.setEditable(false);
        add(txtCedula, new org.netbeans.lib.awtextra.AbsoluteConstraints(155, 135, 225, 25));

        lblSueldo.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblSueldo.setForeground(new java.awt.Color(255, 255, 255));
        lblSueldo.setText("Sueldo*:");
        add(lblSueldo, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 170, 120, 25));
        add(txtSueldo, new org.netbeans.lib.awtextra.AbsoluteConstraints(155, 170, 225, 25));

        lblTelefono.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblTelefono.setForeground(new java.awt.Color(255, 255, 255));
        lblTelefono.setText("Teléfono*:");
        add(lblTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 205, 120, 25));
        add(txtTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(155, 205, 225, 25));

        lblUsuario.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblUsuario.setForeground(new java.awt.Color(255, 255, 255));
        lblUsuario.setText("Usuario*:");
        add(lblUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 240, 120, 25));
        add(txtUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(155, 240, 225, 25));

        lblPassword.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblPassword.setForeground(new java.awt.Color(255, 255, 255));
        lblPassword.setText("Nueva Contraseña:");
        add(lblPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 275, 125, 25));
        add(txtPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(155, 275, 225, 25));

        lblNotaPassword.setFont(new java.awt.Font("Segoe UI", 2, 11)); // NOI18N
        lblNotaPassword.setForeground(new java.awt.Color(220, 220, 220));
        lblNotaPassword.setText("(dejar en blanco para no cambiarla)");
        add(lblNotaPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(155, 303, 225, 18));

        btnGuardar.setBackground(new java.awt.Color(85, 0, 102));
        btnGuardar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnGuardar.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardar.setText("GUARDAR CAMBIOS");
        btnGuardar.setFocusPainted(false);
        add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(55, 335, 170, 35));

        btnCancelar.setBackground(new java.awt.Color(85, 0, 102));
        btnCancelar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnCancelar.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelar.setText("CANCELAR");
        btnCancelar.setFocusPainted(false);
        add(btnCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(235, 335, 120, 35));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JLabel lblApellidos;
    private javax.swing.JLabel lblCedula;
    private javax.swing.JLabel lblNombres;
    private javax.swing.JLabel lblNotaPassword;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblSueldo;
    private javax.swing.JLabel lblTelefono;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblUsuario;
    private javax.swing.JTextField txtApellidos;
    private javax.swing.JTextField txtCedula;
    private javax.swing.JTextField txtNombres;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtSueldo;
    private javax.swing.JTextField txtTelefono;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
