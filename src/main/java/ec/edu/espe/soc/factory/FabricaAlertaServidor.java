package ec.edu.espe.soc.factory;

import ec.edu.espe.soc.modelo.AlertaServidor;
import ec.edu.espe.soc.modelo.IAlerta;

public final class FabricaAlertaServidor extends FabricaAlerta {
    @Override public String tipo() { return "SERVIDOR"; }

    @Override
    protected IAlerta crearAlerta(String descripcion) {
        return new AlertaServidor(descripcion);
    }
}
