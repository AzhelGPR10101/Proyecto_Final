
package Vista.EMPLEADOS;

import Controladores.ControladorSolicitud;
import Modelo.SolicitudAcceso;
import java.util.List;

public class PanelAceptarEmpledos extends javax.swing.JPanel {

    private final ControladorSolicitud controladorSolicitud = new ControladorSolicitud();
    private List<SolicitudAcceso> solicitudesActuales;

    public PanelAceptarEmpledos() {
        initComponents();
        cargarSolicitudes();
        componentes.EstiloTablaKrypton.aplicar(jtblSolicitudes);

    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            cargarSolicitudes();
        }
    }

    private void cargarSolicitudes() {
        solicitudesActuales = controladorSolicitud.listarPendientes();

        javax.swing.table.DefaultTableModel modelo = new javax.swing.table.DefaultTableModel(
                new String[]{"Nombre", "Cedula", "Fecha de solicitud", "Cargo", "Sueldo", "Acción"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4 || column == 5;
            }
        };

        for (SolicitudAcceso s : solicitudesActuales) {
            modelo.addRow(new Object[]{
                s.getNombres() + " " + s.getApellidos(),
                s.getCedula(),
                s.getFechaSolicitud(),
                s.getNombreRol(),
                "",
                "Aceptar"
            });
        }

        jtblSolicitudes.setModel(modelo);
        jtblSolicitudes.getColumnModel().getColumn(5).setCellRenderer(
                new componentes.EstiloTablaKrypton.RenderBotonRedondeado("Aceptar"));
        jtblSolicitudes.getColumnModel().getColumn(5).setCellEditor(
                new ButtonEditor(new javax.swing.JCheckBox(), jtblSolicitudes));
    }

    class ButtonEditor extends javax.swing.DefaultCellEditor {

        private javax.swing.JButton button;
        private String label;
        private boolean clicked;
        private javax.swing.JTable tabla;

        public ButtonEditor(javax.swing.JCheckBox checkBox, javax.swing.JTable tabla) {
            super(checkBox);
            this.tabla = tabla;
            button = new javax.swing.JButton() {
                @Override
                protected void paintComponent(java.awt.Graphics g) {
                    componentes.EstiloTablaKrypton.pintarFondoRedondeado(this, g);
                    super.paintComponent(g);
                }
            };
            componentes.EstiloTablaKrypton.configurarBoton(button);
            button.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        public java.awt.Component getTableCellEditorComponent(javax.swing.JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            clicked = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (clicked) {
                int filaSeleccionada = tabla.getSelectedRow();
                if (filaSeleccionada != -1 && filaSeleccionada < solicitudesActuales.size()) {
                    SolicitudAcceso solicitud = solicitudesActuales.get(filaSeleccionada);
                    Object sueldoCelda = tabla.getValueAt(filaSeleccionada, 4);
                    String sueldoStr = sueldoCelda == null ? "" : sueldoCelda.toString();

                    boolean aceptado = controladorSolicitud.aprobar(PanelAceptarEmpledos.this, solicitud.getIdSolicitud(), sueldoStr);
                    if (aceptado) {
                        javax.swing.SwingUtilities.invokeLater(() -> cargarSolicitudes());
                    }
                }
            }
            clicked = false;
            return label;
        }

        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTituloEmpl = new javax.swing.JLabel();
        panelRedondo1 = new componentes.PanelRedondo();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtblSolicitudes = new javax.swing.JTable();

        setBackground(new java.awt.Color(31, 10, 48));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTituloEmpl.setFont(new java.awt.Font("Lucida Bright", 1, 36)); // NOI18N
        lblTituloEmpl.setForeground(new java.awt.Color(255, 255, 255));
        lblTituloEmpl.setText("SOLICITUDES DE EMPLEADOS");
        add(lblTituloEmpl, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 40, 580, 40));

        panelRedondo1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jtblSolicitudes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Nombre", "Cedula", "Fecha de contratación", "Cargo", "Sueldo", "Acción"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jtblSolicitudes);

        panelRedondo1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 1640, 660));

        add(panelRedondo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 110, 1690, 740));
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jtblSolicitudes;
    private javax.swing.JLabel lblTituloEmpl;
    private componentes.PanelRedondo panelRedondo1;
    // End of variables declaration//GEN-END:variables
}
