package ec.edu.espe.soc.factory;

import ec.edu.espe.soc.modelo.AlertaRed;
import ec.edu.espe.soc.modelo.IAlerta;

public final class FabricaAlertaRed extends FabricaAlerta {
    @Override public String tipo() { return "RED"; }

    @Override
    protected IAlerta crearAlerta(String descripcion) {
        return new AlertaRed(descripcion);
    }
}
