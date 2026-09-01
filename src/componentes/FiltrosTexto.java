package componentes;

import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class FiltrosTexto {

    public static void aplicarSoloNumeros(JTextField campo, int maxLongitud) {
        ((AbstractDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) return;
                String resultado = construirResultado(fb, offset, 0, string);
                if (esNumerico(string) && resultado.length() <= maxLongitud) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) text = "";
                String resultado = construirResultado(fb, offset, length, text);
                if (esNumerico(text) && resultado.length() <= maxLongitud) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            @Override
            public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                super.remove(fb, offset, length);
            }
        });
    }

    public static void aplicarSoloLetras(JTextField campo, int maxLongitud) {
        ((AbstractDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) return;
                String resultado = construirResultado(fb, offset, 0, string);
                if (esLetras(string) && resultado.length() <= maxLongitud) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) text = "";
                String resultado = construirResultado(fb, offset, length, text);
                if (esLetras(text) && resultado.length() <= maxLongitud) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            @Override
            public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                super.remove(fb, offset, length);
            }
        });
    }

    public static void aplicarLetrasYNumeros(JTextField campo, int maxLongitud) {
        ((AbstractDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) return;
                String resultado = construirResultado(fb, offset, 0, string);
                if (esLetrasYNumeros(string) && resultado.length() <= maxLongitud) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) text = "";
                String resultado = construirResultado(fb, offset, length, text);
                if (esLetrasYNumeros(text) && resultado.length() <= maxLongitud) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            @Override
            public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                super.remove(fb, offset, length);
            }
        });
    }

    public static void aplicarSoloDecimal(JTextField campo, int maxDigitosEnteros) {
        ((AbstractDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) return;
                String resultado = construirResultado(fb, offset, 0, string);
                if (esDecimalValido(string, resultado, maxDigitosEnteros)) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) text = "";
                String resultado = construirResultado(fb, offset, length, text);
                if (esDecimalValido(text, resultado, maxDigitosEnteros)) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            @Override
            public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                super.remove(fb, offset, length);
            }
        });
    }

    private static String construirResultado(DocumentFilter.FilterBypass fb, int offset, int length, String textoNuevo) throws BadLocationException {
        String actual = fb.getDocument().getText(0, fb.getDocument().getLength());
        StringBuilder sb = new StringBuilder(actual);
        sb.replace(offset, offset + length, textoNuevo);
        return sb.toString();
    }

    private static boolean esNumerico(String texto) {
        return texto.matches("\\d*");
    }

    private static boolean esLetras(String texto) {
        return texto.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*");
    }

    private static boolean esLetrasYNumeros(String texto) {
        return texto.matches("[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ ]*");
    }

    private static boolean esDecimalValido(String fragmentoNuevo, String resultadoCompleto, int maxDigitosEnteros) {
        if (!fragmentoNuevo.matches("[0-9.]*")) return false;
        if (resultadoCompleto.chars().filter(c -> c == '.').count() > 1) return false;
        String parteEntera = resultadoCompleto.split("\\.", -1)[0];
        if (parteEntera.length() > maxDigitosEnteros) return false;
        return true;
    }
}