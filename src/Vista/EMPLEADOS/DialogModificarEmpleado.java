
package Vista.EMPLEADOS;

import javax.swing.JDialog;
import java.awt.Frame;

public class DialogModificarEmpleado extends JDialog {

    private final PanelDialogModificarEmpleado panel;
    private final String cedulaOriginal;

    public DialogModificarEmpleado(Modelo.Empleado empleado) {
        super((Frame) null, "Modificar Empleado", true);
        this.cedulaOriginal = empleado.getCedula();

        panel = new PanelDialogModificarEmpleado();
        panel.cargarDatos(empleado);
        panel.getBtnGuardar().addActionListener(e -> guardarCambios());
        panel.getBtnCancelar().addActionListener(e -> dispose());

        add(panel);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void guardarCambios() {
        boolean exito = Controladores.EmpleadoControlador.actualizarEmpleado(
                this,
                cedulaOriginal,
                panel.getNombres(),
                panel.getApellidos(),
                panel.getSueldo(),
                panel.getTelefono(),
                panel.getUsuario(),
                panel.getPassword()
        );

        if (exito) {
            dispose();
        }
    }
}
