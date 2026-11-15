package ec.edu.espe.soc;

import ec.edu.espe.soc.factory.FabricaAlertaAcceso;
import ec.edu.espe.soc.factory.FabricaAlertaRed;
import ec.edu.espe.soc.factory.FabricaAlertaServidor;
import ec.edu.espe.soc.factory.RegistroFabricas;
import ec.edu.espe.soc.modelo.IAlerta;
import ec.edu.espe.soc.singleton.ConexionBaseDatos;
import java.util.List;

/** Prueba autocontenida: no necesita MySQL; lanzar como clase Java en IntelliJ. */
public final class PruebasPatrones {
    public static void main(String[] args) {
        ConexionBaseDatos primera = ConexionBaseDatos.getInstancia();
        for (int i = 0; i < 5; i++) {
            if (primera != ConexionBaseDatos.getInstancia()) {
                throw new AssertionError("Singleton devolvio un objeto distinto.");
            }
        }
        RegistroFabricas registro = new RegistroFabricas(List.of(
                new FabricaAlertaAcceso(), new FabricaAlertaRed(), new FabricaAlertaServidor()));
        verificar(registro, "acceso", "AlertaAcceso");
        verificar(registro, "red", "AlertaRed");
        verificar(registro, "servidor", "AlertaServidor");
        boolean invalidoRechazado = false;
        try { registro.crear("OTRO", "Prueba"); }
        catch (IllegalArgumentException ex) { invalidoRechazado = true; }
        if (!invalidoRechazado) throw new AssertionError("No rechazo un tipo invalido.");
        System.out.println("PRUEBAS EXITOSAS: Singleton + Factory Method + validacion.");
    }

    private static void verificar(RegistroFabricas registro, String tipo, String clase) {
        IAlerta alerta = registro.crear(tipo, "Evento simulado");
        if (!clase.equals(alerta.getClass().getSimpleName())) {
            throw new AssertionError("La fabrica " + tipo + " no genero " + clase);
        }
    }
}
