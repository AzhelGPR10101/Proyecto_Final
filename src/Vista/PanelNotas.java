
package Vista;

public class PanelNotas extends javax.swing.JPanel {

    private final Controladores.ControladorNota controladorNota = new Controladores.ControladorNota();
    private String idNotaActual = null;
    private Runnable alGuardarOEliminar;

    public PanelNotas() {
        initComponents();
        prepararNuevaNota();
    }

    public void setAlGuardarOEliminar(Runnable callback) {
        this.alGuardarOEliminar = callback;
    }

    public void prepararNuevaNota() {
        idNotaActual = null;
        txtTitulonota.setText("");
        txtAreacuerpo.setText("");
        btnEliminar.setVisible(false);
    }

    public void cargarNota(Modelo.Nota nota) {
        idNotaActual = nota.getIdNota();
        txtTitulonota.setText(nota.getTitulo());
        txtAreacuerpo.setText(nota.getCuerpo());
        btnEliminar.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblDescripcion = new javax.swing.JLabel();
        lblnota = new javax.swing.JLabel();
        lblfechaactual = new javax.swing.JLabel();
        txtTitulonota = new componentes.TextFieldModerno();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAreacuerpo = new componentes.NotaTextArea();
        BtnGuardar = new componentes.BotonModerno();
        btnEliminar = new componentes.BotonModerno();
        lbltitulo1 = new javax.swing.JLabel();
        lblFecha1 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(31, 10, 48));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblDescripcion.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        lblDescripcion.setForeground(new java.awt.Color(255, 255, 255));
        lblDescripcion.setText("DESCRIPCION:");
        add(lblDescripcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, -1, -1));

        lblnota.setFont(new java.awt.Font("Lucida Bright", 1, 24)); // NOI18N
        lblnota.setForeground(new java.awt.Color(255, 255, 255));
        lblnota.setText("NOTA ");
        add(lblnota, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        lblfechaactual.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        lblfechaactual.setForeground(new java.awt.Color(255, 255, 255));
        lblfechaactual.setText("DD / MM / AA");
        add(lblfechaactual, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 250, -1));
        add(txtTitulonota, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 370, 30));

        txtAreacuerpo.setColumns(20);
        txtAreacuerpo.setRows(5);
        jScrollPane1.setViewportView(txtAreacuerpo);

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, 370, 390));

        BtnGuardar.setText("GUARDAR");
        BtnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        add(BtnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 680, 120, -1));

        btnEliminar.setText("ELIMINAR");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 680, 120, -1));

        lbltitulo1.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        lbltitulo1.setForeground(new java.awt.Color(255, 255, 255));
        lbltitulo1.setText("TITULO:");
        add(lbltitulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, -1, -1));

        lblFecha1.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        lblFecha1.setForeground(new java.awt.Color(255, 255, 255));
        lblFecha1.setText("FECHA:");
        add(lblFecha1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void txtTitulonotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTitulonotaActionPerformed

    }//GEN-LAST:event_txtTitulonotaActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        if (idNotaActual == null) {
            return;
        }
        int confirmacion = javax.swing.JOptionPane.showConfirmDialog(this,
                "¿Eliminar esta nota?", "Confirmar eliminación",
                javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.WARNING_MESSAGE);
        if (confirmacion != javax.swing.JOptionPane.YES_OPTION) {
            return;
        }
        boolean exito = controladorNota.eliminar(this, idNotaActual);
        if (exito) {
            prepararNuevaNota();
            if (alGuardarOEliminar != null) {
                alGuardarOEliminar.run();
            }
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
      String titulo = txtTitulonota.getText();
    String cuerpo = txtAreacuerpo.getText();

    boolean exito;
    if (idNotaActual == null) {
        String idGenerado = controladorNota.registrar(this, titulo, cuerpo);
        exito = idGenerado != null;
        if (exito) idNotaActual = idGenerado;
    } else {
        exito = controladorNota.actualizar(this, idNotaActual, titulo, cuerpo);
    }

    if (exito) {
        javax.swing.JOptionPane.showMessageDialog(this, "Nota guardada correctamente.");
        prepararNuevaNota();
        if (alGuardarOEliminar != null) {
            alGuardarOEliminar.run();
        }
    }
    }//GEN-LAST:event_btnGuardarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private componentes.BotonModerno BtnGuardar;
    private componentes.BotonModerno btnEliminar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDescripcion;
    private javax.swing.JLabel lblFecha1;
    private javax.swing.JLabel lblfechaactual;
    private javax.swing.JLabel lblnota;
    private javax.swing.JLabel lbltitulo1;
    private componentes.NotaTextArea txtAreacuerpo;
    private componentes.TextFieldModerno txtTitulonota;
    // End of variables declaration//GEN-END:variables
}
