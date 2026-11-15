package ec.edu.espe.soc.factory;

import ec.edu.espe.soc.modelo.IAlerta;

/** Creator abstracto: declara el verdadero Factory Method crearAlerta(). */
public abstract class FabricaAlerta {
    public abstract String tipo();

    public final IAlerta crear(String descripcion) {
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("La descripcion no puede estar vacia.");
        }
        String limpia = descripcion.trim();
        if (limpia.length() > 255) {
            throw new IllegalArgumentException("La descripcion supera los 255 caracteres.");
        }
        return crearAlerta(limpia);
    }

    protected abstract IAlerta crearAlerta(String descripcion);
}
