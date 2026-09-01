package Vista.AUTENTICACION;

import java.awt.Window;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class PanelRegistroUsuario extends javax.swing.JPanel {

    private javax.swing.JLabel lblErrorContrasena;

    public PanelRegistroUsuario() {
        initComponents();
        actualizarVisibilidadCamposEmpleado();
        componentes.FiltrosTexto.aplicarSoloNumeros(txtcedula, 10);
        txtcedula.setPlaceholder("Cédula");
        txtnombre.setPlaceholder("Nombres");
        txtapellido.setPlaceholder("Apellidos");
        txtcorreousuario.setPlaceholder("Correo electrónico");
        txttelefono.setPlaceholder("Teléfono");
        pswdcontrasenia.setPlaceholder("Contraseña");
        pswdcontraseniarepetir.setPlaceholder("Repetir contraseña");
        txtCodigoNegocio.setPlaceholder("Código del negocio");
    }

    private void actualizarVisibilidadCamposEmpleado() {
        boolean esEmpleado = jCheckBox1.isSelected();
        lblcodigo.setVisible(esEmpleado);
        txtCodigoNegocio.setVisible(esEmpleado);
        lblcotrasenia3.setVisible(esEmpleado);
        cboxRolAsignado.setVisible(esEmpleado);
        lblRolSolicitado.setVisible(esEmpleado);
        fchRegistro.setVisible(esEmpleado);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        lbIcono = new javax.swing.JLabel();
        lblCrearcuenta = new javax.swing.JLabel();
        lblcorreo = new javax.swing.JLabel();
        txtcedula = new componentes.TextFieldModerno();
        txtnombre = new componentes.TextFieldModerno();
        txtapellido = new componentes.TextFieldModerno();
        txtcorreousuario = new componentes.TextFieldModerno();
        txttelefono = new componentes.TextFieldModerno();
        lblfoto = new javax.swing.JLabel();
        lblnombre = new javax.swing.JLabel();
        lbltelefono = new javax.swing.JLabel();
        lblapellido = new javax.swing.JLabel();
        pswdcontrasenia = new componentes.PasswordModerno();
        lblcotrasenia = new javax.swing.JLabel();
        panelimagen = new componentes.PanelImagen();
        lblcedula = new javax.swing.JLabel();
        btncontinua = new componentes.BotonModerno();
        btniniciarsesion = new componentes.LinkModerno();
        lbltienescuenta = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        lblRolSolicitado = new javax.swing.JLabel();
        lblcodigo = new javax.swing.JLabel();
        cboxRolAsignado = new componentes.ComboBoxModerno();
        lblcotrasenia3 = new javax.swing.JLabel();
        fchRegistro = new componentes.FechaModerna();
        pswdcontraseniarepetir = new componentes.PasswordModerno();
        lblcotrasenia1 = new javax.swing.JLabel();
        txtCodigoNegocio = new componentes.TextFieldModerno();
        lblEstadoContrasena = new javax.swing.JLabel();

        setBackground(new java.awt.Color(31, 10, 48));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        add(filler1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1260, 0, 20, 260));

        lbIcono.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgKrypton/logo.png"))); // NOI18N
        lbIcono.setText("jLabel1");
        add(lbIcono, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 10, 460, 160));

        lblCrearcuenta.setFont(new java.awt.Font("Lucida Bright", 1, 24)); // NOI18N
        lblCrearcuenta.setForeground(new java.awt.Color(255, 255, 255));
        lblCrearcuenta.setText("CREA UNA CUENTA");
        add(lblCrearcuenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 180, -1, 30));

        lblcorreo.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        lblcorreo.setForeground(new java.awt.Color(255, 255, 255));
        lblcorreo.setText("CORREO ELECTRONICO");
        add(lblcorreo, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 580, -1, 30));

        txtcedula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtcedulaActionPerformed(evt);
            }
        });
        add(txtcedula, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 290, 380, 30));
        add(txtnombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 370, 380, 30));
        add(txtapellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 450, 380, 30));

        txtcorreousuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtcorreousuarioActionPerformed(evt);
            }
        });
        add(txtcorreousuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 620, 380, 30));
        add(txttelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 530, 380, 30));

        lblfoto.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        lblfoto.setForeground(new java.awt.Color(255, 255, 255));
        lblfoto.setText("FOTO DE PERFIL");
        add(lblfoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 250, -1, 30));

        lblnombre.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        lblnombre.setForeground(new java.awt.Color(255, 255, 255));
        lblnombre.setText("NOMBRE");
        add(lblnombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 330, -1, 30));

        lbltelefono.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        lbltelefono.setForeground(new java.awt.Color(255, 255, 255));
        lbltelefono.setText("TELEFONO");
        add(lbltelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 490, 120, 30));

        lblapellido.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        lblapellido.setForeground(new java.awt.Color(255, 255, 255));
        lblapellido.setText("APELLIDO");
        add(lblapellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 410, -1, 30));

        pswdcontrasenia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pswdcontraseniaActionPerformed(evt);
            }
        });
        pswdcontrasenia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pswdcontraseniaKeyReleased(evt);
            }
        });
        add(pswdcontrasenia, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 700, 380, -1));

        lblcotrasenia.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        lblcotrasenia.setForeground(new java.awt.Color(255, 255, 255));
        lblcotrasenia.setText("CONTRASEÑA");
        add(lblcotrasenia, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 660, -1, 30));

        javax.swing.GroupLayout panelimagenLayout = new javax.swing.GroupLayout(panelimagen);
        panelimagen.setLayout(panelimagenLayout);
        panelimagenLayout.setHorizontalGroup(
            panelimagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 530, Short.MAX_VALUE)
        );
        panelimagenLayout.setVerticalGroup(
            panelimagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        add(panelimagen, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 290, 530, 300));

        lblcedula.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        lblcedula.setForeground(new java.awt.Color(255, 255, 255));
        lblcedula.setText("CEDULA");
        add(lblcedula, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 250, -1, 30));

        btncontinua.setText("CONTINAR");
        btncontinua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncontinuaActionPerformed(evt);
            }
        });
        add(btncontinua, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 800, 220, 70));

        btniniciarsesion.setText("Inicia Sesión");
        btniniciarsesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btniniciarsesionActionPerformed(evt);
            }
        });
        add(btniniciarsesion, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 770, -1, 20));

        lbltienescuenta.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        lbltienescuenta.setForeground(new java.awt.Color(255, 255, 255));
        lbltienescuenta.setText("¿Ya tienes cuenta?");
        add(lbltienescuenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 770, -1, -1));

        jLabel1.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("¿Es usted empleado de un negocio registrado?");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 840, -1, -1));

        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        add(jCheckBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 840, -1, -1));

        lblRolSolicitado.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        lblRolSolicitado.setForeground(new java.awt.Color(255, 255, 255));
        lblRolSolicitado.setText("Fecha de Contrataciòn");
        add(lblRolSolicitado, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 1060, 180, 30));

        lblcodigo.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        lblcodigo.setForeground(new java.awt.Color(255, 255, 255));
        lblcodigo.setText("CODIGO DE NEGOCIO");
        add(lblcodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 880, -1, 30));

        cboxRolAsignado.setBackground(new java.awt.Color(31, 10, 60));
        cboxRolAsignado.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        cboxRolAsignado.setForeground(new java.awt.Color(255, 255, 255));
        cboxRolAsignado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Vendedor", "Bodegero", "Recursos Humanos" }));
        add(cboxRolAsignado, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 1010, 370, 30));

        lblcotrasenia3.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        lblcotrasenia3.setForeground(new java.awt.Color(255, 255, 255));
        lblcotrasenia3.setText("ROL SOLICITADO");
        add(lblcotrasenia3, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 970, -1, 30));
        add(fchRegistro, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 1100, 380, 30));

        pswdcontraseniarepetir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pswdcontraseniarepetirActionPerformed(evt);
            }
        });
        pswdcontraseniarepetir.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pswdcontraseniarepetirKeyReleased(evt);
            }
        });
        add(pswdcontraseniarepetir, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 780, 380, -1));

        lblcotrasenia1.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        lblcotrasenia1.setForeground(new java.awt.Color(255, 255, 255));
        lblcotrasenia1.setText("REPETIR CONTRASEÑA");
        add(lblcotrasenia1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 740, -1, 30));

        txtCodigoNegocio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoNegocioActionPerformed(evt);
            }
        });
        add(txtCodigoNegocio, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 920, 380, 30));

        lblEstadoContrasena.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                lblEstadoContrasenaKeyReleased(evt);
            }
        });
        add(lblEstadoContrasena, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 790, 270, 30));
    }// </editor-fold>//GEN-END:initComponents

    private void txtcedulaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtcedulaActionPerformed

    }//GEN-LAST:event_txtcedulaActionPerformed

    private void txtcorreousuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtcorreousuarioActionPerformed

    }//GEN-LAST:event_txtcorreousuarioActionPerformed

    private void pswdcontraseniaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pswdcontraseniaActionPerformed

    }//GEN-LAST:event_pswdcontraseniaActionPerformed

    private void btniniciarsesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btniniciarsesionActionPerformed
        Login log = new Login();
        log.setVisible(true);

        Window ventanaActual = SwingUtilities.getWindowAncestor(this);
        if (ventanaActual != null) {
            ventanaActual.dispose();
        }
    }//GEN-LAST:event_btniniciarsesionActionPerformed

    private void btncontinuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncontinuaActionPerformed
        String cedula = txtcedula.getText();
        String nombres = txtnombre.getText();
        String apellidos = txtapellido.getText();
        String correo = txtcorreousuario.getText();
        String telefono = txttelefono.getText();
        String password = new String(pswdcontrasenia.getPassword());
        String passwordRepetido = new String(pswdcontraseniarepetir.getPassword());
        String rutaFoto = panelimagen.getArchivoImagen() != null ? panelimagen.getArchivoImagen().getAbsolutePath() : null;

        if (password.isEmpty() || passwordRepetido.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debes escribir y repetir la contraseña.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!password.equals(passwordRepetido)) {
            lblEstadoContrasena.setForeground(new java.awt.Color(255, 90, 90));
            lblEstadoContrasena.setText("✗ Las contraseñas no coinciden");
            return;
        }

        boolean esEmpleado = jCheckBox1.isSelected();
        String codigoNegocio = txtCodigoNegocio.getText();
        Object rolSeleccionado = cboxRolAsignado.getSelectedItem();
        String rolSolicitado = rolSeleccionado != null ? rolSeleccionado.toString() : "Vendedor";

        if (esEmpleado && (codigoNegocio == null || codigoNegocio.trim().isEmpty())) {
            JOptionPane.showMessageDialog(this, "Ingresa el código del negocio al que quieres unirte.", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Controladores.ControladorUsuario controladorUsuario = new Controladores.ControladorUsuario();
        String idUsuario = controladorUsuario.registrarUsuario(this, cedula, nombres, apellidos, correo, password, telefono, rutaFoto);

        if (idUsuario == null) {
            return;
        }

        if (esEmpleado) {
            Controladores.ControladorSolicitud controladorSolicitud = new Controladores.ControladorSolicitud();
            boolean solicitado = controladorSolicitud.solicitarAcceso(this, idUsuario, codigoNegocio, rolSolicitado);
            if (!solicitado) {
                return;
            }
            JOptionPane.showMessageDialog(this,
                    "Tu solicitud fue enviada. Podrás ingresar en cuanto el dueño del negocio la apruebe.",
                    "Solicitud Enviada", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Cuenta creada. Inicia sesión para configurar tu negocio.",
                    "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
        }

        Login log = new Login();
        log.setVisible(true);
        Window ventanaActual = SwingUtilities.getWindowAncestor(this);
        if (ventanaActual != null) {
            ventanaActual.dispose();
        }
    }//GEN-LAST:event_btncontinuaActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        actualizarVisibilidadCamposEmpleado();
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void pswdcontraseniarepetirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pswdcontraseniarepetirActionPerformed

    }//GEN-LAST:event_pswdcontraseniarepetirActionPerformed

    private void txtCodigoNegocioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoNegocioActionPerformed

    }//GEN-LAST:event_txtCodigoNegocioActionPerformed

    private void lblEstadoContrasenaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lblEstadoContrasenaKeyReleased
        
    }//GEN-LAST:event_lblEstadoContrasenaKeyReleased

    private void pswdcontraseniaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pswdcontraseniaKeyReleased
    pswdcontraseniarepetirKeyReleased(evt);


    }//GEN-LAST:event_pswdcontraseniaKeyReleased

    private void pswdcontraseniarepetirKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pswdcontraseniarepetirKeyReleased
 String password = new String(pswdcontrasenia.getPassword());
    String repetido = new String(pswdcontraseniarepetir.getPassword());

    if (repetido.isEmpty()) {
        lblEstadoContrasena.setText(" ");
    } else if (password.equals(repetido)) {
        lblEstadoContrasena.setForeground(new java.awt.Color(80, 200, 120));
        lblEstadoContrasena.setText("✓ Las contraseñas coinciden");
    } else {
        lblEstadoContrasena.setForeground(new java.awt.Color(255, 90, 90));
        lblEstadoContrasena.setText("✗ Las contraseñas no coinciden");
    }
    }//GEN-LAST:event_pswdcontraseniarepetirKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private componentes.BotonModerno btncontinua;
    private componentes.LinkModerno btniniciarsesion;
    private javax.swing.JComboBox cboxRolAsignado;
    private componentes.FechaModerna fchRegistro;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lbIcono;
    private javax.swing.JLabel lblCrearcuenta;
    private javax.swing.JLabel lblEstadoContrasena;
    private javax.swing.JLabel lblRolSolicitado;
    private javax.swing.JLabel lblapellido;
    private javax.swing.JLabel lblcedula;
    private javax.swing.JLabel lblcodigo;
    private javax.swing.JLabel lblcorreo;
    private javax.swing.JLabel lblcotrasenia;
    private javax.swing.JLabel lblcotrasenia1;
    private javax.swing.JLabel lblcotrasenia3;
    private javax.swing.JLabel lblfoto;
    private javax.swing.JLabel lblnombre;
    private javax.swing.JLabel lbltelefono;
    private javax.swing.JLabel lbltienescuenta;
    private componentes.PanelImagen panelimagen;
    private componentes.PasswordModerno pswdcontrasenia;
    private componentes.PasswordModerno pswdcontraseniarepetir;
    private componentes.TextFieldModerno txtCodigoNegocio;
    private componentes.TextFieldModerno txtapellido;
    private componentes.TextFieldModerno txtcedula;
    private componentes.TextFieldModerno txtcorreousuario;
    private componentes.TextFieldModerno txtnombre;
    private componentes.TextFieldModerno txttelefono;
    // End of variables declaration//GEN-END:variables
}
