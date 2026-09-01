
package Vista.REPORTES;

import Controladores.ControladorEstadistica;
import Controladores.ControladorEstadistica.ResultadoEstadistica;
import Controladores.ControladorEstadistica.TipoPeriodo;
import Modelo.EstadisticaPeriodo;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class PanelEstadistica extends javax.swing.JPanel {

    private static final Color COLOR_POSITIVO = new Color(76, 175, 80);
    private static final Color COLOR_NEGATIVO = new Color(229, 62, 90);
    private static final Color COLOR_NEUTRO = new Color(230, 220, 240);
    private static final Color COLOR_BOTON_NORMAL = new Color(55, 25, 75);
    private static final Color COLOR_BOTON_ACTIVO = new Color(110, 50, 150);
    private static final DecimalFormat FORMATO_MONEDA = new DecimalFormat("$#,##0.00");

    private final ControladorEstadistica controladorEstadistica = new ControladorEstadistica();
    private TipoPeriodo periodoActual = TipoPeriodo.HOY;
    private boolean primeraCarga = true;

    public PanelEstadistica() {
        initComponents();
        estilizarTablaDetalle();
        marcarBotonPeriodoActivo(BtnGraficoHoy);
        cargarEstadisticas(periodoActual);
    }

    private void estilizarTablaDetalle() {
        componentes.EstiloTablaKrypton.aplicar(LstCont);
        jScrollPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        jScrollPane1.getViewport().setBackground(componentes.EstiloTablaKrypton.FONDO_TABLA);
        componentes.ScrollBarModerno.aplicar(jScrollPane1);
    }

    public String getTipoGraficoActual() {
        return graficoEstadisticas.getTipoActual();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        BtnGraficoHoy = new componentes.BotonModerno();
        BtnGraficoSemana = new componentes.BotonModerno();
        BtnGraficoMes = new componentes.BotonModerno();
        BtnGraficoTrimestre = new componentes.BotonModerno();
        BtnGraficoLineas = new componentes.BotonModerno();
        BtnGraficoBarras = new componentes.BotonModerno();
        BtnGraficoBarrasH = new componentes.BotonModerno();
        BtnGraficoArea = new componentes.BotonModerno();
        lbltitulo = new javax.swing.JLabel();
        PanelGrafico = new componentes.PanelRedondo();
        lblmostrargrafico = new javax.swing.JLabel();
        pnlcolorr = new javax.swing.JPanel();
        pnlcolorv = new javax.swing.JPanel();
        lblgasto = new javax.swing.JLabel();
        lblganancias = new javax.swing.JLabel();
        graficoEstadisticas = new componentes.GraficoKrypton();
        PnlCatvendida = new componentes.PanelRedondo();
        lblcatvendidas = new javax.swing.JLabel();
        lblCatVendida = new javax.swing.JLabel();
        PnlGanancias = new componentes.PanelRedondo();
        lblgancias = new javax.swing.JLabel();
        lblCantGanancias = new javax.swing.JLabel();
        lblTITLEganacias = new javax.swing.JLabel();
        lblPeriodoGanancias = new javax.swing.JLabel();
        Pnlbalance = new componentes.PanelRedondo();
        lblnetos = new javax.swing.JLabel();
        lblCantBalance = new javax.swing.JLabel();
        lblTITLEestado = new javax.swing.JLabel();
        lblEstado = new javax.swing.JLabel();
        PnlGastos = new componentes.PanelRedondo();
        lblTgastos = new javax.swing.JLabel();
        lblCantGastos = new javax.swing.JLabel();
        lblTITLEgastos = new javax.swing.JLabel();
        lblPeriodoGastos = new javax.swing.JLabel();
        PnlVendidos = new componentes.PanelRedondo();
        lblvendidos = new javax.swing.JLabel();
        lblCantVendido = new javax.swing.JLabel();
        lbltotalunidades = new javax.swing.JLabel();
        PnlActivos = new componentes.PanelRedondo();
        lblactivos = new javax.swing.JLabel();
        lblCantActivos = new javax.swing.JLabel();
        lblEninventario = new javax.swing.JLabel();
        PnlLista = new componentes.PanelRedondo();
        jScrollPane1 = new javax.swing.JScrollPane();
        LstCont = new javax.swing.JTable();
        lbldescripcion = new javax.swing.JLabel();

        setBackground(new java.awt.Color(31, 10, 48));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        BtnGraficoHoy.setText("HOY");
        BtnGraficoHoy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnGraficoHoyActionPerformed(evt);
            }
        });
        add(BtnGraficoHoy, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 150, 140, 50));

        BtnGraficoSemana.setText("SEMANA");
        BtnGraficoSemana.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnGraficoSemanaActionPerformed(evt);
            }
        });
        add(BtnGraficoSemana, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 150, 140, 50));

        BtnGraficoMes.setText("MES");
        BtnGraficoMes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnGraficoMesActionPerformed(evt);
            }
        });
        add(BtnGraficoMes, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 150, 140, 50));

        BtnGraficoTrimestre.setText("TRIMESTRE");
        BtnGraficoTrimestre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnGraficoTrimestreActionPerformed(evt);
            }
        });
        add(BtnGraficoTrimestre, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 150, 140, 50));

        BtnGraficoLineas.setText("LINEAS");
        BtnGraficoLineas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnGraficoLineasActionPerformed(evt);
            }
        });
        add(BtnGraficoLineas, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 150, 140, 50));

        BtnGraficoBarras.setText("BARRAS");
        BtnGraficoBarras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnGraficoBarrasActionPerformed(evt);
            }
        });
        add(BtnGraficoBarras, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 150, 140, 50));

        BtnGraficoBarrasH.setText("BARRAS HORIZONTALES");
        BtnGraficoBarrasH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnGraficoBarrasHActionPerformed(evt);
            }
        });
        add(BtnGraficoBarrasH, new org.netbeans.lib.awtextra.AbsoluteConstraints(1040, 150, 220, 50));

        BtnGraficoArea.setText("AREA");
        BtnGraficoArea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnGraficoAreaActionPerformed(evt);
            }
        });
        add(BtnGraficoArea, new org.netbeans.lib.awtextra.AbsoluteConstraints(1270, 150, 140, 50));

        lbltitulo.setFont(new java.awt.Font("Lucida Bright", 1, 36)); // NOI18N
        lbltitulo.setForeground(new java.awt.Color(255, 255, 255));
        lbltitulo.setText("ESTADISTICAS");
        add(lbltitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 30, -1, -1));

        PanelGrafico.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblmostrargrafico.setFont(new java.awt.Font("Lucida Bright", 1, 25)); // NOI18N
        lblmostrargrafico.setForeground(new java.awt.Color(255, 255, 255));
        lblmostrargrafico.setText("MOSTRAR EN GRAFICO:");
        PanelGrafico.add(lblmostrargrafico, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        pnlcolorr.setBackground(new java.awt.Color(102, 0, 0));
        pnlcolorr.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        PanelGrafico.add(pnlcolorr, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 60, 20, 20));

        pnlcolorv.setBackground(new java.awt.Color(51, 102, 0));
        pnlcolorv.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        PanelGrafico.add(pnlcolorv, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 20, 20));

        lblgasto.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        lblgasto.setForeground(new java.awt.Color(255, 255, 255));
        lblgasto.setText("GASTOS");
        PanelGrafico.add(lblgasto, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 60, 110, -1));

        lblganancias.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        lblganancias.setForeground(new java.awt.Color(255, 255, 255));
        lblganancias.setText("GANANCIAS");
        PanelGrafico.add(lblganancias, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 60, 110, -1));
        PanelGrafico.add(graficoEstadisticas, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, 1570, 480));

        add(PanelGrafico, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 230, 1660, 610));

        PnlCatvendida.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblcatvendidas.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        lblcatvendidas.setForeground(new java.awt.Color(255, 255, 255));
        lblcatvendidas.setText("CATEGORIA MAS VENDIDA");
        PnlCatvendida.add(lblcatvendidas, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, -1, -1));

        lblCatVendida.setFont(new java.awt.Font("Lucida Bright", 1, 16)); // NOI18N
        lblCatVendida.setForeground(new java.awt.Color(255, 255, 255));
        PnlCatvendida.add(lblCatVendida, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 210, 30));

        add(PnlCatvendida, new org.netbeans.lib.awtextra.AbsoluteConstraints(1460, 870, 260, 130));

        PnlGanancias.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblgancias.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        lblgancias.setForeground(new java.awt.Color(255, 255, 255));
        lblgancias.setText("TOTAL GANANCIAS");
        PnlGanancias.add(lblgancias, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, 160, -1));

        lblCantGanancias.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        lblCantGanancias.setForeground(new java.awt.Color(255, 255, 255));
        PnlGanancias.add(lblCantGanancias, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, 160, 40));

        lblTITLEganacias.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        lblTITLEganacias.setForeground(new java.awt.Color(255, 255, 255));
        lblTITLEganacias.setText("PERIODO:");
        PnlGanancias.add(lblTITLEganacias, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, -1, -1));

        lblPeriodoGanancias.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        lblPeriodoGanancias.setForeground(new java.awt.Color(255, 255, 255));
        PnlGanancias.add(lblPeriodoGanancias, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 100, 120, 20));

        add(PnlGanancias, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 870, 260, 130));

        Pnlbalance.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblnetos.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        lblnetos.setForeground(new java.awt.Color(255, 255, 255));
        lblnetos.setText("BALANCE NETO");
        Pnlbalance.add(lblnetos, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, 130, 20));

        lblCantBalance.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        lblCantBalance.setForeground(new java.awt.Color(255, 255, 255));
        Pnlbalance.add(lblCantBalance, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, 160, 40));

        lblTITLEestado.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        lblTITLEestado.setForeground(new java.awt.Color(255, 255, 255));
        lblTITLEestado.setText("ESTADO:");
        Pnlbalance.add(lblTITLEestado, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, -1, -1));

        lblEstado.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        lblEstado.setForeground(new java.awt.Color(255, 255, 255));
        Pnlbalance.add(lblEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 100, 120, 20));

        add(Pnlbalance, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 870, 260, 130));

        PnlGastos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTgastos.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        lblTgastos.setForeground(new java.awt.Color(255, 255, 255));
        lblTgastos.setText("TOTAL GASTOS");
        PnlGastos.add(lblTgastos, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, 130, -1));

        lblCantGastos.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        lblCantGastos.setForeground(new java.awt.Color(255, 255, 255));
        PnlGastos.add(lblCantGastos, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 40, 160, 40));

        lblTITLEgastos.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        lblTITLEgastos.setForeground(new java.awt.Color(255, 255, 255));
        lblTITLEgastos.setText("PERIODO:");
        PnlGastos.add(lblTITLEgastos, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, -1, -1));

        lblPeriodoGastos.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        lblPeriodoGastos.setForeground(new java.awt.Color(255, 255, 255));
        PnlGastos.add(lblPeriodoGastos, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 100, 120, 20));

        add(PnlGastos, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 870, 260, 130));

        PnlVendidos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblvendidos.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        lblvendidos.setForeground(new java.awt.Color(255, 255, 255));
        lblvendidos.setText("PRODUCTOS VENDIDOS");
        PnlVendidos.add(lblvendidos, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, -1, -1));

        lblCantVendido.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        lblCantVendido.setForeground(new java.awt.Color(255, 255, 255));
        PnlVendidos.add(lblCantVendido, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 40, 160, 40));

        lbltotalunidades.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        lbltotalunidades.setForeground(new java.awt.Color(255, 255, 255));
        lbltotalunidades.setText("TOTAL UNIDADES");
        PnlVendidos.add(lbltotalunidades, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 100, -1, -1));

        add(PnlVendidos, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 870, 260, 130));

        PnlActivos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblactivos.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        lblactivos.setForeground(new java.awt.Color(255, 255, 255));
        lblactivos.setText("PRODUCTOS ACTIVOS");
        PnlActivos.add(lblactivos, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, -1, -1));

        lblCantActivos.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        lblCantActivos.setForeground(new java.awt.Color(255, 255, 255));
        PnlActivos.add(lblCantActivos, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 40, 160, 40));

        lblEninventario.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        lblEninventario.setForeground(new java.awt.Color(255, 255, 255));
        lblEninventario.setText("EN INVENTARIO");
        PnlActivos.add(lblEninventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 100, -1, -1));

        add(PnlActivos, new org.netbeans.lib.awtextra.AbsoluteConstraints(1180, 870, 260, 130));

        PnlLista.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        LstCont.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "PERIODO", "GANANCIAS", "GASTOS", "BALANCE", "PRODUCTOS VENDIDOS"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(LstCont);

        PnlLista.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 1600, 230));

        add(PnlLista, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 1040, 1660, 270));

        lbldescripcion.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        lbldescripcion.setForeground(new java.awt.Color(255, 255, 255));
        lbldescripcion.setText("Evolución de ganancias y gastos con datos reales de tus productos.");
        add(lbldescripcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 80, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void BtnGraficoAreaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnGraficoAreaActionPerformed
        marcarBotonTipoActivo(BtnGraficoArea);
        graficoEstadisticas.mostrarArea();
    }//GEN-LAST:event_BtnGraficoAreaActionPerformed

    private void BtnGraficoHoyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnGraficoHoyActionPerformed
        periodoActual = TipoPeriodo.HOY;
        marcarBotonPeriodoActivo(BtnGraficoHoy);
        cargarEstadisticas(periodoActual);
    }//GEN-LAST:event_BtnGraficoHoyActionPerformed

    private void BtnGraficoSemanaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnGraficoSemanaActionPerformed
        periodoActual = TipoPeriodo.SEMANA;
        marcarBotonPeriodoActivo(BtnGraficoSemana);
        cargarEstadisticas(periodoActual);
    }//GEN-LAST:event_BtnGraficoSemanaActionPerformed

    private void BtnGraficoMesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnGraficoMesActionPerformed
        periodoActual = TipoPeriodo.MES;
        marcarBotonPeriodoActivo(BtnGraficoMes);
        cargarEstadisticas(periodoActual);
    }//GEN-LAST:event_BtnGraficoMesActionPerformed

    private void BtnGraficoTrimestreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnGraficoTrimestreActionPerformed
        periodoActual = TipoPeriodo.TRIMESTRE;
        marcarBotonPeriodoActivo(BtnGraficoTrimestre);
        cargarEstadisticas(periodoActual);
    }//GEN-LAST:event_BtnGraficoTrimestreActionPerformed

    private void BtnGraficoLineasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnGraficoLineasActionPerformed
        marcarBotonTipoActivo(BtnGraficoLineas);
        graficoEstadisticas.mostrarLineas();
    }//GEN-LAST:event_BtnGraficoLineasActionPerformed

    private void BtnGraficoBarrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnGraficoBarrasActionPerformed
        marcarBotonTipoActivo(BtnGraficoBarras);
        graficoEstadisticas.mostrarBarras();
    }//GEN-LAST:event_BtnGraficoBarrasActionPerformed

    private void BtnGraficoBarrasHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnGraficoBarrasHActionPerformed
        marcarBotonTipoActivo(BtnGraficoBarrasH);
        graficoEstadisticas.mostrarBarrasHorizontal();
    }//GEN-LAST:event_BtnGraficoBarrasHActionPerformed

    private void cargarEstadisticas(TipoPeriodo tipo) {
        ResultadoEstadistica resultado = controladorEstadistica.obtenerEstadisticas(tipo);
        List<EstadisticaPeriodo> filas = resultado.getFilas();

        String[] periodos = new String[filas.size()];
        double[] ganancias = new double[filas.size()];
        double[] gastos = new double[filas.size()];
        for (int i = 0; i < filas.size(); i++) {
            EstadisticaPeriodo f = filas.get(i);
            periodos[i] = f.getPeriodo();
            ganancias[i] = f.getGanancias();
            gastos[i] = f.getGastos();
        }
        graficoEstadisticas.setDatos(periodos, ganancias, gastos);
        if (primeraCarga) {
            graficoEstadisticas.mostrarBarras();
            primeraCarga = false;
        }

        lblPeriodoGanancias.setText(resultado.getEtiquetaPeriodo());
        lblPeriodoGastos.setText(resultado.getEtiquetaPeriodo());
        lblCantGanancias.setText(FORMATO_MONEDA.format(resultado.getTotalGanancias()));
        lblCantGastos.setText(FORMATO_MONEDA.format(resultado.getTotalGastos()));
        lblCantVendido.setText(String.valueOf(resultado.getTotalProductosVendidos()));
        lblCantActivos.setText(String.valueOf(resultado.getProductosActivos()));
        lblCatVendida.setText(resultado.getCategoriaMasVendida());

        double balance = resultado.getBalanceNeto();
        lblCantBalance.setText(FORMATO_MONEDA.format(balance));
        if (balance > 0) {
            lblEstado.setText("Positivo");
            lblEstado.setForeground(COLOR_POSITIVO);
            lblCantBalance.setForeground(COLOR_POSITIVO);
        } else if (balance < 0) {
            lblEstado.setText("Negativo");
            lblEstado.setForeground(COLOR_NEGATIVO);
            lblCantBalance.setForeground(COLOR_NEGATIVO);
        } else {
            lblEstado.setText("Neutro");
            lblEstado.setForeground(COLOR_NEUTRO);
            lblCantBalance.setForeground(COLOR_NEUTRO);
        }

        DefaultTableModel modelo = (DefaultTableModel) LstCont.getModel();
        modelo.setRowCount(0);
        for (EstadisticaPeriodo f : filas) {
            modelo.addRow(new Object[]{
                f.getPeriodo(),
                FORMATO_MONEDA.format(f.getGanancias()),
                FORMATO_MONEDA.format(f.getGastos()),
                FORMATO_MONEDA.format(f.getBalance()),
                f.getProductosVendidos()
            });
        }
    }

    private void marcarBotonPeriodoActivo(componentes.BotonModerno activo) {
        for (componentes.BotonModerno b : new componentes.BotonModerno[]{
            BtnGraficoHoy, BtnGraficoSemana, BtnGraficoMes, BtnGraficoTrimestre}) {
            b.setColorNormal(b == activo ? COLOR_BOTON_ACTIVO : COLOR_BOTON_NORMAL);
        }
    }

    private void marcarBotonTipoActivo(componentes.BotonModerno activo) {
        for (componentes.BotonModerno b : new componentes.BotonModerno[]{
            BtnGraficoLineas, BtnGraficoBarras, BtnGraficoBarrasH, BtnGraficoArea}) {
            b.setColorNormal(b == activo ? COLOR_BOTON_ACTIVO : COLOR_BOTON_NORMAL);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private componentes.BotonModerno BtnGraficoArea;
    private componentes.BotonModerno BtnGraficoBarras;
    private componentes.BotonModerno BtnGraficoBarrasH;
    private componentes.BotonModerno BtnGraficoHoy;
    private componentes.BotonModerno BtnGraficoLineas;
    private componentes.BotonModerno BtnGraficoMes;
    private componentes.BotonModerno BtnGraficoSemana;
    private componentes.BotonModerno BtnGraficoTrimestre;
    private javax.swing.JTable LstCont;
    private componentes.PanelRedondo PanelGrafico;
    private componentes.PanelRedondo PnlActivos;
    private componentes.PanelRedondo PnlCatvendida;
    private componentes.PanelRedondo PnlGanancias;
    private componentes.PanelRedondo PnlGastos;
    private componentes.PanelRedondo PnlLista;
    private componentes.PanelRedondo PnlVendidos;
    private componentes.PanelRedondo Pnlbalance;
    private componentes.GraficoKrypton graficoEstadisticas;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCantActivos;
    private javax.swing.JLabel lblCantBalance;
    private javax.swing.JLabel lblCantGanancias;
    private javax.swing.JLabel lblCantGastos;
    private javax.swing.JLabel lblCantVendido;
    private javax.swing.JLabel lblCatVendida;
    private javax.swing.JLabel lblEninventario;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblPeriodoGanancias;
    private javax.swing.JLabel lblPeriodoGastos;
    private javax.swing.JLabel lblTITLEestado;
    private javax.swing.JLabel lblTITLEganacias;
    private javax.swing.JLabel lblTITLEgastos;
    private javax.swing.JLabel lblTgastos;
    private javax.swing.JLabel lblactivos;
    private javax.swing.JLabel lblcatvendidas;
    private javax.swing.JLabel lbldescripcion;
    private javax.swing.JLabel lblganancias;
    private javax.swing.JLabel lblgancias;
    private javax.swing.JLabel lblgasto;
    private javax.swing.JLabel lblmostrargrafico;
    private javax.swing.JLabel lblnetos;
    private javax.swing.JLabel lbltitulo;
    private javax.swing.JLabel lbltotalunidades;
    private javax.swing.JLabel lblvendidos;
    private javax.swing.JPanel pnlcolorr;
    private javax.swing.JPanel pnlcolorv;
    // End of variables declaration//GEN-END:variables
}
