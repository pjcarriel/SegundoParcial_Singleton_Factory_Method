package ec.edu.espe.soc.servicio;

import ec.edu.espe.soc.factory.IProveedorAlertas;
import ec.edu.espe.soc.modelo.AlertaRegistrada;
import ec.edu.espe.soc.modelo.IAlerta;
import ec.edu.espe.soc.repositorio.IRepositorioAlertas;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Caso de uso: Factory Method -> objeto IAlerta -> repositorio -> Singleton -> MySQL. */
public final class ServicioAlertas {
    private final IProveedorAlertas fabricas;
    private final IRepositorioAlertas repositorio;

    public ServicioAlertas(IProveedorAlertas fabricas, IRepositorioAlertas repositorio) {
        this.fabricas = fabricas;
        this.repositorio = repositorio;
    }

    public AlertaRegistrada registrar(String tipo, String descripcion) throws SQLException {
        IAlerta alerta = fabricas.crear(tipo, descripcion);
        return repositorio.guardar(alerta);
    }

    public List<AlertaRegistrada> historial() throws SQLException {
        return repositorio.listar();
    }

    public Map<String, Long> estadisticas() throws SQLException {
        return repositorio.contarPorTipo();
    }

    public Set<String> tiposDisponibles() {
        return fabricas.tiposDisponibles();
    }

    /** Demo del Factory Method sin pedir conexion MySQL. */
    public IAlerta crearVistaPrevia(String tipo, String descripcion) {
        return fabricas.crear(tipo, descripcion);
    }
}
