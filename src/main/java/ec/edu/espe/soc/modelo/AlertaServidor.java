package ec.edu.espe.soc.modelo;

public record AlertaServidor(String descripcion) implements IAlerta {
    @Override public String tipo() { return "SERVIDOR"; }
    @Override public String severidad() { return "CRITICA"; }
}
