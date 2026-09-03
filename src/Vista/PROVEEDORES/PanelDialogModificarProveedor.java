package Vista.PROVEEDORES;

public class PanelDialogModificarProveedor extends javax.swing.JPanel {

    private String rucOriginal;
    private javax.swing.JDialog ventana;

    public PanelDialogModificarProveedor() {
        initComponents();
        btnGuardar.addActionListener(e -> guardarCambios());
        btnCancelar.addActionListener(e -> ventana.dispose());
    }

    public static void mostrar(Modelo.Proveedores proveedor) {
        PanelDialogModificarProveedor panel = new PanelDialogModificarProveedor();
        panel.rucOriginal = proveedor.getRuc();
        panel.cargarDatos(proveedor);

        javax.swing.JDialog ventana = new javax.swing.JDialog((java.awt.Frame) null, "Modificar Proveedor", true);
        panel.ventana = ventana;
        ventana.add(panel);
        ventana.pack();
        ventana.setLocationRelativeTo(null);
        ventana.setResizable(false);
        ventana.setVisible(true);
    }

    private void cargarDatos(Modelo.Proveedores proveedor) {
        txtNombreEmpresa.setText(proveedor.getNombreEmpresa());
        txtNombreContacto.setText(proveedor.getNombreContacto());
        txtRuc.setText(proveedor.getRuc());
        txtDireccion.setText(proveedor.getDireccion());
        txtTelefono.setText(proveedor.getTelefono());
        txtCorreo.setText(proveedor.getCorreo());
    }

    private void guardarCambios() {
        String nombreEmpresa = txtNombreEmpresa.getText().trim();
        String nombreContacto = txtNombreContacto.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim().toLowerCase();
        String direccion = txtDireccion.getText().trim();

        if (nombreEmpresa.isEmpty() || nombreContacto.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(ventana,
                "Los campos Empresa y Contacto son obligatorios.",
                "Campos Incompletos", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (direccion.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(ventana,
                "El campo Dirección es obligatorio.",
                "Campos Incompletos", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!Controladores.Validaciones.validarTelefono(telefono)) {
            javax.swing.JOptionPane.showMessageDialog(ventana,
                "El número de teléfono debe tener exactamente 10 dígitos numéricos.",
                "Error de Validación", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!Controladores.Validaciones.validarCorreo(correo)) {
            javax.swing.JOptionPane.showMessageDialog(ventana,
                "Por favor, ingrese un correo electrónico válido (ej: usuario@ejemplo.com).",
                "Error de Validación", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        Modelo.Proveedores proveedor = new Modelo.Proveedores(
                rucOriginal,
                nombreEmpresa,
                nombreContacto,
                telefono,
                correo,
                direccion
        );

        boolean exito = new Controladores.ControladorProveedor().modificar(proveedor);

        if (exito) {
            ventana.dispose();
        } else {
            javax.swing.JOptionPane.showMessageDialog(ventana,
                "No se pudo modificar el proveedor.",
                "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitulo = new javax.swing.JLabel();
        lblEmpresa = new javax.swing.JLabel();
        txtNombreEmpresa = new javax.swing.JTextField();
        lblContacto = new javax.swing.JLabel();
        txtNombreContacto = new javax.swing.JTextField();
        lblRuc = new javax.swing.JLabel();
        txtRuc = new javax.swing.JTextField();
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
        lblTitulo.setText("MODIFICAR PROVEEDOR");
        add(lblTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 15, 380, 35));

        lblEmpresa.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblEmpresa.setForeground(new java.awt.Color(255, 255, 255));
        lblEmpresa.setText("Empresa*:");
        add(lblEmpresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 65, 110, 25));

        add(txtNombreEmpresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 65, 230, 25));

        lblContacto.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblContacto.setForeground(new java.awt.Color(255, 255, 255));
        lblContacto.setText("Contacto*:");
        add(lblContacto, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 100, 110, 25));

        add(txtNombreContacto, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 100, 230, 25));

        lblRuc.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblRuc.setForeground(new java.awt.Color(255, 255, 255));
        lblRuc.setText("RUC:");
        add(lblRuc, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 135, 110, 25));

        txtRuc.setBackground(new java.awt.Color(60, 60, 60));
        txtRuc.setForeground(new java.awt.Color(255, 255, 255));
        txtRuc.setEditable(false);
        add(txtRuc, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 135, 230, 25));

        lblDireccion.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblDireccion.setForeground(new java.awt.Color(255, 255, 255));
        lblDireccion.setText("Dirección*:");
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
    private javax.swing.JLabel lblContacto;
    private javax.swing.JLabel lblCorreo;
    private javax.swing.JLabel lblDireccion;
    private javax.swing.JLabel lblEmpresa;
    private javax.swing.JLabel lblRuc;
    private javax.swing.JLabel lblTelefono;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtNombreContacto;
    private javax.swing.JTextField txtNombreEmpresa;
    private javax.swing.JTextField txtRuc;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
