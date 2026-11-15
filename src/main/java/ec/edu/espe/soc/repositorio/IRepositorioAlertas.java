package ec.edu.espe.soc.repositorio;

import ec.edu.espe.soc.modelo.AlertaRegistrada;
import ec.edu.espe.soc.modelo.IAlerta;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/** Abstraccion de persistencia: ServicioAlertas no conoce JDBC. */
public interface IRepositorioAlertas {
    AlertaRegistrada guardar(IAlerta alerta) throws SQLException;
    List<AlertaRegistrada> listar() throws SQLException;
    Map<String, Long> contarPorTipo() throws SQLException;
}
