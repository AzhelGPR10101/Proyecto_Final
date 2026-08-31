
package Vista.IA;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioRecorder {

    private static TargetDataLine linea;
    private static Thread hiloGrabacion;
    private static File archivo;

    public static void iniciarGrabacion() throws Exception {
        AudioFormat formato = new AudioFormat(16000, 16, 1, true, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, formato);

        if (!AudioSystem.isLineSupported(info)) {
            throw new RuntimeException("No se encontró un micrófono compatible.");
        }

        linea = (TargetDataLine) AudioSystem.getLine(info);
        linea.open(formato);
        linea.start();

        archivo = new File(System.getProperty("java.io.tmpdir"), "audio_ktp.wav");

        hiloGrabacion = new Thread(() -> {
            try {
                AudioSystem.write(new AudioInputStream(linea), AudioFileFormat.Type.WAVE, archivo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        hiloGrabacion.start();
    }

    public static String detenerGrabacion() throws Exception {
        if (linea != null) {
            linea.stop();
            linea.close();
        }
        if (hiloGrabacion != null) {
            hiloGrabacion.join();
        }
        return archivo.getAbsolutePath();
    }
}