package ec.edu.espe.soc.repositorio;

import ec.edu.espe.soc.modelo.AlertaRegistrada;
import ec.edu.espe.soc.modelo.IAlerta;
import ec.edu.espe.soc.singleton.ConexionBaseDatos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** JDBC/MySQL con consultas preparadas y conexiones cerradas por try-with-resources. */
public final class RepositorioAlertasJdbc implements IRepositorioAlertas {
    private final ConexionBaseDatos conexion;

    public RepositorioAlertasJdbc(ConexionBaseDatos conexion) {
        this.conexion = conexion;
    }

    @Override
    public AlertaRegistrada guardar(IAlerta alerta) throws SQLException {
        String sql = "INSERT INTO alertas (tipo, severidad, descripcion) VALUES (?, ?, ?)";
        try (Connection con = conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, alerta.tipo());
            ps.setString(2, alerta.severidad());
            ps.setString(3, alerta.descripcion());
            ps.executeUpdate();
            long id;
            try (ResultSet claves = ps.getGeneratedKeys()) {
                if (!claves.next()) throw new SQLException("MySQL no devolvio el ID de la alerta.");
                id = claves.getLong(1);
            }
            // Fecha obtenida del mismo motor para que coincida con lo registrado.
            try (PreparedStatement consulta = con.prepareStatement(
                    "SELECT creada_en FROM alertas WHERE id = ?")) {
                consulta.setLong(1, id);
                try (ResultSet rs = consulta.executeQuery()) {
                    if (!rs.next()) throw new SQLException("No se encontro la alerta registrada.");
                    return new AlertaRegistrada(id, alerta.tipo(), alerta.severidad(),
                            alerta.descripcion(), rs.getTimestamp("creada_en").toLocalDateTime());
                }
            }
        }
    }

    @Override
    public List<AlertaRegistrada> listar() throws SQLException {
        String sql = "SELECT id, tipo, severidad, descripcion, creada_en FROM alertas ORDER BY id DESC";
        List<AlertaRegistrada> resultado = new ArrayList<>();
        try (Connection con = conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                resultado.add(new AlertaRegistrada(
                    rs.getLong("id"), rs.getString("tipo"), rs.getString("severidad"),
                    rs.getString("descripcion"), rs.getTimestamp("creada_en").toLocalDateTime()));
            }
        }
        return resultado;
    }

    @Override
    public Map<String, Long> contarPorTipo() throws SQLException {
        String sql = "SELECT tipo, COUNT(*) AS total FROM alertas GROUP BY tipo ORDER BY tipo";
        Map<String, Long> resultado = new LinkedHashMap<>();
        try (Connection con = conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) resultado.put(rs.getString("tipo"), rs.getLong("total"));
        }
        return resultado;
    }
}
