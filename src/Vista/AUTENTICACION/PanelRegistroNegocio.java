
package Vista.AUTENTICACION;

import java.awt.Window;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class PanelRegistroNegocio extends javax.swing.JPanel {

    private final Controladores.ControladorCategoriaProducto controladorCategoria = new Controladores.ControladorCategoriaProducto();
    private final DefaultListModel<String> modeloCatalogos = new DefaultListModel<>();

    public PanelRegistroNegocio() {
        initComponents();

        lstCatalogos.setModel(modeloCatalogos);
        BtnguardarCatalogo.addActionListener(evt -> guardarCatalogoEnLista());

        lblNombredueño.setText(Modelo.Sesion.getNombreUsuario());
        LblApellidodueño.setText(Modelo.Sesion.getApellidosUsuario());
        LblCorreoDueño.setText(Modelo.Sesion.getCorreoUsuario());
        LblTelefonodueño.setText(Modelo.Sesion.getTelefonoUsuario());
        componentes.FiltrosTexto.aplicarSoloNumeros(TxtRuc, 13);
        TxtNombrenegocio.setPlaceholder("Nombre del negocio");
        TxtRuc.setPlaceholder("RUC");
        Txtcalleprincipal.setPlaceholder("Calle principal");
        TxtCallesecundaria.setPlaceholder("Calle secundaria");
        TxtCorreonegocio.setPlaceholder("Correo del negocio");
        txtCatalogo.setPlaceholder("Nombre de la categoría");
    }

    private void guardarCatalogoEnLista() {
        String nombreCatalogo = txtCatalogo.getText().trim();

        if (nombreCatalogo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escribe el nombre del catálogo antes de guardar.", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (modeloCatalogos.contains(nombreCatalogo)) {
            JOptionPane.showMessageDialog(this, "Ese catálogo ya está en la lista.", "Catálogo Duplicado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        modeloCatalogos.addElement(nombreCatalogo);
        txtCatalogo.setText("");
    }

    private void guardarCatalogosEnBD(String idNegocio) {
        java.util.List<String> noGuardados = new java.util.ArrayList<>();
        for (int i = 0; i < modeloCatalogos.size(); i++) {
            String nombreCatalogo = modeloCatalogos.get(i);
            if (controladorCategoria.registrarCategoria(this, idNegocio, nombreCatalogo) == null) {
                noGuardados.add(nombreCatalogo);
            }
        }
        if (!noGuardados.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No se pudieron guardar estos catálogos: " + String.join(", ", noGuardados)
                    + "\nPuedes agregarlos después desde Configuración.",
                    "Catálogos incompletos", JOptionPane.WARNING_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbIcono = new javax.swing.JLabel();
        TxtNombrenegocio = new componentes.TextFieldModerno();
        BackgroundTitleCatalagoso = new componentes.PanelRedondo();
        LblCatalogos = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstCatalogos = new componentes.ListaCatalogos<>();
        Backgroundbodylist = new componentes.PanelRedondo();
        panelRedondo1 = new componentes.PanelRedondo();
        TxtCallesecundaria = new componentes.TextFieldModerno();
        TxtRuc = new componentes.TextFieldModerno();
        lblfechaR = new javax.swing.JLabel();
        LblRUC = new javax.swing.JLabel();
        LblNombrenegocio = new javax.swing.JLabel();
        LblCorreodueño = new javax.swing.JLabel();
        Lbldireccionnegocio = new javax.swing.JLabel();
        LblCallesecundaria = new javax.swing.JLabel();
        LblCiudad = new javax.swing.JLabel();
        LblCorreoDueño = new componentes.LabelModerno();
        lblNombredueño = new componentes.LabelModerno();
        LblApellidodueño = new componentes.LabelModerno();
        lblDatosnegocio = new javax.swing.JLabel();
        LblNomdueño = new javax.swing.JLabel();
        LblApdueño = new javax.swing.JLabel();
        lblDatosdueño = new javax.swing.JLabel();
        LblCallepricipal = new javax.swing.JLabel();
        Txtcalleprincipal = new componentes.TextFieldModerno();
        LblTeldueño = new javax.swing.JLabel();
        LblTelefonodueño = new componentes.LabelModerno();
        lblFecharegistro = new componentes.FechaModerna();
        CbCiudades = new componentes.ComboBoxCiudades();
        txtCatalogo = new componentes.TextFieldModerno();
        BtnguardarCatalogo = new componentes.BotonModerno();
        BtnEliminarCatalogo = new componentes.BotonModerno();
        BtnRegresar = new componentes.BotonModerno();
        BtnAvanzar = new componentes.BotonModerno();
        TxtCorreonegocio = new componentes.TextFieldModerno();
        lblCorreonegocio = new javax.swing.JLabel();

        setBackground(new java.awt.Color(31, 10, 48));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbIcono.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgKrypton/logo.png"))); // NOI18N
        lbIcono.setText("jLabel1");
        add(lbIcono, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 10, 450, 180));

        TxtNombrenegocio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtNombrenegocioActionPerformed(evt);
            }
        });
        add(TxtNombrenegocio, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 280, 370, 40));

        BackgroundTitleCatalagoso.setBackground(new java.awt.Color(31, 10, 58));
        BackgroundTitleCatalagoso.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        LblCatalogos.setFont(new java.awt.Font("Lucida Bright", 1, 36)); // NOI18N
        LblCatalogos.setForeground(new java.awt.Color(255, 255, 255));
        LblCatalogos.setText("CATEGORIAS");
        BackgroundTitleCatalagoso.add(LblCatalogos, new org.netbeans.lib.awtextra.AbsoluteConstraints(272, 5, -1, -1));

        add(BackgroundTitleCatalagoso, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 240, 790, 80));

        lstCatalogos.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        lstCatalogos.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jScrollPane1.setViewportView(lstCatalogos);

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 330, 770, 200));

        Backgroundbodylist.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        add(Backgroundbodylist, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 320, 790, 220));

        panelRedondo1.setBackground(new java.awt.Color(31, 10, 48));
        add(panelRedondo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 240, 790, 300));

        TxtCallesecundaria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtCallesecundariaActionPerformed(evt);
            }
        });
        add(TxtCallesecundaria, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 430, 370, 40));
        add(TxtRuc, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 280, 370, 40));

        lblfechaR.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        lblfechaR.setForeground(new java.awt.Color(255, 255, 255));
        lblfechaR.setText("FECHA REGISTRO*");
        add(lblfechaR, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 600, 230, 30));

        LblRUC.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        LblRUC.setForeground(new java.awt.Color(255, 255, 255));
        LblRUC.setText("RUC *");
        add(LblRUC, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 230, 80, 30));

        LblNombrenegocio.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        LblNombrenegocio.setForeground(new java.awt.Color(255, 255, 255));
        LblNombrenegocio.setText("NOMBRE DEL NEGOCIO *");
        add(LblNombrenegocio, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 230, 280, 30));

        LblCorreodueño.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        LblCorreodueño.setForeground(new java.awt.Color(255, 255, 255));
        LblCorreodueño.setText("Correo Electronico");
        add(LblCorreodueño, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 940, 230, 30));

        Lbldireccionnegocio.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        Lbldireccionnegocio.setForeground(new java.awt.Color(255, 255, 255));
        Lbldireccionnegocio.setText("DIRECCION DEL NEGOCIO");
        add(Lbldireccionnegocio, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 350, 250, 30));

        LblCallesecundaria.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        LblCallesecundaria.setForeground(new java.awt.Color(255, 255, 255));
        LblCallesecundaria.setText("CALLE SECUDARIA");
        add(LblCallesecundaria, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 390, 230, 30));

        LblCiudad.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        LblCiudad.setForeground(new java.awt.Color(255, 255, 255));
        LblCiudad.setText("CIUDAD*");
        add(LblCiudad, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 500, 230, 30));
        add(LblCorreoDueño, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 930, 450, 40));
        add(lblNombredueño, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 750, 450, 40));
        add(LblApellidodueño, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 810, 450, 40));

        lblDatosnegocio.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        lblDatosnegocio.setForeground(new java.awt.Color(255, 255, 255));
        lblDatosnegocio.setText("DATOS DEL NEGOCIO");
        add(lblDatosnegocio, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 170, 230, 30));

        LblNomdueño.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        LblNomdueño.setForeground(new java.awt.Color(255, 255, 255));
        LblNomdueño.setText("Nombre del dueño");
        add(LblNomdueño, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 760, 230, 30));

        LblApdueño.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        LblApdueño.setForeground(new java.awt.Color(255, 255, 255));
        LblApdueño.setText("Apellido del dueño");
        add(LblApdueño, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 820, 230, 30));

        lblDatosdueño.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        lblDatosdueño.setForeground(new java.awt.Color(255, 255, 255));
        lblDatosdueño.setText("DATOS DEL DUEÑO");
        add(lblDatosdueño, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 710, 230, 30));

        LblCallepricipal.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        LblCallepricipal.setForeground(new java.awt.Color(255, 255, 255));
        LblCallepricipal.setText("CALLE PRINCIPAL");
        add(LblCallepricipal, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 390, 230, 30));
        add(Txtcalleprincipal, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 430, 370, 40));

        LblTeldueño.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        LblTeldueño.setForeground(new java.awt.Color(255, 255, 255));
        LblTeldueño.setText("Telefono de contacto");
        add(LblTeldueño, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 880, 230, 30));
        add(LblTelefonodueño, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 870, 450, 40));
        add(lblFecharegistro, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 640, 370, 40));
        add(CbCiudades, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 540, 370, 40));
        add(txtCatalogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 570, 440, 40));

        BtnguardarCatalogo.setText("GUARDAR");
        add(BtnguardarCatalogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(1470, 570, 130, 40));

        BtnEliminarCatalogo.setText("ELIMINAR");
        BtnEliminarCatalogo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnEliminarCatalogoActionPerformed(evt);
            }
        });
        add(BtnEliminarCatalogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(1620, 570, 130, 40));

        BtnRegresar.setText("REGRESAR");
        BtnRegresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnRegresarActionPerformed(evt);
            }
        });
        add(BtnRegresar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1320, 870, 230, 90));

        BtnAvanzar.setText("CONTINUAR");
        BtnAvanzar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAvanzarActionPerformed(evt);
            }
        });
        add(BtnAvanzar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1570, 870, 230, 90));
        add(TxtCorreonegocio, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 640, 390, 40));

        lblCorreonegocio.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        lblCorreonegocio.setForeground(new java.awt.Color(255, 255, 255));
        lblCorreonegocio.setText("Correo del Negocio");
        add(lblCorreonegocio, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 600, 200, 30));
    }// </editor-fold>//GEN-END:initComponents

    private void TxtNombrenegocioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtNombrenegocioActionPerformed

    }//GEN-LAST:event_TxtNombrenegocioActionPerformed

    private void BtnEliminarCatalogoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnEliminarCatalogoActionPerformed
        int seleccionado = lstCatalogos.getSelectedIndex();
        if (seleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un catálogo de la lista para eliminarlo.", "Nada Seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        modeloCatalogos.remove(seleccionado);
    }//GEN-LAST:event_BtnEliminarCatalogoActionPerformed

    private void TxtCallesecundariaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtCallesecundariaActionPerformed

    }//GEN-LAST:event_TxtCallesecundariaActionPerformed

    private void BtnAvanzarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAvanzarActionPerformed
        String idUsuario = Modelo.Sesion.getIdUsuario();
        String nombreNegocio = TxtNombrenegocio.getText();
        String ruc = TxtRuc.getText();
        String correoNegocio = TxtCorreonegocio.getText();
        String callePrincipal = Txtcalleprincipal.getText();
        String calleSecundaria = TxtCallesecundaria.getText();
        String ciudad = CbCiudades.getSelectedItem() != null ? CbCiudades.getSelectedItem().toString() : "";

        Controladores.ControladorNegocio controladorNegocio = new Controladores.ControladorNegocio();
        String idNegocio = controladorNegocio.registrarNegocio(this, idUsuario, nombreNegocio, ruc, correoNegocio, callePrincipal, calleSecundaria, ciudad);

        if (idNegocio == null) {
            return;
        }

        Modelo.Sesion.iniciar(idUsuario, idNegocio, Modelo.Sesion.getNombreUsuario());
        guardarCatalogosEnBD(idNegocio);

        JFrame frameRegistro = new JFrame("Registro");
        componentes.escalado.KryptonVentanaScrollable.agregarConScroll(frameRegistro, new Vista.PRINCIPAL.PanelModulos());
        frameRegistro.setSize(1280, 1080);
        frameRegistro.setLocationRelativeTo(null);
        frameRegistro.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameRegistro.setVisible(true);

        Window ventanaActual = SwingUtilities.getWindowAncestor(this);
        if (ventanaActual != null) {
            ventanaActual.dispose();
        }
    }//GEN-LAST:event_BtnAvanzarActionPerformed

    private void BtnRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnRegresarActionPerformed
        JFrame frameRegistro = new JFrame("Registro");
    componentes.escalado.KryptonVentanaScrollable.agregarConScroll(frameRegistro, new PanelRegistroUsuario());
    frameRegistro.setSize(1280, 1080);
    frameRegistro.setLocationRelativeTo(null);
    frameRegistro.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frameRegistro.setVisible(true);

    Window ventanaActual = SwingUtilities.getWindowAncestor(this);
    if (ventanaActual != null) {
        ventanaActual.dispose();
    }
    }//GEN-LAST:event_BtnRegresarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private componentes.PanelRedondo BackgroundTitleCatalagoso;
    private componentes.PanelRedondo Backgroundbodylist;
    private componentes.BotonModerno BtnAvanzar;
    private componentes.BotonModerno BtnEliminarCatalogo;
    private componentes.BotonModerno BtnRegresar;
    private componentes.BotonModerno BtnguardarCatalogo;
    private componentes.ComboBoxCiudades CbCiudades;
    private javax.swing.JLabel LblApdueño;
    private componentes.LabelModerno LblApellidodueño;
    private javax.swing.JLabel LblCallepricipal;
    private javax.swing.JLabel LblCallesecundaria;
    private javax.swing.JLabel LblCatalogos;
    private javax.swing.JLabel LblCiudad;
    private componentes.LabelModerno LblCorreoDueño;
    private javax.swing.JLabel LblCorreodueño;
    private javax.swing.JLabel LblNombrenegocio;
    private javax.swing.JLabel LblNomdueño;
    private javax.swing.JLabel LblRUC;
    private javax.swing.JLabel LblTeldueño;
    private componentes.LabelModerno LblTelefonodueño;
    private javax.swing.JLabel Lbldireccionnegocio;
    private componentes.TextFieldModerno TxtCallesecundaria;
    private componentes.TextFieldModerno TxtCorreonegocio;
    private componentes.TextFieldModerno TxtNombrenegocio;
    private componentes.TextFieldModerno TxtRuc;
    private componentes.TextFieldModerno Txtcalleprincipal;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbIcono;
    private javax.swing.JLabel lblCorreonegocio;
    private javax.swing.JLabel lblDatosdueño;
    private javax.swing.JLabel lblDatosnegocio;
    private componentes.FechaModerna lblFecharegistro;
    private componentes.LabelModerno lblNombredueño;
    private javax.swing.JLabel lblfechaR;
    private componentes.ListaCatalogos<String> lstCatalogos;
    private componentes.PanelRedondo panelRedondo1;
    private componentes.TextFieldModerno txtCatalogo;
    // End of variables declaration//GEN-END:variables
}
