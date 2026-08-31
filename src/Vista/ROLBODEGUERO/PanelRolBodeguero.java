
package Vista.ROLBODEGUERO;

import Controladores.ControladorInventarioBodega;
import Modelo.Producto;

import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.awt.Frame;
import java.util.List;

public class PanelRolBodeguero extends javax.swing.JPanel {

    public PanelRolBodeguero() {
        initComponents();
        componentes.EstiloTablaKrypton.aplicar(tablaInventario);

        cargarInventario();
        cargarMasSolicitados();
        cargarStockPorAgotarse();
        cargarNotasRapidas();
        cargarMovimientosRecientes();
    }

    private void cargarInventario() {
        DefaultTableModel modelo = (DefaultTableModel) tablaInventario.getModel();
        modelo.setRowCount(0);

        List<Producto> productos = ControladorInventarioBodega.listarInventario();
        for (Producto p : productos) {
            String ubicacion = (p.getUbicacionPasillo() == null || p.getUbicacionPasillo().isEmpty())
                    ? "N/A" : p.getUbicacionPasillo();
            String lote = (p.getLote() == null || p.getLote().isEmpty()) ? "-" : p.getLote();
            String vencimiento = (p.getFechaVencimiento() == null || p.getFechaVencimiento().isEmpty())
                    ? "N/A" : p.getFechaVencimiento();
            String stockMaxMin = (p.getStockMaximo() > 0 ? p.getStockMaximo() : p.getStockMinimo())
                    + "/" + p.getStockMinimo();

            modelo.addRow(new Object[]{
                p.getCodigo(),
                p.getNombre(),
                ubicacion,
                lote + " / " + vencimiento,
                p.getCantidad() + " uds.",
                stockMaxMin
            });
        }
    }

    private void cargarMasSolicitados() {
        List<Object[]> masSolicitados = ControladorInventarioBodega.listarMasSolicitados(3);
        javax.swing.JLabel[] nombres = {lblMasSolNombre1, lblMasSolNombre2, lblMasSolNombre3};
        javax.swing.JLabel[] cantidades = {lblMasSolCantidad1, lblMasSolCantidad2, lblMasSolCantidad3};

        for (int i = 0; i < nombres.length; i++) {
            if (i < masSolicitados.size()) {
                Object[] fila = masSolicitados.get(i);
                nombres[i].setText((String) fila[0]);
                cantidades[i].setText(fila[1] + " vend.");
            } else {
                nombres[i].setText("-");
                cantidades[i].setText("");
            }
        }
    }

    private void cargarStockPorAgotarse() {
        List<Producto> bajoStock = ControladorInventarioBodega.listarBajoStock(3);
        javax.swing.JLabel[] nombres = {lblAgotNombre1, lblAgotNombre2, lblAgotNombre3};
        javax.swing.JLabel[] cantidades = {lblAgotCantidad1, lblAgotCantidad2, lblAgotCantidad3};

        for (int i = 0; i < nombres.length; i++) {
            if (i < bajoStock.size()) {
                Producto p = bajoStock.get(i);
                nombres[i].setText(p.getNombre());
                cantidades[i].setText(p.getCantidad() + " uds.");
            } else {
                nombres[i].setText("-");
                cantidades[i].setText("");
            }
        }
    }

    private void cargarNotasRapidas() {
        List<Modelo.Nota> notas = new Controladores.ControladorNota().listar();
        DefaultListModel<String> modelo = new DefaultListModel<>();
        for (Modelo.Nota n : notas) {
            modelo.addElement("+ " + n.getTitulo());
        }
        listaNotasRapidas.setModel(modelo);
    }

    private void cargarMovimientosRecientes() {
        DefaultListModel<String> modelo = new DefaultListModel<>();
        List<Object[]> movimientos = ControladorInventarioBodega.listarMovimientosRecientes(8);

        if (movimientos.isEmpty()) {
            modelo.addElement("Aun no hay movimientos registrados.");
        } else {
            for (Object[] m : movimientos) {
                String tipo = String.valueOf(m[0]);
                String nombre = String.valueOf(m[1]);
                int cantidad = ((Number) m[2]).intValue();
                java.sql.Date fecha = (java.sql.Date) m[3];
                String fechaTexto = fecha == null ? "" : fecha.toLocalDate().toString();
                modelo.addElement(tipo + "  |  " + nombre + "  |  " + cantidad + " uds.  |  " + fechaTexto);
            }
        }
        listaMovimientos.setModel(modelo);
    }

    private void refrescarDashboard(String resultadoMovimiento) {
        cargarInventario();
        cargarMasSolicitados();
        cargarStockPorAgotarse();
        cargarMovimientosRecientes();
    }

    private Frame ventanaPadre() {
        return (Frame) SwingUtilities.getWindowAncestor(this);
    }

    private void btnRegistrarEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarEntradaActionPerformed
        DialogMovimientoBodega dialogo = new DialogMovimientoBodega(ventanaPadre(), DialogMovimientoBodega.Modo.ENTRADA);
        dialogo.setVisible(true);
        refrescarDashboard(dialogo.getResultado());
    }//GEN-LAST:event_btnRegistrarEntradaActionPerformed

    private void btnConfirmarSalidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmarSalidaActionPerformed
        DialogMovimientoBodega dialogo = new DialogMovimientoBodega(ventanaPadre(), DialogMovimientoBodega.Modo.SALIDA);
        dialogo.setVisible(true);
        refrescarDashboard(dialogo.getResultado());
    }//GEN-LAST:event_btnConfirmarSalidaActionPerformed

    private void btnAjustarInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAjustarInventarioActionPerformed
        DialogAjustarInventario dialogo = new DialogAjustarInventario(ventanaPadre());
        dialogo.setVisible(true);
        if (dialogo.isGuardado()) {
            cargarInventario();
            cargarStockPorAgotarse();
        }
    }//GEN-LAST:event_btnAjustarInventarioActionPerformed

    private void btnAgregarNotaBodegaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarNotaBodegaActionPerformed
        String titulo = javax.swing.JOptionPane.showInputDialog(this,
                "Titulo de la nota operativa (ej: Registrar entrada Lote 551):",
                "Nueva nota rapida", javax.swing.JOptionPane.PLAIN_MESSAGE);
        if (titulo != null && !titulo.trim().isEmpty()) {
            new Controladores.ControladorNota().registrar(this, titulo, "");
            cargarNotasRapidas();
        }
    }//GEN-LAST:event_btnAgregarNotaBodegaActionPerformed

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTituloDashboard = new javax.swing.JLabel();
        panelInventario = new componentes.PanelRedondo();
        lblTituloInventario = new javax.swing.JLabel();
        jScrollPaneInventario = new javax.swing.JScrollPane();
        tablaInventario = new javax.swing.JTable();
        btnRegistrarEntrada = new componentes.BotonModerno();
        btnConfirmarSalida = new componentes.BotonModerno();
        btnAjustarInventario = new componentes.BotonModerno();
        panelMasSolicitados = new componentes.PanelRedondo();
        lblTituloMasSolicitados = new javax.swing.JLabel();
        iconMasSolicitados = new javax.swing.JLabel();
        lblMasSolNombre1 = new javax.swing.JLabel();
        lblMasSolCantidad1 = new javax.swing.JLabel();
        lblMasSolNombre2 = new javax.swing.JLabel();
        lblMasSolCantidad2 = new javax.swing.JLabel();
        lblMasSolNombre3 = new javax.swing.JLabel();
        lblMasSolCantidad3 = new javax.swing.JLabel();
        panelStockAgotarse = new componentes.PanelRedondo();
        lblTituloStockAgotarse = new javax.swing.JLabel();
        iconStockAgotarse = new javax.swing.JLabel();
        lblAgotNombre1 = new javax.swing.JLabel();
        lblAgotCantidad1 = new javax.swing.JLabel();
        lblAgotNombre2 = new javax.swing.JLabel();
        lblAgotCantidad2 = new javax.swing.JLabel();
        lblAgotNombre3 = new javax.swing.JLabel();
        lblAgotCantidad3 = new javax.swing.JLabel();
        panelNotasRapidas = new componentes.PanelRedondo();
        lblTituloNotasRapidas = new javax.swing.JLabel();
        btnAgregarNotaBodega = new componentes.BotonModerno();
        jScrollPaneNotas = new javax.swing.JScrollPane();
        listaNotasRapidas = new javax.swing.JList();
        panelMovimientos = new componentes.PanelRedondo();
        lblTituloMovimientos = new javax.swing.JLabel();
        jScrollPaneMovimientos = new javax.swing.JScrollPane();
        listaMovimientos = new javax.swing.JList();

        setBackground(new java.awt.Color(31, 10, 48));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTituloDashboard.setFont(new java.awt.Font("Lucida Bright", 1, 26)); // NOI18N
        lblTituloDashboard.setForeground(new java.awt.Color(255, 255, 255));
        lblTituloDashboard.setText(" BODEGUERO");
        add(lblTituloDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 15, 900, 40));

        panelInventario.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTituloInventario.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        lblTituloInventario.setForeground(new java.awt.Color(255, 255, 255));
        lblTituloInventario.setText("INVENTARIO EN BODEGA");
        panelInventario.add(lblTituloInventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 15, 400, 30));

        tablaInventario.setBackground(new java.awt.Color(28, 9, 40));
        tablaInventario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Descripción de Producto", "Ubicación (Pasillo)", "Lote / Vencimiento", "Stock Disponible", "Stock Máx./Mín."
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPaneInventario.setViewportView(tablaInventario);

        panelInventario.add(jScrollPaneInventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 55, 1110, 700));

        add(panelInventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 65, 1150, 780));

        btnRegistrarEntrada.setText("Registrar Entrada");
        btnRegistrarEntrada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarEntradaActionPerformed(evt);
            }
        });
        add(btnRegistrarEntrada, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 870, 260, 45));

        btnConfirmarSalida.setText("Confirmar Salida");
        btnConfirmarSalida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmarSalidaActionPerformed(evt);
            }
        });
        add(btnConfirmarSalida, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 870, 260, 45));

        btnAjustarInventario.setText("Ajustar Inventario");
        btnAjustarInventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAjustarInventarioActionPerformed(evt);
            }
        });
        add(btnAjustarInventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 870, 260, 45));

        panelMasSolicitados.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTituloMasSolicitados.setFont(new java.awt.Font("Lucida Bright", 1, 16)); // NOI18N
        lblTituloMasSolicitados.setForeground(new java.awt.Color(200, 170, 255));
        lblTituloMasSolicitados.setText("MÁS SOLICITADOS (DESPACHO)");
        panelMasSolicitados.add(lblTituloMasSolicitados, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 12, 480, 25));

        iconMasSolicitados.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgKrypton/fuego.png"))); // NOI18N
        panelMasSolicitados.add(iconMasSolicitados, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 8, 32, 32));

        lblMasSolNombre1.setBackground(new java.awt.Color(255, 255, 255));
        lblMasSolNombre1.setText("-");
        panelMasSolicitados.add(lblMasSolNombre1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 380, 25));
        panelMasSolicitados.add(lblMasSolCantidad1, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 50, 180, 25));

        lblMasSolNombre2.setBackground(new java.awt.Color(255, 255, 255));
        lblMasSolNombre2.setText("-");
        panelMasSolicitados.add(lblMasSolNombre2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 82, 380, 25));
        panelMasSolicitados.add(lblMasSolCantidad2, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 82, 180, 25));

        lblMasSolNombre3.setBackground(new java.awt.Color(255, 255, 255));
        lblMasSolNombre3.setText("-");
        panelMasSolicitados.add(lblMasSolNombre3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 114, 380, 25));
        panelMasSolicitados.add(lblMasSolCantidad3, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 114, 180, 25));

        add(panelMasSolicitados, new org.netbeans.lib.awtextra.AbsoluteConstraints(1190, 65, 620, 165));

        panelStockAgotarse.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTituloStockAgotarse.setFont(new java.awt.Font("Lucida Bright", 1, 16)); // NOI18N
        lblTituloStockAgotarse.setForeground(new java.awt.Color(255, 190, 130));
        lblTituloStockAgotarse.setText("STOCK POR AGOTARSE");
        panelStockAgotarse.add(lblTituloStockAgotarse, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 12, 480, 25));

        iconStockAgotarse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgKrypton/precaucion.png"))); // NOI18N
        panelStockAgotarse.add(iconStockAgotarse, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 8, 32, 32));

        lblAgotNombre1.setBackground(new java.awt.Color(255, 255, 255));
        lblAgotNombre1.setText("-");
        panelStockAgotarse.add(lblAgotNombre1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 380, 25));
        panelStockAgotarse.add(lblAgotCantidad1, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 50, 180, 25));

        lblAgotNombre2.setBackground(new java.awt.Color(255, 255, 255));
        lblAgotNombre2.setText("-");
        panelStockAgotarse.add(lblAgotNombre2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 82, 380, 25));
        panelStockAgotarse.add(lblAgotCantidad2, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 82, 180, 25));

        lblAgotNombre3.setBackground(new java.awt.Color(255, 255, 255));
        lblAgotNombre3.setText("-");
        panelStockAgotarse.add(lblAgotNombre3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 114, 380, 25));
        panelStockAgotarse.add(lblAgotCantidad3, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 114, 180, 25));

        add(panelStockAgotarse, new org.netbeans.lib.awtextra.AbsoluteConstraints(1190, 250, 620, 165));

        panelNotasRapidas.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTituloNotasRapidas.setFont(new java.awt.Font("Lucida Bright", 1, 16)); // NOI18N
        lblTituloNotasRapidas.setForeground(new java.awt.Color(255, 255, 255));
        lblTituloNotasRapidas.setText("NOTAS RÁPIDAS (OPERATIVAS)");
        panelNotasRapidas.add(lblTituloNotasRapidas, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 12, 460, 25));

        btnAgregarNotaBodega.setText("+");
        btnAgregarNotaBodega.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarNotaBodegaActionPerformed(evt);
            }
        });
        panelNotasRapidas.add(btnAgregarNotaBodega, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 6, 40, 34));

        listaNotasRapidas.setBackground(new java.awt.Color(28, 9, 40));
        listaNotasRapidas.setForeground(new java.awt.Color(255, 255, 255));
        jScrollPaneNotas.setViewportView(listaNotasRapidas);

        panelNotasRapidas.add(jScrollPaneNotas, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 48, 590, 125));

        add(panelNotasRapidas, new org.netbeans.lib.awtextra.AbsoluteConstraints(1190, 435, 620, 190));

        panelMovimientos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTituloMovimientos.setFont(new java.awt.Font("Lucida Bright", 1, 16)); // NOI18N
        lblTituloMovimientos.setForeground(new java.awt.Color(255, 255, 255));
        lblTituloMovimientos.setText("MOVIMIENTOS RECIENTES");
        panelMovimientos.add(lblTituloMovimientos, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 12, 460, 25));

        listaMovimientos.setBackground(new java.awt.Color(28, 9, 40));
        listaMovimientos.setForeground(new java.awt.Color(255, 255, 255));
        jScrollPaneMovimientos.setViewportView(listaMovimientos);

        panelMovimientos.add(jScrollPaneMovimientos, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 48, 590, 140));

        add(panelMovimientos, new org.netbeans.lib.awtextra.AbsoluteConstraints(1190, 640, 620, 210));
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private componentes.BotonModerno btnAgregarNotaBodega;
    private componentes.BotonModerno btnAjustarInventario;
    private componentes.BotonModerno btnConfirmarSalida;
    private componentes.BotonModerno btnRegistrarEntrada;
    private javax.swing.JLabel iconMasSolicitados;
    private javax.swing.JLabel iconStockAgotarse;
    private javax.swing.JScrollPane jScrollPaneInventario;
    private javax.swing.JScrollPane jScrollPaneMovimientos;
    private javax.swing.JScrollPane jScrollPaneNotas;
    private javax.swing.JLabel lblAgotCantidad1;
    private javax.swing.JLabel lblAgotCantidad2;
    private javax.swing.JLabel lblAgotCantidad3;
    private javax.swing.JLabel lblAgotNombre1;
    private javax.swing.JLabel lblAgotNombre2;
    private javax.swing.JLabel lblAgotNombre3;
    private javax.swing.JLabel lblMasSolCantidad1;
    private javax.swing.JLabel lblMasSolCantidad2;
    private javax.swing.JLabel lblMasSolCantidad3;
    private javax.swing.JLabel lblMasSolNombre1;
    private javax.swing.JLabel lblMasSolNombre2;
    private javax.swing.JLabel lblMasSolNombre3;
    private javax.swing.JLabel lblTituloDashboard;
    private javax.swing.JLabel lblTituloInventario;
    private javax.swing.JLabel lblTituloMasSolicitados;
    private javax.swing.JLabel lblTituloMovimientos;
    private javax.swing.JLabel lblTituloNotasRapidas;
    private javax.swing.JLabel lblTituloStockAgotarse;
    private javax.swing.JList listaMovimientos;
    private javax.swing.JList listaNotasRapidas;
    private componentes.PanelRedondo panelInventario;
    private componentes.PanelRedondo panelMasSolicitados;
    private componentes.PanelRedondo panelMovimientos;
    private componentes.PanelRedondo panelNotasRapidas;
    private componentes.PanelRedondo panelStockAgotarse;
    private javax.swing.JTable tablaInventario;
    // End of variables declaration//GEN-END:variables
}
