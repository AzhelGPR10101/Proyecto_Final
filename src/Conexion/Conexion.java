package Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String HOST = "localhost";
    private static final String PUERTO = "5432";
    private static final String BASE_DATOS = "KryptonBase";
    private static final String URL = "jdbc:postgresql://" + HOST + ":" + PUERTO + "/" + BASE_DATOS;
    private static final String USUARIO = "postgres";
    private static final String PASSWORD = "2212";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontro el driver de PostgreSQL.", e);
        }
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }
}
