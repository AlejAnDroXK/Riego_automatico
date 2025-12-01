package ProyectoRiegoAutomatico;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static Terreno terreno = null;
    private static final ControladorRiego controlador = new ControladorRiego();

    public static void main(String[] args) {
        mostrarTitulo();
        menuInicial();
    }

    private static void mostrarTitulo() {
        System.out.println("\n-------- Cultivo y Automatización --------\n");
    }

    private static void menuInicial() {
        while (true) {
            System.out.println("1. Ingresar Terreno");
            System.out.println("2. Salir");
            System.out.print("Seleccione: ");
            int op = leerEntero();
            if (op == 1) ingresarTerreno();
            else return;
        }
    }

    private static void ingresarTerreno() {
        System.out.print("Ingrese área del terreno en m²: ");
        double area = leerDouble();

        terreno = new Terreno(1, area);

        List<Parcela> nuevas = terreno.generarParcelasAutomaticas();
        System.out.println("Parcelas generadas automáticamente: " + nuevas.size());
        System.out.println("Terreno dividido en " + nuevas.size() + " parcelas.");

        // Configurar cada parcela (cultivo, sensores y aspersores)
        for (Parcela p : nuevas) {
            configurarParcelaInteractiva(p);
            controlador.agregarParcela(p);
        }

        presionarEnter("Presione ENTER para continuar");
        menuPrincipal();
    }

    private static void configurarParcelaInteractiva(Parcela p) {

        boolean esParcelaNueva = p.listarSensores().isEmpty() && p.listarAspersores().isEmpty();

        System.out.println("\n--- Configuración de Parcela " + p.getId() + " ---");
        System.out.print("Nombre del cultivo: ");
        String nombre = sc.nextLine().trim();

        System.out.print("Humedad mínima necesaria (%) [ej: 30]: ");
        int humMin = leerEntero();

        System.out.print("Humedad máxima soportada (%) [ej: 80]: ");
        int humMax = leerEntero();

        System.out.print("Frecuencia de riego (horas): ");
        int freq = leerEntero();

        System.out.print("Coeficiente consumo de agua (1-10): ");
        double coef = leerDouble();

        // Crear y asignar cultivo
        Cultivo c = new Cultivo(nombre, humMin, humMax, freq, coef);
        p.setCultivo(c);
        p.setUmbral(c.obtenerUmbralSugerido());

        // Si la parcela es nueva, crear sensor y aspersor
        if (esParcelaNueva) {
            SensorHumedad s = new SensorHumedad(controlador.generarIdSensor());
            p.agregarSensor(s);
            controlador.agregarSensor(s);

            Aspersor a = new Aspersor(controlador.generarIdAspersor());
            p.agregarAspersor(a);
            controlador.agregarAspersor(a);

            System.out.println("Parcela nueva: sensor y aspersor asignados.");
        } else {
            System.out.println("Parcela existente: sensores y aspersores conservados.");
        }

        System.out.println("Parcela configurada: " + p.resumenCorto());
    }

    private static void menuPrincipal() {
        while (true) {
            System.out.println("\n===== MENÚ PRINCIPAL =====");
            System.out.println("1. Ver parcelas");
            System.out.println("2. Ver cultivos (por parcela)");
            System.out.println("3. Ver sensores");
            System.out.println("4. Ver aspersores");
            System.out.println("5. Añadir más terreno (y parcelas)");
            System.out.println("6. Cambiar cultivo en una parcela");
            System.out.println("7. Evaluar riego (leer sensores y controlar aspersores)");
            System.out.println("8. Ver detalle de parcela");
            System.out.println("9. Ver detalle de sensor");
            System.out.println("10. Ver detalle de aspersor");
            System.out.println("11. Agregar sensor a parcela");
            System.out.println("12. Agregar aspersor a parcela");
            System.out.println("13. Mostrar detalle completo del terreno");
            System.out.println("0. Salir");
            System.out.print("Seleccione: ");
            int op = leerEntero();

            switch (op) {
                case 1 -> controlador.mostrarParcelas();
                case 2 -> controlador.mostrarCultivos();
                case 3 -> controlador.mostrarSensores();
                case 4 -> controlador.mostrarAspersores();
                case 5 -> agregarTerreno();
                case 6 -> cambiarCultivoEnParcela();
                case 7 -> {
                    controlador.evaluarParcelas(); // lee sensores simulados y decide
                    presionarEnter("Presione ENTER para continuar");
                }
                case 8 -> detalleParcela();
                case 9 -> detalleSensor();
                case 10 -> detalleAspersor();
                case 11 -> agregarSensorAParcela();
                case 12 -> agregarAspersorAParcela();
                case 13 -> mostrarDetalleTerrenoCompleto();
                case 0 -> {
                    System.out.println("Saliendo");
                    return;
                }
                default -> System.out.println("Opción inválida.");
            }
        }
    }

    private static void agregarTerreno() {
        System.out.print("Área adicional a sumar (m²): ");
        double extra = leerDouble();

        terreno.setArea(terreno.getArea() + extra);

        List<Parcela> nuevas = terreno.generarParcelasAutomaticas();

        System.out.println("Se generaron " + nuevas.size() + " nuevas parcelas automáticamente.");

        for (Parcela p : nuevas) {
            configurarParcelaInteractiva(p);
            controlador.agregarParcela(p);
        }
    }

    private static void cambiarCultivoEnParcela() {
        controlador.mostrarParcelas();
        System.out.print("Ingrese ID de parcela a modificar: ");
        int id = leerEntero();
        Parcela p = controlador.buscarParcela(id);
        if (p == null) {
            System.out.println("Parcela no encontrada.");
            return;
        }
        System.out.println("Configurando nuevo cultivo para la parcela " + id);
        configurarParcelaInteractiva(p);
        System.out.println("Cultivo cambiado.");
    }

    private static void detalleParcela() {
        controlador.mostrarParcelas();
        System.out.print("Ingrese ID de parcela a ver: ");
        int id = leerEntero();
        Parcela p = controlador.buscarParcela(id);
        if (p == null) {
            System.out.println("Parcela no encontrada.");
            return;
        }
        System.out.println(p.detalleCompleto());
        presionarEnter("Presione ENTER para continuar...");
    }

    private static void detalleSensor() {
        controlador.mostrarSensores();
        System.out.print("Ingrese ID de sensor: ");
        int id = leerEntero();
        SensorHumedad s = controlador.buscarSensor(id);
        if (s == null) {
            System.out.println("Sensor no encontrado.");
            return;
        }
        // Forzar lectura simulada al mostrar (como si el sensor leyera ahora)
        s.leerSimulado();
        System.out.println(s.detalleCompleto());
        presionarEnter("Presione ENTER para continuar...");
    }

    private static void detalleAspersor() {
        controlador.mostrarAspersores();
        System.out.print("Ingrese ID de aspersor: ");
        int id = leerEntero();
        Aspersor a = controlador.buscarAspersor(id);
        if (a == null) {
            System.out.println("Aspersor no encontrado.");
            return;
        }
        System.out.println(a.detalleCompleto());
        presionarEnter("Presione ENTER para continuar...");
    }

    private static void agregarSensorAParcela() {
        controlador.mostrarParcelas();
        System.out.print("ID de parcela donde agregar sensor: ");
        int idp = leerEntero();
        Parcela p = controlador.buscarParcela(idp);
        if (p == null) { System.out.println("Parcela no encontrada."); return; }
        SensorHumedad s = new SensorHumedad(controlador.generarIdSensor());
        controlador.agregarSensor(s);
        p.agregarSensor(s);
        System.out.println("Sensor agregado (ID=" + s.getIdSensor() + ") a parcela " + idp);
    }

    private static void agregarAspersorAParcela() {
        controlador.mostrarParcelas();
        System.out.print("ID de parcela donde agregar aspersor: ");
        int idp = leerEntero();
        Parcela p = controlador.buscarParcela(idp);
        if (p == null) { System.out.println("Parcela no encontrada."); return; }
        Aspersor a = new Aspersor(controlador.generarIdAspersor());
        controlador.agregarAspersor(a);
        p.agregarAspersor(a);
        System.out.println("Aspersor agregado (ID=" + a.getIdAspersor() + ") a parcela " + idp);
    }

    // utility
    private static int leerEntero() {
        while (true) {
            try {
                String line = sc.nextLine().trim();
                return Integer.parseInt(line);
            } catch (Exception e) {
                System.out.print("Entrada inválida. Intente de nuevo: ");
            }
        }
    }

    private static double leerDouble() {
        while (true) {
            try {
                String line = sc.nextLine().trim();
                return Double.parseDouble(line);
            } catch (Exception e) {
                System.out.print("Entrada inválida. Intente de nuevo: ");
            }
        }
    }

    private static void presionarEnter(String msg) {
        System.out.println(msg);
        sc.nextLine();
    }

    private static void mostrarDetalleTerrenoCompleto() {
        if (terreno == null) {
            System.out.println("No hay terreno registrado.");
            return;
        }

        System.out.println("\n====== DETALLE COMPLETO DEL TERRENO ======\n");
        System.out.println("Terreno ID: " + terreno.getId());
        System.out.println("Área total: " + terreno.getArea() + " m2");
        System.out.println("Parcelas totales: " + terreno.getParcelas().size());
        System.out.println();

        for (Parcela p : terreno.getParcelas()) {
            System.out.println("----------------------------------------------------");
            System.out.println("Parcela #" + p.getId());
            System.out.println("   - Área: " + p.getSuperficie() + " m2");
            System.out.println("   - Cultivo: " + (p.getCultivo() != null ? p.getCultivo().toString() : "N/A"));
            System.out.println("   - Umbral de humedad: " + p.getUmbral() + "%");
            System.out.println("   - Estado riego: " + (p.isEstadoRiego() ? "ACTIVADO" : "DESACTIVADO"));

            // Sensores
            System.out.println("   - Sensores:");
            if (p.listarSensores().isEmpty()) {
                System.out.println("        (sin sensores)");
            } else {
                for (SensorHumedad s : p.listarSensores()) {
                    // forzar lectura para mostrar valores actualizados
                    s.leerSimulado();
                    System.out.println("        * Sensor #" + s.getIdSensor()
                            + " (humedad=" + String.format("%.2f", s.getPorcentaje()) + "%)");
                }
            }
            // Aspersores
            System.out.println("   - Aspersores:");
            if (p.listarAspersores().isEmpty()) {
                System.out.println("        (sin aspersores)");
            } else {
                for (Aspersor a : p.listarAspersores()) {
                    System.out.println("        * Aspersor #" + a.getIdAspersor()
                            + ": estado=" + (a.isEstado() ? "ON" : "OFF"));
                }
            }

            // Historial Aspersores
            System.out.println("   - Historial de riego:");
            boolean tieneHistorial = false;
            for (Aspersor a : p.listarAspersores()) {
                for (DatosAspersor d : a.listarDatos()) {
                    System.out.println("        - [" + d.getTimestamp() + "] "
                            + d.getAccion() + " (" + d.getDuracionMs() + " ms)");
                    tieneHistorial = true;
                }
            }
            if (!tieneHistorial) System.out.println("        (sin historial)");

            System.out.println("----------------------------------------------------");
        }

        presionarEnter("Presione ENTER para continuar");
    }

}

