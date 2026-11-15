    package ec.edu.espe.soc;

    import ec.edu.espe.soc.factory.FabricaAlerta;
    import ec.edu.espe.soc.factory.FabricaAlertaAcceso;
    import ec.edu.espe.soc.factory.FabricaAlertaRed;
    import ec.edu.espe.soc.factory.FabricaAlertaServidor;
    import ec.edu.espe.soc.factory.RegistroFabricas;
import ec.edu.espe.soc.factory.IProveedorAlertas;
    import ec.edu.espe.soc.modelo.AlertaRegistrada;
    import ec.edu.espe.soc.modelo.IAlerta;
    import ec.edu.espe.soc.repositorio.IRepositorioAlertas;
    import ec.edu.espe.soc.repositorio.RepositorioAlertasJdbc;
    import ec.edu.espe.soc.servicio.ServicioAlertas;
    import ec.edu.espe.soc.singleton.ConexionBaseDatos;
    import java.sql.Connection;
    import java.sql.SQLException;
    import java.util.List;
    import java.util.Map;
    import java.util.Scanner;

    /** Aplicacion de consola. No utiliza Spring, web, HTML ni GUI. */
    public final class Principal {
        private final Scanner teclado;
        private final ServicioAlertas servicio;

        private Principal(Scanner teclado, ServicioAlertas servicio) {
            this.teclado = teclado;
            this.servicio = servicio;
        }

        public static void main(String[] args) {
            // Composition Root: los 'new' concretos se concentran aqui y en las fabricas.
            List<FabricaAlerta> implementaciones = List.of(
                    new FabricaAlertaAcceso(),
                    new FabricaAlertaRed(),
                    new FabricaAlertaServidor());
            IProveedorAlertas registro = new RegistroFabricas(implementaciones);
            IRepositorioAlertas repositorio = new RepositorioAlertasJdbc(ConexionBaseDatos.getInstancia());
            ServicioAlertas servicio = new ServicioAlertas(registro, repositorio);
            try (Scanner teclado = new Scanner(System.in)) {
                new Principal(teclado, servicio).ejecutar();
            } finally {
                ConexionBaseDatos.getInstancia().cerrar();
            }
        }

        private void ejecutar() {
            boolean seguir = true;
            while (seguir) {
                menu();
                String opcion = teclado.nextLine().trim();
                try {
                    switch (opcion) {
                        case "1" -> registrar();
                        case "2" -> mostrarHistorial();
                        case "3" -> mostrarEstadisticas();
                        case "4" -> demostrarSingleton();
                        case "5" -> demostrarFactoryMethod();
                        case "6" -> comprobarMySql();
                        case "0" -> { System.out.println("Fin de la demostracion."); seguir = false; }
                        default -> System.out.println("Seleccione una opcion valida.");
                    }
                } catch (IllegalArgumentException error) {
                    System.out.println("Dato invalido: " + error.getMessage());
                } catch (SQLException error) {
                    System.out.println("No se pudo completar la operacion MySQL: " + error.getMessage());
                    System.out.println("Revise README.md, database/schema.sql y config/database.properties.");
                }
                System.out.println();
            }
        }

        private static void menu() {
            System.out.println("\n=========== SOC - ALERTAS DE CIBERSEGURIDAD ===========");
            System.out.println("1. Crear y guardar alerta en MySQL (ambos patrones)");
            System.out.println("2. Mostrar historial desde MySQL");
            System.out.println("3. Estadisticas de alertas");
            System.out.println("4. Demostracion Singleton (5 referencias)");
            System.out.println("5. Demostracion Factory Method (sin DB)");
            System.out.println("6. Comprobar conexion MySQL");
            System.out.println("0. Salir");
            System.out.print("Opcion: ");
        }

        private void registrar() throws SQLException {
            System.out.println("Tipos disponibles: " + servicio.tiposDisponibles());
            System.out.print("Tipo (ACCESO/RED/SERVIDOR): ");
            String tipo = teclado.nextLine();
            System.out.print("Descripcion del evento ficticio: ");
            String descripcion = teclado.nextLine();
            AlertaRegistrada guardada = servicio.registrar(tipo, descripcion);
            System.out.println("[FACTORY METHOD] Se creo el tipo: " + guardada.tipo());
            System.out.println("[SINGLETON + MYSQL] Alerta guardada, ID: " + guardada.id());
            System.out.println("Severidad: " + guardada.severidad());
            System.out.println("Descripcion: " + guardada.descripcion());
        }

        private void mostrarHistorial() throws SQLException {
            List<AlertaRegistrada> alertas = servicio.historial();
            if (alertas.isEmpty()) {
                System.out.println("No hay alertas guardadas en MySQL.");
                return;
            }
            System.out.println("----- HISTORIAL MYSQL (" + alertas.size() + ") -----");
            for (AlertaRegistrada a : alertas) {
                System.out.printf("#%d | %s | %s | %s | %s%n",
                    a.id(), a.tipo(), a.severidad(), a.creadaEn(), a.descripcion());
            }
        }

        private void mostrarEstadisticas() throws SQLException {
            Map<String, Long> conteos = servicio.estadisticas();
            long total = 0;
            System.out.println("----- ESTADISTICAS MYSQL -----");
            for (Map.Entry<String, Long> e : conteos.entrySet()) {
                System.out.println(e.getKey() + ": " + e.getValue());
                total += e.getValue();
            }
            System.out.println("Total: " + total);
        }

        private static void demostrarSingleton() {
            System.out.println("----- PRUEBA SINGLETON: CINCO SOLICITUDES -----");
            ConexionBaseDatos primera = ConexionBaseDatos.getInstancia();
            boolean todasIguales = true;
            for (int i = 1; i <= 5; i++) {
                ConexionBaseDatos actual = ConexionBaseDatos.getInstancia();
                todasIguales &= primera == actual;
                System.out.printf("Solicitud %d: identityHashCode=%d | misma referencia=%s%n",
                    i, System.identityHashCode(actual), primera == actual);
            }
            System.out.println("Resultado final: " + (todasIguales ? "MISMA INSTANCIA" : "ERROR"));
            System.out.println("Nota: se verifica el gestor, no una sola conexion fisica JDBC.");
        }

        private void demostrarFactoryMethod() {
            System.out.println("----- PRUEBA FACTORY METHOD -----");
            for (String tipo : servicio.tiposDisponibles()) {
                IAlerta alerta = servicio.crearVistaPrevia(tipo, "Simulacion educativa de " + tipo);
                System.out.printf("Texto=%s | clase=%s | severidad=%s%n",
                    tipo, alerta.getClass().getSimpleName(), alerta.severidad());
            }
            System.out.println("Sin 'new AlertaAcceso/Red/Servidor' en el metodo cliente.");
        }

        private static void comprobarMySql() throws SQLException {
            try (Connection con = ConexionBaseDatos.getInstancia().obtenerConexion()) {
                System.out.println(con.isValid(3)
                        ? "Conexion a MySQL disponible (mediante HikariCP)."
                        : "La conexion no paso la comprobacion.");
            }
        }
    }
