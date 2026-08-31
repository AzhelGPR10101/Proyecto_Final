
package Vista.CLIENTES;

import Controladores.ControladorCliente;
import Modelo.Cliente;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import java.awt.Color;
import java.awt.Component;
import java.util.List;

public class PanelClientesBuscar extends javax.swing.JPanel {

    private List<Cliente> listaClientes;

    public PanelClientesBuscar() {
        initComponents();
        configurarEstiloTabla();
        cargarClientes();
        FiltadoClientes.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltro(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltro(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltro(); }
        });
        jcbOrden.addActionListener(e -> aplicarFiltro());
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            cargarClientes();
        }
    }

    private void aplicarFiltro() {
        String texto = FiltadoClientes.getText();
        String orden = (String) jcbOrden.getSelectedItem();
        listaClientes = new ControladorCliente().filtrarClientes(texto, orden);
        actualizarTabla();
    }

    private void configurarEstiloTabla() {
        if (jTable1 == null) {
            return;
        }

        componentes.EstiloTablaKrypton.aplicar(jTable1);

        jTable1.getColumnModel().getColumn(5).setCellRenderer(new componentes.EstiloTablaKrypton.RowRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setForeground(new Color(50, 205, 50));
                setHorizontalAlignment(SwingConstants.LEFT);
                return c;
            }
        });
    }

    private void cargarClientes() {
        listaClientes = new ControladorCliente().listarTodos();
        actualizarTabla();
    }

    public void actualizarTabla() {
        if (jTable1 == null) {
            return;
        }

        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        modelo.setRowCount(0);

        if (listaClientes == null) {
            return;
        }

        for (int i = 0; i < listaClientes.size(); i++) {
            Cliente cli = listaClientes.get(i);

            modelo.addRow(new Object[]{
                (i + 1),
                cli.getNombre(),
                cli.getApellido(),
                cli.getCedula(),
                cli.getTelefono(),
                cli.getCorreo(),
                cli.getDireccion()
            });
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitulo = new javax.swing.JLabel();
        panelRedondo1 = new componentes.PanelRedondo();
        jLabel1 = new javax.swing.JLabel();
        FiltadoClientes = new componentes.TextFieldModerno();
        jcbOrden = new componentes.ComboBoxModerno();
        panelRedondo2 = new componentes.PanelRedondo();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setBackground(new java.awt.Color(31, 10, 48));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitulo.setFont(new java.awt.Font("Lucida Bright", 1, 36)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo.setText("LISTA DE CLIENTE/S");
        add(lblTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 50, 550, -1));

        panelRedondo1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("FIltrar:");
        panelRedondo1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 140, -1));

        FiltadoClientes.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        panelRedondo1.add(FiltadoClientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, 300, 40));

        jcbOrden.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jcbOrden.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Nombre A-Z", "Apellido A-Z" }));
        panelRedondo1.add(jcbOrden, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 30, 350, 30));

        add(panelRedondo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 120, 860, 80));

        panelRedondo2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable1.setBackground(new java.awt.Color(28, 9, 40));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "NO.", "Nombre", "Apellido", "Cedula", "Telefono", "Correo", "Direccion"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        panelRedondo2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 1540, 560));

        add(panelRedondo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 230, 1620, 670));
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField FiltadoClientes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JComboBox jcbOrden;
    private javax.swing.JLabel lblTitulo;
    private componentes.PanelRedondo panelRedondo1;
    private componentes.PanelRedondo panelRedondo2;
    // End of variables declaration//GEN-END:variables
}
