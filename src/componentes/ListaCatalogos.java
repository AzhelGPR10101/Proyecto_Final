package componentes;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

public class ListaCatalogos<E> extends JList<E> {

    private Color fondo = new Color(31, 16, 48);
    private Color texto = Color.WHITE;
    private Color seleccion = new Color(55, 25, 75);

    public ListaCatalogos() {

        configurar();
    }

    private void configurar() {

        setBackground(fondo);

        setForeground(texto);

        setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        setFixedCellHeight(28);

        setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        setBorder(null);

        setCellRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(
                    JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus) {

                super.getListCellRendererComponent(
                        list,
                        value,
                        index,
                        isSelected,
                        cellHasFocus
                );

                setFont(
                        new Font(
                                "Segoe UI",
                                Font.PLAIN,
                                14
                        )
                );

                setForeground(Color.WHITE);

                setOpaque(true);

                if (isSelected) {

                    setBackground(seleccion);

                } else {

                    setBackground(fondo);
                }
                setBorder(
                        javax.swing.BorderFactory.createEmptyBorder(
                                2,
                                10,
                                2,
                                10
                        )
                );

                setText("• " + value);

                return this;
            }
        });

        setFocusable(false);
    }
}