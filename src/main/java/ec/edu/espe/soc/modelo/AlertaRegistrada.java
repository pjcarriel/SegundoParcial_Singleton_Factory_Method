package ec.edu.espe.soc.modelo;

import java.time.LocalDateTime;

/** Datos almacenados y recuperados desde MySQL. */
public record AlertaRegistrada(long id, String tipo, String severidad,
                               String descripcion, LocalDateTime creadaEn) { }
