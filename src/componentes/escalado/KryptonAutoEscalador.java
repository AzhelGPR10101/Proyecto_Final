package componentes.escalado;

import java.awt.Component;
import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.IdentityHashMap;
import java.util.Map;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public final class KryptonAutoEscalador {

    private KryptonAutoEscalador() {
    }

    public static void activar(Container vistaRaiz, int anchoDiseno, int altoDiseno) {

        forzarLayoutRecursivo(vistaRaiz);

        Map<Component, Rectangle> boundsOriginales = new IdentityHashMap<>();
        Map<Component, Float> fuentesOriginales = new IdentityHashMap<>();
        congelar(vistaRaiz, boundsOriginales, fuentesOriginales);

        vistaRaiz.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                escalar(vistaRaiz, boundsOriginales, fuentesOriginales, anchoDiseno, altoDiseno);
            }
        });

        SwingUtilities.invokeLater(() ->
                escalar(vistaRaiz, boundsOriginales, fuentesOriginales, anchoDiseno, altoDiseno));
    }

    private static boolean esComponenteAtomico(Component c) {

        return c instanceof JMenuBar || c instanceof JScrollPane;
    }

    private static void forzarLayoutRecursivo(Container c) {
        c.doLayout();
        for (Component hijo : c.getComponents()) {
            if (hijo instanceof Container hijoContenedor && !esComponenteAtomico(hijo)) {
                forzarLayoutRecursivo(hijoContenedor);
            }
        }
    }

    private static void congelar(Container c, Map<Component, Rectangle> bounds, Map<Component, Float> fuentes) {
        for (Component hijo : c.getComponents()) {
            bounds.put(hijo, hijo.getBounds());
            if (hijo.getFont() != null) {
                fuentes.put(hijo, hijo.getFont().getSize2D());
            }

            if (hijo instanceof Container hijoContenedor && !esComponenteAtomico(hijo)
                    && hijoContenedor.getComponentCount() > 0) {

                boolean esCardLayout = hijoContenedor.getLayout() instanceof java.awt.CardLayout;
                if (!esCardLayout) {
                    hijoContenedor.setLayout(null);
                }
                congelar(hijoContenedor, bounds, fuentes);
            }
        }
    }

    private static void escalar(Container vistaRaiz, Map<Component, Rectangle> bounds,
                                 Map<Component, Float> fuentes, int anchoDiseno, int altoDiseno) {
        int anchoActual = vistaRaiz.getWidth();
        int altoActual = vistaRaiz.getHeight();
        if (anchoActual <= 0 || altoActual <= 0) {
            return;
        }

        double escalaX = anchoActual / (double) anchoDiseno;
        double escalaY = altoActual / (double) altoDiseno;
        double escalaFuente = Math.min(escalaX, escalaY);

        aplicar(vistaRaiz, bounds, fuentes, escalaX, escalaY, escalaFuente);
        vistaRaiz.revalidate();
        vistaRaiz.repaint();
    }

    private static void aplicar(Container c, Map<Component, Rectangle> bounds, Map<Component, Float> fuentes,
                                 double escalaX, double escalaY, double escalaFuente) {
        for (Component hijo : c.getComponents()) {
            Rectangle original = bounds.get(hijo);
            if (original == null) {
                continue;
            }

            int x = (int) Math.round(original.x * escalaX);
            int y = (int) Math.round(original.y * escalaY);
            int w = (int) Math.round(original.width * escalaX);
            int h = (int) Math.round(original.height * escalaY);
            hijo.setBounds(x, y, w, h);

            Float tamOriginal = fuentes.get(hijo);
            if (tamOriginal != null && hijo.getFont() != null) {
                float nuevoTam = (float) Math.max(6, tamOriginal * escalaFuente);
                hijo.setFont(hijo.getFont().deriveFont(nuevoTam));
            }

            if (hijo instanceof Container hijoContenedor && !esComponenteAtomico(hijo)
                    && hijoContenedor.getComponentCount() > 0) {
                aplicar(hijoContenedor, bounds, fuentes, escalaX, escalaY, escalaFuente);
            }
        }
    }
}
