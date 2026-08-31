package componentes.escalado;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

public final class KryptonVentanaScrollable {

    private KryptonVentanaScrollable() {
    }

    public static void agregarConScroll(JFrame frame, JComponent contenido) {
        JScrollPane scroll = new JScrollPane(contenido);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.getVerticalScrollBar().setUnitIncrement(24);
        frame.add(scroll);
    }
}
