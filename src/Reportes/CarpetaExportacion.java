package Reportes;

import java.io.File;

public class CarpetaExportacion {

    public static String obtenerRuta(String nombreArchivo) {
        String descargas = System.getProperty("user.home") + File.separator + "Downloads";
        File carpeta = new File(descargas);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
        return carpeta.getAbsolutePath() + File.separator + nombreArchivo;
    }
}
