package ProyectoRiegoAutomatico;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SensorHumedad {
    private final int idSensor;
    private final List<DatosSensorHumedad> historial = new ArrayList<>();
    private final Random rnd = new Random();
    private double valorCrudo = 0; // 0..1023
    private double porcentaje = 0;
    private Instant fechaLectura;

    public SensorHumedad(int idSensor) {
        this.idSensor = idSensor;
    }
    //simula un valor leido
    public void leerSimulado() {
        this.valorCrudo = rnd.nextInt(1024); // 0..1023
        this.fechaLectura = Instant.now();
        convertirPorcentaje();
        DatosSensorHumedad d = new DatosSensorHumedad(fechaLectura, valorCrudo, porcentaje);
        agregarDato(d);
    }

    private void convertirPorcentaje() {
        this.porcentaje = (valorCrudo / 1023.0) * 100.0;
        if (porcentaje < 0) porcentaje = 0;
        if (porcentaje > 100) porcentaje = 100;
    }

    public boolean validar() {
        return !(Double.isNaN(porcentaje) || valorCrudo < 0 || valorCrudo > 1023);
    }

    public void agregarDato(DatosSensorHumedad d) {
        historial.add(d);
    }

    public List<DatosSensorHumedad> listarDatos() {
        return new ArrayList<>(historial);
    }

    // Getters
    public int getIdSensor() { return idSensor; }
    public double getValorCrudo() { return valorCrudo; }
    public double getPorcentaje() { return porcentaje; }
    public Instant getFechaLectura() { return fechaLectura; }

    public String detalleCompleto() {
        StringBuilder sb = new StringBuilder();
        sb.append("Sensor id=").append(idSensor).append("\n");
        sb.append("Última lectura: ").append(String.format("%.2f", porcentaje)).append("% (raw=").append((int)valorCrudo).append(")\n");
        sb.append("Historial (últimos ").append(historial.size()).append("):\n");
        for (DatosSensorHumedad d : historial) sb.append("  - ").append(d).append("\n");
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Sensor " + idSensor + " | " + String.format("%.2f", porcentaje) + "%";
    }
}

