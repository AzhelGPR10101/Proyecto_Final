package componentes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicTextFieldUI;

public class TextFieldModerno extends JTextField {

    private Color fondo = new Color(55, 25, 75);
    private Color borde = new Color(90, 75, 100);
    private String placeholder = null;
    private Color colorPlaceholder = new Color(220, 210, 230, 140);

    public TextFieldModerno() {

        setUI(new BasicTextFieldUI());

        setOpaque(false);

        setBackground(new Color(0, 0, 0, 0));

        setForeground(Color.WHITE);

        setCaretColor(Color.WHITE);

        setFont(
                new Font("Segoe UI", Font.PLAIN, 14)
        );

        setBorder(null);

        setMargin(
                new Insets(5, 15, 5, 15)
        );

        setHorizontalAlignment(JTextField.CENTER);

    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setColor(fondo);

        int radio = Math.min(18, Math.min(getWidth(), getHeight()) / 2 - 1);

        g2.fillRoundRect(
                0,
                0,
                getWidth(),
                getHeight(),
                radio,
                radio
        );

        g2.setColor(borde);

        g2.draw(new java.awt.geom.RoundRectangle2D.Float(
                0.5f,
                0.5f,
                getWidth() - 1,
                getHeight() - 1,
                radio,
                radio
        ));

        g2.dispose();

        super.paintComponent(g);

        if (placeholder != null && !placeholder.isEmpty() && getText().isEmpty()) {
            Graphics2D g3 = (Graphics2D) g.create();
            g3.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g3.setColor(colorPlaceholder);
            g3.setFont(getFont());
            java.awt.FontMetrics fm = g3.getFontMetrics();
            int ancho = fm.stringWidth(placeholder);
            int x = (getWidth() - ancho) / 2;
            int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
            g3.drawString(placeholder, Math.max(x, getInsets().left), y);
            g3.dispose();
        }
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        repaint();
    }

    public String getPlaceholder() {
        return placeholder;
    }

    @Override
    public void paintBorder(Graphics g) {

    }

}