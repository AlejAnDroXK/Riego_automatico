package ProyectoRiegoAutomatico;

import java.time.Instant;

public class DatosAspersor {
    private final Instant timestamp;
    private final String accion;
    private final long duracionMs;

    public DatosAspersor(Instant timestamp, String accion, long duracionMs) {
        this.timestamp = timestamp;
        this.accion = accion;
        this.duracionMs = duracionMs;
    }

    public Instant getTimestamp() { return timestamp; }
    public String getAccion() { return accion; }
    public long getDuracionMs() { return duracionMs; }

    @Override
    public String toString() {
        return "ts=" + timestamp + " accion=" + accion + " duracionMs=" + duracionMs;
    }
}

