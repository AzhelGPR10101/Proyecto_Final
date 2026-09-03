package Reportes;

import DAO.NegocioDAO;
import Modelo.Cliente;
import Modelo.DetalleFactura;
import Modelo.Factura;
import Modelo.Negocio;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GeneradorPDFFactura {

    private static final int MARGEN_IZQ = 40;
    private static final int MARGEN_DER = 572;
    private static final int ANCHO_TABLA = MARGEN_DER - MARGEN_IZQ;

    public static void generar(Factura factura, String rutaSalida) throws IOException {
        Negocio negocio = factura.getIdNegocio() != null
                ? new NegocioDAO().buscarPorIdNegocio(factura.getIdNegocio())
                : null;
        String contenido = construirContenido(factura, negocio);
        escribirPdf(contenido, rutaSalida);
    }

    private static String construirContenido(Factura factura, Negocio negocio) {
        StringBuilder cs = new StringBuilder();

        texto(cs, "F2", 14, MARGEN_IZQ, 750, negocio != null ? safe(negocio.getNombreNegocio()) : "MI NEGOCIO");
        texto(cs, "F1", 9, MARGEN_IZQ, 734, "RUC: " + (negocio != null ? safe(negocio.getRucNegocio()) : ""));
        texto(cs, "F1", 9, MARGEN_IZQ, 720, "Correo: " + (negocio != null ? safe(negocio.getCorreoContacto()) : ""));

        texto(cs, "F2", 11, 350, 750, "FACTURA N°: " + safe(factura.getNumFactura()));
        texto(cs, "F1", 9, 350, 734, "Fecha: " + safe(factura.getFecha()));
        texto(cs, "F1", 9, 350, 720, "Atendido por: " + safe(factura.getNombreEmpleado()));
        texto(cs, "F1", 9, 350, 706, "Metodo de pago: " + safe(factura.getMetodoPago()));

        linea(cs, MARGEN_IZQ, 695, MARGEN_DER, 695);

        texto(cs, "F2", 11, MARGEN_IZQ, 675, "DATOS DEL CLIENTE");

        Cliente cliente = factura.getCliente();
        String nombreCliente = cliente != null ? (safe(cliente.getNombre()) + " " + safe(cliente.getApellido())).trim() : "";
        String cedula = cliente != null ? safe(cliente.getCedula()) : "";
        String telefono = cliente != null ? safe(cliente.getTelefono()) : "";
        String direccion = cliente != null ? safe(cliente.getDireccion()) : "";

        texto(cs, "F2", 9, MARGEN_IZQ, 658, "Cedula:");
        texto(cs, "F1", 9, MARGEN_IZQ, 645, cedula);
        texto(cs, "F2", 9, 200, 658, "Nombre:");
        texto(cs, "F1", 9, 200, 645, nombreCliente);
        texto(cs, "F2", 9, 380, 658, "Telefono:");
        texto(cs, "F1", 9, 380, 645, telefono);

        texto(cs, "F2", 9, MARGEN_IZQ, 628, "Direccion:");
        texto(cs, "F1", 9, MARGEN_IZQ, 615, direccion);

        int yTablaTop = 595;
        int alturaEncabezado = 20;

        int colCant = MARGEN_IZQ + 5;
        int colDesc = 100;
        int colPU = 350;
        int colPT = 465;

        rectRelleno(cs, MARGEN_IZQ, yTablaTop - alturaEncabezado, ANCHO_TABLA, alturaEncabezado, 0.85);
        int yEncTexto = yTablaTop - 14;
        texto(cs, "F2", 9, colCant, yEncTexto, "Cantidad");
        texto(cs, "F2", 9, colDesc, yEncTexto, "Descripcion");
        texto(cs, "F2", 9, colPU, yEncTexto, "P. Unitario");
        texto(cs, "F2", 9, colPT, yEncTexto, "P. Total");

        int alturaFila = 16;
        int yFilaTop = yTablaTop - alturaEncabezado;
        List<DetalleFactura> detalles = factura.getDetalles() != null ? factura.getDetalles() : new ArrayList<>();
        for (DetalleFactura d : detalles) {
            int yTexto = yFilaTop - 11;
            texto(cs, "F1", 9, colCant, yTexto, String.valueOf(d.getCantidad()));
            texto(cs, "F1", 9, colDesc, yTexto, recortar(safe(d.getNombreProducto()), 38));
            texto(cs, "F1", 9, colPU, yTexto, String.format("$%.2f", d.getPrecioUnitario()));
            texto(cs, "F1", 9, colPT, yTexto, String.format("$%.2f", d.getSubtotal()));
            yFilaTop -= alturaFila;
        }

        int yTablaBottom = yFilaTop;
        rectBorde(cs, MARGEN_IZQ, yTablaBottom, ANCHO_TABLA, yTablaTop - yTablaBottom);
        linea(cs, MARGEN_IZQ, yTablaTop - alturaEncabezado, MARGEN_DER, yTablaTop - alturaEncabezado);
        linea(cs, colDesc, yTablaBottom, colDesc, yTablaTop);
        linea(cs, colPU, yTablaBottom, colPU, yTablaTop);
        linea(cs, colPT, yTablaBottom, colPT, yTablaTop);

        int yTot = yTablaBottom - 20;
        texto(cs, "F1", 9, 400, yTot, "Subtotal:");
        texto(cs, "F1", 9, 480, yTot, String.format("$%.2f", factura.getSubtotal()));
        yTot -= 14;
        texto(cs, "F1", 9, 400, yTot, "IVA:");
        texto(cs, "F1", 9, 480, yTot, String.format("$%.2f", factura.getValorIva()));
        yTot -= 14;
        texto(cs, "F1", 9, 400, yTot, "Descuento:");
        texto(cs, "F1", 9, 480, yTot, String.format("$%.2f", factura.getDescuento()));
        yTot -= 18;
        texto(cs, "F2", 12, 380, yTot, "TOTAL A PAGAR:");
        texto(cs, "F2", 12, 480, yTot, String.format("$%.2f", factura.getTotal()));

        yTot -= 20;
        texto(cs, "F1", 8, MARGEN_IZQ, yTot, "Estado SRI: " + safe(factura.getEstadoSri()));

        int yFirma = yTot - 60;
        texto(cs, "F2", 10, 240, yFirma, "Cancelacion y firma");
        linea(cs, 220, yFirma - 35, 400, yFirma - 35);

        int yFooter = yFirma - 70;
        texto(cs, "F2", 10, 220, yFooter, "Gracias por su compra!");

        return cs.toString();
    }

    private static void texto(StringBuilder cs, String fuente, int size, int x, int y, String valor) {
        cs.append("BT\n/").append(fuente).append(" ").append(size).append(" Tf\n")
                .append("1 0 0 1 ").append(x).append(" ").append(y).append(" Tm\n")
                .append("(").append(escapar(valor)).append(") Tj\nET\n");
    }

    private static void rectRelleno(StringBuilder cs, int x, int y, int w, int h, double gris) {
        cs.append(gris).append(" ").append(gris).append(" ").append(gris).append(" rg\n")
                .append(x).append(" ").append(y).append(" ").append(w).append(" ").append(h).append(" re f\n")
                .append("0 0 0 rg\n");
    }

    private static void rectBorde(StringBuilder cs, int x, int y, int w, int h) {
        cs.append("0.5 0.5 0.5 RG\n0.7 w\n")
                .append(x).append(" ").append(y).append(" ").append(w).append(" ").append(h).append(" re S\n")
                .append("0 0 0 RG\n");
    }

    private static void linea(StringBuilder cs, int x1, int y1, int x2, int y2) {
        cs.append("0.5 0.5 0.5 RG\n0.7 w\n")
                .append(x1).append(" ").append(y1).append(" m ").append(x2).append(" ").append(y2).append(" l S\n")
                .append("0 0 0 RG\n");
    }

    private static String safe(String s) {
        if (s == null) {
            return "";
        }
        StringBuilder out = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '‘': case '’': c = '\''; break;
                case '“': case '”': c = '"'; break;
                case '–': case '—': c = '-'; break;
                case '…': out.append("..."); continue;
                default: break;
            }
            out.append(c <= 0xFF ? c : '?');
        }
        return out.toString();
    }

    private static String recortar(String s, int max) {
        return s.length() > max ? s.substring(0, max) : s;
    }

    private static String escapar(String texto) {
        return texto.replace("\\", "\\\\").replace("(", "\\(").replace(")", "\\)");
    }

    private static void escribirPdf(String contenido, String rutaSalida) throws IOException {
        byte[] streamBytes = contenido.getBytes(StandardCharsets.ISO_8859_1);

        try (FileOutputStream fos = new FileOutputStream(rutaSalida)) {
            List<Integer> offsets = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            sb.append("%PDF-1.4\n");
            offsets.add(sb.length());
            sb.append("1 0 obj\n<< /Type /Catalog /Pages 2 0 R >>\nendobj\n");
            offsets.add(sb.length());
            sb.append("2 0 obj\n<< /Type /Pages /Kids [3 0 R] /Count 1 >>\nendobj\n");
            offsets.add(sb.length());
            sb.append("3 0 obj\n<< /Type /Page /Parent 2 0 R /MediaBox [0 0 612 792] "
                    + "/Resources << /Font << /F1 4 0 R /F2 5 0 R >> >> /Contents 6 0 R >>\nendobj\n");
            offsets.add(sb.length());
            sb.append("4 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica /Encoding /WinAnsiEncoding >>\nendobj\n");
            offsets.add(sb.length());
            sb.append("5 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica-Bold /Encoding /WinAnsiEncoding >>\nendobj\n");
            offsets.add(sb.length());
            sb.append("6 0 obj\n<< /Length ").append(streamBytes.length).append(" >>\nstream\n");

            byte[] headerBytes = sb.toString().getBytes(StandardCharsets.ISO_8859_1);
            fos.write(headerBytes);
            fos.write(streamBytes);

            String tail1 = "\nendstream\nendobj\n";
            byte[] tail1Bytes = tail1.getBytes(StandardCharsets.ISO_8859_1);
            fos.write(tail1Bytes);

            int xrefOffset = headerBytes.length + streamBytes.length + tail1Bytes.length;

            StringBuilder xref = new StringBuilder();
            xref.append("xref\n0 7\n");
            xref.append("0000000000 65535 f \n");
            for (int off : offsets) {
                xref.append(String.format("%010d 00000 n \n", off));
            }
            xref.append("trailer\n<< /Size 7 /Root 1 0 R >>\nstartxref\n").append(xrefOffset).append("\n%%EOF");

            fos.write(xref.toString().getBytes(StandardCharsets.ISO_8859_1));
        }
    }
}
