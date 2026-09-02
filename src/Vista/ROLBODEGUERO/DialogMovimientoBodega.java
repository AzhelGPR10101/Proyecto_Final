
package Vista.ROLBODEGUERO;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import java.awt.Frame;

public class DialogMovimientoBodega extends JDialog {

    public enum Modo {
        ENTRADA("Registrar Entrada"),
        SALIDA("Confirmar Salida"),
        TRANSFERENCIA("Transferir Bodega");

        final String titulo;

        Modo(String titulo) {
            this.titulo = titulo;
        }
    }

    private final Modo modo;
    private final PanelDialogMovimientoBodega panel;
    private String resultado;

    public DialogMovimientoBodega(Frame parent, Modo modo) {
        super(parent, modo.titulo, true);
        this.modo = modo;

        panel = new PanelDialogMovimientoBodega();
        panel.configurarModo(modo.titulo);
        panel.getBtnConfirmar().addActionListener(e -> confirmar());
        panel.getBtnCancelar().addActionListener(e -> dispose());

        add(panel);
        pack();
        setLocationRelativeTo(parent);
        setResizable(false);
        componentes.escalado.KryptonPanelScrollable.envolverJDialog(this);
    }

    public String getResultado() {
        return resultado;
    }

    private void confirmar() {
        if (!panel.hayProductoSeleccionado()) {
            JOptionPane.showMessageDialog(this, "Busca y selecciona un producto antes de continuar.",
                    "Producto no seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String codigo = panel.getCodigoSeleccionado();
        String cantidad = panel.getCantidadTexto();

        switch (modo) {
            case ENTRADA:
                resultado = Controladores.ControladorInventarioBodega.registrarEntrada(this, codigo, cantidad);
                break;
            case SALIDA:
                resultado = Controladores.ControladorInventarioBodega.confirmarSalida(this, codigo, cantidad);
                break;

        }

        if (resultado != null) {
            dispose();
        }
    }
}
