package ec.edu.espe.soc.singleton;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Singleton clasico: UNA instancia gestora por JVM/cargador de clases.
 * Las conexiones JDBC son prestadas por HikariCP, no son un Singleton.
 */
public final class ConexionBaseDatos {
    private static final ConexionBaseDatos INSTANCIA = new ConexionBaseDatos();
    private HikariDataSource pool;

    private ConexionBaseDatos() { }

    public static ConexionBaseDatos getInstancia() {
        return INSTANCIA;
    }

    public Connection obtenerConexion() throws SQLException {
        inicializarSiNecesario();
        return pool.getConnection();
    }

    /** Evita crear varias veces el pool cuando dos hilos lo solicitan. */
    private synchronized void inicializarSiNecesario() throws SQLException {
        if (pool != null) return;
        Path archivo = Path.of("config", "database.properties");
        if (!Files.isRegularFile(archivo)) {
            throw new SQLException("Falta config/database.properties. Copie el archivo .example y configure MySQL.");
        }
        Properties propiedades = new Properties();
        try (Reader lector = Files.newBufferedReader(archivo, StandardCharsets.UTF_8)) {
            propiedades.load(lector);
        } catch (IOException error) {
            throw new SQLException("No se pudo leer la configuracion: " + error.getMessage(), error);
        }
        String url = propiedades.getProperty("jdbc.url", "").trim();
        String usuario = propiedades.getProperty("jdbc.user", "").trim();
        String clave = propiedades.getProperty("jdbc.password", "");
        if (url.isEmpty() || usuario.isEmpty()) {
            throw new SQLException("Defina jdbc.url y jdbc.user en config/database.properties.");
        }
        int maximo;
        try {
            maximo = Integer.parseInt(propiedades.getProperty("pool.maximo", "5").trim());
        } catch (NumberFormatException error) {
            throw new SQLException("pool.maximo debe ser un numero entero.", error);
        }
        if (maximo < 1 || maximo > 20) {
            throw new SQLException("pool.maximo debe estar entre 1 y 20.");
        }
        HikariConfig configuracion = new HikariConfig();
        configuracion.setJdbcUrl(url);
        configuracion.setUsername(usuario);
        configuracion.setPassword(clave);
        configuracion.setDriverClassName("com.mysql.cj.jdbc.Driver");
        configuracion.setPoolName("SOC-Pool-MySQL");
        configuracion.setMaximumPoolSize(maximo);
        configuracion.setMinimumIdle(1);
        configuracion.setConnectionTimeout(5000);
        configuracion.setInitializationFailTimeout(2000);
        try {
            pool = new HikariDataSource(configuracion);
        } catch (RuntimeException error) {
            throw new SQLException("No se pudo iniciar el pool MySQL. Verifique servicio, base, usuario y clave.", error);
        }
    }

    public synchronized void cerrar() {
        if (pool != null) {
            pool.close();
            pool = null;
        }
    }
}
