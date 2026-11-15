package ec.edu.espe.soc.factory;

import ec.edu.espe.soc.modelo.IAlerta;
import java.util.Set;

/** Contrato de seleccion de productos, independiente de su implementacion concreta. */
public interface IProveedorAlertas {
    IAlerta crear(String tipo, String descripcion);
    Set<String> tiposDisponibles();
}
