package Principal;

import Vista.Login;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.UIManager;

public class Main {

   public static void main(String[] args) {
        aplicarTemaVisual();
        Login acceso = new Login();
        acceso.setVisible(true);
    }

    private static void aplicarTemaVisual() {
        Color fondoMenu = new Color(51, 0, 51);
        Color fondoPopup = new Color(40, 15, 55);
        Color hover = new Color(92, 32, 72);
        Color seleccion = new Color(120, 45, 95);
        Color borde = new Color(90, 75, 100);
        Color texto = Color.WHITE;

        UIManager.put("MenuBar.background", fondoMenu);
        UIManager.put("MenuBar.foreground", texto);
        UIManager.put("Menu.background", fondoMenu);
        UIManager.put("Menu.foreground", texto);
        UIManager.put("Menu.selectionBackground", hover);
        UIManager.put("Menu.selectionForeground", texto);
        UIManager.put("Menu.border", BorderFactory.createEmptyBorder());
        UIManager.put("MenuItem.background", fondoPopup);
        UIManager.put("MenuItem.foreground", texto);
        UIManager.put("MenuItem.selectionBackground", hover);
        UIManager.put("MenuItem.selectionForeground", texto);
        UIManager.put("MenuItem.acceleratorForeground", texto);
        UIManager.put("MenuItem.acceleratorSelectionForeground", texto);
        UIManager.put("PopupMenu.background", fondoPopup);
        UIManager.put("PopupMenu.foreground", texto);
        UIManager.put("PopupMenu.border", BorderFactory.createLineBorder(borde));
        UIManager.put("Separator.background", fondoPopup);
        UIManager.put("Separator.foreground", borde);

        UIManager.put("ComboBox.background", fondoPopup);
        UIManager.put("ComboBox.foreground", texto);
        UIManager.put("ComboBox.selectionBackground", seleccion);
        UIManager.put("ComboBox.selectionForeground", texto);
        UIManager.put("ComboBox.buttonBackground", fondoPopup);

        UIManager.put("List.background", fondoPopup);
        UIManager.put("List.foreground", texto);
        UIManager.put("List.selectionBackground", seleccion);
        UIManager.put("List.selectionForeground", texto);

        UIManager.put("ScrollBar.thumb", borde);
        UIManager.put("ScrollBar.thumbShadow", borde);
        UIManager.put("ScrollBar.track", fondoPopup);

        UIManager.put("TextField.selectionBackground", seleccion);
        UIManager.put("TextField.selectionForeground", texto);
        UIManager.put("TextField.caretForeground", texto);
        UIManager.put("FormattedTextField.selectionBackground", seleccion);
        UIManager.put("PasswordField.selectionBackground", seleccion);

        UIManager.put("ToolTip.background", fondoPopup);
        UIManager.put("ToolTip.foreground", texto);
        UIManager.put("ToolTip.border", BorderFactory.createLineBorder(borde));
    }
}
