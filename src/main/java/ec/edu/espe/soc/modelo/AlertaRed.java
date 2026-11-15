package ec.edu.espe.soc.modelo;

public record AlertaRed(String descripcion) implements IAlerta {
    @Override public String tipo() { return "RED"; }
    @Override public String severidad() { return "MEDIA"; }
}
