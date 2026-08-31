
package Vista;

public class PanelModulos extends javax.swing.JPanel {

    public PanelModulos() {
        initComponents();
        ModuloCatalogo.setNombreModulo("Catálogo");
        ModuloVentasFacturacion.setNombreModulo("Ventas y Facturación");
        ModuloFinanzas.setNombreModulo("Finanzas");
        ModuloRRHH.setNombreModulo("Recursos Humanos");
        ModuloConfiguracion.setNombreModulo("Configuración");

        cargarDescripciones();

        ModuloConfiguracion.setBloqueado(true);

        BtnSiguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSiguienteClicked(evt);
            }
        });
    }

    private void cargarDescripciones() {
        Controladores.ControladorModulo controladorModulo = new Controladores.ControladorModulo();
        java.util.List<Modelo.Modulo> catalogo = controladorModulo.obtenerCatalogoCompleto();

        for (Modelo.Modulo m : catalogo) {
            switch (m.getNombreModulo()) {
                case "Catálogo":
                    ModuloCatalogo.setDescripcion(m.getDescripcion());
                    break;
                case "Ventas y Facturación":
                    ModuloVentasFacturacion.setDescripcion(m.getDescripcion());
                    break;
                case "Finanzas":
                    ModuloFinanzas.setDescripcion(m.getDescripcion());
                    break;
                case "Recursos Humanos":
                    ModuloRRHH.setDescripcion(m.getDescripcion());
                    break;
                case "Configuración":
                    ModuloConfiguracion.setDescripcion(m.getDescripcion());
                    break;
                default:
                    break;
            }
        }
    }
        private void btnSiguienteClicked(java.awt.event.ActionEvent evt) {
        String idNegocio = Modelo.Sesion.getIdNegocio();
        Controladores.ControladorModulo controladorModulo = new Controladores.ControladorModulo();

        if (ModuloCatalogo.isActivo()) controladorModulo.activarModulo(idNegocio, ModuloCatalogo.getNombreModulo());
        if (ModuloVentasFacturacion.isActivo()) controladorModulo.activarModulo(idNegocio, ModuloVentasFacturacion.getNombreModulo());
        if (ModuloFinanzas.isActivo()) controladorModulo.activarModulo(idNegocio, ModuloFinanzas.getNombreModulo());
        if (ModuloRRHH.isActivo()) controladorModulo.activarModulo(idNegocio, ModuloRRHH.getNombreModulo());
        controladorModulo.activarModulo(idNegocio, ModuloConfiguracion.getNombreModulo());

        MenuPrincipal.usuarioActivo = Modelo.Sesion.getNombreUsuario();
        MenuPrincipal menu = new MenuPrincipal();
        menu.setVisible(true);

        java.awt.Window ventanaActual = javax.swing.SwingUtilities.getWindowAncestor(this);
        if (ventanaActual != null) {
            ventanaActual.dispose();
        }
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ModuloCatalogo = new componentes.ModuloRol();
        ModuloVentasFacturacion = new componentes.ModuloRol();
        ModuloFinanzas = new componentes.ModuloRol();
        ModuloRRHH = new componentes.ModuloRol();
        ModuloConfiguracion = new componentes.ModuloRol();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        BtnSiguiente = new componentes.BotonModerno();

        setBackground(new java.awt.Color(31, 10, 48));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        add(ModuloCatalogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 320, -1, -1));
        add(ModuloVentasFacturacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 320, -1, -1));
        add(ModuloFinanzas, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 320, -1, -1));
        add(ModuloRRHH, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 520, -1, -1));
        add(ModuloConfiguracion, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 520, -1, -1));

        jLabel1.setFont(new java.awt.Font("Lucida Bright", 1, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("MODULOS");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 30, -1, -1));

        jLabel2.setFont(new java.awt.Font("Lucida Bright", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Puedes cambiar esta selección después desde Configuración ---> Módulos.");
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 160, -1, -1));

        jLabel3.setFont(new java.awt.Font("Lucida Bright", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("El menú superior se adapta automáticamente según los módulos que actives. ");
        add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 120, -1, -1));

        BtnSiguiente.setText("CONTINAR");
        add(BtnSiguiente, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 880, 240, 80));
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private componentes.BotonModerno BtnSiguiente;
    private componentes.ModuloRol ModuloCatalogo;
    private componentes.ModuloRol ModuloConfiguracion;
    private componentes.ModuloRol ModuloFinanzas;
    private componentes.ModuloRol ModuloRRHH;
    private componentes.ModuloRol ModuloVentasFacturacion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    // End of variables declaration//GEN-END:variables

}
