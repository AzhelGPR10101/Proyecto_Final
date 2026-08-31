/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Vista.ROLBODEGUERO;

import Controladores.ControladorInventarioBodega;
import Modelo.Producto;

import javax.swing.*;
import java.awt.*;


public class DialogAjustarInventario extends JDialog {

    private JTextField txtCodigo;
    private JTextField txtUbicacion;
    private JTextField txtLote;
    private JTextField txtStockMinimo;
    private JTextField txtStockMaximo;
    private JTextField txtStockActual;
    private JButton btnBuscar;
    private JButton btnGuardar;
    private JButton btnCancelar;

    private boolean guardado = false;

    private static final Color COLOR_FONDO = new Color(31, 11, 43);
    private static final Color COLOR_BOTON = new Color(85, 0, 102);
    private static final Color COLOR_TEXTO = Color.WHITE;
    private static final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 15);
    private static final Font FONT_TITULO = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font FONT_BOTON = new Font("Segoe UI", Font.BOLD, 13);

    public DialogAjustarInventario(Frame parent) {
        super(parent, "Ajustar Inventario", true);
        initComponents();
        componentes.escalado.KryptonPanelScrollable.envolverJDialog(this);
    }

    public boolean isGuardado() {
        return guardado;
    }

    private void initComponents() {
        getContentPane().setBackground(COLOR_FONDO);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 15, 8, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        JLabel titulo = new JLabel("AJUSTAR INVENTARIO");
        titulo.setFont(FONT_TITULO);
        titulo.setForeground(COLOR_TEXTO);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        add(titulo, gbc);
        gbc.gridwidth = 1;
        row++;

        txtCodigo = new JTextField(14);
        btnBuscar = new JButton("Buscar");
        estilizarBoton(btnBuscar);
        btnBuscar.addActionListener(e -> buscarProducto());

        JPanel panelCodigo = new JPanel(new BorderLayout(8, 0));
        panelCodigo.setBackground(COLOR_FONDO);
        panelCodigo.add(txtCodigo, BorderLayout.CENTER);
        panelCodigo.add(btnBuscar, BorderLayout.EAST);
        row = agregarFila(gbc, row, "Código de producto*:", panelCodigo);

        txtUbicacion = new JTextField(16);
        txtLote = new JTextField(16);
        txtStockMinimo = new JTextField(16);
        txtStockMaximo = new JTextField(16);
        txtStockActual = new JTextField(16);

        row = agregarFila(gbc, row, "Ubicación (pasillo):", txtUbicacion);
        row = agregarFila(gbc, row, "Lote:", txtLote);
        row = agregarFila(gbc, row, "Stock mínimo*:", txtStockMinimo);
        row = agregarFila(gbc, row, "Stock máximo*:", txtStockMaximo);
        row = agregarFila(gbc, row, "Stock actual*:", txtStockActual);

        btnGuardar = new JButton("GUARDAR CAMBIOS");
        estilizarBoton(btnGuardar);
        btnGuardar.addActionListener(e -> guardar());

        btnCancelar = new JButton("CANCELAR");
        estilizarBoton(btnCancelar);
        btnCancelar.addActionListener(e -> dispose());

        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(COLOR_FONDO);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        add(panelBotones, gbc);

        setSize(460, 480);
        setLocationRelativeTo(getParent());
        setResizable(false);
    }

    private int agregarFila(GridBagConstraints gbc, int row, String etiqueta, JComponent campo) {
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(FONT_LABEL);
        lbl.setForeground(COLOR_TEXTO);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        add(lbl, gbc);

        gbc.gridx = 1;
        add(campo, gbc);

        return row + 1;
    }

    private void estilizarBoton(JButton boton) {
        boton.setBackground(COLOR_BOTON);
        boton.setForeground(COLOR_TEXTO);
        boton.setFont(FONT_BOTON);
        boton.setFocusPainted(false);
    }

    private void buscarProducto() {
        Producto p = ControladorInventarioBodega.buscarPorCodigo(this, txtCodigo.getText());
        if (p == null) {
            return;
        }
        txtUbicacion.setText(p.getUbicacionPasillo() == null ? "" : p.getUbicacionPasillo());
        txtLote.setText(p.getLote() == null ? "" : p.getLote());
        txtStockMinimo.setText(String.valueOf(p.getStockMinimo()));
        txtStockMaximo.setText(String.valueOf(p.getStockMaximo()));
        txtStockActual.setText(String.valueOf(p.getCantidad()));
    }

    private void guardar() {
        boolean exito = ControladorInventarioBodega.ajustarInventario(this,
                txtCodigo.getText(), txtUbicacion.getText(), txtLote.getText(),
                txtStockMinimo.getText(), txtStockMaximo.getText(), txtStockActual.getText());
        if (exito) {
            guardado = true;
            dispose();
        }
    }
}
