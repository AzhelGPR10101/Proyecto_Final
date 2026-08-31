package Conexion;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Conexion {

    // Pool sencillo de conexiones reales ya abiertas hacia la base. Antes,
    // getConnection() abria una conexion TCP/TLS nueva contra la base en CADA
    // llamada (y son 100+ llamadas repartidas en los DAO) -- eso era lo que
    // hacia lento el login/carga del menu contra una base remota (Neon), no
    // las consultas en si. Ahora las conexiones se reusan: al hacer close()
    // no se cierra el socket, se devuelve al pool.
    private static final int TAMANO_POOL = 8;
    private static final BlockingQueue<Connection> POOL = new ArrayBlockingQueue<>(TAMANO_POOL);

    private static volatile boolean configurado = false;
    private static String url;
    private static String usuario;
    private static String password;

    private static synchronized void asegurarConfiguracion() {
        if (configurado) {
            return;
        }
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream("config.properties")) {
            props.load(in);
        } catch (IOException e) {
            throw new RuntimeException("No se encontro config.properties en la raiz del proyecto. "
                    + "Copia config.properties.example, renombralo a config.properties y coloca ahi "
                    + "los datos de conexion a la base de datos.", e);
        }

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("No se encontro el driver de PostgreSQL.", e);
        }

        String host = props.getProperty("db.host", "localhost");
        String puerto = props.getProperty("db.port", "5432");
        String baseDatos = props.getProperty("db.name", "KryptonBase");
        String sslmode = props.getProperty("db.sslmode", "disable");

        url = "jdbc:postgresql://" + host + ":" + puerto + "/" + baseDatos + "?sslmode=" + sslmode;
        usuario = props.getProperty("db.user", "postgres");
        password = props.getProperty("db.password", "");
        configurado = true;
    }

    private static Connection abrirConexionReal() throws SQLException {
        return DriverManager.getConnection(url, usuario, password);
    }

    public static Connection getConnection() throws SQLException {
        asegurarConfiguracion();

        Connection real = POOL.poll();
        if (real != null) {
            try {
                if (real.isClosed() || !real.isValid(2)) {
                    real = abrirConexionReal();
                }
            } catch (SQLException e) {
                real = abrirConexionReal();
            }
        } else {
            real = abrirConexionReal();
        }

        return crearProxyDevolutivo(real);
    }

    // Envuelve la conexion real en un proxy que se comporta exactamente igual
    // (delega todos los metodos), excepto close(): en vez de cerrar el socket,
    // devuelve la conexion al pool para que la siguiente llamada la reuse.
    private static Connection crearProxyDevolutivo(Connection real) {
        return (Connection) Proxy.newProxyInstance(
                Connection.class.getClassLoader(),
                new Class<?>[]{Connection.class},
                (Object proxy, Method method, Object[] args) -> {
                    if ("close".equals(method.getName())) {
                        if (!POOL.offer(real)) {
                            real.close();
                        }
                        return null;
                    }
                    try {
                        return method.invoke(real, args);
                    } catch (InvocationTargetException e) {
                        throw e.getCause();
                    }
                });
    }
}
