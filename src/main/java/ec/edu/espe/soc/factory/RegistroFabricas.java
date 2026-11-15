package ec.edu.espe.soc.factory;

import ec.edu.espe.soc.modelo.IAlerta;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/** Selección por texto sin conocer las clases concretas de las alertas. */
public final class RegistroFabricas implements IProveedorAlertas {
    private final Map<String, FabricaAlerta> fabricas = new LinkedHashMap<>();

    public RegistroFabricas(Collection<FabricaAlerta> implementaciones) {
        for (FabricaAlerta fabrica : implementaciones) {
            String clave = fabrica.tipo().toUpperCase(Locale.ROOT);
            if (fabricas.putIfAbsent(clave, fabrica) != null) {
                throw new IllegalArgumentException("Tipo de fabrica duplicado: " + clave);
            }
        }
    }

    @Override
    public IAlerta crear(String tipo, String descripcion) {
        String clave = tipo == null ? "" : tipo.trim().toUpperCase(Locale.ROOT);
        FabricaAlerta fabrica = fabricas.get(clave);
        if (fabrica == null) {
            throw new IllegalArgumentException("Tipo no disponible: " + clave + ". Use: " + fabricas.keySet());
        }
        return fabrica.crear(descripcion);
    }

    @Override
    public Set<String> tiposDisponibles() {
        return Collections.unmodifiableSet(fabricas.keySet());
    }
}
