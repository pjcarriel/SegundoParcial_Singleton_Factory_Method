package ec.edu.espe.soc.modelo;

public record AlertaAcceso(String descripcion) implements IAlerta {
    @Override public String tipo() { return "ACCESO"; }
    @Override public String severidad() { return "ALTA"; }
}
