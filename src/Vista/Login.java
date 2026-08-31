
package Vista;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Login extends javax.swing.JFrame {

    private boolean autenticado = false;

    public Login() {
        initComponents();

        setLocationRelativeTo(null);
        txtUsuario.setPlaceholder("Correo electrónico");
        txtContrasenia.setPlaceholder("Contraseña");

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PanelLogin = new javax.swing.JPanel();
        Iconokripton = new javax.swing.JLabel();
        panelRedondo1 = new componentes.PanelRedondo();
        JlbBienvenido = new javax.swing.JLabel();
        Jlbuser1 = new javax.swing.JLabel();
        txtUsuario = new componentes.TextFieldModerno();
        JlbContraseña = new javax.swing.JLabel();
        txtContrasenia = new componentes.PasswordModerno();
        BtnIngresar = new componentes.BotonModerno();
        btnRecuperarContrasena = new componentes.LinkModerno();
        JlbCrearcuenta = new javax.swing.JLabel();
        btnregistrarse = new componentes.LinkModerno();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        PanelLogin.setBackground(new java.awt.Color(31, 10, 48));
        PanelLogin.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Iconokripton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgKrypton/logo.png"))); // NOI18N
        PanelLogin.add(Iconokripton, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 80, 540, 170));

        panelRedondo1.setBackground(new java.awt.Color(31, 10, 48));
        panelRedondo1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        JlbBienvenido.setFont(new java.awt.Font("Lucida Bright", 1, 48)); // NOI18N
        JlbBienvenido.setForeground(new java.awt.Color(255, 255, 255));
        JlbBienvenido.setText("BIENVENIDO");
        panelRedondo1.add(JlbBienvenido, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 30, 380, 60));

        Jlbuser1.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        Jlbuser1.setForeground(new java.awt.Color(255, 255, 255));
        Jlbuser1.setText("CORREO ELECTRONICO");
        panelRedondo1.add(Jlbuser1, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 110, 240, 60));

        txtUsuario.setToolTipText("Ingrese su usuario o correo registrado");
        txtUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUsuarioActionPerformed(evt);
            }
        });
        panelRedondo1.add(txtUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 180, 440, 40));

        JlbContraseña.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        JlbContraseña.setForeground(new java.awt.Color(255, 255, 255));
        JlbContraseña.setText("CONTRASEÑA");
        panelRedondo1.add(JlbContraseña, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 240, 150, 60));

        txtContrasenia.setToolTipText("Ingrese su contraseña de acceso");
        panelRedondo1.add(txtContrasenia, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 310, 440, 40));

        BtnIngresar.setText("INICIAR SESIÓN");
        BtnIngresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnIngresarActionPerformed(evt);
            }
        });
        panelRedondo1.add(BtnIngresar, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 390, 210, 60));

        btnRecuperarContrasena.setText("¿Olvido su contraseña?");
        btnRecuperarContrasena.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRecuperarContrasenaActionPerformed(evt);
            }
        });
        panelRedondo1.add(btnRecuperarContrasena, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 480, -1, -1));

        JlbCrearcuenta.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        JlbCrearcuenta.setForeground(new java.awt.Color(255, 255, 255));
        JlbCrearcuenta.setText("¿No tiene cuenta?");
        panelRedondo1.add(JlbCrearcuenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 530, -1, -1));

        btnregistrarse.setText("Crea una ");
        btnregistrarse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnregistrarseActionPerformed(evt);
            }
        });
        panelRedondo1.add(btnregistrarse, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 530, -1, 20));

        PanelLogin.add(panelRedondo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 320, 540, 600));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PanelLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 1026, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PanelLogin, javax.swing.GroupLayout.DEFAULT_SIZE, 988, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsuarioActionPerformed

    }//GEN-LAST:event_txtUsuarioActionPerformed

    private void BtnIngresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnIngresarActionPerformed
        String correo = txtUsuario.getText();
        String contrasenia = new String(txtContrasenia.getPassword());

        Controladores.ControladorLogin controladorLogin = new Controladores.ControladorLogin();
        Controladores.ControladorLogin.ResultadoLogin resultado = controladorLogin.login(this, correo, contrasenia);

        switch (resultado) {
            case DUENIO_CON_NEGOCIO:
            case EMPLEADO_ACTIVO:
                autenticado = true;
                MenuPrincipal.usuarioActivo = Modelo.Sesion.getNombreUsuario();
                MenuPrincipal menu = new MenuPrincipal();
                menu.setVisible(true);
                this.dispose();
                break;
            case DUENIO_SIN_NEGOCIO:
                autenticado = true;
                JFrame frameNegocio = new JFrame("Registro de Negocio");
                componentes.escalado.KryptonVentanaScrollable.agregarConScroll(frameNegocio, new Vista.PanelRegistroNegocio());
                frameNegocio.setSize(1920, 1080);
                frameNegocio.setLocationRelativeTo(null);
                frameNegocio.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frameNegocio.setVisible(true);
                this.dispose();
                break;
            case EMPLEADO_PENDIENTE:
                JOptionPane.showMessageDialog(this,
                        "Tu solicitud de acceso todavía está pendiente de aprobación por el dueño del negocio.",
                        "Solicitud Pendiente", JOptionPane.INFORMATION_MESSAGE);
                break;
            case CREDENCIALES_INVALIDAS:
            default:
                break;
        }
    }//GEN-LAST:event_BtnIngresarActionPerformed

    private void btnregistrarseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnregistrarseActionPerformed
         JFrame frameRegistro = new JFrame("Registro");
    componentes.escalado.KryptonVentanaScrollable.agregarConScroll(frameRegistro, new Vista.PanelRegistroUsuario());
    frameRegistro.setSize(1280, 1020);
    frameRegistro.setLocationRelativeTo(null);
    frameRegistro.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frameRegistro.setVisible(true);
    this.dispose();
    }//GEN-LAST:event_btnregistrarseActionPerformed

    private void btnRecuperarContrasenaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecuperarContrasenaActionPerformed
        DialogRecuperarContrasena dialogo = new DialogRecuperarContrasena(this);
        dialogo.setVisible(true);
    }//GEN-LAST:event_btnRecuperarContrasenaActionPerformed

    public static void main(String args[]) {

        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private componentes.BotonModerno BtnIngresar;
    private javax.swing.JLabel Iconokripton;
    private javax.swing.JLabel JlbBienvenido;
    private javax.swing.JLabel JlbContraseña;
    private javax.swing.JLabel JlbCrearcuenta;
    private javax.swing.JLabel Jlbuser1;
    private javax.swing.JPanel PanelLogin;
    private componentes.LinkModerno btnRecuperarContrasena;
    private componentes.LinkModerno btnregistrarse;
    private componentes.PanelRedondo panelRedondo1;
    private componentes.PasswordModerno txtContrasenia;
    private componentes.TextFieldModerno txtUsuario;
    // End of variables declaration//GEN-END:variables
}
