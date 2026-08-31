
package Vista;

import Modelo.Negocio;
import Modelo.Sesion;
import Modelo.UsuarioCuenta;
import java.io.File;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

public class PanelConfiguracion extends javax.swing.JPanel {

    private final Controladores.ControladorNegocio controladorNegocio = new Controladores.ControladorNegocio();
    private final Controladores.ControladorUsuario controladorUsuario = new Controladores.ControladorUsuario();
    private final Controladores.ControladorCategoriaProducto controladorCategoria = new Controladores.ControladorCategoriaProducto();
    private final Controladores.ControladorModulo controladorModulo = new Controladores.ControladorModulo();
    private final componentes.ModuloRol[] modulosUI = new componentes.ModuloRol[5];

    private final DefaultListModel<String> modeloCategorias = new DefaultListModel<>();
    private java.util.List<Modelo.CategoriaProducto> categoriasActuales = new java.util.ArrayList<>();

    private final String idUsuario = Sesion.getIdUsuario();
    private String idNegocio = Sesion.getIdNegocio();



    public PanelConfiguracion() {
        initComponents();

        lstCategorias.setModel(modeloCategorias);

        cargarDatosNegocio();
        cargarDatosUsuario();
        cargarCategorias();
        cargarModulos();

        lstCategorias.addListSelectionListener(evt -> {
            if (!evt.getValueIsAdjusting()) {
                int i = lstCategorias.getSelectedIndex();
                if (i >= 0 && i < categoriasActuales.size()) {
                    txtCategorias.setText(categoriasActuales.get(i).getNombreCategoria());
                }
            }
        });
    }

    private void cargarDatosNegocio() {
        if (idNegocio == null) {
            return;
        }
        Negocio negocio = controladorNegocio.buscarPorUsuario(idUsuario);
        if (negocio == null) {
            return;
        }
        idNegocio = negocio.getIdNegocio();
        lblCodigo.setText(idNegocio);
        txtNombrenegocio.setText(negocio.getNombreNegocio());
        txtCorreoelectronico.setText(negocio.getCorreoContacto());

        txtnombrenegocio.setText(negocio.getRucNegocio());
        txtnombrenegocio.setEditable(false);
    }

    private void cargarDatosUsuario() {
        if (idUsuario == null) {
            return;
        }
        UsuarioCuenta usuario = controladorUsuario.obtenerUsuario(idUsuario);
        if (usuario == null) {
            return;
        }
        txtnombreUsuario.setText(usuario.getNombres());
        txtApellidoUsuario.setText(usuario.getApellidos());
        txtCorreo.setText(usuario.getCorreo());

        if (usuario.getFotoPerfil() != null && !usuario.getFotoPerfil().isEmpty()) {
            JPnlfotoUsuario.cargarImagen(new File(usuario.getFotoPerfil()));
        }
    }

    private void cargarCategorias() {
        modeloCategorias.clear();
        if (idNegocio == null) {
            categoriasActuales = new java.util.ArrayList<>();
            return;
        }
        categoriasActuales = controladorCategoria.listarCategorias(idNegocio);
        for (Modelo.CategoriaProducto c : categoriasActuales) {
            modeloCategorias.addElement(c.getNombreCategoria());
        }
    }

    private void agregarCategoria() {
        if (idNegocio == null) {
            JOptionPane.showMessageDialog(this, "Primero debes tener un negocio registrado.", "Sin Negocio", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String idCreado = controladorCategoria.registrarCategoria(this, idNegocio, txtCategorias.getText());
        if (idCreado != null) {
            txtCategorias.setText("");
            cargarCategorias();
        }
    }

    private void eliminarCategoria() {
        int i = lstCategorias.getSelectedIndex();
        if (i < 0 || i >= categoriasActuales.size()) {
            JOptionPane.showMessageDialog(this, "Selecciona en la lista la categoría que quieres eliminar.", "Nada Seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Modelo.CategoriaProducto seleccionada = categoriasActuales.get(i);
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Eliminar la categoría \"" + seleccionada.getNombreCategoria() + "\"?",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }
        boolean exito = controladorCategoria.eliminarCategoria(this, seleccionada.getIdCategoria());
        if (exito) {
            txtCategorias.setText("");
            cargarCategorias();
        }
    }

    private void cargarModulos() {
        modulosUI[0] = moduloRol1;
        modulosUI[1] = moduloRol2;
        modulosUI[2] = moduloRol3;
        modulosUI[3] = moduloRol4;
        modulosUI[4] = moduloRol5;

        java.util.List<Modelo.Modulo> catalogo = controladorModulo.obtenerCatalogoCompleto();
        java.util.List<String> activos = idNegocio != null
                ? controladorModulo.obtenerNombresModulosActivos(idNegocio)
                : new java.util.ArrayList<>();

        for (int i = 0; i < catalogo.size() && i < modulosUI.length; i++) {
            Modelo.Modulo m = catalogo.get(i);
            modulosUI[i].setNombreModulo(m.getNombreModulo());
            modulosUI[i].setDescripcion(m.getDescripcion());
            modulosUI[i].setActivo(activos.contains(m.getNombreModulo()));
            if ("Configuración".equals(m.getNombreModulo())) {
                modulosUI[i].setBloqueado(true);
            }
        }
    }

    private boolean guardarModulos() {
        if (idNegocio == null) {
            return true;
        }
        boolean exito = true;
        for (componentes.ModuloRol modulo : modulosUI) {
            if (modulo == null || modulo.getNombreModulo() == null) {
                continue;
            }
            boolean resultado = modulo.isActivo()
                    ? controladorModulo.activarModulo(idNegocio, modulo.getNombreModulo())
                    : controladorModulo.desactivarModulo(idNegocio, modulo.getNombreModulo());
            exito = exito && resultado;
        }
        return exito;
    }

    private void guardarCambios() {
        String rutaFoto = JPnlfotoUsuario.getArchivoImagen() != null
                ? JPnlfotoUsuario.getArchivoImagen().getAbsolutePath()
                : (Sesion.getFotoPerfilUsuario());

        boolean usuarioOk = controladorUsuario.actualizarDatos(this, idUsuario,
                txtnombreUsuario.getText().trim(), txtApellidoUsuario.getText().trim(),
                txtCorreo.getText().trim(), rutaFoto);

        boolean negocioOk = true;
        if (idNegocio != null) {
            negocioOk = controladorNegocio.actualizarNegocio(this, idNegocio,
                    txtNombrenegocio.getText().trim(), txtCorreoelectronico.getText().trim());
        }

        boolean modulosOk = guardarModulos();

        if (usuarioOk && negocioOk && modulosOk) {
            Sesion.guardarDatosUsuario(Sesion.getCedulaUsuario(), txtApellidoUsuario.getText().trim(),
                    txtCorreo.getText().trim(), Sesion.getTelefonoUsuario(), rutaFoto);
            avisarYCerrarSesion();
        } else if (!modulosOk) {
            JOptionPane.showMessageDialog(this, "Los datos se guardaron, pero no se pudieron actualizar los módulos.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void avisarYCerrarSesion() {
        javax.swing.Timer temporizador = new javax.swing.Timer(5000, evt -> {
            Sesion.cerrar();
            java.awt.Window ventanaActual = javax.swing.SwingUtilities.getWindowAncestor(this);
            Vista.Login login = new Vista.Login();
            login.setVisible(true);
            login.setLocationRelativeTo(null);
            if (ventanaActual != null) {
                ventanaActual.dispose();
            }
        });
        temporizador.setRepeats(false);
        temporizador.start();

        JOptionPane.showMessageDialog(this,
                "Cambios guardados correctamente.\nLa sesión se cerrará en 5 segundos para aplicar los módulos actualizados.",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void cambiarContrasenia() {
        String nueva = new String(txtContrasenia.getText());
        String confirmacion = new String(txtverificacion.getText());

        boolean cambiada = controladorUsuario.cambiarContrasena(this, idUsuario, nueva, confirmacion);
        if (cambiada) {
            txtContrasenia.setText("");
            txtverificacion.setText("");
        }
    }

    private void eliminarCuenta() {
        int respuesta = JOptionPane.showConfirmDialog(this,
                "Esta acción eliminará tu cuenta de forma permanente. ¿Deseas continuar?",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        boolean eliminado = controladorUsuario.eliminarCuenta(this, idUsuario);
        if (eliminado) {
            JOptionPane.showMessageDialog(this, "Cuenta eliminada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            Sesion.cerrar();
            java.awt.Window ventanaActual = javax.swing.SwingUtilities.getWindowAncestor(this);
            if (ventanaActual != null) {
                ventanaActual.dispose();
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        panelRedondo1 = new componentes.PanelRedondo();
        lbltitulo = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        JPnlfotoUsuario = new componentes.PanelImagen();
        txtNombrenegocio = new componentes.TextFieldModerno();
        jLabel7 = new javax.swing.JLabel();
        txtCorreoelectronico = new componentes.TextFieldModerno();
        jLabel9 = new javax.swing.JLabel();
        txtnombrenegocio = new componentes.TextFieldModerno();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtnombreUsuario = new componentes.TextFieldModerno();
        jLabel13 = new javax.swing.JLabel();
        txtApellidoUsuario = new componentes.TextFieldModerno();
        jLabel14 = new javax.swing.JLabel();
        panelRedondo2 = new componentes.PanelRedondo();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstCategorias = new componentes.ListaCatalogos<>();
        txtCategorias = new componentes.TextFieldModerno();
        jLabel15 = new javax.swing.JLabel();
        btnAgregarCategoria = new componentes.BotonModerno();
        btnEliminarCategoria = new componentes.BotonModerno();
        txtCorreo = new componentes.TextFieldModerno();
        jLabel16 = new javax.swing.JLabel();
        txtverificacion = new componentes.TextFieldModerno();
        jLabel17 = new javax.swing.JLabel();
        txtContrasenia = new componentes.TextFieldModerno();
        BtnCambiar = new componentes.BotonModerno();
        botonModerno4 = new componentes.BotonModerno();
        botonModerno5 = new componentes.BotonModerno();
        lblCodigo = new javax.swing.JLabel();
        lblTitulocodigo = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        panelRedondo3 = new componentes.PanelRedondo();
        moduloRol1 = new componentes.ModuloRol();
        moduloRol2 = new componentes.ModuloRol();
        moduloRol3 = new componentes.ModuloRol();
        moduloRol4 = new componentes.ModuloRol();
        moduloRol5 = new componentes.ModuloRol();

        setBackground(new java.awt.Color(31, 10, 48));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Lucida Bright", 1, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("CONFIGURACION DE USUARIO");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, -10, 790, 120));

        panelRedondo1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbltitulo.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        lbltitulo.setForeground(new java.awt.Color(255, 255, 255));
        lbltitulo.setText("CONFIGURACION DEL NEGOCIO");
        panelRedondo1.add(lbltitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 340, 40));

        jLabel3.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Nombre del negocio:");
        panelRedondo1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 340, 40));

        jLabel6.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("CONFIGURACION DEL USUARIO");
        panelRedondo1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 20, 340, 40));

        javax.swing.GroupLayout JPnlfotoUsuarioLayout = new javax.swing.GroupLayout(JPnlfotoUsuario);
        JPnlfotoUsuario.setLayout(JPnlfotoUsuarioLayout);
        JPnlfotoUsuarioLayout.setHorizontalGroup(
            JPnlfotoUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        JPnlfotoUsuarioLayout.setVerticalGroup(
            JPnlfotoUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );

        panelRedondo1.add(JPnlfotoUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 80, 590, 200));

        txtNombrenegocio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombrenegocioActionPerformed(evt);
            }
        });
        panelRedondo1.add(txtNombrenegocio, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, 500, 40));

        jLabel7.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Correo del negocio:");
        panelRedondo1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, 340, 40));
        panelRedondo1.add(txtCorreoelectronico, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 500, 40));

        jLabel9.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Asistente IA");
        panelRedondo1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 670, 340, 40));
        panelRedondo1.add(txtnombrenegocio, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 320, 500, 40));

        jLabel10.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Modulos");
        panelRedondo1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 380, 340, 40));

        jLabel12.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Confirmar antes de eliminar siempre");
        panelRedondo1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 620, 340, 40));

        txtnombreUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnombreUsuarioActionPerformed(evt);
            }
        });
        panelRedondo1.add(txtnombreUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 350, 350, 40));

        jLabel13.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Nombre del Usuario:");
        panelRedondo1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 300, 340, 40));
        panelRedondo1.add(txtApellidoUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(1240, 350, 380, 40));

        jLabel14.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Apellido del Usuario:");
        panelRedondo1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(1250, 300, 340, 40));

        panelRedondo2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lstCategorias.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(lstCategorias);

        panelRedondo2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 488, 130));

        panelRedondo1.add(panelRedondo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 430, 510, 150));
        panelRedondo1.add(txtCategorias, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 620, 330, 40));

        jLabel15.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Nombre del negocio");
        panelRedondo1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 270, 340, 40));

        btnAgregarCategoria.setText("AGREGAR");
        btnAgregarCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarCategoriaActionPerformed(evt);
            }
        });
        panelRedondo1.add(btnAgregarCategoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 620, 110, 40));

        btnEliminarCategoria.setText("ELIMINAR");
        btnEliminarCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarCategoriaActionPerformed(evt);
            }
        });
        panelRedondo1.add(btnEliminarCategoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 620, 110, 40));

        txtCorreo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCorreoActionPerformed(evt);
            }
        });
        panelRedondo1.add(txtCorreo, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 450, 770, 40));

        jLabel16.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Correo Electronico:");
        panelRedondo1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 400, 340, 40));

        txtverificacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtverificacionActionPerformed(evt);
            }
        });
        panelRedondo1.add(txtverificacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(1180, 560, 310, 40));

        jLabel17.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Cambiar contraseña:");
        panelRedondo1.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 510, 340, 40));

        txtContrasenia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtContraseniaActionPerformed(evt);
            }
        });
        panelRedondo1.add(txtContrasenia, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 560, 310, 40));

        BtnCambiar.setText("CAMBIAR");
        BtnCambiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCambiarActionPerformed(evt);
            }
        });
        panelRedondo1.add(BtnCambiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1506, 560, 100, 40));

        add(panelRedondo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 100, 1690, 750));

        botonModerno4.setText("GUARDAR CAMBIOS");
        botonModerno4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonModerno4ActionPerformed(evt);
            }
        });
        add(botonModerno4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1560, 1480, 180, 60));

        botonModerno5.setText("ELIMINAR CUENTA");
        botonModerno5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonModerno5ActionPerformed(evt);
            }
        });
        add(botonModerno5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1370, 1480, 180, 60));

        lblCodigo.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        lblCodigo.setForeground(new java.awt.Color(255, 255, 255));
        add(lblCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(1410, 40, 270, 40));

        lblTitulocodigo.setFont(new java.awt.Font("Lucida Bright", 1, 24)); // NOI18N
        lblTitulocodigo.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulocodigo.setText("CODIGO DEL NEGOCIO:");
        add(lblTitulocodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 40, 310, 40));

        jLabel2.setFont(new java.awt.Font("Lucida Bright", 1, 48)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("MODULOS:");
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 860, 460, 120));

        panelRedondo3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        panelRedondo3.add(moduloRol1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 70, -1, -1));
        panelRedondo3.add(moduloRol2, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 70, -1, -1));
        panelRedondo3.add(moduloRol3, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 70, -1, -1));
        panelRedondo3.add(moduloRol4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1240, 70, -1, -1));
        panelRedondo3.add(moduloRol5, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 250, -1, -1));

        add(panelRedondo3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 980, 1690, 460));
    }// </editor-fold>//GEN-END:initComponents

    private void txtNombrenegocioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombrenegocioActionPerformed

    }//GEN-LAST:event_txtNombrenegocioActionPerformed

    private void txtnombreUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnombreUsuarioActionPerformed

    }//GEN-LAST:event_txtnombreUsuarioActionPerformed

    private void btnAgregarCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarCategoriaActionPerformed
        agregarCategoria();
    }//GEN-LAST:event_btnAgregarCategoriaActionPerformed

    private void btnEliminarCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarCategoriaActionPerformed
        eliminarCategoria();
    }//GEN-LAST:event_btnEliminarCategoriaActionPerformed

    private void txtCorreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCorreoActionPerformed

    }//GEN-LAST:event_txtCorreoActionPerformed

    private void txtverificacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtverificacionActionPerformed
        cambiarContrasenia();
    }//GEN-LAST:event_txtverificacionActionPerformed

    private void txtContraseniaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtContraseniaActionPerformed

    }//GEN-LAST:event_txtContraseniaActionPerformed

    private void BtnCambiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCambiarActionPerformed
        cambiarContrasenia();
    }//GEN-LAST:event_BtnCambiarActionPerformed

    private void botonModerno4ActionPerformed(java.awt.event.ActionEvent evt) {
        guardarCambios();
    }

    private void botonModerno5ActionPerformed(java.awt.event.ActionEvent evt) {
        eliminarCuenta();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private componentes.BotonModerno BtnCambiar;
    private componentes.PanelImagen JPnlfotoUsuario;
    private componentes.BotonModerno botonModerno4;
    private componentes.BotonModerno botonModerno5;
    private componentes.BotonModerno btnAgregarCategoria;
    private componentes.BotonModerno btnEliminarCategoria;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCodigo;
    private javax.swing.JLabel lblTitulocodigo;
    private javax.swing.JLabel lbltitulo;
    private componentes.ListaCatalogos<String> lstCategorias;
    private componentes.ModuloRol moduloRol1;
    private componentes.ModuloRol moduloRol2;
    private componentes.ModuloRol moduloRol3;
    private componentes.ModuloRol moduloRol4;
    private componentes.ModuloRol moduloRol5;
    private componentes.PanelRedondo panelRedondo1;
    private componentes.PanelRedondo panelRedondo2;
    private componentes.PanelRedondo panelRedondo3;
    private componentes.TextFieldModerno txtApellidoUsuario;
    private componentes.TextFieldModerno txtCategorias;
    private componentes.TextFieldModerno txtContrasenia;
    private componentes.TextFieldModerno txtCorreo;
    private componentes.TextFieldModerno txtCorreoelectronico;
    private componentes.TextFieldModerno txtNombrenegocio;
    private componentes.TextFieldModerno txtnombreUsuario;
    private componentes.TextFieldModerno txtnombrenegocio;
    private componentes.TextFieldModerno txtverificacion;
    // End of variables declaration//GEN-END:variables
}
