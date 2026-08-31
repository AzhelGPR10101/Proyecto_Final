package Reportes;

import Modelo.DetalleFactura;
import Modelo.Factura;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GeneradorPDFFactura {

    public static void generar(Factura factura, String rutaSalida) throws IOException {
        List<String> lineas = construirLineas(factura);
        escribirPdf(lineas, rutaSalida);
    }

    private static List<String> construirLineas(Factura factura) {
        List<String> lineas = new ArrayList<>();
        lineas.add("FACTURA " + safe(factura.getNumFactura()));
        lineas.add("Fecha: " + safe(factura.getFecha()));
        String nombreCliente = factura.getCliente() != null
                ? (safe(factura.getCliente().getNombre()) + " " + safe(factura.getCliente().getApellido()))
                : "";
        lineas.add("Cliente: " + nombreCliente);
        lineas.add("Atendido por: " + safe(factura.getNombreEmpleado()));
        lineas.add("Metodo de pago: " + safe(factura.getMetodoPago()));
        lineas.add("");
        lineas.add(String.format("%-30s %6s %12s %12s", "PRODUCTO", "CANT", "P.UNIT", "SUBTOTAL"));
        lineas.add("--------------------------------------------------------------");
        if (factura.getDetalles() != null) {
            for (DetalleFactura d : factura.getDetalles()) {
                lineas.add(String.format("%-30s %6d %12.2f %12.2f",
                        recortar(safe(d.getNombreProducto()), 30), d.getCantidad(), d.getPrecioUnitario(), d.getSubtotal()));
            }
        }
        lineas.add("--------------------------------------------------------------");
        lineas.add(String.format("%-42s %12.2f", "SUBTOTAL", factura.getSubtotal()));
        lineas.add(String.format("%-42s %12.2f", "IVA", factura.getValorIva()));
        lineas.add(String.format("%-42s %12.2f", "DESCUENTO", factura.getDescuento()));
        lineas.add(String.format("%-42s %12.2f", "TOTAL", factura.getTotal()));
        lineas.add("");
        lineas.add("Estado SRI: " + safe(factura.getEstadoSri()));
        return lineas;
    }

    private static String safe(String s) { return s == null ? "" : s; }
    private static String recortar(String s, int max) { return s.length() > max ? s.substring(0, max) : s; }

    private static String escapar(String texto) {
        return texto.replace("\\", "\\\\").replace("(", "\\(").replace(")", "\\)");
    }

    private static void escribirPdf(List<String> lineas, String rutaSalida) throws IOException {
        StringBuilder contenido = new StringBuilder();
        contenido.append("BT\n/F1 10 Tf\n");
        int y = 780;
        for (String linea : lineas) {
            contenido.append("1 0 0 1 40 ").append(y).append(" Tm (").append(escapar(linea)).append(") Tj\n");
            y -= 14;
        }
        contenido.append("ET");

        byte[] streamBytes = contenido.toString().getBytes(StandardCharsets.ISO_8859_1);

        try (FileOutputStream fos = new FileOutputStream(rutaSalida)) {
            List<Integer> offsets = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            sb.append("%PDF-1.4\n");
            offsets.add(sb.length());
            sb.append("1 0 obj\n<< /Type /Catalog /Pages 2 0 R >>\nendobj\n");
            offsets.add(sb.length());
            sb.append("2 0 obj\n<< /Type /Pages /Kids [3 0 R] /Count 1 >>\nendobj\n");
            offsets.add(sb.length());
            sb.append("3 0 obj\n<< /Type /Page /Parent 2 0 R /MediaBox [0 0 612 792] /Resources << /Font << /F1 4 0 R >> >> /Contents 5 0 R >>\nendobj\n");
            offsets.add(sb.length());
            sb.append("4 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica /Encoding /WinAnsiEncoding >>\nendobj\n");
            offsets.add(sb.length());
            sb.append("5 0 obj\n<< /Length ").append(streamBytes.length).append(" >>\nstream\n");

            byte[] headerBytes = sb.toString().getBytes(StandardCharsets.ISO_8859_1);
            fos.write(headerBytes);
            fos.write(streamBytes);

            String tail1 = "\nendstream\nendobj\n";
            byte[] tail1Bytes = tail1.getBytes(StandardCharsets.ISO_8859_1);
            fos.write(tail1Bytes);

            int xrefOffset = headerBytes.length + streamBytes.length + tail1Bytes.length;

            StringBuilder xref = new StringBuilder();
            xref.append("xref\n0 6\n");
            xref.append("0000000000 65535 f \n");
            for (int off : offsets) {
                xref.append(String.format("%010d 00000 n \n", off));
            }
            xref.append("trailer\n<< /Size 6 /Root 1 0 R >>\nstartxref\n").append(xrefOffset).append("\n%%EOF");

            fos.write(xref.toString().getBytes(StandardCharsets.ISO_8859_1));
        }
    }
}