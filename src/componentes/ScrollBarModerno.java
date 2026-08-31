package componentes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JButton;
import javax.swing.JScrollBar;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class ScrollBarModerno extends BasicScrollBarUI {

    private static final Color COLOR_PISTA = new Color(31, 10, 48);
    private static final Color COLOR_BARRA = new Color(90, 55, 120);
    private static final Color COLOR_BARRA_HOVER = new Color(115, 70, 150);

    @Override
    protected void configureScrollBarColors() {
        trackColor = COLOR_PISTA;
        thumbColor = COLOR_BARRA;
    }

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
        return boton;
    }

    @Override
    protected void paintTrack(Graphics g, javax.swing.JComponent c, java.awt.Rectangle trackBounds) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(COLOR_PISTA);
        g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        g2.dispose();
    }

    @Override
    protected void paintThumb(Graphics g, javax.swing.JComponent c, java.awt.Rectangle thumbBounds) {
        if (thumbBounds.isEmpty() || !c.isEnabled()) {
            return;
        }
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        boolean sobreElRaton = isThumbRollover();
        g2.setColor(sobreElRaton ? COLOR_BARRA_HOVER : COLOR_BARRA);
        int margen = 3;
        g2.fillRoundRect(thumbBounds.x + margen, thumbBounds.y + margen,
                thumbBounds.width - margen * 2, thumbBounds.height - margen * 2, 10, 10);
        g2.dispose();
    }

    public static void aplicar(javax.swing.JScrollPane scroll) {
        scroll.getVerticalScrollBar().setUI(new ScrollBarModerno());
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
        scroll.getHorizontalScrollBar().setUI(new ScrollBarModerno());
        scroll.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    }
}
