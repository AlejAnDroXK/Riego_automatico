package ProyectoRiegoAutomatico;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ControladorRiego {

    private final List<Parcela> parcelas = new ArrayList<>();
    private final List<SensorHumedad> sensores = new ArrayList<>();
    private final List<Aspersor> aspersores = new ArrayList<>();

    private int nextSensorId = 1;
    private int nextAspId = 1;

    // Anti-bombeo: tiempo mínimo entre cambios (ms)
    private long tiempoMinCambioMs = 30_000L;
    private boolean estadoBombaGeneral = false;

    // ID generators
    public int generarIdSensor() { return nextSensorId++; }
    public int generarIdAspersor() { return nextAspId++; }

    // Agregar a listas globales
    public void agregarParcela(Parcela p) {
        if (p != null) parcelas.add(p);
    }

    public void agregarSensor(SensorHumedad s) {
        if (s != null) sensores.add(s);
    }

    public void agregarAspersor(Aspersor a) {
        if (a != null) aspersores.add(a);
    }

    // Buscar por id
    public Parcela buscarParcela(int id) {
        for (Parcela p : parcelas) if (p.getId() == id) return p;
        return null;
    }

    public SensorHumedad buscarSensor(int id) {
        for (SensorHumedad s : sensores) if (s.getIdSensor() == id) return s;
        return null;
    }

    public Aspersor buscarAspersor(int id) {
        for (Aspersor a : aspersores) if (a.getIdAspersor() == id) return a;
        return null;
    }

    // Mostrar colecciones
    public void mostrarParcelas() {
        System.out.println("Parcelas disponibles:");
        for (Parcela p : parcelas) System.out.println("  " + p.resumenCorto());
    }

    public void mostrarCultivos() {
        System.out.println("Cultivos por parcela:");
        for (Parcela p : parcelas) System.out.println("  Parcela " + p.getId() + ": " + (p.getCultivo() != null ? p.getCultivo() : "N/A"));
    }

    public void mostrarSensores() {
        System.out.println("Sensores registrados:");
        for (SensorHumedad s : sensores) System.out.println("  " + s.toString());
    }

    public void mostrarAspersores() {
        System.out.println("Aspersores registrados:");
        for (Aspersor a : aspersores) System.out.println("  " + a.toString());
    }

    public void evaluarParcelas() {
        for (Parcela p : parcelas) {
            double humProm = p.obtenerHumedadPromedio();
            System.out.println("Parcela " + p.getId() + " -> humProm=" + String.format("%.2f", humProm) + "% umbral=" + p.getUmbral());
            if (humProm < p.getUmbral()) {
                // activar aspersores de la parcela (si anti-bombeo permite)
                for (Aspersor asp : p.listarAspersores()) {
                    if (!asp.isEstado()) {
                        Instant ultimo = asp.getUltimoCambio();
                        Instant ahora = Instant.now();
                        if (ultimo == null || Duration.between(ultimo, ahora).toMillis() >= tiempoMinCambioMs) {
                            asp.activar(0);
                            asp.agregarDato(new DatosAspersor(Instant.now(), "ACTIVAR", 0));
                            registrarEvento("ACTIVAR", p.getId(), asp.getIdAspersor());
                        } else {
                            System.out.println("Anti-bombeo: no activar aspersor " + asp.getIdAspersor());
                        }
                    }
                }
                p.setEstadoRiego(true);
            } else {
                // desactivar aspersores si están ON
                for (Aspersor asp : p.listarAspersores()) {
                    if (asp.isEstado()) {
                        asp.desactivar();
                        asp.agregarDato(new DatosAspersor(Instant.now(), "DESACTIVAR", 0));
                        registrarEvento("DESACTIVAR", p.getId(), asp.getIdAspersor());
                    }
                }
                p.setEstadoRiego(false);
            }
        }
        // actualizar estado bomba general (simple)
        boolean algunaOn = false;
        for (Aspersor a : aspersores) if (a.isEstado()) { algunaOn = true; break; }
        estadoBombaGeneral = algunaOn;
        System.out.println("Bomba general: " + (estadoBombaGeneral ? "ON" : "OFF"));
    }

    public void registrarEvento(String accion, int parcelaId, int aspId) {
        System.out.println("Evento: " + accion + " Parcela:" + parcelaId + " Asp:" + aspId + " ts=" + Instant.now());
    }

    // getters / setters para parámetros
    public long getTiempoMinCambioMs() { return tiempoMinCambioMs; }
    public void setTiempoMinCambioMs(long ms) { this.tiempoMinCambioMs = ms; }
    public List<Parcela> getParcelas() { return new ArrayList<>(parcelas); }
    public List<SensorHumedad> getSensores() { return new ArrayList<>(sensores); }
    public List<Aspersor> getAspersores() { return new ArrayList<>(aspersores); }
}

