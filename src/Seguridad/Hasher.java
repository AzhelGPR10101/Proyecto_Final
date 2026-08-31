package Seguridad;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Hasher {

    private static final int ITERACIONES = 65536;
    private static final int LONGITUD_LLAVE = 128;
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String hashear(String passwordPlano) {
        byte[] sal = new byte[16];
        RANDOM.nextBytes(sal);
        byte[] hash = pbkdf2(passwordPlano.toCharArray(), sal);
        return Base64.getEncoder().encodeToString(sal) + ":" + Base64.getEncoder().encodeToString(hash);
    }

    public static boolean verificar(String passwordPlano, String valorGuardado) {
        if (valorGuardado == null || !valorGuardado.contains(":")) {
            return false;
        }
        String[] partes = valorGuardado.split(":", 2);
        byte[] sal = Base64.getDecoder().decode(partes[0]);
        byte[] hashEsperado = Base64.getDecoder().decode(partes[1]);
        byte[] hashIngresado = pbkdf2(passwordPlano.toCharArray(), sal);
        return java.security.MessageDigest.isEqual(hashEsperado, hashIngresado);
    }

    private static byte[] pbkdf2(char[] password, byte[] sal) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, sal, ITERACIONES, LONGITUD_LLAVE);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Error al hashear la contraseña", e);
        }
    }
}