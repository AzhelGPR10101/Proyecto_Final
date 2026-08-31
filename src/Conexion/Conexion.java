package Conexion;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Conexion {

    private static Properties cargarConfiguracion() {
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream("config.properties")) {
            props.load(in);
        } catch (IOException e) {
            throw new RuntimeException("No se encontro config.properties en la raiz del proyecto. "
                    + "Copia config.properties.example, renombralo a config.properties y coloca ahi "
                    + "los datos de conexion a la base de datos.", e);
        }
        return props;
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontro el driver de PostgreSQL.", e);
        }

        Properties config = cargarConfiguracion();
        String host = config.getProperty("db.host", "localhost");
        String puerto = config.getProperty("db.port", "5432");
        String baseDatos = config.getProperty("db.name", "KryptonBase");
        String usuario = config.getProperty("db.user", "postgres");
        String password = config.getProperty("db.password", "");
        String sslmode = config.getProperty("db.sslmode", "disable");

        String url = "jdbc:postgresql://" + host + ":" + puerto + "/" + baseDatos + "?sslmode=" + sslmode;
        return DriverManager.getConnection(url, usuario, password);
    }
}
