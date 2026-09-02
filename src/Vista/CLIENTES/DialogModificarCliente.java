
package Vista.CLIENTES;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import java.awt.Frame;

public class DialogModificarCliente extends JDialog {

    private final PanelDialogModificarCliente panel;
    private final String cedulaOriginal;

    public DialogModificarCliente(Modelo.Cliente cliente) {
        super((Frame) null, "Modificar Cliente", true);
        this.cedulaOriginal = cliente.getCedula();

        panel = new PanelDialogModificarCliente();
        panel.cargarDatos(cliente);
        panel.getBtnGuardar().addActionListener(e -> guardarCambios());
        panel.getBtnCancelar().addActionListener(e -> dispose());

        add(panel);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void guardarCambios() {
        String nombre = Controladores.Validaciones.aMayusculas(panel.getNombre());
        String apellido = Controladores.Validaciones.aMayusculas(panel.getApellido());
        String telefono = panel.getTelefono().trim();
        String correo = panel.getCorreo().trim().toLowerCase();
        String direccion = Controladores.Validaciones.aMayusculas(panel.getDireccion());

        if (nombre.isEmpty() || apellido.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Los campos Nombre y Apellido son obligatorios.",
                "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (direccion.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "El campo Dirección es obligatorio.",
                "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!Controladores.Validaciones.validarTelefono(telefono)) {
            JOptionPane.showMessageDialog(this,
                "El número de teléfono debe tener exactamente 10 dígitos numéricos.",
                "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!Controladores.Validaciones.validarCorreo(correo)) {
            JOptionPane.showMessageDialog(this,
                "Por favor, ingrese un correo electrónico válido (ej: usuario@ejemplo.com).",
                "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Modelo.Cliente cliente = new Modelo.Cliente(
                cedulaOriginal,
                nombre,
                apellido,
                telefono,
                correo,
                direccion
        );

        boolean exito = new Controladores.ControladorCliente().modificar(cliente);

        if (exito) {
            JOptionPane.showMessageDialog(this, "Cliente modificado con éxito.");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "No se pudo modificar el cliente.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
