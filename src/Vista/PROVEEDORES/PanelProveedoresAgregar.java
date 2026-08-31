
package Vista.PROVEEDORES;

import Controladores.ControladorProveedor;
import Controladores.Validaciones;
import Modelo.Proveedores;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class PanelProveedoresAgregar extends javax.swing.JPanel {
    private ControladorProveedor controlador = new ControladorProveedor();

    public PanelProveedoresAgregar() {

        initComponents();
        componentes.EstiloTablaKrypton.aplicar(tablaProveedores);
        cargarTablaProveedores();
        ((componentes.TextFieldModerno) txtNombreContacto).setPlaceholder("Nombre de contacto");
        ((componentes.TextFieldModerno) txtNombreEmpresa).setPlaceholder("Nombre de la empresa");
        ((componentes.TextFieldModerno) txtRuc).setPlaceholder("RUC");
        ((componentes.TextFieldModerno) txtTelefono).setPlaceholder("Teléfono");
        ((componentes.TextFieldModerno) txtCorreoElectronico).setPlaceholder("Correo electrónico");
        ((componentes.TextFieldModerno) txtDireccion).setPlaceholder("Dirección");

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelRedondo2 = new componentes.PanelRedondo();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaProveedores = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        panelRedondo1 = new componentes.PanelRedondo();
        btnAgregarProveedor = new componentes.BotonModerno();
        btnLimpiarFormulario = new componentes.BotonModerno();
        txtNombreContacto = new componentes.TextFieldModerno();
        jLabel4 = new javax.swing.JLabel();
        txtNombreEmpresa = new componentes.TextFieldModerno();
        jLabel5 = new javax.swing.JLabel();
        txtRuc = new componentes.TextFieldModerno();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtTelefono = new componentes.TextFieldModerno();
        jLabel9 = new javax.swing.JLabel();
        txtCorreoElectronico = new componentes.TextFieldModerno();
        jLabel7 = new javax.swing.JLabel();
        txtDireccion = new componentes.TextFieldModerno();
        jLabel2 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(31, 10, 48));
        setPreferredSize(new java.awt.Dimension(1920, 1066));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelRedondo2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tablaProveedores.setBackground(new java.awt.Color(177, 121, 155));
        tablaProveedores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "RUC", "NOMBRE EMPRESA", "NOMBRE CONTACTO", "TELEFONO", "CORREO ELECTRONICO", "DIRECCIÓN"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tablaProveedores.setVerifyInputWhenFocusTarget(false);
        jScrollPane1.setViewportView(tablaProveedores);

        panelRedondo2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 1390, 260));

        add(panelRedondo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 660, 1430, 320));

        jLabel1.setFont(new java.awt.Font("Lucida Bright", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("LISTA DE PROVEEDORES");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 600, 490, 30));

        panelRedondo1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnAgregarProveedor.setFont(new java.awt.Font("Lucida Bright", 1, 24)); // NOI18N
        btnAgregarProveedor.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregarProveedor.setText("Agregar Proveedor");
        btnAgregarProveedor.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnAgregarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarProveedorActionPerformed(evt);
            }
        });
        panelRedondo1.add(btnAgregarProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(1070, 370, 280, 50));

        btnLimpiarFormulario.setFont(new java.awt.Font("Lucida Bright", 1, 24)); // NOI18N
        btnLimpiarFormulario.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpiarFormulario.setText("Limpiar");
        btnLimpiarFormulario.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnLimpiarFormulario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarFormularioActionPerformed(evt);
            }
        });
        panelRedondo1.add(btnLimpiarFormulario, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 370, 260, 50));

        txtNombreContacto.setBackground(new java.awt.Color(31, 10, 60));
        txtNombreContacto.setForeground(new java.awt.Color(255, 255, 255));
        txtNombreContacto.setBorder(null);
        panelRedondo1.add(txtNombreContacto, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 270, 560, 50));

        jLabel4.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("NOMBRE CONTACTO");
        panelRedondo1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 230, 470, 40));

        txtNombreEmpresa.setBackground(new java.awt.Color(31, 10, 60));
        txtNombreEmpresa.setForeground(new java.awt.Color(255, 255, 255));
        txtNombreEmpresa.setBorder(null);
        panelRedondo1.add(txtNombreEmpresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 170, 560, 50));

        jLabel5.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("NOMBRE EMPRESA");
        panelRedondo1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 130, 420, 40));

        txtRuc.setBackground(new java.awt.Color(31, 10, 60));
        txtRuc.setForeground(new java.awt.Color(255, 255, 255));
        txtRuc.setBorder(null);
        txtRuc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRucActionPerformed(evt);
            }
        });
        txtRuc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtRucKeyTyped(evt);
            }
        });
        panelRedondo1.add(txtRuc, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 70, 560, 50));

        jLabel3.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("RUC");
        panelRedondo1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, 420, 40));

        jLabel6.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("TELÉFONO");
        panelRedondo1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 20, 420, 40));

        txtTelefono.setBackground(new java.awt.Color(31, 10, 60));
        txtTelefono.setForeground(new java.awt.Color(255, 255, 255));
        txtTelefono.setBorder(null);
        txtTelefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTelefonoKeyTyped(evt);
            }
        });
        panelRedondo1.add(txtTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 70, 560, 50));

        jLabel9.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("CORREO ELECTRÓNICO");
        panelRedondo1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 130, 470, 40));

        txtCorreoElectronico.setBackground(new java.awt.Color(31, 10, 60));
        txtCorreoElectronico.setForeground(new java.awt.Color(255, 255, 255));
        txtCorreoElectronico.setBorder(null);
        panelRedondo1.add(txtCorreoElectronico, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 170, 560, 50));

        jLabel7.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("DIRECCIÓN");
        panelRedondo1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 240, 420, 30));

        txtDireccion.setBackground(new java.awt.Color(31, 10, 60));
        txtDireccion.setForeground(new java.awt.Color(255, 255, 255));
        txtDireccion.setBorder(null);
        panelRedondo1.add(txtDireccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 270, 560, 50));

        add(panelRedondo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 110, 1430, 450));

        jLabel2.setFont(new java.awt.Font("Lucida Bright", 1, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("REGISTRAR PROVEEDORES");
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 50, 570, 30));
    }// </editor-fold>//GEN-END:initComponents

    private void btnLimpiarFormularioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarFormularioActionPerformed
        limpiarFormulario();
    }//GEN-LAST:event_btnLimpiarFormularioActionPerformed

    private void btnAgregarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarProveedorActionPerformed

        String ruc = txtRuc.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreoElectronico.getText().trim().toLowerCase();

        String empresa = Validaciones.aMayusculas(txtNombreEmpresa.getText());
        String contacto = Validaciones.aMayusculas(txtNombreContacto.getText());
        String direccion = Validaciones.aMayusculas(txtDireccion.getText());

        txtNombreEmpresa.setText(empresa);
        txtNombreContacto.setText(contacto);
        txtDireccion.setText(direccion);

        if (!Validaciones.validarRuc(ruc)) {
            JOptionPane.showMessageDialog(this,
                "El RUC ingresado es inválido. Debe contener exactamente 13 dígitos numéricos.",
                "Error de Validación", JOptionPane.WARNING_MESSAGE);
            txtRuc.requestFocus();
            return;
        }

        if (!Validaciones.validarTelefono(telefono)) {
            JOptionPane.showMessageDialog(this,
                "El número de teléfono debe tener exactamente 10 dígitos numéricos.",
                "Error de Validación", JOptionPane.WARNING_MESSAGE);
            txtTelefono.requestFocus();
            return;
        }

        if (!Validaciones.validarCorreo(correo)) {
            JOptionPane.showMessageDialog(this,
                "Por favor, ingrese un correo electrónico válido (ej: usuario@ejemplo.com).",
                "Error de Validación", JOptionPane.WARNING_MESSAGE);
            txtCorreoElectronico.requestFocus();
            return;
        }

        if (empresa.isEmpty() || contacto.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Los campos Nombre Empresa y Nombre Contacto son obligatorios.",
                "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Proveedores nuevo = new Proveedores(ruc, empresa, contacto, telefono, correo, direccion);

        if (controlador.guardar(nuevo)) {
            JOptionPane.showMessageDialog(this, "¡Proveedor registrado con éxito en Kripton ERP!");
            limpiarFormulario();
            cargarTablaProveedores();
        } else {
            JOptionPane.showMessageDialog(this,
                "No se pudo guardar. Es posible que el RUC ya exista en la base de datos.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_btnAgregarProveedorActionPerformed

    private void txtTelefonoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefonoKeyTyped
        char c = evt.getKeyChar();
        if (!Character.isDigit(c) || txtTelefono.getText().length() >= 10) {
            evt.consume();
        }
    }//GEN-LAST:event_txtTelefonoKeyTyped

    private void txtRucKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRucKeyTyped

        char c = evt.getKeyChar();
        if (!Character.isDigit(c) || txtRuc.getText().length() >= 13) {
            evt.consume();
        }
    }//GEN-LAST:event_txtRucKeyTyped

    private void txtRucActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRucActionPerformed

    }//GEN-LAST:event_txtRucActionPerformed
    private void limpiarFormulario() {
        txtRuc.setText("");
        txtNombreEmpresa.setText("");
        txtNombreContacto.setText("");
        txtTelefono.setText("");
        txtCorreoElectronico.setText("");
        txtDireccion.setText("");
        txtRuc.requestFocus();
    }

public void cargarTablaProveedores() {
    String[] columnas = {"RUC", "NOMBRE EMPRESA", "NOMBRE CONTACTO", "TELÉFONO", "CORREO ELECTRÓNICO", "DIRECCIÓN"};

    DefaultTableModel modelo = new DefaultTableModel(null, columnas) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    List<Proveedores> lista = controlador.listarTodos();

    for (Proveedores p : lista) {
        Object[] fila = new Object[6];
        fila[0] = p.getRuc();
        fila[1] = p.getNombreEmpresa();
        fila[2] = p.getNombreContacto();
        fila[3] = p.getTelefono();
        fila[4] = p.getCorreo();
        fila[5] = p.getDireccion();

        modelo.addRow(fila);
    }

    tablaProveedores.setModel(modelo);
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarProveedor;
    private javax.swing.JButton btnLimpiarFormulario;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private componentes.PanelRedondo panelRedondo1;
    private componentes.PanelRedondo panelRedondo2;
    private javax.swing.JTable tablaProveedores;
    private javax.swing.JTextField txtCorreoElectronico;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtNombreContacto;
    private javax.swing.JTextField txtNombreEmpresa;
    private javax.swing.JTextField txtRuc;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
