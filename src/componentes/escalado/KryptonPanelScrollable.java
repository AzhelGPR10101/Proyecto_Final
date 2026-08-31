package componentes.escalado;

import java.awt.Container;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

public final class KryptonPanelScrollable {

    private KryptonPanelScrollable() {
    }

    public static JScrollPane agregarConScroll(Container contenedorPadre, JComponent contenido,
            int x, int y, int ancho, int alto) {

        contenedorPadre.remove(contenido);

        JScrollPane scroll = new JScrollPane(contenido);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.getVerticalScrollBar().setUnitIncrement(24);
        scroll.getHorizontalScrollBar().setUnitIncrement(24);

        contenedorPadre.add(scroll, new org.netbeans.lib.awtextra.AbsoluteConstraints(x, y, ancho, alto));
        contenedorPadre.revalidate();
        contenedorPadre.repaint();

        return scroll;
    }

    public static void envolverJDialog(JDialog dialogo) {
        Container contenidoOriginal = dialogo.getContentPane();
        JScrollPane scroll = crearScroll(contenidoOriginal);
        dialogo.setContentPane(scroll);
    }

    public static void envolverJFrame(JFrame frame) {
        Container contenidoOriginal = frame.getContentPane();
        JScrollPane scroll = crearScroll(contenidoOriginal);
        frame.setContentPane(scroll);
    }

    private static JScrollPane crearScroll(Container contenido) {
        JScrollPane scroll = new JScrollPane(contenido);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.getVerticalScrollBar().setUnitIncrement(24);
        scroll.getHorizontalScrollBar().setUnitIncrement(24);
        return scroll;
    }
}
