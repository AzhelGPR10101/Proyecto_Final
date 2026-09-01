
package Vista.CLIENTES;

import Controladores.ControladorCliente;
import Modelo.Cliente;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class PanelClientesAgregar extends javax.swing.JPanel {

    private ControladorCliente controlador = new ControladorCliente();

    public PanelClientesAgregar() {
        initComponents();
        componentes.EstiloTablaKrypton.aplicar(tablaClientes);
        cargarTablaClientes();
        btnAgregarCliente.addActionListener(e -> guardarCliente());
        cbTipoDocumento.addActionListener(e -> aplicarFiltroDocumento());
        aplicarFiltroDocumento();
        ((componentes.TextFieldModerno) txtNombre).setPlaceholder("Nombres");
        ((componentes.TextFieldModerno) txtApellido).setPlaceholder("Apellidos");
        ((componentes.TextFieldModerno) txtCedula1).setPlaceholder("Documento");
        ((componentes.TextFieldModerno) txtTelefono).setPlaceholder("Teléfono");
        componentes.FiltrosTexto.aplicarSoloNumeros(txtTelefono, 10);
        ((componentes.TextFieldModerno) txtCorreo).setPlaceholder("Correo electrónico");
        ((componentes.TextFieldModerno) txtDireccion).setPlaceholder("Dirección");
    }

    private void aplicarFiltroDocumento() {
        Object seleccion = cbTipoDocumento.getSelectedItem();
        if ("PASAPORTE".equals(seleccion)) {
            componentes.FiltrosTexto.aplicarLetrasYNumeros(txtCedula1, 20);
        } else {
            componentes.FiltrosTexto.aplicarSoloNumeros(txtCedula1, 10);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        panelRedondo1 = new componentes.PanelRedondo();
        jLabel2 = new javax.swing.JLabel();
        cbTipoDocumento = new componentes.ComboBoxModerno();
        jLabel = new javax.swing.JLabel();
        txtCedula1 = new componentes.TextFieldModerno();
        jLabel6 = new javax.swing.JLabel();
        txtCorreo = new componentes.TextFieldModerno();
        jLabel4 = new javax.swing.JLabel();
        txtDireccion = new componentes.TextFieldModerno();
        txtTelefono = new componentes.TextFieldModerno();
        jLabel7 = new javax.swing.JLabel();
        txtApellido = new componentes.TextFieldModerno();
        jLabel5 = new javax.swing.JLabel();
        txtNombre = new componentes.TextFieldModerno();
        jLabel3 = new javax.swing.JLabel();
        btnAgregarCliente = new componentes.BotonModerno();
        jLabel8 = new javax.swing.JLabel();
        panelRedondo2 = new componentes.PanelRedondo();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaClientes = new javax.swing.JTable();

        setBackground(new java.awt.Color(31, 10, 48));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Lucida Bright", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("REGISTRAR CLIENTES");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 50, 600, -1));

        panelRedondo1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Lucida Bright", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("-TIPO DE DOCUMENTO*");
        panelRedondo1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 30, 380, -1));

        cbTipoDocumento.setBackground(new java.awt.Color(31, 10, 48));
        cbTipoDocumento.setFont(new java.awt.Font("Tw Cen MT Condensed", 0, 24)); // NOI18N
        cbTipoDocumento.setForeground(new java.awt.Color(255, 255, 255));
        cbTipoDocumento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CÉDULA", "PASAPORTE" }));
        panelRedondo1.add(cbTipoDocumento, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 80, 490, 50));

        jLabel.setFont(new java.awt.Font("Lucida Bright", 1, 24)); // NOI18N
        jLabel.setForeground(new java.awt.Color(255, 255, 255));
        jLabel.setText("- CEDULA*");
        panelRedondo1.add(jLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 150, 280, -1));

        txtCedula1.setBackground(new java.awt.Color(31, 10, 60));
        txtCedula1.setFont(new java.awt.Font("Tw Cen MT Condensed", 0, 24)); // NOI18N
        txtCedula1.setForeground(new java.awt.Color(255, 255, 255));
        panelRedondo1.add(txtCedula1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 200, 490, 50));

        jLabel6.setFont(new java.awt.Font("Lucida Bright", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("- CORREO*");
        panelRedondo1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 270, 290, -1));

        txtCorreo.setBackground(new java.awt.Color(31, 10, 60));
        txtCorreo.setFont(new java.awt.Font("Tw Cen MT Condensed", 0, 24)); // NOI18N
        txtCorreo.setForeground(new java.awt.Color(255, 255, 255));
        panelRedondo1.add(txtCorreo, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 320, 490, 50));

        jLabel4.setFont(new java.awt.Font("Lucida Bright", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("-DIRECCION *");
        panelRedondo1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 390, 500, -1));

        txtDireccion.setBackground(new java.awt.Color(31, 10, 60));
        txtDireccion.setFont(new java.awt.Font("Tw Cen MT Condensed", 0, 24)); // NOI18N
        txtDireccion.setForeground(new java.awt.Color(255, 255, 255));
        panelRedondo1.add(txtDireccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 440, 490, 50));

        txtTelefono.setBackground(new java.awt.Color(31, 10, 60));
        txtTelefono.setFont(new java.awt.Font("Tw Cen MT Condensed", 0, 24)); // NOI18N
        txtTelefono.setForeground(new java.awt.Color(255, 255, 255));
        panelRedondo1.add(txtTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 320, 490, 50));

        jLabel7.setFont(new java.awt.Font("Lucida Bright", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("- TELEFONO*");
        panelRedondo1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 270, 330, -1));

        txtApellido.setBackground(new java.awt.Color(31, 10, 60));
        txtApellido.setFont(new java.awt.Font("Tw Cen MT Condensed", 0, 24)); // NOI18N
        txtApellido.setForeground(new java.awt.Color(255, 255, 255));
        panelRedondo1.add(txtApellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 200, 490, 50));

        jLabel5.setFont(new java.awt.Font("Lucida Bright", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("-APELLIDO DEL CLIENTE *");
        panelRedondo1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 150, 420, -1));

        txtNombre.setBackground(new java.awt.Color(31, 10, 60));
        txtNombre.setFont(new java.awt.Font("Tw Cen MT Condensed", 0, 24)); // NOI18N
        txtNombre.setForeground(new java.awt.Color(255, 255, 255));
        panelRedondo1.add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 80, 490, 50));

        jLabel3.setFont(new java.awt.Font("Lucida Bright", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("- NOMBRE DEL CLIENTE *");
        panelRedondo1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 40, 390, -1));

        btnAgregarCliente.setBackground(new java.awt.Color(165, 24, 139));
        btnAgregarCliente.setFont(new java.awt.Font("Tw Cen MT Condensed", 0, 36)); // NOI18N
        btnAgregarCliente.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregarCliente.setText("AGREGAR CLIENTE");
        panelRedondo1.add(btnAgregarCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 450, -1, -1));

        add(panelRedondo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 120, 1490, 580));

        jLabel8.setFont(new java.awt.Font("Lucida Bright", 1, 36)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("LISTA DE CLIENTES");
        add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 730, -1, -1));

        panelRedondo2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tablaClientes.setBackground(new java.awt.Color(51, 0, 102));
        tablaClientes.setForeground(new java.awt.Color(255, 255, 255));
        tablaClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CEDULA", "NOMBRE", "APELLIDO", "TELEFONO", "CORREO", "DIRECCION"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tablaClientes);

        panelRedondo2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 1420, 240));

        add(panelRedondo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 790, 1490, 300));
    }// </editor-fold>//GEN-END:initComponents

    private void txtNombreKeyTyped(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();
        if (Character.isDigit(c)) {
            evt.consume();
        }
    }

    private void txtApellidoKeyTyped(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();
        if (Character.isDigit(c)) {
            evt.consume();
        }
    }

    private void limpiarFormulario() {
        cbTipoDocumento.setSelectedIndex(0);
        jLabel.setText("- CEDULA*");
        txtCedula1.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        txtDireccion.setText("");
        cbTipoDocumento.requestFocus();
    }

    private void guardarCliente() {
        String tipoDocumento = cbTipoDocumento.getSelectedItem() != null ? cbTipoDocumento.getSelectedItem().toString() : "";
        String cedula = txtCedula1.getText().trim();
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();
        String direccion = txtDireccion.getText().trim();

        String resultado = controlador.registrarCliente(tipoDocumento, cedula, nombre, apellido, telefono, correo, direccion);

        if ("OK".equals(resultado)) {
            JOptionPane.showMessageDialog(this, "Cliente registrado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            cargarTablaClientes();
        } else {
            JOptionPane.showMessageDialog(this, resultado, "No se pudo registrar", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void cargarTablaClientes() {
        String[] columnas = {"CEDULA", "NOMBRE", "APELLIDO", "TELÉFONO", "CORREO", "DIRECCIÓN"};

        DefaultTableModel modelo = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        List<Cliente> lista = controlador.listarTodos();

        for (Cliente c : lista) {
            Object[] fila = new Object[6];
            fila[0] = c.getCedula();
            fila[1] = c.getNombre();
            fila[2] = c.getApellido();
            fila[3] = c.getTelefono();
            fila[4] = c.getCorreo();
            fila[5] = c.getDireccion();

            modelo.addRow(fila);
        }

        tablaClientes.setModel(modelo);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarCliente;
    private javax.swing.JComboBox<String> cbTipoDocumento;
    private javax.swing.JLabel jLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private componentes.PanelRedondo panelRedondo1;
    private componentes.PanelRedondo panelRedondo2;
    private javax.swing.JTable tablaClientes;
    private javax.swing.JTextField txtApellido;
    private javax.swing.JTextField txtCedula1;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
