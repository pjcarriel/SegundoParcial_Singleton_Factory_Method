package ec.edu.espe.soc.modelo;

/** Producto abstracto del patrón Factory Method. */
public interface IAlerta {
    String tipo();
    String severidad();
    String descripcion();
}
