package ec.edu.espe.soc.factory;

import ec.edu.espe.soc.modelo.AlertaAcceso;
import ec.edu.espe.soc.modelo.IAlerta;

public final class FabricaAlertaAcceso extends FabricaAlerta {
    @Override public String tipo() { return "ACCESO"; }

    @Override
    protected IAlerta crearAlerta(String descripcion) {
        return new AlertaAcceso(descripcion);
    }
}
