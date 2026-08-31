package Reportes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class GeneradorExcel {

    public static void generar(String rutaSalida, String nombreHoja, String[] encabezados, List<String[]> filas) throws IOException {
        StringBuilder sheet = new StringBuilder();
        sheet.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
        sheet.append("<worksheet xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\"><sheetData>\n");

        int numFila = 1;
        sheet.append("<row r=\"").append(numFila).append("\">");
        for (int i = 0; i < encabezados.length; i++) {
            String celda = columnaLetra(i) + numFila;
            sheet.append("<c r=\"").append(celda).append("\" t=\"inlineStr\"><is><t>")
                 .append(escaparXml(encabezados[i])).append("</t></is></c>");
        }
        sheet.append("</row>\n");

        for (String[] fila : filas) {
            numFila++;
            sheet.append("<row r=\"").append(numFila).append("\">");
            for (int i = 0; i < fila.length; i++) {
                String celda = columnaLetra(i) + numFila;
                String valor = fila[i] == null ? "" : fila[i];
                if (esNumero(valor)) {
                    sheet.append("<c r=\"").append(celda).append("\"><v>").append(valor).append("</v></c>");
                } else {
                    sheet.append("<c r=\"").append(celda).append("\" t=\"inlineStr\"><is><t>")
                         .append(escaparXml(valor)).append("</t></is></c>");
                }
            }
            sheet.append("</row>\n");
        }
        sheet.append("</sheetData></worksheet>");

        String contentTypes = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                + "<Types xmlns=\"http://schemas.openxmlformats.org/package/2006/content-types\">"
                + "<Default Extension=\"rels\" ContentType=\"application/vnd.openxmlformats-package.relationships+xml\"/>"
                + "<Default Extension=\"xml\" ContentType=\"application/xml\"/>"
                + "<Override PartName=\"/xl/workbook.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml\"/>"
                + "<Override PartName=\"/xl/worksheets/sheet1.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml\"/>"
                + "</Types>";

        String rootRels = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                + "<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\">"
                + "<Relationship Id=\"rId1\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument\" Target=\"xl/workbook.xml\"/>"
                + "</Relationships>";

        String workbook = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                + "<workbook xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\">"
                + "<sheets><sheet name=\"" + escaparXml(nombreHoja) + "\" sheetId=\"1\" r:id=\"rId1\"/></sheets>"
                + "</workbook>";

        String workbookRels = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                + "<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\">"
                + "<Relationship Id=\"rId1\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet\" Target=\"worksheets/sheet1.xml\"/>"
                + "</Relationships>";

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(rutaSalida))) {
            escribirEntrada(zos, "[Content_Types].xml", contentTypes);
            escribirEntrada(zos, "_rels/.rels", rootRels);
            escribirEntrada(zos, "xl/workbook.xml", workbook);
            escribirEntrada(zos, "xl/_rels/workbook.xml.rels", workbookRels);
            escribirEntrada(zos, "xl/worksheets/sheet1.xml", sheet.toString());
        }
    }

    private static boolean esNumero(String s) {
        if (s == null || s.isEmpty()) return false;
        try { Double.parseDouble(s); return true; } catch (NumberFormatException e) { return false; }
    }

    private static String escaparXml(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&apos;");
    }

    private static String columnaLetra(int indiceCero) {
        StringBuilder sb = new StringBuilder();
        int n = indiceCero;
        while (true) {
            sb.insert(0, (char) ('A' + (n % 26)));
            n = n / 26 - 1;
            if (n < 0) break;
        }
        return sb.toString();
    }

    private static void escribirEntrada(ZipOutputStream zos, String nombre, String contenido) throws IOException {
        ZipEntry entry = new ZipEntry(nombre);
        zos.putNextEntry(entry);
        zos.write(contenido.getBytes(StandardCharsets.UTF_8));
        zos.closeEntry();
    }
}