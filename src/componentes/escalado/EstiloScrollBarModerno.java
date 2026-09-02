package componentes.escalado;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class EstiloScrollBarModerno extends BasicScrollBarUI {

    private static final Color COLOR_HILO = new Color(120, 100, 130, 150);
    private static final Color COLOR_HILO_HOVER = new Color(150, 120, 160, 220);

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return botonInvisible();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return botonInvisible();
    }

    private JButton botonInvisible() {
        JButton boton = new JButton();
        boton.setPreferredSize(new Dimension(0, 0));
        boton.setMinimumSize(new Dimension(0, 0));
        boton.setMaximumSize(new Dimension(0, 0));
        boton.setFocusable(false);
        return boton;
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        if (thumbBounds.isEmpty() || !c.isEnabled()) {
            return;
        }
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(isThumbRollover() ? COLOR_HILO_HOVER : COLOR_HILO);
        int margen = 3;
        int arco = 8;
        g2.fillRoundRect(
                thumbBounds.x + margen, thumbBounds.y + margen,
                Math.max(0, thumbBounds.width - margen * 2),
                Math.max(0, thumbBounds.height - margen * 2),
                arco, arco);
        g2.dispose();
    }

    @Override
    protected Dimension getMinimumThumbSize() {
        return new Dimension(30, 30);
    }
}
