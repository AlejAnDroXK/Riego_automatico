package ProyectoRiegoAutomatico;

import java.time.Instant;

public class DatosSensorHumedad {
    private final Instant timestamp;
    private final double valorCrudo;
    private final double porcentaje;

    public DatosSensorHumedad(Instant timestamp, double valorCrudo, double porcentaje) {
        this.timestamp = timestamp;
        this.valorCrudo = valorCrudo;
        this.porcentaje = porcentaje;
    }

    public Instant getTimestamp() { return timestamp; }
    public double getValorCrudo() { return valorCrudo; }
    public double getPorcentaje() { return porcentaje; }

    @Override
    public String toString() {
        return "ts=" + timestamp + " raw=" + ((int)valorCrudo) + " pct=" + String.format("%.2f", porcentaje) + "%";
    }
}

