package ProyectoRiegoAutomatico;

import java.util.ArrayList;
import java.util.List;

public class Parcela {
    private final int id;
    private final double superficie;
    private Cultivo cultivo;
    private int umbral;
    private boolean estadoRiego = false;

    private final List<SensorHumedad> sensores = new ArrayList<>();
    private final List<Aspersor> aspersores = new ArrayList<>();

    public Parcela(int id, double superficie) {
        this.id = id;
        this.superficie = superficie;
    }

    // Métodos de gestión de sensores/aspersores (agregar y buscar)
    public void agregarSensor(SensorHumedad s) {
        if (s != null) sensores.add(s);
    }

    public SensorHumedad buscarSensorPorId(int idSensor) {
        for (SensorHumedad s : sensores) if (s.getIdSensor() == idSensor) return s;
        return null;
    }

    public List<SensorHumedad> listarSensores() {
        return new ArrayList<>(sensores);
    }

    public void agregarAspersor(Aspersor a) {
        if (a != null) aspersores.add(a);
    }

    public Aspersor buscarAspersorPorId(int idAsp) {
        for (Aspersor a : aspersores) if (a.getIdAspersor() == idAsp) return a;
        return null;
    }

    public List<Aspersor> listarAspersores() {
        return new ArrayList<>(aspersores);
    }

    // Obtener humedad promedio leyendo (simulado) todos los sensores asociados
    public double obtenerHumedadPromedio() {
        if (sensores.isEmpty()) return 0.0;
        double sum = 0.0;
        int cnt = 0;
        for (SensorHumedad s : sensores) {
            s.leerSimulado(); // forzar lectura simulada actual
            if (!s.validar()) continue;
            sum += s.getPorcentaje();
            cnt++;
        }
        return cnt == 0 ? 0.0 : sum / cnt;
    }

    public boolean necesitaRiego() {
        double prom = obtenerHumedadPromedio();
        return prom < umbral;
    }

    // Resúmenes y detalles
    public String resumenCorto() {
        return "Parcela " + id + " | sup=" + superficie + " m2 | cultivo=" + (cultivo != null ? cultivo.getNombre() : "N/A");
    }

    public String detalleCompleto() {
        StringBuilder sb = new StringBuilder();
        sb.append("Parcela ").append(id).append("\n");
        sb.append("Superficie: ").append(superficie).append(" m2\n");
        sb.append("Cultivo: ").append(cultivo != null ? cultivo.toString() : "N/A").append("\n");
        sb.append("Umbral: ").append(umbral).append("\n");
        sb.append("Estado riego: ").append(estadoRiego ? "ON" : "OFF").append("\n");
        sb.append("Humedad promedio (última lectura): ").append(String.format("%.2f", obtenerHumedadPromedio())).append("%\n");
        sb.append("Sensores asociados: ").append(sensores.size()).append("\n");
        for (SensorHumedad s : sensores) sb.append("  - ").append(s.toString()).append("\n");
        sb.append("Aspersores asociados: ").append(aspersores.size()).append("\n");
        for (Aspersor a : aspersores) sb.append("  - ").append(a.toString()).append("\n");
        return sb.toString();
    }

    // Getters / setters
    public int getId() { return id; }
    public double getSuperficie() { return superficie; }
    public Cultivo getCultivo() { return cultivo; }
    public void setCultivo(Cultivo cultivo) {
        this.cultivo = cultivo;
        setUmbral(cultivo.obtenerUmbralSugerido());
    }
    public int getUmbral() { return umbral; }
    public void setUmbral(int umbral) { this.umbral = umbral; }
    public boolean isEstadoRiego() { return estadoRiego; }
    public void setEstadoRiego(boolean estadoRiego) { this.estadoRiego = estadoRiego; }
}

